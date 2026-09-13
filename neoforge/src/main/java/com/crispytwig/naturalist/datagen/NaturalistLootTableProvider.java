package com.crispytwig.naturalist.datagen;

import com.crispytwig.naturalist.Naturalist;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import static com.crispytwig.naturalist.registry.NaturalistRegistry.BASS;
import static net.minecraft.core.registries.Registries.LOOT_TABLE;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class NaturalistLootTableProvider {
    public static LootTableProvider create(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        return new LootTableProvider(output, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(NaturalistBlockLootProvider::new, LootContextParamSets.BLOCK),
                new LootTableProvider.SubProviderEntry(NaturalistEntityLootProvider::new, LootContextParamSets.ENTITY),
                new LootTableProvider.SubProviderEntry(FishingLootProvider::new, LootContextParamSets.FISHING)
        ), registries);
    }

    public static class FishingLootProvider implements LootTableSubProvider {
        public FishingLootProvider(HolderLookup.Provider registries) {
        }

        @Override
        public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
            output.accept(
                    ResourceKey.create(LOOT_TABLE, Identifier.fromNamespaceAndPath(Naturalist.MOD_ID, "gameplay/fishing/fish")),
                    LootTable.lootTable().withPool(
                            LootPool.lootPool()
                                    .setRolls(ConstantValue.exactly(1))
                                    .setBonusRolls(ConstantValue.exactly(0))
                                    .add(LootItem.lootTableItem(BASS.get()).setWeight(40))
                    )
            );
        }
    }
}
