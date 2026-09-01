package net.minecraftforge.compat.util.text;

public class TextComponentScore extends TextComponentBase {
    private final String name;
    private final String objective;
    private String value;

    public TextComponentScore(String name, String objective) {
        this(name, objective, null);
    }

    public TextComponentScore(String name, String objective, String value) {
        this.name = name;
        this.objective = objective;
        this.value = value;
    }

    @Override
    public String getUnformattedText() {
        return value == null ? "" : value;
    }

    @Override
    public ITextComponent appendText(String text) {
        appendSibling(new TextComponentString(text));
        return this;
    }

    @Override
    public ITextComponent createCopy() {
        return new TextComponentScore(name, objective, value);
    }

    public String getName() {
        return name;
    }

    public String getObjective() {
        return objective;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
