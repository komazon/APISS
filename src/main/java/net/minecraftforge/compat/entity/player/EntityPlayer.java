package net.minecraftforge.compat.entity.player;

import net.minecraft.world.entity.player.Player;

/**
 * 1.12.2 用 EntityPlayer クラスの互換性ラッパー
 */
public class EntityPlayer {
    
    private final Player vanilla;
    
    public EntityPlayer(Player vanilla) {
        this.vanilla = vanilla;
    }
    
    public Player getVanilla() {
        return vanilla;
    }
}
