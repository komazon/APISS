package net.minecraftforge.client.event;

/**
 * 互換レイヤー: 1.12.2 の TextureStitchEvent のダミー実装。
 */
public class TextureStitchEvent {
    public final Object map;

    public TextureStitchEvent(Object map) {
        this.map = map;
    }

    public static class Pre extends TextureStitchEvent {
        public Pre(Object map) {
            super(map);
        }
    }

    public static class Post extends TextureStitchEvent {
        public Post(Object map) {
            super(map);
        }
    }
}
