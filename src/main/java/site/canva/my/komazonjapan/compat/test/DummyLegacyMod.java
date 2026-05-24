package site.canva.my.komazonjapan.compat.test;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 【Phase 1 MVP 動作確認用】
 *
 * 設計書に書かれた「空の1.12.2ダミーMod」の実装。
 * ブロックもアイテムも登録せず、@EventHandlerでログを出すだけ。
 *
 * LegacyModLifecycleBridge に登録することで、
 * NeoForge 26.1.2 上で1.12.2スタイルのライフサイクルが
 * 正しく呼び出されることを検証できる。
 *
 * 使い方 (McModAPIs のコンストラクタ内):
 *   LegacyModLifecycleBridge bridge = new LegacyModLifecycleBridge();
 *   DummyLegacyMod dummy = new DummyLegacyMod();
 *   bridge.registerLegacyMod(DummyLegacyMod.class, dummy);
 *   modEventBus.addListener(bridge::onCommonSetup);
 */
@Mod(modid = "dummy_legacy_mod", name = "Dummy Legacy Mod", version = "1.0.0")
public class DummyLegacyMod {

    private static final Logger LOGGER = LoggerFactory.getLogger(DummyLegacyMod.class);

    @Mod.Instance("dummy_legacy_mod")
    public static DummyLegacyMod INSTANCE;

    // ── 1.12.2スタイルのライフサイクルメソッド ──

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        LOGGER.info("[DummyLegacyMod] ★ PRE-INIT 開始! ★");
        LOGGER.info("[DummyLegacyMod] configDir = {}", event.getModConfigurationDirectory());
        LOGGER.info("[DummyLegacyMod] modId     = {}", event.getModId());
        LOGGER.info("[DummyLegacyMod] ★ PRE-INIT 完了! ★");
        LOGGER.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        LOGGER.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        LOGGER.info("[DummyLegacyMod] ★ INIT 開始! ★");
        LOGGER.info("[DummyLegacyMod] modId = {}", event.getModId());
        LOGGER.info("[DummyLegacyMod] ★ INIT 完了! ★");
        LOGGER.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        LOGGER.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        LOGGER.info("[DummyLegacyMod] ★ POST-INIT 開始! ★");
        LOGGER.info("[DummyLegacyMod] modId = {}", event.getModId());
        LOGGER.info("[DummyLegacyMod] ★ POST-INIT 完了! 互換レイヤーのPhase 1 MVPは成功! ★");
        LOGGER.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }
}
