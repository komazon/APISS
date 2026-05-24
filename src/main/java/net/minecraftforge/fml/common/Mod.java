package net.minecraftforge.fml.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 互換レイヤー: 1.12.2の @Mod アノテーションのダミー実装。
 *
 * 1.12.2のModはクラスに @Mod(modid="xxx") を付けて自分を宣言する。
 * このダミーを同じパッケージ・名前で用意することで、
 * 1.12.2 Mod の .class ファイルが「net.minecraftforge.fml.common.Mod を
 * 見つけられない」というNoClassDefFoundErrorを回避できる。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Mod {

    /** Modの識別子 (例: "mymod") */
    String modid();

    /** Modの表示名 (任意) */
    String name() default "";

    /** Modのバージョン (任意) */
    String version() default "";

    /**
     * 1.12.2 で指定される依存関係文字列。
     * 例: "required-after:Forge@[11.14.0.1280,);required-after:codechickenlib"
     */
    String dependencies() default "";

    /**
     * 旧形式の追加依存関係文字列。
     */
    String requiredMods() default "";

    /**
     * 1.12.2 のバージョン受け入れ文字列。
     */
    String acceptedMinecraftVersions() default "";

    /**
     * 1.12.2 のメタデータ有効化フラグ。
     */
    boolean useMetadata() default false;

    // ─── 1.12.2でよく使われたが現代では無視するフィールド群 ───

    /** @deprecated 互換レイヤーでは無視される */
    @Deprecated
    boolean clientSideOnly() default false;

    /** @deprecated 互換レイヤーでは無視される */
    @Deprecated
    boolean serverSideOnly() default false;



    /**
     * 1.12.2の @Mod.EventHandler を再現するためのネストアノテーション。
     * 互換レイヤーがリフレクションでこのアノテーションを検索し、
     * FMLPreInitializationEvent などを渡して呼び出す。
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface EventHandler {
        // マーカーアノテーション。値は持たない。
    }

    /**
     * 1.12.2の @Mod.Instance を再現するためのネストアノテーション。
     * 互換レイヤーがModのインスタンスをフィールドにインジェクトする際に使う。
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface Instance {
        String value() default "";
    }
}
