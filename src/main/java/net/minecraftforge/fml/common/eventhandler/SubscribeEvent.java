package net.minecraftforge.fml.common.eventhandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 互換レイヤー: 1.12.2の @SubscribeEvent のダミー実装。
 *
 * 1.12.2ではイベントバス購読メソッドにこのアノテーションを付ける。
 * LegacyRegistryBridge がリフレクションでこのアノテーションを検索して
 * 登録メソッドを発見するために必要。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SubscribeEvent {
    /** イベントの優先度 (無視される) */
    EventPriority priority() default EventPriority.NORMAL;

    /** trueの場合キャンセル済みイベントも受け取る (無視される) */
    boolean receiveCanceled() default false;

    /** 優先度の列挙型 */
    enum EventPriority {
        HIGHEST, HIGH, NORMAL, LOW, LOWEST
    }
}
