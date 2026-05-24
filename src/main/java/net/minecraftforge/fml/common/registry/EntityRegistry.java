package net.minecraftforge.fml.common.registry;

import net.minecraft.world.entity.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 互換レイヤー: 1.12.2 の EntityRegistry のダミー実装。
 */
public final class EntityRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(EntityRegistry.class);

    private EntityRegistry() {
    }

    public static void registerGlobalEntityID(Class<? extends Entity> entityClass, String entityName, int id) {
        LOGGER.debug("[互換レイヤー] EntityRegistry.registerGlobalEntityID({}, {}, {})", entityClass, entityName, id);
    }

    public static void registerModEntity(Class<? extends Entity> entityClass, String entityName, int id, Object mod, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
        LOGGER.debug("[互換レイヤー] EntityRegistry.registerModEntity({}, {}, {}, {}, {}, {}, {})",
                entityClass, entityName, id, mod, trackingRange, updateFrequency, sendsVelocityUpdates);
    }
}
