package net.minecraft.command;

/**
 * 互換レイヤー: 1.12.2 の CommandException のダミー実装。
 */
public class CommandException extends Exception {

    public CommandException(String message, Object... args) {
        super(String.format(message, args));
    }

    public CommandException(String message) {
        super(message);
    }
}
