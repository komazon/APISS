package net.minecraftforge.compat.util.text;

public class TextComponentKeybind extends TextComponentBase {
    private final String keybind;

    public TextComponentKeybind(String keybind) {
        this.keybind = keybind;
    }

    @Override
    public String getUnformattedText() {
        return keybind;
    }

    @Override
    public ITextComponent appendText(String text) {
        appendSibling(new TextComponentString(text));
        return this;
    }

    @Override
    public ITextComponent createCopy() {
        return new TextComponentKeybind(keybind);
    }
}
