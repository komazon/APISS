package net.minecraftforge.event.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 互換レイヤー: 1.12.2 の EntityEvent のダミー実装。
 */
public class EntityEvent {
    public final Object entity;
    protected List<String> cancelled = new ArrayList<>();

    public EntityEvent(Object entity) {
        this.entity = entity;
    }

    public Object getEntity() {
        return entity;
    }
}
