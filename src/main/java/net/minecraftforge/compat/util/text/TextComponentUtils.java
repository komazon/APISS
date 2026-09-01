package net.minecraftforge.compat.util.text;

public class TextComponentUtils {
    public static String toPlainText(ITextComponent component) {
        if (component == null) {
            return "";
        }
        return component.getFormattedText();
    }

    public static ITextComponent mergeStyles(ITextComponent target, ITextComponent source) {
        if (target == null || source == null) {
            return target;
        }
        if (target.getStyle() != null && source.getStyle() != null) {
            target.getStyle().setClickEvent(source.getStyle().getClickEvent());
            target.getStyle().setHoverEvent(source.getStyle().getHoverEvent());
            target.getStyle().setColor(source.getStyle().getColor());
            target.getStyle().setBold(source.getStyle().getBold());
            target.getStyle().setItalic(source.getStyle().getItalic());
            target.getStyle().setUnderlined(source.getStyle().getUnderlined());
            target.getStyle().setStrikethrough(source.getStyle().getStrikethrough());
            target.getStyle().setObfuscated(source.getStyle().getObfuscated());
            target.getStyle().setInsertion(source.getStyle().getInsertion());
            target.getStyle().setFont(source.getStyle().getFont());
        }
        return target;
    }
}
