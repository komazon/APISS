package net.minecraftforge.fml.common.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 互換レイヤー: 1.12.2 の DimensionManager のダミー実装。
 */
public final class DimensionManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(DimensionManager.class);

    private DimensionManager() {
    }

    public static Object getDimensionType(int id) {
        LOGGER.debug("[互換レイヤー] DimensionManager.getDimensionType({})", id);
        return null;
    }

    public static int[] getDimensionIDs() {
        LOGGER.debug("[互換レイヤー] DimensionManager.getDimensionIDs()");
        return new int[]{0};
    }

    public static void registerDimension(int id, Object type) {
        LOGGER.debug("[互換レイヤー] DimensionManager.registerDimension({}, {})", id, type);
    }
}
