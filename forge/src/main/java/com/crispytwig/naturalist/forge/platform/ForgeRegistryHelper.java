package com.crispytwig.naturalist.forge.platform;

import com.crispytwig.naturalist.platform.services.IRegistryHelper;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DataPackRegistryEvent;

import java.util.function.Supplier;

public class ForgeRegistryHelper implements IRegistryHelper {
    @Override
    public SpawnEggItem createSpawnEgg(Supplier<? extends EntityType<? extends Mob>> type, int primaryColor, int secondaryColor, Item.Properties properties) {
        return new ForgeSpawnEggItem(type, primaryColor, secondaryColor, properties);
    }

    @Override
    public RecordItem createRecordItem(int comparatorValue, Supplier<SoundEvent> sound, Item.Properties properties, int lengthInTicks) {
        return new RecordItem(comparatorValue, sound, properties, lengthInTicks);
    }

    @Override
    public <T> void registerSyncedDataPackRegistry(ResourceKey<Registry<T>> registryKey, Codec<T> codec, Codec<T> networkCodec) {
        if (ForgeRegistrationProvider.EVENT_BUS == null) {
            throw new IllegalStateException("Naturalist ModEventBus was not set before datapack registry!");
        }
        ForgeRegistrationProvider.EVENT_BUS.addListener((DataPackRegistryEvent.NewRegistry event) ->
                event.dataPackRegistry(registryKey, codec, networkCodec));
    }
}
