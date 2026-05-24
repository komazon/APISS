package net.minecraftforge.fml.common.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FMLServerStoppingEvent extends FMLStateEvent {

    private static final Logger LOGGER = LoggerFactory.getLogger(FMLServerStoppingEvent.class);

    public FMLServerStoppingEvent(String modId, Object server) {
        super(modId);
        LOGGER.debug("[互換レイヤー] FMLServerStoppingEvent を生成: modId={}", modId);
    }
}
