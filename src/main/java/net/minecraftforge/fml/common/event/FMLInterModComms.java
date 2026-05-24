package net.minecraftforge.fml.common.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public class FMLInterModComms {

    private static final Logger LOGGER = LoggerFactory.getLogger(FMLInterModComms.class);

    public static class IMCEvent extends FMLStateEvent {

        public IMCEvent(String modId) {
            super(modId);
            LOGGER.debug("[互換レイヤー] FMLInterModComms.IMCEvent を生成: modId={}", modId);
        }

        public List<IMCMessage> getMessages() {
            return Collections.emptyList();
        }
    }

    public static void sendMessage(String modId, String key, Object message) {
        LOGGER.debug("[互換レイヤー] FMLInterModComms.sendMessage: modId={}, key={}, message={}",
                modId, key, message);
    }

    public interface IMCMessage {
        default String key() {
            return "";
        }

        default Object getMessage() {
            return null;
        }
    }
}
