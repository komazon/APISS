package net.minecraftforge.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 1.12.2 の MinecraftForge クラスをエミュレートする互換クラス。
 * 現代の NeoForge では FML や ModLoading への移行が進んでおり、
 * 多くの静的メソッドは不要または代替手段が存在します。
 */
public class MinecraftForge {
    private static final Logger LOGGER = LoggerFactory.getLogger("MinecraftForgeCompat");

    public static final net.minecraftforge.fml.common.eventhandler.EventBus EVENT_BUS = new net.minecraftforge.fml.common.eventhandler.EventBus();

    public static void registerEventBus(Object listener) {
        EVENT_BUS.register(listener);
    }

    public static void registerCapability(Object capability, Object storage) {
        LOGGER.info("Legacy capability registration requested: {} with storage {}", 
                    capability != null ? capability.getClass().getName() : "null", 
                    storage != null ? storage.getClass().getName() : "null");
        // Capability$IStorage への参照を避けるため、汎用的な処理に留めます。
    }

    // 1.12.2 の Mod で頻繁に呼ばれるその他の静的メソッドをここに追加します。
}
