package net.minecraftforge.fml.common.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 互換レイヤー: 1.12.2の FMLPostInitializationEvent のダミー実装。
 *
 * 1.12.2ではこのフェーズでModが全ての登録が完了した後の後処理を行う。
 * 互換レイヤーは現代の RegisterEvent がすべて完了した後にこれを発火する。
 */
public class FMLPostInitializationEvent extends FMLStateEvent {

    private static final Logger LOGGER = LoggerFactory.getLogger(FMLPostInitializationEvent.class);

    public FMLPostInitializationEvent(String modId) {
        super(modId);
        LOGGER.debug("[互換レイヤー] FMLPostInitializationEvent を生成: modId={}", modId);
    }
}
