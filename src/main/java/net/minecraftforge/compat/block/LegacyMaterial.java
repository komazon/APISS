package net.minecraftforge.compat.block;

/**
 * 互換レイヤー: 1.12.2の net.minecraft.block.material.Material のダミー列挙型。
 *
 * 1.12.2では Material.ROCK, Material.WOOD などの定数が存在していたが、
 * 現代では廃止されて BlockBehaviour.Properties のビルダーに統合された。
 *
 * このクラスを net.minecraft.block.material.Material という
 * 旧パッケージ名でも参照できるよう、ASMリマッパーが
 * パスを net.minecraftforge.compat.block.LegacyMaterial に書き換える。
 */
public enum LegacyMaterial {
    AIR,
    GRASS,
    GROUND,     // 土・砂利
    WOOD,
    ROCK,       // 石・鉱石
    IRON,       // 金属
    ANVIL,
    WATER,
    LAVA,
    LEAVES,
    PLANTS,
    VINE,
    SPONGE,
    CLOTH,      // 羊毛
    FIRE,
    SAND,
    CIRCUITS,   // レッドストーン系
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
    GOURD,      // カボチャ・スイカ
    DRAGON_EGG,
    PORTAL,
    CAKE,
    WEB,
    PISTON,
    BARRIER,
    STRUCTURE_VOID
}
