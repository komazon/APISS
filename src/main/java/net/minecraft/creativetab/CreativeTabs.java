package net.minecraft.creativetab;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

/**
 * 互換レイヤー: 1.12.2 の CreativeTabs の最小限スタブ。
 *
 * Legacy mod が古いクリエイティブタブ API を参照する際にクラス解決を通すための
 * 互換層です。
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
