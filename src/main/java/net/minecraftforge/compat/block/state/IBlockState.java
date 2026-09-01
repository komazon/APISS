package net.minecraftforge.compat.block.state;

/**
 * 莠呈鋤繝ｬ繧､繝､繝ｼ: 1.12.2 縺ｮ IBlockState 縺ｮ繝繝溘・螳溯｣・・
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
