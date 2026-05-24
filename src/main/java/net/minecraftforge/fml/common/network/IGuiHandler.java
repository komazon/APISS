package net.minecraftforge.fml.common.network;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * 互換レイヤー: 1.12.2 の IGuiHandler インターフェースのダミー実装。
 *
 * 1.12.2 Mod が GUI を開く際にこのインターフェースを実装することで、
 * ネットワークと GUI の橋渡しを行います。
 *
 * LegacyClassLoader により古い型参照は現代型へリマップされるため、
 * ここでは現代の Player / Level を利用します。
 */
public interface IGuiHandler {

    Object getServerGuiElement(int id, Player player, Level world, int x, int y, int z);

    Object getClientGuiElement(int id, Player player, Level world, int x, int y, int z);
}
