package net.minecraftforge.compat.util;

/**
 * 莠呈鋤繝ｬ繧､繝､繝ｼ: 1.12.2 縺ｮ ResourceLocation 縺ｮ繝繝溘・螳溯｣・・
 */
public class ResourceLocation {
    private final String namespace;
    private final String path;

    public ResourceLocation(String location) {
        if (location.contains(":")) {
            String[] parts = location.split(":", 2);
            this.namespace = parts[0];
            this.path = parts[1];
        } else {
            this.namespace = "minecraft";
            this.path = location;
        }
    }

    public ResourceLocation(String namespace, String path) {
        this.namespace = namespace;
        this.path = path;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return namespace + ":" + path;
    }
}
