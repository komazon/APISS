package net.minecraftforge.compat.block;

import java.lang.reflect.Field;

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

    /**
     * Whether this material can burn.
     * Non-final so the static initializer can set per-constant values.
     */
    public boolean canBurn;

    /**
     * 1.12.2 obfuscated name for canBurn (field_151576_e).
     * RTM and other mods compiled against obfuscated 1.12.2 mappings
     * access this field by its srg name directly via bytecode, so both
     * names must exist as real fields.
     */
    public boolean field_151576_e;

    LegacyMaterial() {
        this.canBurn = false;
        this.field_151576_e = false;
    }

    static {
        // Materials that could catch fire in 1.12.2
        for (LegacyMaterial m : new LegacyMaterial[]{WOOD, PLANTS, VINE, CLOTH, CARPET, LEAVES}) {
            m.canBurn = true;
            m.field_151576_e = true;
        }
    }
}
