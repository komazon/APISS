package net.minecraftforge.registries;

import net.minecraftforge.compat.util.ResourceLocation;

/**
 * 互換レイヤー: 1.12.2 の IForgeRegistryEntry インターフェースのダミー定義。
 *
 * 旧Forge ではレジストリ登録可能なオブジェクトがこのインターフェースを実装しており、
 * setRegistryName()/getRegistryName() を呼び出せることが期待される。
 */
public interface IForgeRegistryEntry<T extends IForgeRegistryEntry<T>> {

    T setRegistryName(ResourceLocation name);

    T setRegistryName(String modID, String name);

    T setRegistryName(String name);

    ResourceLocation getRegistryName();

    boolean hasRegistryName();
}
