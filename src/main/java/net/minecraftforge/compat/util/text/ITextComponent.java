package net.minecraftforge.compat.util.text;

import java.util.List;

public interface ITextComponent {
    String getUnformattedText();
    String getFormattedText();
    ITextComponent appendText(String text);
    ITextComponent appendSibling(ITextComponent component);
    List<ITextComponent> getSiblings();
    Style getStyle();
    ITextComponent createCopy();

    class Style {
        private ClickEvent clickEvent;
        private HoverEvent hoverEvent;
        private TextFormatting color;
        private Boolean bold;
        private Boolean italic;
        private Boolean underlined;
        private Boolean strikethrough;
        private Boolean obfuscated;
        private String insertion;
        private String font;

        public ClickEvent getClickEvent() {
            return clickEvent;
        }

        public HoverEvent getHoverEvent() {
            return hoverEvent;
        }

        public TextFormatting getColor() {
            return color;
        }

        public Boolean getBold() {
            return bold;
        }

        public Boolean getItalic() {
            return italic;
        }

        public Boolean getUnderlined() {
            return underlined;
        }

        public Boolean getStrikethrough() {
            return strikethrough;
        }

        public Boolean getObfuscated() {
            return obfuscated;
        }

        public String getInsertion() {
            return insertion;
        }

        public String getFont() {
            return font;
        }

        public Style setClickEvent(ClickEvent clickEvent) {
            this.clickEvent = clickEvent;
            return this;
        }

        public Style setHoverEvent(HoverEvent hoverEvent) {
            this.hoverEvent = hoverEvent;
            return this;
        }

        public Style setColor(TextFormatting color) {
            this.color = color;
            return this;
        }

        public Style setBold(Boolean bold) {
            this.bold = bold;
            return this;
        }

        public Style setItalic(Boolean italic) {
            this.italic = italic;
            return this;
        }

        public Style setUnderlined(Boolean underlined) {
            this.underlined = underlined;
            return this;
        }

        public Style setStrikethrough(Boolean strikethrough) {
            this.strikethrough = strikethrough;
            return this;
        }

        public Style setObfuscated(Boolean obfuscated) {
            this.obfuscated = obfuscated;
            return this;
        }

        public Style setInsertion(String insertion) {
            this.insertion = insertion;
            return this;
        }

        public Style setFont(String font) {
            this.font = font;
            return this;
        }

        public Style createShallowCopy() {
            Style copy = new Style();
            copy.clickEvent = this.clickEvent;
            copy.hoverEvent = this.hoverEvent;
            copy.color = this.color;
            copy.bold = this.bold;
            copy.italic = this.italic;
            copy.underlined = this.underlined;
            copy.strikethrough = this.strikethrough;
            copy.obfuscated = this.obfuscated;
            copy.insertion = this.insertion;
            copy.font = this.font;
            return copy;
        }
    }

    class Serializer {
        public static ITextComponent jsonToComponent(String json) {
            return new TextComponentString(json);
        }

        public static String componentToJson(ITextComponent component) {
            return component == null ? "" : component.getFormattedText();
        }
    }
}
