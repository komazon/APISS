package net.minecraftforge.compat.util.text;

public class TextComponentSelector extends TextComponentBase {
    private final String selector;

    public TextComponentSelector(String selector) {
        this.selector = selector;
    }

    @Override
    public String getUnformattedText() {
        return selector;
    }

    @Override
    public ITextComponent appendText(String text) {
        appendSibling(new TextComponentString(text));
        return this;
    }

    @Override
    public ITextComponent createCopy() {
        return new TextComponentSelector(selector);
    }
}
