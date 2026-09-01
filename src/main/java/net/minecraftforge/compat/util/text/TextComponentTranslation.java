package net.minecraftforge.compat.util.text;

public class TextComponentTranslation extends TextComponentBase {
    private final String key;
    private final Object[] args;

    public TextComponentTranslation(String key, Object... args) {
        this.key = key;
        this.args = args;
    }

    @Override
    public String getUnformattedText() {
        return key;
    }

    @Override
    public ITextComponent appendText(String text) {
        appendSibling(new TextComponentString(text));
        return this;
    }

    @Override
    public ITextComponent createCopy() {
        return new TextComponentTranslation(key, args);
    }

    public Object[] getFormatArgs() {
        return args;
    }
}
