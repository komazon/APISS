package net.minecraftforge.fml.common;

import net.minecraftforge.fml.relauncher.Side;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 互換レイヤー: 1.12.2 の FMLCommonHandler のダミー実装。
 */
public class FMLCommonHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(FMLCommonHandler.class);
    private static final FMLCommonHandler INSTANCE = new FMLCommonHandler();

    private FMLCommonHandler() {
    }

    public static FMLCommonHandler instance() {
        return INSTANCE;
    }

    public Side getEffectiveSide() {
        // NeoForge の実行環境に合わせてクライアント側を返す。
        return Side.CLIENT;
    }

    public void handleModStateEvent(Object event) {
        LOGGER.debug("[互換レイヤー] FMLCommonHandler.handleModStateEvent: {}", event);
    }
}
