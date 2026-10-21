package com.crispytwig.naturalist.platform.services;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.RecordItem;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;

import java.util.function.Supplier;

public interface IRegistryHelper {
    SpawnEggItem createSpawnEgg(Supplier<? extends EntityType<? extends Mob>> type, int primaryColor, int secondaryColor, Item.Properties properties);

    RecordItem createRecordItem(int comparatorValue, Supplier<SoundEvent> sound, Item.Properties properties, int lengthInTicks);

    <T> void registerSyncedDataPackRegistry(ResourceKey<Registry<T>> registryKey, Codec<T> codec);
}
