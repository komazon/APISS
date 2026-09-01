package net.minecraftforge.compat.util.text.translation;

/**
 * Old Forge translation compatibility stub.
 */
public class I18n {
    public static boolean canTranslate(String key) {
        return false;
    }

    public static String translateToLocal(String key) {
        return key;
    }

    public static String translateToLocalFormatted(String key, Object... args) {
        try {
            return String.format(translateToLocal(key), args);
        } catch (Exception e) {
            return translateToLocal(key);
        }
    }

    public static String translateToFallback(String key) {
        return key;
    }
}
