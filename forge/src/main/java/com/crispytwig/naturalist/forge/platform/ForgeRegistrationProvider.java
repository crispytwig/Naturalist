package com.crispytwig.naturalist.forge.platform;

import com.crispytwig.naturalist.platform.registry.DeferredHolder;
import com.crispytwig.naturalist.platform.registry.RegistrationProvider;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ForgeRegistrationProvider<T> implements RegistrationProvider<T> {
    public static IEventBus EVENT_BUS;

    private final DeferredRegister<T> registry;

    public ForgeRegistrationProvider(ResourceKey<? extends Registry<T>> registryKey, String modId) {
        if (EVENT_BUS == null) {
            throw new IllegalStateException("Naturalist ModEventBus was not set before registry creation!");
        }
        this.registry = DeferredRegister.create(registryKey, modId);
        this.registry.register(EVENT_BUS);
    }

    @Override
    public <I extends T> DeferredHolder<T, I> register(String name, Supplier<I> supplier) {
        return new DeferredHolder<>(this.registry.register(name, supplier));
    }

    public static class Factory implements RegistrationProvider.Factory {
        @Override
        public <T> RegistrationProvider<T> create(ResourceKey<? extends Registry<T>> registryKey, String modId) {
            return new ForgeRegistrationProvider<>(registryKey, modId);
        }
    }
}
