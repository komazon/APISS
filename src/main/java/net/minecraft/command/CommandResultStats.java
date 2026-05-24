package net.minecraft.command;

/**
 * 互換レイヤー: 1.12.2 の CommandResultStats の最小限スタブ。
 */
public class CommandResultStats {

    public enum Type {
        SUCCESS_COUNT,
        AFFECTED_ENTITIES,
        AFFECTED_BLOCKS,
        AFFECTED_ROWS,
        AFFECTED_ITEMS,
        QUERY_RESULT
    }
}
