package net.minecraftforge.fml.common.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 互換レイヤー: 1.12.2の FMLInitializationEvent のダミー実装。
 *
 * 1.12.2ではこのフェーズでModが他のModとの連携登録を行う。
 * 互換レイヤーは現代の FMLCommonSetupEvent 後に相当するタイミングでこれを発火する。
 */
public class FMLInitializationEvent {

    private static final Logger LOGGER = LoggerFactory.getLogger(FMLInitializationEvent.class);

    private final String modId;

    public FMLInitializationEvent(String modId) {
        this.modId = modId;
        LOGGER.debug("[互換レイヤー] FMLInitializationEvent を生成: modId={}", modId);
    }

    public String getModId() {
        return modId;
    }
}
