package site.canva.my.komazonjapan.compat;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 【Phase 1 MVP】ライフサイクル・ブリッジ
 *
 * 役割:
 *   1. 1.12.2 Mod のクラスをスキャンして Mod インスタンスを生成する
 *   2. @EventHandler が付いたメソッドを発見する
 *   3. 現代の FMLCommonSetupEvent 発火時に、ダミーの FMLPreInitializationEvent /
 *      FMLInitializationEvent / FMLPostInitializationEvent を生成して渡し、
 *      1.12.2Mod のメソッドを強制的に実行する
 *   4. @Mod アノテーションの proxyFactory/proxy クラスを読み取り、proxy フィールドを初期化する
 *
 * 設計書「実装手順 2: ライフサイクル・ブリッジ」に相当。
 */
public class LegacyModLifecycleBridge {

    private static final Logger LOGGER = LoggerFactory.getLogger(LegacyModLifecycleBridge.class);

    /**
     * ロード済みの 1.12.2 Mod エントリ。
     * 将来的には LegacyModDiscoverer がここにエントリを追加する。
     */
    private final List<LegacyModEntry> loadedMods = new ArrayList<>();
    private boolean postInitFired = false;

    // ─── 公開 API ───

    /**
     * 1.12.2Mod のクラスとインスタンスを手動で登録する。
     * Phase 1 では、テスト用に直接呼び出すことを想定。
     * Phase 2 以降は LegacyModDiscoverer が自動的に呼び出す。
     *
     * @param modClass   @Mod アノテーションが付いたクラス
     * @param modInstance そのクラスのインスタンス（リフレクションで生成済み）
     */
    public void registerLegacyMod(Class<?> modClass, Object modInstance) {
        Mod annotation = modClass.getAnnotation(Mod.class);
        String modId = annotation != null ? annotation.modid() : "unknown";
        registerLegacyMod(modClass, modInstance, new FMLPreInitializationEvent.ModMetadataStub(modId));
    }

    public void registerLegacyMod(Class<?> modClass, Object modInstance,
                                  FMLPreInitializationEvent.ModMetadataStub metadata) {
        Mod modAnnotation = modClass.getAnnotation(Mod.class);
        if (modAnnotation == null) {
            LOGGER.warn("[互換レイヤー] {} は @Mod アノテーションを持っていないため無視します", modClass.getName());
            return;
        }

        String modId = modAnnotation.modid();
        LOGGER.info("[互換レイヤー] 1.12.2 Mod を登録：modId={}, class={}", modId, modClass.getName());

        // @EventHandler が付いたメソッドを全スキャン
        List<Method> preInitMethods  = new ArrayList<>();
        List<Method> initMethods     = new ArrayList<>();
        List<Method> postInitMethods = new ArrayList<>();

        for (Method method : modClass.getMethods()) {
            if (!method.isAnnotationPresent(Mod.EventHandler.class)) continue;

            Class<?>[] params = method.getParameterTypes();
            if (params.length != 1) continue;

            Class<?> paramType = params[0];
            if (paramType.equals(FMLPreInitializationEvent.class)) {
                preInitMethods.add(method);
                LOGGER.debug("[互換レイヤー]   PreInit メソッド発見：{}", method.getName());
            } else if (paramType.equals(FMLInitializationEvent.class)) {
                initMethods.add(method);
                LOGGER.debug("[互換レイヤー]   Init メソッド発見：{}", method.getName());
            } else if (paramType.equals(FMLPostInitializationEvent.class)) {
                postInitMethods.add(method);
                LOGGER.debug("[互換レイヤー]   PostInit メソッド発見：{}", method.getName());
            }
        }

        // proxy フィールドの初期化を試みる
        initializeProxyField(modClass, modInstance, modAnnotation);

        loadedMods.add(new LegacyModEntry(
                modId, modInstance, preInitMethods, initMethods, postInitMethods, metadata));
    }

    /**
     * @Mod アノテーションから proxy クラス情報を読み取り、proxy フィールドを初期化する。
     */
    private void initializeProxyField(Class<?> modClass, Object modInstance, Mod modAnnotation) {
        // proxy フィールドを探す
        Field proxyField = null;
        try {
            proxyField = modClass.getDeclaredField("proxy");
            proxyField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            LOGGER.debug("[互換レイヤー] {} に proxy フィールドが見つかりません", modClass.getSimpleName());
            return;
        }

        // 既に設定されている場合はスキップ
        try {
            if (proxyField.get(modInstance) != null) {
                LOGGER.debug("[互換レイヤー] {} の proxy フィールドは既に設定されています", modClass.getSimpleName());
                return;
            }
        } catch (IllegalAccessException e) {
            LOGGER.warn("[互換レイヤー] proxy フィールドへのアクセスに失敗しました", e);
            return;
        }

        // 1.12.2 の @Mod アノテーションには clientSideProxy/serverSideProxy は存在しない
        // proxyFactory 属性を使用するが、これは SidedProxy を使用した古いパターン
        // 実際に必要なのは、各サイドのプロキシクラスを直接インスタンス化すること
        
        // クライアント環境なので、クライアント側のプロキシを優先的に探す
        // プロキシクラスの命名規則: CommonProxy -> ClientProxy
        String proxyClassName = null;
        
        // まず、modId からパッケージ名を推測して ClientProxy を探す
        String modPackage = modClass.getPackage().getName();
        String[] possibleProxyNames = {
            modPackage + ".ClientProxy",
            modPackage + ".client.ClientProxy",
            modPackage + ".proxy.ClientProxy",
            modPackage + ".CommonProxy$Client", // インナークラスの場合
        };
        
        for (String candidate : possibleProxyNames) {
            try {
                Class<?> proxyClass = Class.forName(candidate, false, modClass.getClassLoader());
                proxyClassName = candidate;
                LOGGER.debug("[互換レイヤー] 候補の proxy クラスを発見：{}", proxyClassName);
                break;
            } catch (ClassNotFoundException e) {
                // 次の候補を試す
            }
        }
        
        // 見つからなければ CommonProxy を試す
        if (proxyClassName == null) {
            String commonProxyName = modPackage + ".CommonProxy";
            try {
                Class.forName(commonProxyName, false, modClass.getClassLoader());
                proxyClassName = commonProxyName;
                LOGGER.debug("[互換レイヤー] CommonProxy を使用：{}", proxyClassName);
            } catch (ClassNotFoundException e) {
                // CommonProxy も存在しない
            }
        }

        if (proxyClassName == null || proxyClassName.isEmpty()) {
            LOGGER.info("[互換レイヤー] {} に proxy クラス指定がありません。proxy フィールドは null のままです。", modClass.getSimpleName());
            return;
        }

        // proxy クラスをロードしてインスタンス化
        try {
            LOGGER.info("[互換レイヤー] {} の proxy クラスをロード中：{}", modClass.getSimpleName(), proxyClassName);
            Class<?> proxyClass = Class.forName(proxyClassName, true, modClass.getClassLoader());
            LOGGER.info("[互換レイヤー] {} の proxy クラスをロード完了：{}", modClass.getSimpleName(), proxyClass.getName());
            Object proxyInstance = proxyClass.getDeclaredConstructor().newInstance();
            proxyField.set(modInstance, proxyInstance);
            LOGGER.info("[互換レイヤー] {} の proxy フィールドを初期化：{} -> {}", 
                    modClass.getSimpleName(), proxyClassName, proxyInstance.getClass().getSimpleName());
        } catch (ClassNotFoundException e) {
            LOGGER.error("[互換レイヤー] proxy クラスが見つかりません：{} (ClassLoader: {})", proxyClassName, modClass.getClassLoader(), e);
        } catch (InstantiationException e) {
            LOGGER.error("[互換レイヤー] proxy インスタンスの生成に失敗しました（インスタンス化エラー）：{}", proxyClassName, e);
        } catch (IllegalAccessException e) {
            LOGGER.error("[互換レイヤー] proxy インスタンスの生成に失敗しました（アクセスエラー）：{}", proxyClassName, e);
        } catch (java.lang.reflect.InvocationTargetException e) {
            LOGGER.error("[互換レイヤー] proxy インスタンスの生成に失敗しました（コンストラクタエラー）：{}", proxyClassName, e.getCause());
        } catch (NoSuchMethodException e) {
            LOGGER.error("[互換レイヤー] proxy クラスにデフォルトコンストラクタが見つかりません：{}", proxyClassName, e);
        } catch (ExceptionInInitializerError e) {
            LOGGER.error("[互換レイヤー] proxy クラスの初期化中にエラーが発生しました：{}", proxyClassName, e.getCause());
        } catch (NoClassDefFoundError e) {
            LOGGER.error("[互換レイヤー] proxy クラスまたはその依存クラスが見つかりません：{} - {}", proxyClassName, e.getMessage());
        }
    }

    /**
     * 現代の FMLCommonSetupEvent に対応して呼び出す。
     * 全登録済み 1.12.2Mod の PreInit → Init → PostInit を順に発火する。
     *
     * McModAPIs の commonSetup() からこのメソッドを呼ぶこと。
     */
    public void onCommonSetup(FMLCommonSetupEvent event) {
        // config ディレクトリ：現代の "config/" フォルダ
        File configDir = new File("config");

        for (LegacyModEntry entry : loadedMods) {
            LOGGER.info("[互換レイヤー] === {} のライフサイクルを開始 ===", entry.modId);

            // ── PreInitializationEvent ──
            FMLPreInitializationEvent preInitEvent =
                    new FMLPreInitializationEvent(entry.modId, configDir, entry.metadata);
            invokeAll(entry.preInitMethods, entry.modInstance, preInitEvent, "PreInit");

            // ── InitializationEvent ──
            FMLInitializationEvent initEvent =
                    new FMLInitializationEvent(entry.modId);
            invokeAll(entry.initMethods, entry.modInstance, initEvent, "Init");

            LOGGER.info("[互換レイヤー] === {} の PreInit/Init フェーズ完了 ===", entry.modId);
        }
    }

    /**
     * 現代の RegisterEvent による旧レジストリ登録が完了したときに呼び出す。
     */
    public void onLegacyRegistryComplete() {
        if (postInitFired) {
            return;
        }
        postInitFired = true;

        for (LegacyModEntry entry : loadedMods) {
            LOGGER.info("[互換レイヤー] === {} の PostInit を開始 ===", entry.modId);
            FMLPostInitializationEvent postInitEvent =
                    new FMLPostInitializationEvent(entry.modId);
            invokeAll(entry.postInitMethods, entry.modInstance, postInitEvent, "PostInit");
            LOGGER.info("[互換レイヤー] === {} の PostInit 完了 ===", entry.modId);
        }
    }

    // ─── 内部実装 ───

    /**
     * メソッドリストを順に呼び出す。
     * アクセス修飾子を無視してリフレクションで強制呼び出しする。
     */
    private void invokeAll(List<Method> methods, Object instance,
                           Object event, String phaseName) {
        for (Method method : methods) {
            try {
                method.setAccessible(true);
                method.invoke(instance, event);
                LOGGER.debug("[互換レイヤー] {} フェーズ：{}.{}() 実行完了",
                        phaseName, instance.getClass().getSimpleName(), method.getName());
            } catch (Exception e) {
                LOGGER.error("[互換レイヤー] {} フェーズ：{}.{}() の実行中にエラー",
                        phaseName, instance.getClass().getSimpleName(), method.getName(), e);
            }
        }
    }

    /**
     * ロード済み 1.12.2Mod のエントリを保持するレコード。
     */
    private record LegacyModEntry(
            String modId,
            Object modInstance,
            List<Method> preInitMethods,
            List<Method> initMethods,
            List<Method> postInitMethods,
            FMLPreInitializationEvent.ModMetadataStub metadata
    ) {}
}
