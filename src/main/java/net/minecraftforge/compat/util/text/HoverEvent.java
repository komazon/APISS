package net.minecraftforge.compat.util.text;

public class HoverEvent {
    private final Action action;
    private final ITextComponent value;

    public HoverEvent(Action action, ITextComponent value) {
        this.action = action;
        this.value = value;
    }

    public Action getAction() {
        return action;
    }

    public ITextComponent getValue() {
        return value;
    }

    public enum Action {
        SHOW_TEXT,
        SHOW_ITEM,
        SHOW_ENTITY
    }
}
