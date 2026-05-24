package net.minecraftforge.fml.common.network.simpleimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 互換レイヤー: 1.12.2 の SimpleNetworkWrapper のダミー実装。
 */
public class SimpleNetworkWrapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleNetworkWrapper.class);

    private final String channelName;
    private int discriminator = 0;

    public SimpleNetworkWrapper(String channelName) {
        this.channelName = channelName;
        LOGGER.debug("[互換レイヤー] SimpleNetworkWrapper を生成: {}", channelName);
    }

    public <REQ extends IMessage, REPLY extends IMessage> void registerMessage(Class<? extends IMessageHandler<REQ, REPLY>> handler,
                                                                                  Class<REQ> messageType,
                                                                                  int discriminator,
                                                                                  Object side) {
        LOGGER.debug("[互換レイヤー] registerMessage({}, {}, {}, {})",
                handler, messageType, discriminator, side);
    }

    public void sendToAll(IMessage message) {
        LOGGER.debug("[互換レイヤー] sendToAll({})", message);
    }

    public void sendTo(IMessage message, Object player) {
        LOGGER.debug("[互換レイヤー] sendTo({}, {})", message, player);
    }

    public void sendToServer(IMessage message) {
        LOGGER.debug("[互換レイヤー] sendToServer({})", message);
    }

    public String getChannelName() {
        return channelName;
    }
}
