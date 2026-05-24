package net.minecraftforge.fml.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 互換レイヤー: 1.12.2 の @SidedProxy のダミー実装。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SidedProxy {
    String clientSide() default "";
    String serverSide() default "";
}
