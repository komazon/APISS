package net.minecraftforge.compat.world;

import net.minecraft.world.level.Level;

/**
 * 1.12.2 用 World クラスの互換性ラッパー
 */
public class World {
    
    private final Level vanilla;
    
    public World(Level vanilla) {
        this.vanilla = vanilla;
    }
    
    public Level getVanilla() {
        return vanilla;
    }
}
