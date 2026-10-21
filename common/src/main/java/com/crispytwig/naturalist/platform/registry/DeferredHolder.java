package com.crispytwig.naturalist.platform.registry;

import java.util.function.Supplier;

public final class DeferredHolder<R, T extends R> implements Supplier<T> {
    private final Supplier<T> supplier;

    public DeferredHolder(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public T get() {
        return supplier.get();
    }
}
