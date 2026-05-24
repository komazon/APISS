package site.canva.my.komazonjapan.compat.test;

import net.minecraftforge.compat.block.LegacyBlock;
import net.minecraftforge.compat.block.LegacyMaterial;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import net.minecraft.world.level.block.Block;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 【Phase 2 動作確認用】
 *
 * 設計書「Phase 2: ダミーの RegistryEvent を発火させ、
 * 1.12.2Modが『無機能のブロック』を1つ登録・ゲーム内に登場させる」の実装。
 *
 * 1.12.2の本物のModがどのように書くかを忠実に再現している。
 * このクラス自体は互換レイヤーの一部ではなく「テスト対象」。
 */
@Mod(modid = "legacy_block_test", name = "Legacy Block Test", version = "1.0.0")
public class DummyLegacyModWithBlock {

    private static final Logger LOGGER = LoggerFactory.getLogger(DummyLegacyModWithBlock.class);

    @Mod.Instance("legacy_block_test")
    public static DummyLegacyModWithBlock INSTANCE;

    // 1.12.2スタイルのフィールド宣言
    public static Block MY_BLOCK;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info("[DummyLegacyModWithBlock] PreInit: 開始");
        // 1.12.2ではPreInitでブロックのインスタンスを生成することが多い
        MY_BLOCK = new LegacyBlock(LegacyMaterial.ROCK)
                .setRegistryName("legacy_block_test", "my_stone_block")
                .setUnlocalizedName("my_stone_block");
        LOGGER.info("[DummyLegacyModWithBlock] PreInit: MY_BLOCK 生成完了 -> {}",
                ((LegacyBlock) MY_BLOCK).getRegistryName());
    }

    /**
     * 1.12.2スタイルのブロック登録。
     * LegacyRegistryBridge がこのメソッドを発見して呼び出す。
     * event.getRegistry() は内部キャッシュに溜め込むIForgeRegistryのダミー。
     */
    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        LOGGER.info("[DummyLegacyModWithBlock] registerBlocks: 開始");
        event.getRegistry().register(MY_BLOCK);
        LOGGER.info("[DummyLegacyModWithBlock] registerBlocks: MY_BLOCK をキャッシュに登録完了");
        LOGGER.info("[DummyLegacyModWithBlock] ★ ブロックが現代のゲームに登録されれば Phase 2 成功! ★");
    }
}
