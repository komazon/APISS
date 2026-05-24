package net.minecraftforge.common.capabilities;

/**
 * 互換レイヤー: 1.12.2 の Capability のダミー実装。
 */
public interface Capability<T> {
    String getName();
    Class<T> getInterfaceClass();
}
