package net.minecraftforge.compat.util;

/**
 * 1.12.2 用 Timer クラスの互換性ラッパー
 * 
 * 1.12.2: net.minecraft.util.Timer
 * Modern: 存在しない（ゲームループ機構が変更されたため）
 * 
 * このクラスは 1.12.2 Mod との互換性のために存在するダミークラスです。
 */
public class Timer {
    
    /**
     * 最後のフレーム時間（秒）
     * 1.12.2: field_74319_b
     */
    public float elapsedPartialTicks = 0.0F;
    
    /**
     * ゲーム時間（ティック数）
     * 1.12.2: field_74320_c
     */
    public int gameSpeed = 20;
    
    /**
     * 描画時間（秒）
     * 1.12.2: field_74318_a
     */
    public float renderPartialTicks = 0.0F;
    
    /**
     * 最後のフレーム時間（ms）
     * 1.12.2: field_74321_d
     */
    public long lastSyncSysClock = 0L;
    
    /**
     * 最後のフレーム時間（ティック）
     * 1.12.2: field_74322_e
     */
    public long lastSyncHRClock = 0L;
    
    /**
     * タイムスケール
     * 1.12.2: field_152690_f
     */
    public float timeScale = 1.0F;
    
    /**
     * コンストラクタ
     */
    public Timer(float partialTicks) {
        this.renderPartialTicks = partialTicks;
    }
    
    /**
     * 部分ティックを更新（1.12.2 風）
     * 実際には何もしない（ダミー実装）
     */
    public void updateTimer() {
        // 現代の Minecraft ではゲームループが異なるため、これはダミー実装
        // 必要に応じて RenderTickEvent 等から値を取得可能
    }
}
