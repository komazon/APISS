package net.minecraftforge.common.config;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 互換レイヤー: 1.12.2 の net.minecraftforge.common.config.Configuration ダミー実装。
 * 古い Forge Mod が設定ファイルを扱う際の最小限の API を提供する。
 */
public class Configuration {

    private final File file;
    private final Map<String, Property> properties = new LinkedHashMap<>();

    public Configuration(File file) {
        this.file = file;
    }

    public Configuration(File file, String version) {
        this(file);
    }

    public Configuration(File file, String version, boolean useCategoryComment) {
        this(file);
    }

    public Configuration(File file, String version, String modID, boolean useCategoryComment) {
        this(file);
    }

    public void load() {
        // no-op for compatibility
    }

    public void save() {
        // no-op for compatibility
    }

    public void saveIfChanged() {
        // no-op for compatibility
    }

    public Property get(String category, String key, String defaultValue) {
        return get(category, key, defaultValue, "");
    }

    public Property get(String category, String key, String defaultValue, String comment) {
        String fullKey = category + "." + key;
        return properties.computeIfAbsent(fullKey, k -> new Property(defaultValue, comment));
    }

    public void loadFromFile() {
        // no-op for compatibility
    }

    public Property get(String key) {
        return properties.computeIfAbsent(key, k -> new Property("", ""));
    }
}
