package net.minecraftforge.compat.renderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 莠呈鋤繝ｬ繧､繝､繝ｼ: 1.12.2縺ｮ GlStateManager 縺ｮ譛蟆上せ繧ｿ繝門ｮ溯｣・・
 */
public final class GlStateManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlStateManager.class);

    private GlStateManager() {
    }

    public static void pushMatrix() {
        LOGGER.debug("[莠呈鋤繝ｬ繧､繝､繝ｼ] GlStateManager.pushMatrix()");
    }

    public static void popMatrix() {
        LOGGER.debug("[莠呈鋤繝ｬ繧､繝､繝ｼ] GlStateManager.popMatrix()");
    }

    public static void translate(double x, double y, double z) {
        LOGGER.debug("[莠呈鋤繝ｬ繧､繝､繝ｼ] GlStateManager.translate({}, {}, {})", x, y, z);
    }

    public static void scale(float x, float y, float z) {
        LOGGER.debug("[莠呈鋤繝ｬ繧､繝､繝ｼ] GlStateManager.scale({}, {}, {})", x, y, z);
    }

    public static void rotate(float angle, float x, float y, float z) {
        LOGGER.debug("[莠呈鋤繝ｬ繧､繝､繝ｼ] GlStateManager.rotate({}, {}, {}, {})", angle, x, y, z);
    }

    public static void color(float red, float green, float blue, float alpha) {
        LOGGER.debug("[莠呈鋤繝ｬ繧､繝､繝ｼ] GlStateManager.color({}, {}, {}, {})", red, green, blue, alpha);
    }

    public static void enableBlend() {
        LOGGER.debug("[莠呈鋤繝ｬ繧､繝､繝ｼ] GlStateManager.enableBlend()");
    }

    public static void disableBlend() {
        LOGGER.debug("[莠呈鋤繝ｬ繧､繝､繝ｼ] GlStateManager.disableBlend()");
    }

    public static void blendFuncSeparate(int srcFactor, int dstFactor, int srcFactorAlpha, int dstFactorAlpha) {
        LOGGER.debug("[莠呈鋤繝ｬ繧､繝､繝ｼ] GlStateManager.blendFuncSeparate({}, {}, {}, {})",
                srcFactor, dstFactor, srcFactorAlpha, dstFactorAlpha);
    }
}
