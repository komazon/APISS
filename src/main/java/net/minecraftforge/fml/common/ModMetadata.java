package net.minecraftforge.fml.common;

import java.util.ArrayList;
import java.util.List;

/**
 * 互換レイヤー: 1.12.2 の ModMetadata のダミー実装。
 */
public class ModMetadata {
    public String modId;
    public String name = "";
    public String version = "";
    public String description = "";
    public String url = "";
    public String updateUrl = "";
    public String credits = "";
    public List<String> authorList = new ArrayList<>();
    public List<String> screenShots = new ArrayList<>();
    public String logoFile = "";
    public boolean useDependencyInformation = false;
    public String parent = "";
    public List<String> childMods = new ArrayList<>();
    public String dependencies = "";
    public String requiredMods = "";

    public ModMetadata() {
    }

    public ModMetadata(String modId) {
        this.modId = modId;
    }
}
