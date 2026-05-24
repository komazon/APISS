package net.minecraft.util;

/**
 * 互換レイヤー: 1.12.2 の SoundEvent のダミー実装。
 */
public class SoundEvent {
    private final ResourceLocation name;

    public SoundEvent(ResourceLocation name) {
        this.name = name;
    }

    public ResourceLocation getName() {
        return name;
    }
}
