package com.crispytwig.naturalist.fabric.platform;

import com.crispytwig.naturalist.platform.registry.DeferredHolder;
import com.crispytwig.naturalist.platform.registry.RegistrationProvider;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

public class FabricRegistrationProvider<T> implements RegistrationProvider<T> {
    private final Registry<T> registry;
    private final String modId;

    @SuppressWarnings("unchecked")
    public FabricRegistrationProvider(ResourceKey<? extends Registry<T>> registryKey, String modId) {
        this.registry = (Registry<T>) BuiltInRegistries.REGISTRY.getValue(registryKey.identifier());
        this.modId = modId;
    }

    @Override
    public <I extends T> DeferredHolder<T, I> register(String name, Supplier<I> supplier) {
        I value = Registry.register(registry, Identifier.fromNamespaceAndPath(modId, name), supplier.get());
        return new DeferredHolder<>(() -> value);
    }

    public static class Factory implements RegistrationProvider.Factory {
        @Override
        public <T> RegistrationProvider<T> create(ResourceKey<? extends Registry<T>> registryKey, String modId) {
            return new FabricRegistrationProvider<>(registryKey, modId);
        }
    }
}
