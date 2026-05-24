package net.minecraft.command;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

/**
 * 互換レイヤー: 1.12.2 の ICommandSender のダミー実装。
 */
public interface ICommandSender {

    String getName();

    Component getDisplayName();

    void sendMessage(Component component);

    boolean canUseCommand(int permLevel, String commandName);

    BlockPos getPosition();

    net.minecraft.world.phys.Vec3 getPositionVector();

    Level getEntityWorld();

    Entity getCommandSenderEntity();

    boolean sendCommandFeedback();

    void setCommandStat(CommandResultStats.Type type, int amount);
}
