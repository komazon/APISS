package net.minecraftforge.compat.tileentity;

/**
 * 莠呈鋤繝ｬ繧､繝､繝ｼ: 1.12.2 縺ｮ TileEntity 縺ｮ繝繝溘・螳溯｣・・
 */
public class TileEntity {
    public boolean isInvalid() {
        return false;
    }

    public int getComparatorInputOverride(int side) {
        return 0;
    }
}
