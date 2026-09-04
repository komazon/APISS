package net.minecraftforge.compat.client.gui;

import net.minecraftforge.compat.client.Minecraft;

/**
 * 1.12.2 Compatibility Layer: Base class for legacy GUI screens
 * Provides compatibility for 1.12.2 mods that extend GuiScreen
 */
public class LegacyGuiScreen {
    
    /**
     * Minecraft instance (field_146297_k in 1.12.2)
     */
    protected Minecraft field_146297_k;
    
    /**
     * Default constructor for 1.12.2 compatibility
     */
    public LegacyGuiScreen() {
        this.field_146297_k = Minecraft.getInstance();
    }
    
    /**
     * Called when the screen is initialized
     */
    public void init() {
        // Legacy init method
    }
    
    /**
     * Called when the screen should draw
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
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
