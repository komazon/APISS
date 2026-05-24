package site.canva.my.komazonjapan.compat.asm;

import org.objectweb.asm.commons.Remapper;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 互換レイヤー: 1.12.2 の型名を現代 NeoForge 用の互換型にリマップする。
 */
public class LegacyRemapper extends Remapper {

    private static final Map<String, String> TYPE_MAP = new LinkedHashMap<>();

    static {
        TYPE_MAP.put("net/minecraft/block/Block", "net/minecraftforge/compat/block/LegacyBlock");
        TYPE_MAP.put("net/minecraft/item/Item", "net/minecraftforge/compat/item/LegacyItem");
        TYPE_MAP.put("net/minecraft/block/material/Material", "net/minecraftforge/compat/block/LegacyMaterial");
        TYPE_MAP.put("net/minecraft/entity/player/EntityPlayer", "net/minecraft/world/entity/player/Player");
        TYPE_MAP.put("net/minecraft/entity/Entity", "net/minecraft/world/entity/Entity");
        TYPE_MAP.put("net/minecraft/entity/EntityLivingBase", "net/minecraft/world/entity/LivingEntity");

        TYPE_MAP.put("net/minecraft/util/math/BlockPos", "net/minecraft/core/BlockPos");
        TYPE_MAP.put("net/minecraft/util/math/Vec3d", "net/minecraft/world/phys/Vec3");
        TYPE_MAP.put("net/minecraft/world/World", "net/minecraft/world/level/Level");
        TYPE_MAP.put("net/minecraft/world/WorldServer", "net/minecraft/server/level/ServerLevel");
        TYPE_MAP.put("net/minecraft/world/chunk/Chunk", "net/minecraft/world/level/chunk/LevelChunk");

        // 旧パッケージの主要なクラス群だけを変換する
        TYPE_MAP.put("net/minecraft/block", "net/minecraft/world/level/block");
        TYPE_MAP.put("net/minecraft/item", "net/minecraft/world/item");
        TYPE_MAP.put("net/minecraft/block/material", "net/minecraft/world/level/material");
        TYPE_MAP.put("net/minecraft/world/chunk", "net/minecraft/world/level/chunk");
    }

    @Override
    public String map(String internalName) {
        if (internalName == null) {
            return null;
        }

        String mapped = TYPE_MAP.get(internalName);
        if (mapped != null) {
            return mapped;
        }

        for (Map.Entry<String, String> entry : TYPE_MAP.entrySet()) {
            String from = entry.getKey();
            String to = entry.getValue();
            if (internalName.startsWith(from + "/")) {
                return to + internalName.substring(from.length());
            }
        }

        return internalName;
    }
}
