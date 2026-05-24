package net.minecraft.nbt;

import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import java.util.ArrayList;
import java.util.List;

/**
 * 互換レイヤー: 1.12.2 の NBTTagList のダミー実装（委譲パターン）。
 */
public class NBTTagList {
    private final ListTag delegate;

    public NBTTagList() {
        this.delegate = new ListTag();
    }

    public void appendTag(Tag element) {
        delegate.add(element);
    }

    public Tag get(int index) {
        return delegate.get(index);
    }

    public int tagCount() {
        return delegate.size();
    }

    public void removeTag(int index) {
        delegate.remove(index);
    }

    public ListTag getDelegate() {
        return delegate;
    }
}
