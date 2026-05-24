package net.minecraft.util;

/**
 * 互換レイヤー: 1.12.2 の AxisAlignedBB のダミー実装。
 */
public class AxisAlignedBB {
    public double minX, minY, minZ, maxX, maxY, maxZ;

    public AxisAlignedBB(double x1, double y1, double z1, double x2, double y2, double z2) {
        this.minX = Math.min(x1, x2);
        this.minY = Math.min(y1, y2);
        this.minZ = Math.min(z1, z2);
        this.maxX = Math.max(x1, x2);
        this.maxY = Math.max(y1, y2);
        this.maxZ = Math.max(z1, z2);
    }

    public static AxisAlignedBB fromBounds(double x1, double y1, double z1, double x2, double y2, double z2) {
        return new AxisAlignedBB(x1, y1, z1, x2, y2, z2);
    }

    public double getAverageEdgeLength() {
        return (maxX - minX + maxY - minY + maxZ - minZ) / 3.0;
    }
}
