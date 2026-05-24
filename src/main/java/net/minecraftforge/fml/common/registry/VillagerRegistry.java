package net.minecraftforge.fml.common.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 互換レイヤー: 1.12.2 の VillagerRegistry のダミー実装。
 */
public final class VillagerRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(VillagerRegistry.class);

    private VillagerRegistry() {
    }

    public static void registerVillagerSkin(int id, Object skin) {
        LOGGER.debug("[互換レイヤー] VillagerRegistry.registerVillagerSkin({}, {})", id, skin);
    }

    public static void registerVillagerTrade(Object profession, Object trade) {
        LOGGER.debug("[互換レイヤー] VillagerRegistry.registerVillagerTrade({}, {})", profession, trade);
    }
}
