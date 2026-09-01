package net.minecraftforge.common.config;

/**
 * 互換レイヤー: 1.12.2 の net.minecraftforge.common.config.Property ダミー実装。
 * 旧Forge Mod の設定登録に最低限対応する。
 */
public class Property {

    private final String value;
    private final String comment;

    public Property(String value, String comment) {
        this.value = value;
        this.comment = comment;
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

    public void set(String value) {
        // no-op for compatibility
    }

    public void setComment(String comment) {
        // no-op for compatibility
    }
}
