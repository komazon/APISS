package codechicken.lib.packet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 互換レイヤー: CodeChickenLib の PacketCustom のダミー実装。
 *
 * 旧 Mod がパケットを生成・送信しようとした場合に、最低限の型とメソッドを提供する。
 */
public class PacketCustom {
    private final String channel;
    private final List<Object> payload = new ArrayList<>();

    public PacketCustom(String channel) {
        this.channel = channel;
    }

    public PacketCustom(String channel, int packetId) {
        this.channel = channel;
        this.payload.add(packetId);
    }

    public String getChannel() {
        return channel;
    }

    public void writeInt(int value) {
        payload.add(value);
    }

    public void writeString(String value) {
        payload.add(value);
    }

    public void writeByteArray(byte[] data) {
        payload.add(data.clone());
    }

    public void writeObject(Object object) {
        payload.add(object);
    }

    public byte[] getPayload() {
        return new byte[0];
    }

    public void sendToServer() {
        // no-op stub for compatibility
    }

    public void sendToAllPlayers() {
        // no-op stub for compatibility
    }

    public void sendToPlayer(Object player) {
        // no-op stub for compatibility
    }

    public List<Object> getPayloadObjects() {
        return Collections.unmodifiableList(payload);
    }
}
