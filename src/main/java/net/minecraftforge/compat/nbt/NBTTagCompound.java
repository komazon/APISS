package net.minecraftforge.compat.nbt;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import java.util.Set;

/**
 * 旧 Forge の NBTTagCompound 互換ラッパー。
 */
public class NBTTagCompound extends NBTBase {
    public NBTTagCompound() {
        super(new CompoundTag());
    }

    public NBTTagCompound(NBTTagCompound other) {
        super(other.getDelegate().copy());
    }

    public NBTTagCompound(CompoundTag delegate) {
        super(delegate);
    }

    private CompoundTag delegate() {
        return (CompoundTag) super.getDelegate();
    }

    public NBTTagCompound setString(String key, String value) {
        delegate().putString(key, value);
        return this;
    }

    public String getString(String key) {
        return delegate().getString(key).orElse("");
    }

    public NBTTagCompound setInteger(String key, int value) {
        delegate().putInt(key, value);
        return this;
    }

    public int getInteger(String key) {
        return delegate().getInt(key).orElse(0);
    }

    public NBTTagCompound setBoolean(String key, boolean value) {
        delegate().putBoolean(key, value);
        return this;
    }

    public boolean getBoolean(String key) {
        return delegate().getBoolean(key).orElse(false);
    }

    public boolean hasKey(String key) {
        return delegate().contains(key);
    }

    public void removeTag(String key) {
        delegate().remove(key);
    }

    public Set<String> getKeySet() {
        return delegate().keySet();
    }

    public Tag getTag(String key) {
        return delegate().get(key);
    }

    @Override
    public CompoundTag getDelegate() {
        return (CompoundTag) super.getDelegate();
    }

    @Override
    public NBTTagCompound copy() {
        return new NBTTagCompound(this);
    }
}
