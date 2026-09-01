package net.minecraftforge.compat.command;

/**
 * 莠呈鋤繝ｬ繧､繝､繝ｼ: 1.12.2 縺ｮ CommandException 縺ｮ繝繝溘・螳溯｣・・
 */
public class CommandException extends Exception {

    public CommandException(String message, Object... args) {
        super(String.format(message, args));
    }

    public CommandException(String message) {
        super(message);
    }
}
