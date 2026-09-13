package com.crispytwig.naturalist.fabric.platform;

import com.crispytwig.naturalist.platform.services.IRegistryHelper;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class FabricRegistryHelper implements IRegistryHelper {
    @Override
    public <T> void registerSyncedDataPackRegistry(ResourceKey<Registry<T>> registryKey, Codec<T> codec) {
        DynamicRegistries.registerSynced(registryKey, codec, DynamicRegistries.SyncOption.SKIP_WHEN_EMPTY);
    }
}
