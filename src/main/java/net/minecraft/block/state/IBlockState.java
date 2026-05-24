package net.minecraft.block.state;

/**
 * 互換レイヤー: 1.12.2 の IBlockState のダミー実装。
 */
public interface IBlockState {
    default Object getBlock() {
        return null;
    }

    default Object getMaterial() {
        return null;
    }

    default boolean isFullBlock() {
        return false;
    }

    default int getLightValue() {
        return 0;
    }
}
