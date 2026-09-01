package net.minecraftforge.compat.renderer;

import net.minecraftforge.compat.renderer.vertex.VertexFormat;

/**
 * 莠呈鋤繝ｬ繧､繝､繝ｼ: 1.12.2縺ｮ BufferBuilder 縺ｮ譛蟆上せ繧ｿ繝門ｮ溯｣・・
 */
public class BufferBuilder {

    private boolean building;
    private VertexFormat vertexFormat;
    private int drawMode;

    public BufferBuilder begin(int mode, VertexFormat format) {
        this.building = true;
        this.drawMode = mode;
        this.vertexFormat = format;
        return this;
    }

    public BufferBuilder pos(double x, double y, double z) {
        return this;
    }

    public BufferBuilder color(int red, int green, int blue, int alpha) {
        return this;
    }

    public BufferBuilder tex(double u, double v) {
        return this;
    }

    public BufferBuilder normal(float nx, float ny, float nz) {
        return this;
    }

    public void endVertex() {
    }

    public void reset() {
        this.building = false;
        this.vertexFormat = null;
        this.drawMode = 0;
    }
}
