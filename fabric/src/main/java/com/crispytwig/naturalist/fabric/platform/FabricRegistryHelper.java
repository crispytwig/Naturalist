package com.crispytwig.naturalist.fabric.platform;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.RecordItem;
import com.crispytwig.naturalist.platform.services.IRegistryHelper;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;

import java.util.function.Supplier;

public class FabricRegistryHelper implements IRegistryHelper {
    @Override
    public SpawnEggItem createSpawnEgg(Supplier<? extends EntityType<? extends Mob>> type, int primaryColor, int secondaryColor, Item.Properties properties) {
        return new SpawnEggItem(type.get(), primaryColor, secondaryColor, properties);
    }

    @Override
    public RecordItem createRecordItem(int comparatorValue, Supplier<SoundEvent> sound, Item.Properties properties, int lengthInTicks) {
        return new RecordItem(comparatorValue, sound.get(), properties, lengthInTicks / 20);
    }

    @Override
    public <T> void registerSyncedDataPackRegistry(ResourceKey<Registry<T>> registryKey, Codec<T> codec, Codec<T> networkCodec) {
        DynamicRegistries.registerSynced(registryKey, codec, networkCodec, DynamicRegistries.SyncOption.SKIP_WHEN_EMPTY);
    }
}
