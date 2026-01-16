package com.finger.damoa.core.config.api;

import com.finger.damoa.core.exception.BizException;

import java.util.function.Consumer;

@FunctionalInterface
public interface ThrowingConsumer<T, E extends Throwable> {

    void accept(T t) throws E;

    static <T, E extends Throwable> Consumer<T> unchecked(ThrowingConsumer<T, E> f) {
        return t -> {
            try {
                f.accept(t);
            } catch (Throwable e) {
                throw new BizException(e);
            }
        };
    }

    static <T, E extends Throwable> Consumer<T> unchecked(ThrowingConsumer<T, E> f, Consumer<Throwable> c) {
        return t -> {
            try {
                f.accept(t);
            } catch (Throwable e) {
                c.accept(e);
            }
        };
    }
}
