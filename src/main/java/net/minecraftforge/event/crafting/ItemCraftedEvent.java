package net.minecraftforge.event.crafting;

/**
 * 互換レイヤー: 1.12.2 の ItemCraftedEvent のダミー実装。
 */
public class ItemCraftedEvent {
    public final Object crafting;
    public final Object player;

    public ItemCraftedEvent(Object crafting, Object player) {
        this.crafting = crafting;
        this.player = player;
    }
}
