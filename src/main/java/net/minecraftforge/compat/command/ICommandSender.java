package net.minecraftforge.compat.command;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

/**
 * 莠呈鋤繝ｬ繧､繝､繝ｼ: 1.12.2 縺ｮ ICommandSender 縺ｮ繝繝溘・螳溯｣・・
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
