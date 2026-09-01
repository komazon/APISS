package net.minecraftforge.compat.command;

import java.util.Collections;
import java.util.List;

/**
 * 莠呈鋤繝ｬ繧､繝､繝ｼ: 1.12.2 縺ｮ CommandBase 縺ｮ繝繝溘・螳溯｣・・
 */
public abstract class CommandBase implements ICommand {

    @Override
    public int compareTo(ICommand command) {
        return getName().compareTo(command.getName());
    }

    @Override
    public List<String> getAliases() {
        return Collections.emptyList();
    }
}
