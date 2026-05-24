package net.minecraft.command;

import java.util.List;

/**
 * 互換レイヤー: 1.12.2 の ICommand インターフェースのダミー実装。
 *
 * Legacy mod が古いコマンド API を参照する際にクラス解決を通すための
 * 最小限の互換層です。
 */
public interface ICommand extends Comparable<ICommand> {

    String getName();

    String getUsage(ICommandSender sender);

    List<String> getAliases();

    void execute(net.minecraft.server.MinecraftServer server, ICommandSender sender, String[] args) throws CommandException;

    boolean checkPermission(net.minecraft.server.MinecraftServer server, ICommandSender sender);

    java.util.List<String> getTabCompletions(net.minecraft.server.MinecraftServer server, ICommandSender sender, String[] args, net.minecraft.core.BlockPos targetPos);

    boolean isUsernameIndex(String[] args, int index);
}
