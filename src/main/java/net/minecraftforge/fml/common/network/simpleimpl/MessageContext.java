package net.minecraftforge.fml.common.network.simpleimpl;

/**
 * 互換レイヤー: 1.12.2 の MessageContext のダミー実装。
 */
public class MessageContext {
    private final boolean clientSide;

    public MessageContext(boolean clientSide) {
        this.clientSide = clientSide;
    }

    public boolean isClientSide() {
        return clientSide;
    }

    public boolean isServerSide() {
        return !clientSide;
    }

    public Object getServerHandler() {
        return null;
    }

    public Object getClientHandler() {
        return null;
    }
}
