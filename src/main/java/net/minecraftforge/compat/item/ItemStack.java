package net.minecraftforge.compat.item;

import net.minecraft.world.item.Item;

/**
 * 莠呈鋤繝ｬ繧､繝､繝ｼ: 1.12.2 縺ｮ ItemStack 縺ｮ繝繝溘・螳溯｣・ｼ亥ｧ碑ｭｲ繝代ち繝ｼ繝ｳ・峨・
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
