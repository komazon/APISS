package net.minecraftforge.compat.util.text;

import java.util.ArrayList;
import java.util.List;

public abstract class TextComponentBase implements ITextComponent {
    private final List<ITextComponent> siblings = new ArrayList<>();
    private Style style = new Style();

    @Override
    public ITextComponent appendSibling(ITextComponent component) {
        siblings.add(component);
        return this;
    }

    @Override
    public List<ITextComponent> getSiblings() {
        return siblings;
    }

    @Override
    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style == null ? new Style() : style;
    }

    @Override
    public String getFormattedText() {
        StringBuilder builder = new StringBuilder(getUnformattedText());
        for (ITextComponent sibling : siblings) {
            builder.append(sibling.getFormattedText());
        }
        return builder.toString();
    }
}
