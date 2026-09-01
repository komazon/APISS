package net.minecraftforge.compat.network;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 互換レイヤー: 1.12.2 の net.minecraftforge.compat.network.SimpleNetworkWrapper のダミー実装。
 *
 * NGTLib などの旧 Mod が NetworkRegistry.newSimpleChannel() の戻り値型として
 * net.minecraftforge.compat.network.SimpleNetworkWrapper を期待するため、
 * このクラスを提供する。
 */
public class SimpleNetworkWrapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleNetworkWrapper.class);

    private final String channelName;

    public SimpleNetworkWrapper(String channelName) {
        this.channelName = channelName;
        LOGGER.debug("[互換レイヤー] compat.network.SimpleNetworkWrapper を生成: {}", channelName);
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
