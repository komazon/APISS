package net.minecraftforge.compat.client.model;

/**
 * 1.12.2 Compatibility Layer: ModelRenderer stub
 *
 * RTM/NGTLib は1.12.2の難読化済みバイトコードをそのまま使うため、
 * addBox系メソッドを SRG 難読化名 (func_78789_a 等) でも呼び出す。
 * 難読化名エイリアスを全て定義する。
 *
 * SRG mapping (1.12.2):
 *   func_78789_a = addBox(float,float,float,int,int,int)
 *   func_78788_a = addBox(float,float,float,int,int,int,float)  [delta variant]
 *   func_78790_a = setRotationPoint(float,float,float)
 *   func_78769_b = setTextureOffset(int,int)
 */
public class ModelRenderer {

    // --- Fields (SRG names must also be present as real fields) ---

    /** rotationPointX — field_78799_f */
    public float rotationPointX;
    public float field_78799_f;

    /** rotationPointY — field_78798_g */
    public float rotationPointY;
    public float field_78798_g;

    /** rotationPointZ — field_78797_h */
    public float rotationPointZ;
    public float field_78797_h;

    /** rotateAngleX — field_78795_j */
    public float rotateAngleX;
    public float field_78795_j;

    /** rotateAngleY — field_78796_k */
    public float rotateAngleY;
    public float field_78796_k;

    /** rotateAngleZ — field_78808_l */
    public float rotateAngleZ;
    public float field_78808_l;

    /** showModel — field_78801_d */
    public boolean showModel = true;
    public boolean field_78801_d = true;

    /** mirror — field_78807_m */
    public boolean mirror = false;
    public boolean field_78807_m = false;

    /** Texture offset X — field_78809_t */
    public int field_78809_t;

    /** Texture offset Y — field_78810_n */
    public int field_78810_n;

    @SuppressWarnings("unused")
    private final ModelBase model;

    // ---------------------------------------------------------------
    // Constructors
    // ---------------------------------------------------------------

    /** Primary 1.12.2 constructor */
    public ModelRenderer(ModelBase model, int texOffX, int texOffY) {
        this.model = model;
        this.field_78809_t = texOffX;
        this.field_78810_n = texOffY;
    }

    /** No-offset constructor */
    public ModelRenderer(ModelBase model) {
        this(model, model != null ? model.field_78089_u : 0, 0);
    }

    // ---------------------------------------------------------------
    // addBox — readable name + SRG alias
    // ---------------------------------------------------------------

    /** addBox(float x, float y, float z, int dx, int dy, int dz) */
    public ModelRenderer addBox(float x, float y, float z, int dx, int dy, int dz) {
        return this;
    }

    /** SRG alias: func_78789_a */
    public ModelRenderer func_78789_a(float x, float y, float z, int dx, int dy, int dz) {
        return addBox(x, y, z, dx, dy, dz);
    }

    /** addBox with expansion delta */
    public ModelRenderer addBox(float x, float y, float z, int dx, int dy, int dz, float delta) {
        return this;
    }

    /** SRG alias: func_78788_a */
    public ModelRenderer func_78788_a(float x, float y, float z, int dx, int dy, int dz, float delta) {
        return addBox(x, y, z, dx, dy, dz, delta);
    }

    // ---------------------------------------------------------------
    // setRotationPoint — readable name + SRG alias
    // ---------------------------------------------------------------

    public void setRotationPoint(float x, float y, float z) {
        this.rotationPointX = x; this.field_78799_f = x;
        this.rotationPointY = y; this.field_78798_g = y;
        this.rotationPointZ = z; this.field_78797_h = z;
    }

    /** SRG alias: func_78790_a */
    public void func_78790_a(float x, float y, float z) {
        setRotationPoint(x, y, z);
    }

    // ---------------------------------------------------------------
    // setTextureOffset — readable name + SRG alias
    // ---------------------------------------------------------------

    public ModelRenderer setTextureOffset(int x, int y) {
        this.field_78809_t = x;
        this.field_78810_n = y;
        return this;
    }

    /** SRG alias: func_78769_b */
    public ModelRenderer func_78769_b(int x, int y) {
        return setTextureOffset(x, y);
    }

    // ---------------------------------------------------------------
    // render — no-op stub
    // ---------------------------------------------------------------

    public void render(float scale) {
        // RTM/NGTLib モデルは互換レイヤー上では実描画しない
    }
}
