package net.minecraftforge.compat.command;

import java.util.List;

/**
 * 莠呈鋤繝ｬ繧､繝､繝ｼ: 1.12.2 縺ｮ ICommand 繧､繝ｳ繧ｿ繝ｼ繝輔ぉ繝ｼ繧ｹ縺ｮ繝繝溘・螳溯｣・・
 *
 * Legacy mod 縺悟商縺・さ繝槭Φ繝・API 繧貞盾辣ｧ縺吶ｋ髫帙↓繧ｯ繝ｩ繧ｹ隗｣豎ｺ繧帝壹☆縺溘ａ縺ｮ
 * 譛蟆城剞縺ｮ莠呈鋤螻､縺ｧ縺吶・
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
