package net.minecraftforge.compat.block;

/**
 * 1.12.2 Compatibility Layer: Compatible class for net.minecraft.block.material.Material in 1.12.2
 * Provides values like Material.ROCK, Material.WOOD for 1.12.2
 * Blocks use this directly in LegacyBlock and convert to BlockBehaviour.Properties
 */
public enum LegacyMaterial {
    AIR,
    GRASS,
    GROUND,
    WOOD,
    ROCK,
    IRON,
    ANVIL,
    WATER,
    LAVA,
    LEAVES,
    PLANTS,
    VINE,
    SPONGE,
    CLOTH,
    FIRE,
    SAND,
    CIRCUITS,
    CARPET,
    GLASS,
    REDSTONE_LIGHT,
    TNT,
    CORAL,
    ICE,
    PACKED_ICE,
    SNOW,
    CRAFTED_SNOW,
    CACTUS,
    CLAY,
    GOURD,
    DRAGON_EGG,
    PORTAL,
    CAKE,
    WEB,
    PISTON,
    BARRIER,
    STRUCTURE_VOID;

    // 1.12.2 compatibility field (field_151576_e - canBurn)
    public final boolean canBurn;

    LegacyMaterial() {
        // canBurn is set in static initializer block
        this.canBurn = false;
    }
    
    static {
        // Can't modify final fields directly, use reflection as workaround
        try {
            java.lang.reflect.Field canBurnField = LegacyMaterial.class.getDeclaredField("canBurn");
            canBurnField.setAccessible(true);
            
            canBurnField.set(WOOD, true);
            canBurnField.set(PLANTS, true);
            canBurnField.set(VINE, true);
            canBurnField.set(CLOTH, true);
            canBurnField.set(CARPET, true);
            canBurnField.set(LEAVES, true);
        } catch (Exception e) {
            // Ignore reflection failures
        }
    }
}
