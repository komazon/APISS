package net.minecraftforge.fml.common;

import java.util.Collections;
import java.util.List;

/**
 * 互換レイヤー: 1.12.2 の ModContainer のダミー実装。
 */
public class ModContainer {

    private final String modId;
    private final String name;
    private final String version;

    public ModContainer(String modId, String name, String version) {
        this.modId = modId;
        this.name = name;
        this.version = version;
    }

    public String getModId() {
        return modId;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public List<String> getDependencies() {
        return Collections.emptyList();
    }
}
