package com.crispytwig.naturalist.datagen;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.registry.NaturalistEntityTypes;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityEquipmentPredicate;
import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.PickedUpItemTrigger;
import net.minecraft.advancements.critereon.PlayerInteractTrigger;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class NaturalistAdvancementProvider implements ForgeAdvancementProvider.AdvancementGenerator {
    public static ForgeAdvancementProvider create(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
        return new ForgeAdvancementProvider(output, registries, existingFileHelper, List.of(new NaturalistAdvancementProvider()));
    }

    @Override
    public void generate(HolderLookup.Provider registries, Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {
        husbandry("feed_bear_honeycomb", Items.HONEYCOMB)
                .addCriterion("feed_bear", PickedUpItemTrigger.TriggerInstance.thrownItemPickedUpByEntity(
                        ContextAwarePredicate.ANY,
                        ItemPredicate.Builder.item().of(Items.HONEYCOMB).build(),
                        EntityPredicate.wrap(EntityPredicate.Builder.entity()
                                .of(NaturalistEntityTypes.BEAR.get())
                                .flags(EntityFlagsPredicate.Builder.flags().setIsBaby(false).build())
                                .build())))
                .save(saver, Naturalist.location("husbandry/feed_bear_honeycomb"), existingFileHelper);

        husbandry("feed_hippo_melon", Items.MELON)
                .addCriterion("feed_hippo", PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(
                        ItemPredicate.Builder.item().of(Items.MELON),
                        EntityPredicate.wrap(EntityPredicate.Builder.entity().of(NaturalistEntityTypes.HIPPO.get()).build())))
                .save(saver, Naturalist.location("husbandry/feed_hippo_melon"), existingFileHelper);

        husbandry("ride_giraffe_with_map", Items.FILLED_MAP)
                .addCriterion("holding_map_mainhand", ridingGiraffe(EntityEquipmentPredicate.Builder.equipment()
                        .mainhand(ItemPredicate.Builder.item().of(Items.FILLED_MAP).build())))
                .addCriterion("holding_map_offhand", ridingGiraffe(EntityEquipmentPredicate.Builder.equipment()
                        .offhand(ItemPredicate.Builder.item().of(Items.FILLED_MAP).build())))
                .requirements(RequirementsStrategy.OR)
                .save(saver, Naturalist.location("husbandry/ride_giraffe_with_map"), existingFileHelper);
    }

    private static Advancement.Builder husbandry(String name, ItemLike icon) {
        return Advancement.Builder.recipeAdvancement()
                .parent(new ResourceLocation("husbandry/root"))
                .display(icon,
                        Component.translatable("advancements.husbandry." + name + ".title"),
                        Component.translatable("advancements.husbandry." + name + ".description"),
                        null, FrameType.TASK, true, true, false);
    }

    private static PlayerTrigger.TriggerInstance ridingGiraffe(EntityEquipmentPredicate.Builder equipment) {
        return new PlayerTrigger.TriggerInstance(CriteriaTriggers.TICK.getId(), EntityPredicate.wrap(EntityPredicate.Builder.entity()
                .equipment(equipment.build())
                .vehicle(EntityPredicate.Builder.entity().of(NaturalistEntityTypes.GIRAFFE.get()).build())
                .build()));
    }
}
