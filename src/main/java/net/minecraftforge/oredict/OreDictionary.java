package net.minecraftforge.oredict;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 互換レイヤー: 1.12.2 の OreDictionary のダミー実装。
 */
public final class OreDictionary {
    private static final Map<String, List<Object>> REGISTRY = new HashMap<>();

    private OreDictionary() {
    }

    public static void registerOre(String name, Object ore) {
        REGISTRY.computeIfAbsent(name, key -> new ArrayList<>()).add(ore);
    }

    public static List<Object> getOres(String name) {
        return Collections.unmodifiableList(REGISTRY.getOrDefault(name, Collections.emptyList()));
    }

    public static int[] getOreIDs(Object itemStack) {
        return new int[0];
    }

    public static boolean contains(String name) {
        return REGISTRY.containsKey(name);
    }
}
