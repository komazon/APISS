package net.minecraft.command;

import java.util.Collections;
import java.util.List;

/**
 * 互換レイヤー: 1.12.2 の CommandBase のダミー実装。
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
