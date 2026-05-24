package net.minecraftforge.fml.common.network;

import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

/**
 * 互換レイヤー: 1.12.2 の NetworkRegistry のダミー実装。
 *
 * 1.12.2 では NetworkRegistry.INSTANCE.registerGuiHandler(...) を使って
 * GUI ハンドラを登録します。NeoForge 互換レイヤーでは実際の登録処理を
 * 行わず、古い mod がクラスを解決できるようにします。
 */
public class NetworkRegistry {

    public static final NetworkRegistry INSTANCE = new NetworkRegistry();

    private NetworkRegistry() {
    }

    public static void registerGuiHandler(Object mod, IGuiHandler handler) {
        INSTANCE.registerGuiHandlerInstance(mod, handler);
    }

    public static SimpleNetworkWrapper newSimpleChannel(String channelName) {
        return INSTANCE.newSimpleChannelInstance(channelName);
    }

    public SimpleNetworkWrapper newSimpleChannelInstance(String channelName) {
        return new SimpleNetworkWrapper(channelName);
    }

    public void registerGuiHandlerInstance(Object mod, IGuiHandler handler) {
        // No-op compatibility stub. Legacy mods may register GUI handlers,
        // but NeoForge の現代 GUI システムではこの互換レイヤーで直接処理しない。
    }
}
