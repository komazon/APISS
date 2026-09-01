package net.minecraftforge.fml.common.network;

import net.minecraftforge.compat.network.SimpleNetworkWrapper;

/**
 * 互換レイヤー: 1.12.2 の NetworkRegistry のダミー実装。
 *
 * newSimpleChannel() はインスタンスメソッドとして提供する。
 * NGTLib は NetworkRegistry.INSTANCE.newSimpleChannel(...) のように
 * インスタンス経由で呼び出すため、static にしてはならない。
 */
public class NetworkRegistry {

    public static final NetworkRegistry INSTANCE = new NetworkRegistry();

    private NetworkRegistry() {
    }

    public static void registerGuiHandler(Object mod, IGuiHandler handler) {
        // No-op compatibility stub.
    }

    /** インスタンスメソッド — NGTLib などが INSTANCE.newSimpleChannel(...) で呼ぶ */
    public SimpleNetworkWrapper newSimpleChannel(String channelName) {
        return new SimpleNetworkWrapper(channelName);
    }

    public void registerGuiHandlerInstance(Object mod, IGuiHandler handler) {
        // No-op compatibility stub.
    }
}
