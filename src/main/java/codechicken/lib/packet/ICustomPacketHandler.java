package codechicken.lib.packet;

/**
 * 互換レイヤー: CodeChickenLib の ICustomPacketHandler インターフェースのダミー実装。
 *
 * 旧 Mod がこのインターフェースを参照する場合、最低限クラス解決と型チェックを通す。
 */
public interface ICustomPacketHandler {
    void handlePacket(PacketCustom packet, Object sender);
}
