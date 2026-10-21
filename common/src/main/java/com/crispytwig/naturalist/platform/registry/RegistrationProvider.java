package com.crispytwig.naturalist.platform.registry;

import com.crispytwig.naturalist.platform.Services;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.function.Supplier;

public interface RegistrationProvider<T> {
    <I extends T> DeferredHolder<T, I> register(String name, Supplier<I> supplier);

    static <T> RegistrationProvider<T> get(ResourceKey<? extends Registry<T>> registryKey, String modId) {
        return Factory.INSTANCE.create(registryKey, modId);
    }

    interface Factory {
        Factory INSTANCE = Services.load(Factory.class);

        <T> RegistrationProvider<T> create(ResourceKey<? extends Registry<T>> registryKey, String modId);
    }
}
