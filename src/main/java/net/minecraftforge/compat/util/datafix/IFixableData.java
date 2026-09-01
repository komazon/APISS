package net.minecraftforge.compat.util.datafix;

import net.minecraftforge.compat.nbt.NBTTagCompound;

public interface IFixableData {
    int getFixVersion();
    NBTTagCompound fixTagCompound(NBTTagCompound compoundTag);
}
