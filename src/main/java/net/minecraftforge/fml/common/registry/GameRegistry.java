package net.minecraftforge.fml.common.registry;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.relauncher.Side;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 互換レイヤー: 1.12.2 の GameRegistry のダミー実装。
 */
public final class GameRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameRegistry.class);

    private GameRegistry() {
    }

    public static void registerBlock(Block block, String name) {
        LOGGER.debug("[互換レイヤー] GameRegistry.registerBlock({}, {})", block, name);
    }

    public static void registerBlock(Block block, Class<?> itemClass, String name) {
        LOGGER.debug("[互換レイヤー] GameRegistry.registerBlock({}, {}, {})", block, itemClass, name);
    }

    public static void registerItem(Item item, String name) {
        LOGGER.debug("[互換レイヤー] GameRegistry.registerItem({}, {})", item, name);
    }

    public static void registerTileEntity(Class<?> tileEntityClass, String name) {
        LOGGER.debug("[互換レイヤー] GameRegistry.registerTileEntity({}, {})", tileEntityClass, name);
    }

    public static void registerWorldGenerator(Object generator, int weight) {
        LOGGER.debug("[互換レイヤー] GameRegistry.registerWorldGenerator({}, {})", generator, weight);
    }

    public static void registerFuelHandler(Object handler) {
        LOGGER.debug("[互換レイヤー] GameRegistry.registerFuelHandler({})", handler);
    }

    public static void registerModEntity(Class<? extends Entity> entityClass, String entityName, int id, Object mod, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
        LOGGER.debug("[互換レイヤー] GameRegistry.registerModEntity({}, {}, {}, {}, {}, {}, {})",
                entityClass, entityName, id, mod, trackingRange, updateFrequency, sendsVelocityUpdates);
    }

    public static void registerGlobalEntityID(Class<? extends Entity> entityClass, String entityName, int id) {
        LOGGER.debug("[互換レイヤー] GameRegistry.registerGlobalEntityID({}, {}, {})",
                entityClass, entityName, id);
    }
}
