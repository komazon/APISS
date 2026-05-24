package net.minecraftforge.fml.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 互換レイヤー: 1.12.2 の @Optional のダミー実装。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
public @interface Optional {
    String modid();
    String stripname() default "";

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface Interface {
        String iface();
        String modid();
        String stripname() default "";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface Method {
        String method();
        String value() default "";
        String modid();
        String stripname() default "";
    }
}
