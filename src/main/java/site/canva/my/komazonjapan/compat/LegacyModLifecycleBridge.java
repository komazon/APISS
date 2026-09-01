package site.canva.my.komazonjapan.compat;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 【Phase 1 MVP】ライフサイクル・ブリッジ
 *
 * 役割:
 *   1. 1.12.2 ModのクラスをスキャンしてModインスタンスを生成する
 *   2. @EventHandler が付いたメソッドを発見する
 *   3. 現代の FMLCommonSetupEvent 発火時に、ダミーの FMLPreInitializationEvent /
 *      FMLInitializationEvent / FMLPostInitializationEvent を生成して渡し、
 *      1.12.2Modのメソッドを強制的に実行する
 *
 * 設計書 「実装手順 2: ライフサイクル・ブリッジ」 に相当。
 */
public class LegacyModLifecycleBridge {

    private static final Logger LOGGER = LoggerFactory.getLogger(LegacyModLifecycleBridge.class);

    /**
     * ロード済みの 1.12.2 Modエントリ。
     * 将来的には LegacyModDiscoverer がここにエントリを追加する。
     */
    private final List<LegacyModEntry> loadedMods = new ArrayList<>();
    private boolean postInitFired = false;

    // ─── 公開API ───

    /**
     * 1.12.2Modのクラスとインスタンスを手動で登録する。
     * Phase 1では、テスト用に直接呼び出すことを想定。
     * Phase 2以降は LegacyModDiscoverer が自動的に呼び出す。
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
        LOGGER.info("[互換レイヤー] 1.12.2 Mod を登録: modId={}, class={}", modId, modClass.getName());

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
                LOGGER.debug("[互換レイヤー]   PreInit メソッド発見: {}", method.getName());
            } else if (paramType.equals(FMLInitializationEvent.class)) {
                initMethods.add(method);
                LOGGER.debug("[互換レイヤー]   Init メソッド発見: {}", method.getName());
            } else if (paramType.equals(FMLPostInitializationEvent.class)) {
                postInitMethods.add(method);
                LOGGER.debug("[互換レイヤー]   PostInit メソッド発見: {}", method.getName());
            }
        }

        loadedMods.add(new LegacyModEntry(
                modId, modInstance, preInitMethods, initMethods, postInitMethods, metadata));
    }

    /**
     * 現代の FMLCommonSetupEvent に対応して呼び出す。
     * 全登録済み1.12.2Modの PreInit → Init → PostInit を順に発火する。
     *
     * McModAPIs の commonSetup() からこのメソッドを呼ぶこと。
     */
    public void onCommonSetup(FMLCommonSetupEvent event) {
        // configディレクトリ: 現代の "config/" フォルダ
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
                LOGGER.debug("[互換レイヤー] {} フェーズ: {}.{}() 実行完了",
                        phaseName, instance.getClass().getSimpleName(), method.getName());
            } catch (Exception e) {
                LOGGER.error("[互換レイヤー] {} フェーズ: {}.{}() の実行中にエラー",
                        phaseName, instance.getClass().getSimpleName(), method.getName(), e);
            }
        }
    }

    /**
     * ロード済み1.12.2Modのエントリを保持するレコード。
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
