package net.minecraftforge.compat.client;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.compat.util.Timer;

/**
 * 1.12.2 用 Minecraft クラスの互換性ラッパー
 * 
 * 1.12.2 Mod が使用する古いメソッド名（func_71410_x など）を、
 * 現代の NeoForge/Minecraft にマッピングする。
 */
public class Minecraft {
    
    private static Minecraft instance;
    private final net.minecraft.client.Minecraft vanilla;
    
    public Minecraft(net.minecraft.client.Minecraft vanilla) {
        this.vanilla = vanilla;
    }
    
    /**
     * @return 現在の Minecraft インスタンスを取得する（1.12.2 風）
     * 
     * 1.12.2: Minecraft.func_71410_x()
     * Modern: Minecraft.getInstance()
     */
    public static Minecraft func_71410_x() {
        if (instance == null) {
            instance = new Minecraft(net.minecraft.client.Minecraft.getInstance());
        }
        return instance;
    }
    
    /**
     * @return ゲーム世界の取得（1.12.2 風）
     * 
     * 1.12.2: Minecraft.func_71401_C()
     * Modern: Minecraft.getInstance().level
     */
    public net.minecraftforge.compat.world.World func_71401_C() {
        if (vanilla.level == null) {
            return null;
        }
        return new net.minecraftforge.compat.world.World(vanilla.level);
    }
    
    /**
     * @return プレイヤーの取得（1.12.2 風）
     * 
     * 1.12.2: Minecraft.func_175606_aa()
     * Modern: Minecraft.getInstance().player
     */
    public net.minecraftforge.compat.entity.player.EntityPlayer func_175606_aa() {
        if (vanilla.player == null) {
            return null;
        }
        return new net.minecraftforge.compat.entity.player.EntityPlayer(vanilla.player);
    }
    
    /**
     * @return サーバーの取得（1.12.2 風）
     * 
     * 1.12.2: Minecraft.func_71401_C().getMinecraftServer()
     * Modern: Minecraft.getInstance().getSingleplayerServer() or integrated server
     */
    public MinecraftServer func_71401_C_server() {
        return vanilla.getSingleplayerServer();
    }
    
    /**
     * @return Timer の取得（1.12.2 風）
     * 
     * 1.12.2: Minecraft.field_71428_T
     * Modern: 存在しないためダミーの Timer を返す
     */
    public Timer func_71411_J() {
        // 現代の Minecraft には timer フィールドが存在しないため、ダミーを返す
        return new Timer(0.0F);
    }
    
    /**
     * バニラの Minecraft インスタンスを取得
     */
    public net.minecraft.client.Minecraft getVanilla() {
        return vanilla;
    }
}
