package net.minecraftforge.fml.client.event;

/**
 * 互換レイヤー: 1.12.2 の RenderGameOverlayEvent のダミー実装。
 */
public class RenderGameOverlayEvent {
    public final Object matrixStack;
    public final float ticks;

    public RenderGameOverlayEvent(Object matrixStack, float ticks) {
        this.matrixStack = matrixStack;
        this.ticks = ticks;
    }

    public static class Text extends RenderGameOverlayEvent {
        public Text(Object matrixStack, float ticks) {
            super(matrixStack, ticks);
        }
    }
}
