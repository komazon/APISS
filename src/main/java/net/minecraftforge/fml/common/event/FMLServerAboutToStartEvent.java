package net.minecraftforge.fml.common.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 互換レイヤー: 1.12.2 の FMLServerAboutToStartEvent のダミー実装。
 */
public class FMLServerAboutToStartEvent {

    private static final Logger LOGGER = LoggerFactory.getLogger(FMLServerAboutToStartEvent.class);

    private final String modId;
    private final Object server;

    public FMLServerAboutToStartEvent(String modId, Object server) {
        this.modId = modId;
        this.server = server;
        LOGGER.debug("[互換レイヤー] FMLServerAboutToStartEvent を生成: modId={}", modId);
    }

    public String getModId() {
        return modId;
    }

    public Object getServer() {
        return server;
    }
}
