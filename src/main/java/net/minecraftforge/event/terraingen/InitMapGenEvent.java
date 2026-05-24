package net.minecraftforge.event.terraingen;

/**
 * 互換レイヤー: 1.12.2 の InitMapGenEvent のダミー実装。
 */
public class InitMapGenEvent {
    public final Object originalGen;
    private Object newGen;

    public InitMapGenEvent(Object originalGen) {
        this.originalGen = originalGen;
        this.newGen = originalGen;
    }

    public Object getOriginalGen() {
        return originalGen;
    }

    public Object getNewGen() {
        return newGen;
    }

    public void setNewGen(Object newGen) {
        this.newGen = newGen;
    }
}
