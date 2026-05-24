package net.minecraftforge.fml.common.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FMLServerStartedEvent extends FMLStateEvent {

    private static final Logger LOGGER = LoggerFactory.getLogger(FMLServerStartedEvent.class);

    public FMLServerStartedEvent(String modId, Object server) {
        super(modId);
        LOGGER.debug("[互換レイヤー] FMLServerStartedEvent を生成: modId={}", modId);
    }
}
