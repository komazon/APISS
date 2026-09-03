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
        this.canBurn = (this == WOOD || this == PLANTS || this == VINE || 
                        this == CLOTH || this == CARPET || this == LEAVES);
    }
}
