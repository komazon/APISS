package net.minecraftforge.fml.common.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 互換レイヤー: 1.12.2 の EnchantmentHelper のダミー実装。
 */
public final class EnchantmentHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnchantmentHelper.class);

    private EnchantmentHelper() {
    }

    public static int getEnchantmentLevel(Object enchantment, Object itemStack) {
        LOGGER.debug("[互換レイヤー] EnchantmentHelper.getEnchantmentLevel({}, {})", enchantment, itemStack);
        return 0;
    }

    public static Object getEnchantedItem(Object enchantment, Object itemStack) {
        LOGGER.debug("[互換レイヤー] EnchantmentHelper.getEnchantedItem({}, {})", enchantment, itemStack);
        return null;
    }
}
