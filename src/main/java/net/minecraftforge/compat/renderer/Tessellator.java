package net.minecraftforge.compat.renderer;

/**
 * 莠呈鋤繝ｬ繧､繝､繝ｼ: 1.12.2縺ｮ Tessellator 縺ｮ譛蟆上せ繧ｿ繝門ｮ溯｣・・
 */
public class Tessellator {
    private static final Tessellator INSTANCE = new Tessellator();
    private final BufferBuilder bufferBuilder = new BufferBuilder();

    private Tessellator() {
    }

    public static Tessellator getInstance() {
        return INSTANCE;
    }

    public BufferBuilder getBuffer() {
        return bufferBuilder;
    }

    public void draw() {
        bufferBuilder.reset();
    }
}
