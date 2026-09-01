package net.minecraftforge.compat.creativetab;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

/**
 * 莠呈鋤繝ｬ繧､繝､繝ｼ: 1.12.2 縺ｮ CreativeTabs 縺ｮ譛蟆城剞繧ｹ繧ｿ繝悶・
 *
 * Legacy mod 縺悟商縺・け繝ｪ繧ｨ繧､繝・ぅ繝悶ち繝・API 繧貞盾辣ｧ縺吶ｋ髫帙↓繧ｯ繝ｩ繧ｹ隗｣豎ｺ繧帝壹☆縺溘ａ縺ｮ
 * 莠呈鋤螻､縺ｧ縺吶・
 */
public abstract class CreativeTabs {

    protected final String tabLabel;

    public CreativeTabs(String label) {
        this.tabLabel = label;
    }

    public String getTabLabel() {
        return this.tabLabel;
    }

    public String getTranslatedTabLabel() {
        return this.tabLabel;
    }

    public Component getTranslatedTabLabelComponent() {
        return Component.literal(this.tabLabel);
    }

    public abstract ItemStack getIconItemStack();

    public ItemStack getTabIconItem() {
        return getIconItemStack();
    }

    public boolean hasSearchBar() {
        return false;
    }

    public boolean drawInForegroundOfTab() {
        return false;
    }

    public String getBackgroundImageName() {
        return "item_search.png";
    }

    public int getTabColumn() {
        return 0;
    }

    public int getTabIndex() {
        return 0;
    }
}
