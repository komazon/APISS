package net.minecraftforge.fml.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 互換レイヤー: 1.12.2 の Loader クラスのダミー実装。
 */
public class Loader {

    private static final Loader INSTANCE = new Loader();
    private final List<ModContainer> activeModList = new ArrayList<>();

    private Loader() {
    }

    public static Loader instance() {
        return INSTANCE;
    }

    public List<ModContainer> getActiveModList() {
        return Collections.unmodifiableList(activeModList);
    }

    public boolean isModLoaded(String modId) {
        return activeModList.stream().anyMatch(container -> container.getModId().equalsIgnoreCase(modId));
    }

    public boolean hasReachedState(Object state) {
        return false;
    }

    public String getMCVersionString() {
        return "1.12.2";
    }

    public void registerModContainer(ModContainer container) {
        activeModList.add(container);
    }
}
