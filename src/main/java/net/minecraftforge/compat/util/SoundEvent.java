package net.minecraftforge.compat.util;

/**
 * 莠呈鋤繝ｬ繧､繝､繝ｼ: 1.12.2 縺ｮ SoundEvent 縺ｮ繝繝溘・螳溯｣・・
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
