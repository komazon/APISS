package net.minecraftforge.compat.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 莠呈鋤繝ｬ繧､繝､繝ｼ: 1.12.2縺ｮ Block 繧ｯ繝ｩ繧ｹ縺ｮ繝悶Μ繝・ず螳溯｣・・ *
 * 1.12.2縺ｧ縺ｯ:
 *   new Block(Material.ROCK)
 *   block.setRegistryName("mymod", "my_block")
 *   block.setUnlocalizedName("my_block")
 *
 * 迴ｾ莉｣縺ｧ縺ｯ BlockBehaviour.Properties.of() 縺ｮ繝薙Ν繝繝ｼ繝代ち繝ｼ繝ｳ縺悟ｿ・ｦ√・ * 縺薙・繧ｯ繝ｩ繧ｹ縺梧立繧ｹ繧ｿ繧､繝ｫ縺ｮ繧ｳ繝ｳ繧ｹ繝医Λ繧ｯ繧ｿ繝ｻ繝｡繧ｽ繝・ラ繧貞女縺大叙繧翫・ * 蜀・Κ縺ｧ迴ｾ莉｣縺ｮ Block 縺ｨ縺励※蜍穂ｽ懊☆繧九ｈ縺・､画鋤縺吶ｋ縲・ *
 * 險ｭ險域嶌縲悟ｮ溯｣・焔鬆・4: 繧ｳ繝ｳ繧ｹ繝医Λ繧ｯ繧ｿ縺ｮ繧､繝ｳ繧ｿ繝ｼ繧ｻ繝励ヨ縲阪↓逶ｸ蠖薙・ */
public class LegacyBlock extends Block {

    private static final Logger LOGGER = LoggerFactory.getLogger(LegacyBlock.class);

    /**
     * IForgeRegistry縺檎匳骭ｲ蜷阪ｒ蜿悶ｊ蜃ｺ縺吶◆繧√↓蜿ら・縺吶ｋ繝輔ぅ繝ｼ繝ｫ繝峨・     * setRegistryName() 縺ｧ險ｭ螳壹＆繧後ｋ縲・     */
    String _legacyRegistryName = null;

    /** 1.12.2縺ｮ setUnlocalizedName() 縺ｧ險ｭ螳壹＆繧後ｋ鄙ｻ險ｳ繧ｭ繝ｼ */
    String _unlocalizedName = null;

    // 笏笏笏 1.12.2繧ｹ繧ｿ繧､繝ｫ縺ｮ繧ｳ繝ｳ繧ｹ繝医Λ繧ｯ繧ｿ鄒､ 笏笏笏

    /**
     * 譛繧ゆｸ闊ｬ逧・↑ 1.12.2 繧ｳ繝ｳ繧ｹ繝医Λ繧ｯ繧ｿ: Material 繧貞女縺大叙繧九・     * 莠呈鋤繝ｬ繧､繝､繝ｼ縺ｧ Material 竊・MapColor 縺ｫ繝槭ャ繝斐Φ繧ｰ縺励※迴ｾ莉｣縺ｮ Properties 繧堤函謌舌・     */
    public LegacyBlock(LegacyMaterial material) {
        super(mapMaterialToProperties(material));
        LOGGER.debug("[莠呈鋤繝ｬ繧､繝､繝ｼ] LegacyBlock 逕滓・: material={}", material);
    }

    /**
     * 繝・ヵ繧ｩ繝ｫ繝医さ繝ｳ繧ｹ繝医Λ繧ｯ繧ｿ・・aterial.ROCK逶ｸ蠖難ｼ峨・     * 荳驛ｨ縺ｮMod縺ｯ蠑墓焚縺ｪ縺励し繝悶け繝ｩ繧ｹ繧ｳ繝ｳ繧ｹ繝医Λ繧ｯ繧ｿ繧呈戟縺､縲・     */
    public LegacyBlock() {
        this(LegacyMaterial.ROCK);
    }

    // 笏笏笏 1.12.2縺ｮ繝｡繧ｽ繝・ラ繝√ぉ繝ｼ繝ｳ 笏笏笏

    /**
     * 1.12.2縺ｮ block.setRegistryName("mymod", "my_block") 縺ｫ逶ｸ蠖薙・     * IForgeRegistry縺檎匳骭ｲ蜷阪・迚ｹ螳壹↓菴ｿ縺・_legacyRegistryName 縺ｫ菫晏ｭ倥☆繧九・     *
     * @return this (繝｡繧ｽ繝・ラ繝√ぉ繝ｼ繝ｳ逕ｨ)
     */
    public LegacyBlock setRegistryName(String domain, String path) {
        this._legacyRegistryName = domain + ":" + path;
        LOGGER.debug("[莠呈鋤繝ｬ繧､繝､繝ｼ] RegistryName 險ｭ螳・ {}", this._legacyRegistryName);
        return this;
    }

    /**
     * 1.12.2縺ｮ block.setRegistryName("mymod:my_block") 縺ｫ逶ｸ蠖薙・     */
    public LegacyBlock setRegistryName(String registryName) {
        this._legacyRegistryName = registryName;
        return this;
    }

    /**
     * 1.12.2縺ｮ block.setUnlocalizedName("my_block") 縺ｫ逶ｸ蠖薙・     * 迴ｾ莉｣縺ｧ縺ｯ鄙ｻ險ｳ繧ｭ繝ｼ縺ｯ閾ｪ蜍慕函謌舌＆繧後ｋ縺後∽ｺ呈鋤諤ｧ縺ｮ縺溘ａ繝輔ぅ繝ｼ繝ｫ繝峨↓菫晏ｭ倥☆繧九・     *
     * @return this (繝｡繧ｽ繝・ラ繝√ぉ繝ｼ繝ｳ逕ｨ)
     */
    public LegacyBlock setUnlocalizedName(String name) {
        this._unlocalizedName = name;
        return this;
    }

    public LegacyBlock setHardness(float hardness) {
        LOGGER.debug("[莠呈鋤繝ｬ繧､繝､繝ｼ] LegacyBlock setHardness({})", hardness);
        return this;
    }

    public LegacyBlock setResistance(float resistance) {
        LOGGER.debug("[莠呈鋤繝ｬ繧､繝､繝ｼ] LegacyBlock setResistance({})", resistance);
        return this;
    }

    /**
     * 逋ｻ骭ｲ蜷阪ｒ霑斐☆縲・.12.2Mod縺檎｢ｺ隱阪・縺溘ａ縺ｫ蜻ｼ縺ｶ縺薙→縺後≠繧九・     */
    public String getRegistryName() {
        return _legacyRegistryName;
    }

    // 笏笏笏 Material 竊・BlockBehaviour.Properties 螟画鋤 笏笏笏

    /**
     * 1.12.2縺ｮ Material 繧堤樟莉｣縺ｮ BlockBehaviour.Properties 縺ｫ螟画鋤縺吶ｋ縲・     * 險ｭ險域嶌縲檎ｬｬ4繝輔ぉ繝ｼ繧ｺ・咤lock縺ｨItem縺ｮ繝励Ο繝代ユ繧｣螟画鋤縲阪↓逶ｸ蠖薙・     */
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
