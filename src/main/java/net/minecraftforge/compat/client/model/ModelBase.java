package net.minecraftforge.compat.client.model;

/**
 * 1.12.2 Compatibility Layer: Base class for legacy models
 * Provides compatibility for 1.12.2 mods that extend ModelBase
 */
public class ModelBase {
    
    /**
     * Texture width (field_78090_t in 1.12.2)
     */
    public int field_78090_t = 64;
    
    /**
     * Texture height
     */
    public int textureHeight = 32;
    
    /**
     * Default constructor for 1.12.2 compatibility
     */
    public ModelBase() {
        // Empty constructor for legacy mod compatibility
    }
    
    /**
     * Render the model
     */
    public void render(float scale) {
        // Legacy render method
    }
    
    /**
     * Set rotation angles for the model
     */
    public void setRotationAngles(float limbSwing, float limbSwingAmount, 
                                   float ageInTicks, float netHeadYaw, 
                                   float headPitch, float scale) {
        // Legacy rotation method
    }
}
