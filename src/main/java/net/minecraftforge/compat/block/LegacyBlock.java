package net.minecraftforge.compat.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 互換レイヤー: 1.12.2の Block クラスのブリッジ実装。
 *
 * 1.12.2では:
 *   new Block(Material.ROCK)
 *   block.setRegistryName("mymod", "my_block")
 *   block.setUnlocalizedName("my_block")
 *
 * 現代では BlockBehaviour.Properties.of() のビルダーパターンが必要。
 * このクラスが旧スタイルのコンストラクタ・メソッドを受け取り、
 * 内部で現代の Block として動作するよう変換する。
 *
 * 設計書「実装手順 4: コンストラクタのインターセプト」に相当。
 */
public class LegacyBlock extends Block {

    private static final Logger LOGGER = LoggerFactory.getLogger(LegacyBlock.class);

    /**
     * IForgeRegistryが登録名を取り出すために参照するフィールド。
     * setRegistryName() で設定される。
     */
    String _legacyRegistryName = null;

    /** 1.12.2の setUnlocalizedName() で設定される翻訳キー */
    String _unlocalizedName = null;

    // ─── 1.12.2スタイルのコンストラクタ群 ───

    /**
     * 最も一般的な 1.12.2 コンストラクタ: Material を受け取る。
     * 互換レイヤーで Material → MapColor にマッピングして現代の Properties を生成。
     */
    public LegacyBlock(LegacyMaterial material) {
        super(mapMaterialToProperties(material));
        LOGGER.debug("[互換レイヤー] LegacyBlock 生成: material={}", material);
    }

    /**
     * デフォルトコンストラクタ（Material.ROCK相当）。
     * 一部のModは引数なしサブクラスコンストラクタを持つ。
     */
    public LegacyBlock() {
        this(LegacyMaterial.ROCK);
    }

    // ─── 1.12.2のメソッドチェーン ───

    /**
     * 1.12.2の block.setRegistryName("mymod", "my_block") に相当。
     * IForgeRegistryが登録名の特定に使う _legacyRegistryName に保存する。
     *
     * @return this (メソッドチェーン用)
     */
    public LegacyBlock setRegistryName(String domain, String path) {
        this._legacyRegistryName = domain + ":" + path;
        LOGGER.debug("[互換レイヤー] RegistryName 設定: {}", this._legacyRegistryName);
        return this;
    }

    /**
     * 1.12.2の block.setRegistryName("mymod:my_block") に相当。
     */
    public LegacyBlock setRegistryName(String registryName) {
        this._legacyRegistryName = registryName;
        return this;
    }

    /**
     * 1.12.2の block.setUnlocalizedName("my_block") に相当。
     * 現代では翻訳キーは自動生成されるが、互換性のためフィールドに保存する。
     *
     * @return this (メソッドチェーン用)
     */
    public LegacyBlock setUnlocalizedName(String name) {
        this._unlocalizedName = name;
        return this;
    }

    /**
     * 登録名を返す。1.12.2Modが確認のために呼ぶことがある。
     */
    public String getRegistryName() {
        return _legacyRegistryName;
    }

    // ─── Material → BlockBehaviour.Properties 変換 ───

    /**
     * 1.12.2の Material を現代の BlockBehaviour.Properties に変換する。
     * 設計書「第4フェーズ：BlockとItemのプロパティ変換」に相当。
     */
    private static BlockBehaviour.Properties mapMaterialToProperties(LegacyMaterial material) {
        return switch (material) {
            case ROCK       -> BlockBehaviour.Properties.of().mapColor(MapColor.STONE)
                                    .requiresCorrectToolForDrops().strength(1.5f, 6.0f);
            case IRON       -> BlockBehaviour.Properties.of().mapColor(MapColor.METAL)
                                    .requiresCorrectToolForDrops().strength(5.0f, 10.0f);
            case WOOD       -> BlockBehaviour.Properties.of().mapColor(MapColor.WOOD)
                                    .strength(2.0f, 3.0f);
            case GROUND     -> BlockBehaviour.Properties.of().mapColor(MapColor.DIRT)
                                    .strength(0.5f);
            case GRASS      -> BlockBehaviour.Properties.of().mapColor(MapColor.GRASS)
                                    .strength(0.6f);
            case SAND       -> BlockBehaviour.Properties.of().mapColor(MapColor.SAND)
                                    .strength(0.5f);
            case GLASS      -> BlockBehaviour.Properties.of().mapColor(MapColor.NONE)
                                    .strength(0.3f).noOcclusion();
            case LEAVES     -> BlockBehaviour.Properties.of().mapColor(MapColor.PLANT)
                                    .strength(0.2f).noOcclusion();
            case WATER,
                 LAVA       -> BlockBehaviour.Properties.of().mapColor(MapColor.WATER)
                                    .noCollision().strength(100.0f);
            case AIR        -> BlockBehaviour.Properties.of().noCollision()
                                    .noOcclusion().air();
            default         -> BlockBehaviour.Properties.of().mapColor(MapColor.STONE)
                                    .strength(1.5f, 6.0f);
        };
    }
}
