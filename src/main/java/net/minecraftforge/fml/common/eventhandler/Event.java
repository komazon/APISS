package net.minecraftforge.fml.common.eventhandler;

/**
 * 互換レイヤー: 1.12.2 の Event 基底クラスのダミー実装。
 * 古い Forge Mod が Event を継承または参照する際に必要。
 */
public class Event {

    private boolean canceled;

    public Event() {
    }

    public boolean isCancelable() {
        return false;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public void cancel() {
        setCanceled(true);
    }

    public boolean hasResult() {
        return false;
    }

    public Result getResult() {
        return Result.DEFAULT;
    }

    public void setResult(Result result) {
        // no-op for compatibility
    }

    public enum Result {
        DEFAULT,
        ALLOW,
        DENY
    }
}
