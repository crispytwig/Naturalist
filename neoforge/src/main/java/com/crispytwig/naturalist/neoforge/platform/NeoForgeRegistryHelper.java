package com.crispytwig.naturalist.neoforge.platform;

import com.crispytwig.naturalist.platform.services.IRegistryHelper;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

public class NeoForgeRegistryHelper implements IRegistryHelper {
    @Override
    public <T> void registerSyncedDataPackRegistry(ResourceKey<Registry<T>> registryKey, Codec<T> codec) {
        if (NeoForgeRegistrationProvider.EVENT_BUS == null) {
            throw new IllegalStateException("Naturalist ModEventBus was not set before datapack registry!");
        }
        NeoForgeRegistrationProvider.EVENT_BUS.addListener((DataPackRegistryEvent.NewRegistry event) ->
                event.dataPackRegistry(registryKey, codec, codec));
    }
}
