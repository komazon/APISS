package net.minecraftforge.fml.common.eventhandler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 互換レイヤー: 1.12.2 の EventBus のダミー実装。
 */
public class EventBus {
    private final List<Subscriber> subscribers = new ArrayList<>();

    public void register(Object listener) {
        if (listener == null) {
            return;
        }
        for (Method method : listener.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(SubscribeEvent.class)) {
                continue;
            }
            if (method.getParameterCount() != 1) {
                continue;
            }
            method.setAccessible(true);
            subscribers.add(new Subscriber(listener, method));
        }
    }

    public void unregister(Object listener) {
        if (listener == null) {
            return;
        }
        subscribers.removeIf(subscriber -> subscriber.target == listener);
    }

    public boolean post(Object event) {
        if (event == null) {
            return false;
        }
        for (Subscriber subscriber : new ArrayList<>(subscribers)) {
            if (subscriber.method.getParameterTypes()[0].isAssignableFrom(event.getClass())) {
                try {
                    subscriber.method.invoke(subscriber.target, event);
                } catch (Exception ignored) {
                }
            }
        }
        return true;
    }

    public void register(Class<?> clazz) {
        // 互換性のための no-op。
    }

    private static final class Subscriber {
        private final Object target;
        private final Method method;

        private Subscriber(Object target, Method method) {
            this.target = target;
            this.method = method;
        }
    }
}
