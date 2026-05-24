package net.minecraftforge.fml.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 互換レイヤー: 1.12.2 の FMLLog のダミー実装。
 */
public final class FMLLog {
    private static final Logger LOGGER = LoggerFactory.getLogger("FML");

    private FMLLog() {
    }

    public static void log(Level level, String format, Object... data) {
        switch (level) {
            case INFO -> LOGGER.info(format, data);
            case WARN -> LOGGER.warn(format, data);
            case ERROR -> LOGGER.error(format, data);
            case DEBUG -> LOGGER.debug(format, data);
            case FATAL -> LOGGER.error(format, data);
        }
    }

    public static void info(String format, Object... data) {
        LOGGER.info(format, data);
    }

    public static void warn(String format, Object... data) {
        LOGGER.warn(format, data);
    }

    public static void error(String format, Object... data) {
        LOGGER.error(format, data);
    }

    public static void debug(String format, Object... data) {
        LOGGER.debug(format, data);
    }

    public enum Level {
        INFO,
        WARN,
        ERROR,
        DEBUG,
        FATAL
    }
}
