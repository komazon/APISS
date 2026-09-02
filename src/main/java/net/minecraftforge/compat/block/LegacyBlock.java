package net.minecraftforge.compat.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 互換レイヤー：1.12.2 の Block クラスのラッパー。
 *
 * 1.12.2 では:
 *   new Block(Material.ROCK)
 *   block.setRegistryName("mymod", "my_block")
 *   block.setUnlocalizedName("my_block")
 *
 * 現代では BlockBehaviour.Properties.of() を使用するが、
 * 旧 Mod はレガシーな API で Material や硬度を設定しようとする。
 * このクラスはそれらの呼び出しを吸収し、
 * 最終的に現代の Block コンストラクタに渡す。
 *
 * 注意：このクラスは IForgeRegistry.register() 前に
 * setRegistryName() を呼ぶことを想定している。
 */
public class LegacyBlock extends Block {

    private static final Logger LOGGER = LoggerFactory.getLogger(LegacyBlock.class);

    /**
     * IForgeRegistry.register() 前に setRegistryName() を呼ぶための内部フィールド。
     * getRegistryName() で参照される。
     */
    String _legacyRegistryName = null;

    /** 1.12.2 の setUnlocalizedName() で設定する内部フィールド */
    String _unlocalizedName = null;

    // ━━━━━━━━━━ 1.12.2 互換のコンストラクタ ━━━━━━━━━━

    /**
     * デフォルトコンストラクタ：Material.ROCK 相当。
     * この後で setRegistryName() を呼ぶ必要がある。
     */
    public LegacyBlock() {
        this(LegacyMaterial.ROCK);
    }

    /**
     * Material を指定するコンストラクタ。
     * 1.12.2 互換：Material → MapColor + BlockBehaviour.Properties
     */
    public LegacyBlock(LegacyMaterial material) {
        super(mapMaterialToProperties(material));
        this._legacyRegistryName = null;
        this._unlocalizedName = null;
        LOGGER.debug("[LegacyBlock] 生成：material={}", material);
    }

    /**
     * 1.12.2 互換：ブロック ID と Material を受け取るコンストラクタ。
     * 古い Mod では "mymod:my_block" のような文字列 ID を直接渡すことがある。
     */
    public LegacyBlock(String blockId, LegacyMaterial material) {
        this(material);
        if (blockId != null && !blockId.isEmpty()) {
            setRegistryName(blockId);
        }
    }

    // ━━━━━━━━━━ 1.12.2 互換メソッド ━━━━━━━━━━

    /**
     * 1.12.2 の block.setRegistryName("mymod", "my_block") を模倣。
     * IForgeRegistry.register() 時に _legacyRegistryName を参照する。
     *
     * @return this (メソッドチェーン用)
     */
    public LegacyBlock setRegistryName(String domain, String path) {
        this._legacyRegistryName = domain + ":" + path;
        LOGGER.debug("[LegacyBlock] RegistryName 設定：{}", this._legacyRegistryName);
        return this;
    }

    /**
     * 1.12.2 の block.setRegistryName("mymod:my_block") を模倣。
     */
    public LegacyBlock setRegistryName(String registryName) {
        this._legacyRegistryName = registryName;
        return this;
    }

    /**
     * 1.12.2 の block.setUnlocalizedName("my_block") を模倣。
     * 現代では翻訳キーとして使用される可能性がある。
     *
     * @return this (メソッドチェーン用)
     */
    public LegacyBlock setUnlocalizedName(String name) {
        this._unlocalizedName = name;
        return this;
    }

    public LegacyBlock setHardness(float hardness) {
        LOGGER.debug("[LegacyBlock] setHardness({})", hardness);
        return this;
    }

    public LegacyBlock setResistance(float resistance) {
        LOGGER.debug("[LegacyBlock] setResistance({})", resistance);
        return this;
    }

    /**
     * レジストリ名を取得する。
     * 1.12.2Mod が IForgeRegistry.register() 時に参照する。
     */
    public String getRegistryName() {
        return _legacyRegistryName;
    }

    // ━━━━━━━━━━ Material → BlockBehaviour.Properties 変換 ━━━━━━━━━━

    /**
     * 1.12.2 の Material に基づいて BlockBehaviour.Properties を生成する。
     * 硬度やドロップ設定などを含む。
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
