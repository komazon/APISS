package net.minecraftforge.common.capabilities;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 互換レイヤー: 1.12.2 の @CapabilityInject のダミー実装。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CapabilityInject {
    Class<?> value();
}
