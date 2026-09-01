package net.minecraftforge.fml.common.registry;

import io.netty.buffer.ByteBuf;

/**
 * 互換レイヤー: 1.12.2 の IEntityAdditionalSpawnData インターフェース。
 */
public interface IEntityAdditionalSpawnData {
    /**
     * エンティティスポーン時にクライアントへ送信する追加データを書き込む。
     */
    void writeSpawnData(ByteBuf buffer);

    /**
     * クライアントがエンティティスポーンデータを受信したときに読み込む。
     */
    void readSpawnData(ByteBuf additionalData);
}
