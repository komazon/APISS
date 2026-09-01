package net.minecraftforge.compat.item;

import net.minecraft.world.item.Item;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 莠呈鋤繝ｬ繧､繝､繝ｼ: 1.12.2縺ｮ Item 繧ｯ繝ｩ繧ｹ縺ｮ繝悶Μ繝・ず螳溯｣・・ *
 * 1.12.2縺ｧ縺ｯ:
 *   new Item()
 *   item.setRegistryName("mymod", "my_item")
 *   item.setUnlocalizedName("my_item")
 *   item.setMaxStackSize(1)
 *   item.setMaxDamage(250)
 *
 * 縺薙・繧ｯ繝ｩ繧ｹ縺梧立繧ｹ繧ｿ繧､繝ｫ縺ｮ繝｡繧ｽ繝・ラ繝√ぉ繝ｼ繝ｳ繧貞女縺大叙繧翫・ * 蜀・Κ縺ｧ迴ｾ莉｣縺ｮ Item 縺ｨ縺励※蜍穂ｽ懊☆繧九ｈ縺・､画鋤縺吶ｋ縲・ */
public class LegacyItem extends Item {

    private static final Logger LOGGER = LoggerFactory.getLogger(LegacyItem.class);

    /** IForgeRegistry縺檎匳骭ｲ蜷阪ｒ蜿悶ｊ蜃ｺ縺吶◆繧√↓蜿ら・縺吶ｋ繝輔ぅ繝ｼ繝ｫ繝・*/
    String _legacyRegistryName = null;

    String _unlocalizedName = null;
    int _legacyMaxStackSize = 64;
    int _legacyMaxDamage = 0;

    // 笏笏笏 1.12.2繧ｹ繧ｿ繧､繝ｫ縺ｮ繧ｳ繝ｳ繧ｹ繝医Λ繧ｯ繧ｿ 笏笏笏

    /**
     * 1.12.2縺ｮ new Item() 縺ｫ逶ｸ蠖薙☆繧九ョ繝輔か繝ｫ繝医さ繝ｳ繧ｹ繝医Λ繧ｯ繧ｿ縲・     * 迴ｾ莉｣縺ｮ Item.Properties() 繧貞・驛ｨ縺ｧ逕滓・縺吶ｋ縲・     */
    public LegacyItem() {
        super(new Item.Properties());
        LOGGER.debug("[莠呈鋤繝ｬ繧､繝､繝ｼ] LegacyItem 逕滓・");
    }

    /**
     * 迴ｾ莉｣縺ｮProperties繧堤峩謗･蜿励￠蜿悶ｋ繧ｳ繝ｳ繧ｹ繝医Λ繧ｯ繧ｿ・医し繝悶け繝ｩ繧ｹ逕ｨ・峨・     */
    protected LegacyItem(Item.Properties properties) {
        super(properties);
    }

    // 笏笏笏 1.12.2縺ｮ繝｡繧ｽ繝・ラ繝√ぉ繝ｼ繝ｳ 笏笏笏

    public LegacyItem setRegistryName(String domain, String path) {
        this._legacyRegistryName = domain + ":" + path;
        LOGGER.debug("[莠呈鋤繝ｬ繧､繝､繝ｼ] Item RegistryName 險ｭ螳・ {}", this._legacyRegistryName);
        return this;
    }

    public LegacyItem setRegistryName(String registryName) {
        this._legacyRegistryName = registryName;
        return this;
    }

    public LegacyItem setUnlocalizedName(String name) {
        this._unlocalizedName = name;
        return this;
    }

    /**
     * 1.12.2縺ｮ setMaxStackSize() 窶・迴ｾ莉｣縺ｧ縺ｯ Properties 縺ｧ險ｭ螳壹☆繧九′縲・     * 縺薙・繝｡繧ｽ繝・ラ縺ｯ繧ｹ繧ｿ繝悶→縺励※蜿励￠蜿悶ｋ縺縺托ｼ亥ｾ檎ｶ壹・Phase縺ｧ蟇ｾ蠢懶ｼ峨・     */
    public LegacyItem setMaxStackSize(int maxStackSize) {
        this._legacyMaxStackSize = maxStackSize;
        LOGGER.debug("[莠呈鋤繝ｬ繧､繝､繝ｼ] LegacyItem setMaxStackSize({})", maxStackSize);
        return this;
    }

    /**
     * 1.12.2縺ｮ setMaxDamage() 窶・閠蝉ｹ・､險ｭ螳壹ゅせ繧ｿ繝悶・     */
    public LegacyItem setMaxDamage(int maxDamage) {
        this._legacyMaxDamage = maxDamage;
        LOGGER.debug("[莠呈鋤繝ｬ繧､繝､繝ｼ] LegacyItem setMaxDamage({})", maxDamage);
        return this;
    }

    public int getLegacyMaxStackSize() {
        return _legacyMaxStackSize;
    }

    public int getLegacyMaxDamage() {
        return _legacyMaxDamage;
    }

    public String getRegistryName() {
        return _legacyRegistryName;
    }
}
