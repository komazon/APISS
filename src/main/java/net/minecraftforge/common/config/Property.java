package net.minecraftforge.common.config;

/**
 * 互換レイヤー：1.12.2 の net.minecraftforge.common.config.Property ダミー実装。
 * 旧 Forge Mod の設定登録に最低限対応する。
 */
public class Property {

    private String value;
    private final String comment;
    private final Type propertyType;

    /** Property の型定義 */
    public enum Type {
        STRING, BOOLEAN, INTEGER, DOUBLE
    }

    public Property(String value, String comment) {
        this(value, comment, Type.STRING);
    }

    public Property(String value, String comment, Type type) {
        this.value = value;
        this.comment = comment;
        this.propertyType = type;
    }

    public String getString() {
        return value;
    }

    public int getInt() {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public boolean getBoolean() {
        return Boolean.parseBoolean(value);
    }

    public double getDouble() {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public String getComment() {
        return comment;
    }

    public Type getType() {
        return propertyType;
    }

    public void set(String value) {
        this.value = value;
    }

    public void setComment(String comment) {
        // no-op for compatibility
    }

    /** 1.12.2 互換：boolean 値を設定 */
    public void set(boolean value) {
        this.value = Boolean.toString(value);
    }

    /** 1.12.2 互換：int 値を設定 */
    public void set(int value) {
        this.value = Integer.toString(value);
    }

    /** 1.12.2 互換：デフォルト値を取得（boolean） */
    public boolean getBoolean(boolean defaultValue) {
        if (value == null || value.isEmpty()) {
            return defaultValue;
        }
        return getBoolean();
    }

    /** 1.12.2 互換：デフォルト値を取得（int） */
    public int getInt(int defaultValue) {
        if (value == null || value.isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
