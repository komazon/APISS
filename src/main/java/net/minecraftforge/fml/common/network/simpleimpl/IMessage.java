package net.minecraftforge.fml.common.network.simpleimpl;

import io.netty.buffer.ByteBuf;

/**
 * 互換レイヤー: 1.12.2 の IMessage のダミー実装。
 */
public interface IMessage {
    void fromBytes(ByteBuf buf);
    void toBytes(ByteBuf buf);
}
