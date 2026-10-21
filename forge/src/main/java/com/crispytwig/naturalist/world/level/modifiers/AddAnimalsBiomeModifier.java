package com.crispytwig.naturalist.world.level.modifiers;

import com.mojang.serialization.Codec;
import com.crispytwig.naturalist.NaturalistConfig;
import com.crispytwig.naturalist.forge.registry.NaturalistBiomeModifiers;
import com.crispytwig.naturalist.world.level.NaturalistSpawns;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import org.jetbrains.annotations.NotNull;

public class AddAnimalsBiomeModifier implements BiomeModifier {
    @Override
    public void modify(@NotNull Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.@NotNull Builder builder) {
        if (phase.equals(Phase.ADD)) {
            NaturalistSpawns.forEachSpawn((hasTag, blacklistTag, category, entityType, weight, min, max) -> {
                if (NaturalistConfig.isRemoved(entityType)) {
                    return;
                }
                if (biome.is(hasTag) && (blacklistTag == null || !biome.is(blacklistTag))) {
                    builder.getMobSpawnSettings().addSpawn(category, new MobSpawnSettings.SpawnerData(entityType, weight, min, max));
                }
            });
        }
    }

    @Override
    public @NotNull Codec<? extends BiomeModifier> codec() {
        return NaturalistBiomeModifiers.ADD_ANIMALS_CODEC.get();
    }
}
