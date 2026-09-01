package net.minecraftforge.compat.util.text;

public class TextComponentString extends TextComponentBase {
    private final String text;

    public TextComponentString(String text) {
        this.text = text;
    }

    @Override
    public String getUnformattedText() {
        return text;
    }

    @Override
    public ITextComponent appendText(String text) {
        appendSibling(new TextComponentString(text));
        return this;
    }

    @Override
    public ITextComponent createCopy() {
        return new TextComponentString(text);
    }
}
