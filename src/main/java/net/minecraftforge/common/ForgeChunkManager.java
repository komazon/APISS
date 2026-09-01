package net.minecraftforge.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 互換レイヤー: 1.12.2 の ForgeChunkManager のダミー実装。
 *
 * RTM などの旧 Mod が RTMChunkManager implements ForgeChunkManager.LoadingCallback
 * という形で参照するため、LoadingCallback を interface として提供する。
 * 実際のチャンク強制ロードは NeoForge の仕組みに委譲する想定だが、
 * 互換レイヤーでは no-op で握りつぶす。
 */
public class ForgeChunkManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ForgeChunkManager.class);

    /**
     * 1.12.2 の ForgeChunkManager.LoadingCallback インターフェース。
     * 旧 Mod がこれを implements するため、必ず interface でなければならない。
     */
    public interface LoadingCallback {
        /**
         * ゲームロード時にチャンクチケットを再登録するために呼ばれる。
         *
         * @param tickets そのMod が持っていた Ticket のリスト (互換レイヤーでは常に空)
         * @param world   対象ワールド
         */
        void ticketsLoaded(java.util.List<Ticket> tickets, Object world);
    }

    /**
     * 1.12.2 の ForgeChunkManager.Ticket のダミー。
     * 旧 Mod が型参照のみで使う場合があるため用意する。
     */
    public static class Ticket {
        private final String modId;
        private final Object world;

        public Ticket(String modId, Object world) {
            this.modId = modId;
            this.world = world;
        }

        public String getModId() { return modId; }
        public Object getWorld()  { return world;  }
    }

    /** No-op: 互換レイヤーではチャンクの強制ロードを行わない */
    public static void setForcedChunkLoadingCallback(Object mod, LoadingCallback callback) {
        LOGGER.debug("[互換レイヤー] setForcedChunkLoadingCallback: mod={}", mod);
    }

    /** No-op: ダミーチケットを返す */
    public static Ticket requestTicket(Object mod, Object world, Type type) {
        LOGGER.debug("[互換レイヤー] requestTicket: mod={}", mod);
        return new Ticket(mod != null ? mod.toString() : "unknown", world);
    }

    /** No-op */
    public static void releaseTicket(Ticket ticket) {
        LOGGER.debug("[互換レイヤー] releaseTicket: {}", ticket);
    }

    /** No-op */
    public static void forceChunk(Ticket ticket, net.minecraft.world.level.ChunkPos chunk) {
        LOGGER.debug("[互換レイヤー] forceChunk: {}", chunk);
    }

    /** No-op */
    public static void unforceChunk(Ticket ticket, net.minecraft.world.level.ChunkPos chunk) {
        LOGGER.debug("[互換レイヤー] unforceChunk: {}", chunk);
    }

    /** 1.12.2 の Type 列挙型 */
    public enum Type {
        NORMAL,
        ENTITY
    }
}
