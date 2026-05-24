package net.minecraftforge.event.entity.living;

/**
 * 互換レイヤー: 1.12.2 の LivingEvent のダミー実装。
 */
public class LivingEvent {
    public final Object entity;

    public LivingEvent(Object entity) {
        this.entity = entity;
    }

    public Object getEntity() {
        return entity;
    }

    public static class LivingUpdateEvent extends LivingEvent {
        public LivingUpdateEvent(Object entity) {
            super(entity);
        }
    }

    public static class LivingDeathEvent extends LivingEvent {
        public LivingDeathEvent(Object entity) {
            super(entity);
        }
    }
}
