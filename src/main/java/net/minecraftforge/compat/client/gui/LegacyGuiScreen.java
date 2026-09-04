package net.minecraftforge.compat.client.gui;

/**
 * 1.12.2 Compatibility Layer: Base class for legacy GUI screens
 * Provides compatibility for 1.12.2 mods that extend GuiScreen
 *
 * NOTE: Does NOT extend net.minecraft.client.gui.Gui.
 * Gui.<init> internally instantiates DebugScreenOverlay which calls
 * RenderSystem.getSequentialBuffer(), asserting the render thread.
 * Legacy mod ClientProxy static initializers run on modloading-worker
 * threads, so extending Gui causes ExceptionInInitializerError.
 */
public class LegacyGuiScreen {

    /**
     * Minecraft instance (field_146297_k in 1.12.2) - lazily initialized
     */
    protected net.minecraftforge.compat.client.Minecraft field_146297_k;

    /**
     * Width of the screen - lazily initialized on render thread
     */
    public int width = 0;

    /**
     * Height of the screen - lazily initialized on render thread
     */
    public int height = 0;

    /**
     * Default constructor for 1.12.2 compatibility
     * Does NOT touch any MC RenderSystem to allow safe construction off render thread
     */
    public LegacyGuiScreen() {
        // Intentionally empty — no MC classes initialized here
    }

    /**
     * Get the Minecraft instance (field_146297_k in 1.12.2)
     * Lazily initialized to avoid RenderSystem calls from wrong thread
     */
    protected net.minecraftforge.compat.client.Minecraft getField_146297_k() {
        if (this.field_146297_k == null) {
            this.field_146297_k = net.minecraftforge.compat.client.Minecraft.func_71410_x();
            this.updateDimensions();
        }
        return this.field_146297_k;
    }

    /**
     * Update screen dimensions (call from render thread only)
     */
    protected void updateDimensions() {
        if (this.field_146297_k != null && this.field_146297_k.getVanilla() != null) {
            this.width = this.field_146297_k.getVanilla().getWindow().getScreenWidth();
            this.height = this.field_146297_k.getVanilla().getWindow().getScreenHeight();
        }
    }

    /**
     * Called when the screen is initialized
     */
    public void init() {
        if (this.field_146297_k == null) {
            getField_146297_k();
        } else {
            updateDimensions();
        }
    }

    /**
     * Called when the screen should draw
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (this.field_146297_k == null) {
            getField_146297_k();
        } else {
            updateDimensions();
        }
    }

    /**
     * Called when a key is typed
     */
    public void keyTyped(char typedChar, int keyCode) {
    }

    /**
     * Called when the mouse is clicked
     */
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    }
}
