package net.minecraftforge.fml.common.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FMLStateEvent {

    private static final Logger LOGGER = LoggerFactory.getLogger(FMLStateEvent.class);
    private final String modId;

    public FMLStateEvent(String modId) {
        this.modId = modId;
        LOGGER.debug("[互換レイヤー] FMLStateEvent を生成: modId={}", modId);
    }

    public String getModId() {
        return modId;
    }
}
