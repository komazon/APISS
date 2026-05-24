package net.minecraftforge.fml.common.registry;

import net.minecraft.world.entity.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 互換レイヤー: 1.12.2 の RenderingRegistry のダミー実装。
 */
public final class RenderingRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(RenderingRegistry.class);

    private RenderingRegistry() {
    }

    public static void registerEntityRenderingHandler(Class<? extends Entity> entityClass, Object rendererFactory) {
        LOGGER.debug("[互換レイヤー] RenderingRegistry.registerEntityRenderingHandler({}, {})",
                entityClass, rendererFactory);
    }

    public static void registerBlockHandler(Object renderer) {
        LOGGER.debug("[互換レイヤー] RenderingRegistry.registerBlockHandler({})", renderer);
    }
}
