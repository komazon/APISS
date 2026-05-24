package net.minecraft.item;

import net.minecraft.world.item.Item;

/**
 * 互換レイヤー: 1.12.2 の ItemStack のダミー実装（委譲パターン）。
 */
public class ItemStack {
    private final net.minecraft.world.item.ItemStack delegate;

    public ItemStack(Item item) {
        this(item, 1);
    }

    public ItemStack(Item item, int count) {
        this.delegate = new net.minecraft.world.item.ItemStack(item, count);
    }

    public ItemStack(net.minecraft.world.item.ItemStack stack) {
        this.delegate = stack.copy();
    }

    public net.minecraft.world.item.ItemStack getDelegate() {
        return delegate;
    }
}
