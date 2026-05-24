package net.minecraftforge.fml.common.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FMLConstructionEvent extends FMLStateEvent {

    private static final Logger LOGGER = LoggerFactory.getLogger(FMLConstructionEvent.class);

    public FMLConstructionEvent(String modId) {
        super(modId);
        LOGGER.debug("[互換レイヤー] FMLConstructionEvent を生成: modId={}", modId);
    }
}
