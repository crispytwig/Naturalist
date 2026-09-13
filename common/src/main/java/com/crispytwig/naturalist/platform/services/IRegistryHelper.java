package com.crispytwig.naturalist.platform.services;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public interface IRegistryHelper {
    <T> void registerSyncedDataPackRegistry(ResourceKey<Registry<T>> registryKey, Codec<T> codec);
}
