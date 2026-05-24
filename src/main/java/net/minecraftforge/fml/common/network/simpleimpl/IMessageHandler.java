package net.minecraftforge.fml.common.network.simpleimpl;

/**
 * 互換レイヤー: 1.12.2 の IMessageHandler のダミー実装。
 */
public interface IMessageHandler<REQ extends IMessage, REPLY extends IMessage> {
    REPLY onMessage(REQ message, MessageContext ctx);
}
