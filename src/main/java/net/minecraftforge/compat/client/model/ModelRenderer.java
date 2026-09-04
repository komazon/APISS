package net.minecraftforge.compat.client.model;

/**
 * 1.12.2 Compatibility Layer: ModelRenderer stub
 * Provides compatibility for 1.12.2 mods that use ModelRenderer to define model parts.
 *
 * RTM's ModelMissing (and many other mods) call:
 *   new ModelRenderer(this, texOffX, texOffY)
 * which maps to net.minecraft.client.model.ModelRenderer in 1.12.2.
 */
public class ModelRenderer {

    /** Rotation point X (field_78799_f in 1.12.2) */
    public float rotationPointX;
    /** Rotation point Y (field_78798_g in 1.12.2) */
    public float rotationPointY;
    /** Rotation point Z (field_78797_h in 1.12.2) */
    public float rotationPointZ;

    /** Rotation angle X (field_78795_j in 1.12.2) */
    public float rotateAngleX;
    /** Rotation angle Y (field_78796_k in 1.12.2) */
    public float rotateAngleY;
    /** Rotation angle Z (field_78808_l in 1.12.2) */
    public float rotateAngleZ;

    /** Whether this part is rendered (field_78801_d in 1.12.2) */
    public boolean showModel = true;

    /** Mirror flag (field_78807_m in 1.12.2) */
    public boolean mirror = false;

    /** Texture offset X (field_78809_t in 1.12.2) */
    public int field_78809_t;

    /** Texture offset Y (field_78810_n in 1.12.2) */
    public int field_78810_n;

    @SuppressWarnings("unused")
    private final ModelBase model;

    /**
     * Primary 1.12.2 constructor: ModelRenderer(ModelBase model, int texOffX, int texOffY)
     */
    public ModelRenderer(ModelBase model, int texOffX, int texOffY) {
        this.model = model;
        this.field_78809_t = texOffX;
        this.field_78810_n = texOffY;
    }

    /**
     * Secondary constructor without texture offset (uses model's current offset)
     */
    public ModelRenderer(ModelBase model) {
        this(model, model != null ? model.field_78089_u : 0, 0);
    }

    /**
     * setTextureOffset(int x, int y) — returns this for chaining
     */
    public ModelRenderer setTextureOffset(int x, int y) {
        this.field_78809_t = x;
        this.field_78810_n = y;
        return this;
    }

    /**
     * addBox(float x, float y, float z, int dx, int dy, int dz)
     */
    public ModelRenderer addBox(float x, float y, float z, int dx, int dy, int dz) {
        return this; // stub — actual geometry not needed for compat layer
    }

    /**
     * addBox with expansion delta
     */
    public ModelRenderer addBox(float x, float y, float z, int dx, int dy, int dz, float delta) {
        return this;
    }

    /**
     * setRotationPoint(float x, float y, float z)
     */
    public void setRotationPoint(float x, float y, float z) {
        this.rotationPointX = x;
        this.rotationPointY = y;
        this.rotationPointZ = z;
    }

    /**
     * render(float scale) — no-op stub
     */
    public void render(float scale) {
        // RTM/NGTLib models are not rendered through the compat layer
    }
}
