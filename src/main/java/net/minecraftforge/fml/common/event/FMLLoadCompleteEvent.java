package net.minecraftforge.fml.common.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FMLLoadCompleteEvent extends FMLStateEvent {

    private static final Logger LOGGER = LoggerFactory.getLogger(FMLLoadCompleteEvent.class);

    public FMLLoadCompleteEvent(String modId) {
        super(modId);
        LOGGER.debug("[互換レイヤー] FMLLoadCompleteEvent を生成: modId={}", modId);
    }
}
