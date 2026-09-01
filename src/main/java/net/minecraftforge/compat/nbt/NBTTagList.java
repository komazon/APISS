package net.minecraftforge.compat.nbt;

import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import java.util.List;

/**
 * 旧 Forge の NBTTagList 互換ラッパー。
 */
public class NBTTagList extends NBTBase {
    public NBTTagList() {
        super(new ListTag());
    }

    public NBTTagList(NBTTagList other) {
        super(other.getDelegate().copy());
    }

    public NBTTagList(ListTag delegate) {
        super(delegate);
    }

    private ListTag delegate() {
        return (ListTag) super.getDelegate();
    }

    public void appendTag(Tag element) {
        delegate().add(element);
    }

    public Tag get(int index) {
        return delegate().get(index);
    }

    public int tagCount() {
        return delegate().size();
    }

    public void removeTag(int index) {
        delegate().remove(index);
    }

    @Override
    public ListTag getDelegate() {
        return (ListTag) super.getDelegate();
    }

    @Override
    public NBTTagList copy() {
        return new NBTTagList(this);
    }
}
