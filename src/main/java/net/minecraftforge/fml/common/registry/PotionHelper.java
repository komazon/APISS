package net.minecraftforge.fml.common.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 互換レイヤー: 1.12.2 の PotionHelper のダミー実装。
 */
public final class PotionHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(PotionHelper.class);

    private PotionHelper() {
    }

    public static void registerPotionType(Object potion) {
        LOGGER.debug("[互換レイヤー] PotionHelper.registerPotionType({})", potion);
    }

    public static Object getPotionTypeForName(String name) {
        LOGGER.debug("[互換レイヤー] PotionHelper.getPotionTypeForName({})", name);
        return null;
    }
}
