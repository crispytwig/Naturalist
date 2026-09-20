package com.crispytwig.naturalist.datagen;

import com.crispytwig.naturalist.tags.NaturalistItemTags;
import com.crispytwig.naturalist.registry.NaturalistRegistry;
import com.crispytwig.naturalist.world.level.block.ChrysalisBlock;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyBlockState;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProviders;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
import org.jspecify.annotations.NonNull;

import java.util.Set;
import java.util.stream.Collectors;
import com.crispytwig.naturalist.world.level.block.AntHillBlock;
import net.minecraft.advancements.predicates.StatePropertiesPredicate;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchBlock;

public class NaturalistBlockLootProvider extends BlockLootSubProvider {
    protected NaturalistBlockLootProvider(LootTableSubProvider.Context output) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), output);
    }

    @Override
    protected void generate() {
        dropSelf(NaturalistRegistry.SHELLSTONE.get());
        dropSelf(NaturalistRegistry.SHELLSTONE_STAIRS.get());
        dropSelf(NaturalistRegistry.SHELLSTONE_SLAB.get());
        dropSelf(NaturalistRegistry.SHELLSTONE_WALL.get());
        dropSelf(NaturalistRegistry.SHELLSTONE_BRICKS.get());
        dropSelf(NaturalistRegistry.SHELLSTONE_BRICK_STAIRS.get());
        dropSelf(NaturalistRegistry.SHELLSTONE_BRICK_SLAB.get());
        dropSelf(NaturalistRegistry.SHELLSTONE_BRICK_WALL.get());
        dropSelf(NaturalistRegistry.CUT_SHELLSTONE.get());
        dropSelf(NaturalistRegistry.CUT_SHELLSTONE_STAIRS.get());
        dropSelf(NaturalistRegistry.CUT_SHELLSTONE_SLAB.get());
        dropSelf(NaturalistRegistry.CUT_SHELLSTONE_WALL.get());
        dropSelf(NaturalistRegistry.SMOOTH_SHELLSTONE.get());
        dropSelf(NaturalistRegistry.SMOOTH_SHELLSTONE_STAIRS.get());
        dropSelf(NaturalistRegistry.SMOOTH_SHELLSTONE_SLAB.get());
        dropSelf(NaturalistRegistry.SMOOTH_SHELLSTONE_WALL.get());

        dropSelf(NaturalistRegistry.GLOW_GOOP_BLOCK.get());
        dropSelf(NaturalistRegistry.PLUSH_BEAR.get());

        dropSelf(NaturalistRegistry.RED_STARFISH.get());
        dropSelf(NaturalistRegistry.ORANGE_STARFISH.get());
        dropSelf(NaturalistRegistry.BLUE_STARFISH.get());
        dropSelf(NaturalistRegistry.PURPLE_STARFISH.get());

        dropWhenSilkTouch(NaturalistRegistry.OSTRICH_EGG.get());
        dropWhenSilkTouch(NaturalistRegistry.ALLIGATOR_EGG.get());
        dropWhenSilkTouch(NaturalistRegistry.TORTOISE_EGG.get());
        dropWhenSilkTouch(NaturalistRegistry.AZURE_FROGLASS.get());
        dropWhenSilkTouch(NaturalistRegistry.VERDANT_FROGLASS.get());
        dropWhenSilkTouch(NaturalistRegistry.CRIMSON_FROGLASS.get());


        add(NaturalistRegistry.ANT_HILL.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ContextIntProviders.exactly(1))
                        .setBonusRolls(ContextFloatProviders.exactly(0.0F))
                        .when(ExplosionCondition.survivesExplosion())
                        .add(LootItem.lootTableItem(NaturalistRegistry.ANT_HILL.get())))
                .withPool(LootPool.lootPool()
                        .setRolls(ContextIntProviders.exactly(1))
                        .setBonusRolls(ContextFloatProviders.exactly(0.0F))
                        .when(MatchBlock.blockMatches(this.blocks, NaturalistRegistry.ANT_HILL.get(),
                                StatePropertiesPredicate.Builder.properties().hasProperty(AntHillBlock.HAS_QUEEN, true)))
                        .add(LootItem.lootTableItem(NaturalistRegistry.QUEEN_ANT.get()))));

        add(NaturalistRegistry.CHRYSALIS_BLOCK.get(), LootTable.lootTable().withPool(
                LootPool.lootPool()
                        .setRolls(ContextIntProviders.exactly(1))
                        .setBonusRolls(ContextFloatProviders.exactly(0.0F))
                        .add(AlternativesEntry.alternatives(
                                LootItem.lootTableItem(NaturalistRegistry.CHRYSALIS_BLOCK.get())
                                        .apply(CopyBlockState.copyState(NaturalistRegistry.CHRYSALIS_BLOCK.get()).copy(ChrysalisBlock.AGE))
                                        .when(new AnyOfCondition.Builder()
                                                .or(hasSilkTouch())
                                                .or(MatchTool.toolMatches(ItemPredicate.Builder.item().of(this.items, NaturalistItemTags.SHEARS))))
                        ))
        ));
    }

    @Override
    protected @NonNull Iterable<Block> getKnownBlocks() {
        return NaturalistRegistry.BLOCKS.getEntries().stream()
                .map(holder -> (Block) holder.get())
                .filter(block ->
                        block != NaturalistRegistry.AZURE_FROGLASS_PANE.get() &&
                        block != NaturalistRegistry.VERDANT_FROGLASS_PANE.get() &&
                        block != NaturalistRegistry.CRIMSON_FROGLASS_PANE.get() &&
                        block != NaturalistRegistry.SNAIL_EGGS.get() &&
                        block != NaturalistRegistry.SNAIL_SHELL_BLOCK.get()
                )
                .collect(Collectors.toList());
    }
}
