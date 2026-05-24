package net.minecraftforge.event.world;

/**
 * 互換レイヤー: 1.12.2 の WorldEvent のダミー実装。
 */
public class WorldEvent {

    private final Object world;

    public WorldEvent(Object world) {
        this.world = world;
    }

    public Object getWorld() {
        return world;
    }

    public static class Load extends WorldEvent {
        public Load(Object world) {
            super(world);
        }
    }

    public static class Unload extends WorldEvent {
        public Unload(Object world) {
            super(world);
        }
    }

    public static class Save extends WorldEvent {
        public Save(Object world) {
            super(world);
        }
    }
}
