package net.minecraftforge.fml.common.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 互換レイヤー: 1.12.2 の FMLServerStartingEvent のダミー実装。
 *
 * 1.12.2 Modは @EventHandler の付いたメソッドでこのイベントを受け取り、
 * サーバー起動時の初期化を行う。
 */
public class FMLServerStartingEvent extends FMLStateEvent {

    private static final Logger LOGGER = LoggerFactory.getLogger(FMLServerStartingEvent.class);

    private final Object server;

    public FMLServerStartingEvent(String modId, Object server) {
        super(modId);
        this.server = server;
        LOGGER.debug("[互換レイヤー] FMLServerStartingEvent を生成: modId={}", modId);
    }

    public String getModId() {
        return super.getModId();
    }

    public Object getServer() {
        return server;
    }
}
