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
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProviders;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
import static com.crispytwig.naturalist.registry.NaturalistRegistry.BASS;
import static net.minecraft.core.registries.Registries.LOOT_TABLE;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class NaturalistLootTableProvider {
    public static LootTableProvider create() {
        return new LootTableProvider(Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(NaturalistBlockLootProvider::new, LootContextParamSets.BLOCK),
                new LootTableProvider.SubProviderEntry(FishingLootProvider::new, LootContextParamSets.FISHING),
                new LootTableProvider.SubProviderEntry(NaturalistEntityLootProvider::new, LootContextParamSets.ENTITY)
        ));
    }

    public static class FishingLootProvider implements LootTableSubProvider {
        private final LootTableSubProvider.Context output;

        public FishingLootProvider(LootTableSubProvider.Context output) {
            this.output = output;
        }

        @Override
        public void run() {
            this.output.accept(
                    ResourceKey.create(LOOT_TABLE, Identifier.fromNamespaceAndPath(Naturalist.MOD_ID, "gameplay/fishing/fish")),
                    LootTable.lootTable().withPool(
                            LootPool.lootPool()
                                    .setRolls(ContextIntProviders.exactly(1))
                                    .setBonusRolls(ContextFloatProviders.exactly(0.0F))
                                    .add(LootItem.lootTableItem(BASS.get()).setWeight(40))
                    )
            );
        }
    }
}
