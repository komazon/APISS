package net.minecraftforge.fml.common.registry;

import net.minecraftforge.fml.relauncher.Side;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 互換レイヤー: 1.12.2 の TickRegistry のダミー実装。
 */
public final class TickRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(TickRegistry.class);

    private TickRegistry() {
    }

    public static void registerTickHandler(Object handler, Side side) {
        LOGGER.debug("[互換レイヤー] TickRegistry.registerTickHandler({}, {})", handler, side);
    }

    public static void registerTickHandler(Object handler, Side side, Object... types) {
        LOGGER.debug("[互換レイヤー] TickRegistry.registerTickHandler({}, {}, {})",
                handler, side, types);
    }
}
