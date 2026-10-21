package com.crispytwig.naturalist.platform.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public final class DeferredRegister<T> {
    private final RegistrationProvider<T> provider;
    private final List<DeferredHolder<T, ? extends T>> entries = new ArrayList<>();
    private final List<String> names = new ArrayList<>();

    private DeferredRegister(ResourceKey<? extends Registry<T>> registryKey, String modId) {
        this.provider = RegistrationProvider.get(registryKey, modId);
    }

    public static <T> DeferredRegister<T> create(Registry<T> registry, String modId) {
        return new DeferredRegister<>(registry.key(), modId);
    }

    public static <T> DeferredRegister<T> create(ResourceKey<? extends Registry<T>> registryKey, String modId) {
        return new DeferredRegister<>(registryKey, modId);
    }

    public <I extends T> DeferredHolder<T, I> register(String name, Supplier<I> supplier) {
        DeferredHolder<T, I> holder = provider.register(name, supplier);
        entries.add(holder);
        names.add(name);
        return holder;
    }

    public Collection<DeferredHolder<T, ? extends T>> getEntries() {
        return Collections.unmodifiableList(entries);
    }

    public Collection<String> getRegisteredNames() {
        return Collections.unmodifiableList(names);
    }
}
