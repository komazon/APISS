package net.minecraft.tileentity;

/**
 * 互換レイヤー: 1.12.2 の TileEntity のダミー実装。
 */
public class TileEntity {
    public boolean isInvalid() {
        return false;
    }

    public int getComparatorInputOverride(int side) {
        return 0;
    }
}
