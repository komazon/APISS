package site.canva.my.komazonjapan.compat;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import net.minecraftforge.compat.block.LegacyBlock;
import net.minecraftforge.compat.item.LegacyItem;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 【Phase 2】レジストリ・ブリッジ
 *
 * 役割:
 *   1. 1.12.2Modの @SubscribeEvent で registerBlocks / registerItems などの
 *      メソッドを発見する
 *   2. ダミーの RegistryEvent.Register<Block> / <Item> を生成して呼び出し、
 *      IForgeRegistry の内部キャッシュに登録エントリを溜め込む
 *   3. 現代の NeoForge RegisterEvent が発火したタイミングで、
 *      キャッシュ内のエントリを現代のレジストリに一気に流し込む
 *
 * 設計書「第3フェーズ：レジストリ（登録機構）の翻訳」に相当。
 */
public class LegacyRegistryBridge {

    private static final Logger LOGGER = LoggerFactory.getLogger(LegacyRegistryBridge.class);

    // 1.12.2Modからの登録待ちエントリを溜めるキャッシュ
    private final IForgeRegistry<Block> blockRegistry = new IForgeRegistry<>("block");
    private final IForgeRegistry<Item>  itemRegistry  = new IForgeRegistry<>("item");

    // スキャン対象の1.12.2Modエントリ
    private final List<LegacyModEntry> modEntries = new ArrayList<>();

    // ─── 公開API ───

    /**
     * 1.12.2ModのクラスとインスタンスをBridgeに登録する。
     * LegacyModLifecycleBridge と同じタイミングでコンストラクタから呼ぶ。
     */
    public void registerLegacyMod(Class<?> modClass, Object modInstance) {
        Mod modAnnotation = modClass.getAnnotation(Mod.class);
        if (modAnnotation == null) return;

        String modId = modAnnotation.modid();
        List<Method> blockRegMethods = new ArrayList<>();
        List<Method> itemRegMethods  = new ArrayList<>();

        for (Method method : modClass.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(net.minecraftforge.fml.common.eventhandler.SubscribeEvent.class)
                && !method.isAnnotationPresent(net.minecraftforge.fml.common.Mod.EventHandler.class)) {
                continue;
            }
            if (method.getParameterCount() != 1) continue;

            Class<?> paramType = method.getParameterTypes()[0];
            // パラメータが RegistryEvent.Register かどうか確認（型消去があるため生クラスで判定）
            if (paramType.equals(RegistryEvent.Register.class)) {
                // ブロック登録かアイテム登録かはメソッド名のヒューリスティックで判定
                String methodName = method.getName().toLowerCase();
                if (methodName.contains("block")) {
                    blockRegMethods.add(method);
                    LOGGER.debug("[互換レイヤー] Block登録メソッド発見: {}.{}", modId, method.getName());
                } else if (methodName.contains("item")) {
                    itemRegMethods.add(method);
                    LOGGER.debug("[互換レイヤー] Item登録メソッド発見: {}.{}", modId, method.getName());
                } else {
                    // どちらとも判断できない場合は両方試みる
                    blockRegMethods.add(method);
                    itemRegMethods.add(method);
                }
            }
        }
        modEntries.add(new LegacyModEntry(modId, modInstance, blockRegMethods, itemRegMethods));
        LOGGER.info("[互換レイヤー] RegistryBridge に登録: modId={}", modId);
    }

    /**
     * 現代の RegisterEvent が発火した時にこのメソッドを呼ぶ。
     * McModAPIs の modEventBus.addListener(legacyRegistryBridge::onRegister) で登録する。
     *
     * 処理の流れ:
     *   1. 1.12.2Modのregisterブロックメソッドを呼び出してキャッシュに溜める
     *   2. キャッシュを現代のNeoForge RegisterEvent に流し込む
     */
    public void onRegister(RegisterEvent event) {
        // ── ブロック登録フェーズ ──
        if (event.getRegistryKey().equals(Registries.BLOCK)) {
            LOGGER.info("[互換レイヤー] Block登録フェーズ開始");
            fireLegacyBlockRegistration();
            flushBlockCache(event);
            LOGGER.info("[互換レイヤー] Block登録フェーズ完了: {}件",
                    blockRegistry.getPendingEntries().size());
        }

        // ── アイテム登録フェーズ ──
        if (event.getRegistryKey().equals(Registries.ITEM)) {
            LOGGER.info("[互換レイヤー] Item登録フェーズ開始");
            fireLegacyItemRegistration();
            flushItemCache(event);
            LOGGER.info("[互換レイヤー] Item登録フェーズ完了: {}件",
                    itemRegistry.getPendingEntries().size());
        }
    }

    // ─── 内部実装 ───

    /** 1.12.2ModのBlock登録メソッドを呼び出してキャッシュに詰める */
    private void fireLegacyBlockRegistration() {
        RegistryEvent.Register<Block> fakeEvent = new RegistryEvent.Register<>(blockRegistry);
        for (LegacyModEntry entry : modEntries) {
            for (Method method : entry.blockRegMethods) {
                try {
                    method.setAccessible(true);
                    method.invoke(entry.modInstance, fakeEvent);
                } catch (Exception e) {
                    LOGGER.error("[互換レイヤー] Block登録メソッド {}.{}() の実行中にエラー",
                            entry.modId, method.getName(), e);
                }
            }
        }
    }

    /** 1.12.2ModのItem登録メソッドを呼び出してキャッシュに詰める */
    private void fireLegacyItemRegistration() {
        RegistryEvent.Register<Item> fakeEvent = new RegistryEvent.Register<>(itemRegistry);
        for (LegacyModEntry entry : modEntries) {
            for (Method method : entry.itemRegMethods) {
                try {
                    method.setAccessible(true);
                    method.invoke(entry.modInstance, fakeEvent);
                } catch (Exception e) {
                    LOGGER.error("[互換レイヤー] Item登録メソッド {}.{}() の実行中にエラー",
                            entry.modId, method.getName(), e);
                }
            }
        }
    }

    /** Blockキャッシュを現代のNeoForge RegisterEvent に流し込む */
    private void flushBlockCache(RegisterEvent event) {
        for (Map.Entry<String, Block> entry : blockRegistry.getPendingEntries().entrySet()) {
            Identifier rl = Identifier.parse(entry.getKey());
            event.register(Registries.BLOCK, rl, () -> entry.getValue());
            LOGGER.info("[互換レイヤー] Block を現代レジストリに登録: {}", rl);
        }
        blockRegistry.getPendingEntries().clear();
    }

    /** Itemキャッシュを現代のNeoForge RegisterEvent に流し込む */
    private void flushItemCache(RegisterEvent event) {
        for (Map.Entry<String, Item> entry : itemRegistry.getPendingEntries().entrySet()) {
            Identifier rl = Identifier.parse(entry.getKey());
            event.register(Registries.ITEM, rl, () -> entry.getValue());
            LOGGER.info("[互換レイヤー] Item を現代レジストリに登録: {}", rl);
        }
        itemRegistry.getPendingEntries().clear();
    }

    private record LegacyModEntry(
            String modId,
            Object modInstance,
            List<Method> blockRegMethods,
            List<Method> itemRegMethods
    ) {}
}
