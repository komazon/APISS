package net.minecraftforge.compat.nbt;

import net.minecraft.nbt.Tag;

/**
 * 旧 Forge の NBT 系互換基底型。
 */
public abstract class NBTBase {
    private final Tag delegate;

    protected NBTBase(Tag delegate) {
        this.delegate = delegate;
    }

    public Tag getDelegate() {
        return delegate;
    }

    public byte getId() {
        return delegate.getId();
    }

    public abstract NBTBase copy();

    @Override
    public String toString() {
        return delegate.toString();
    }
}
