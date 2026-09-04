package net.minecraftforge.compat.client.gui;

import net.minecraftforge.compat.client.Minecraft;
import net.minecraft.client.gui.Gui;

/**
 * 1.12.2 Compatibility Layer: Base class for legacy GUI screens
 * Provides compatibility for 1.12.2 mods that extend GuiScreen
 */
public class LegacyGuiScreen extends Gui {
    
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
     * Does NOT call Minecraft.getInstance() to avoid RenderSystem errors from wrong thread
     */
    public LegacyGuiScreen() {
        super(null); // Pass null to avoid RenderSystem call in Gui constructor
        // All fields are lazily initialized when accessed on the correct thread
    }
    
    /**
     * Get the Minecraft instance (field_146297_k in 1.12.2)
     * Lazily initialized to avoid RenderSystem calls from wrong thread
     */
    protected net.minecraftforge.compat.client.Minecraft getField_146297_k() {
        if (this.field_146297_k == null) {
            this.field_146297_k = Minecraft.func_71410_x();
            // Initialize dimensions when Minecraft instance is first accessed
            this.updateDimensions();
        }
        return this.field_146297_k;
    }
    
    /**
     * Update screen dimensions (called from render thread)
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
        // Ensure we're on the render thread and have valid dimensions
        if (this.field_146297_k == null) {
            getField_146297_k();
        } else {
            updateDimensions();
        }
        // Legacy init method
    }
    
    /**
     * Called when the screen should draw
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Ensure we're on the render thread and have valid dimensions
        if (this.field_146297_k == null) {
            getField_146297_k();
        } else {
            updateDimensions();
        }
        // Legacy draw method
    }
    
    /**
     * Called when a key is typed
     */
    public void keyTyped(char typedChar, int keyCode) {
        // Legacy key input method
    }
    
    /**
     * Called when the mouse is clicked
     */
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        // Legacy mouse click method
    }
}
