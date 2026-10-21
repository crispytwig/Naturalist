package com.crispytwig.naturalist.datagen;

import com.crispytwig.naturalist.registry.NaturalistEntityTypes;
import com.crispytwig.naturalist.registry.NaturalistRegistry;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.stream.Stream;

public class NaturalistEntityLootProvider extends EntityLootSubProvider {
    protected NaturalistEntityLootProvider() {
        super(FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    public void generate() {
        add(NaturalistEntityTypes.ALLIGATOR.get(), table(drop(NaturalistRegistry.TOOTH.get(), 1, 3)));

        add(NaturalistEntityTypes.ANGLERFISH.get(), LootTable.lootTable()
                .withPool(pool()
                        .add(LootItem.lootTableItem(Items.GLOWSTONE_DUST)
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
                        .add(EmptyLootItem.emptyItem().setWeight(4)))
                .withPool(smeltOnly(NaturalistRegistry.ANGLERFISH.get())));

        add(NaturalistEntityTypes.ANT.get(), LootTable.lootTable());

        add(NaturalistEntityTypes.BASS.get(), table(smeltOnly(NaturalistRegistry.BASS.get())));
        add(NaturalistEntityTypes.BEAR.get(), table(drop(NaturalistRegistry.FUR.get(), 1, 2)));
        add(NaturalistEntityTypes.BIRD.get(), table(drop(Items.FEATHER, 1, 2)));
        add(NaturalistEntityTypes.BLACK_BEAR.get(), table(drop(NaturalistRegistry.FUR.get(), 1, 2)));
        add(NaturalistEntityTypes.BLOBFISH.get(), table(smeltOnly(NaturalistRegistry.BLOBFISH.get())));

        add(NaturalistEntityTypes.BOAR.get(), LootTable.lootTable()
                .withPool(meat(Items.PORKCHOP, 1, 3))
                .withPool(drop(NaturalistRegistry.FAT.get(), 0, 2))
                .withPool(drop(NaturalistRegistry.HIDE.get(), 0, 2)));

        add(NaturalistEntityTypes.BUTTERFLY.get(), table(drop(Items.BONE_MEAL, 0, 1)));

        add(NaturalistEntityTypes.CAPYBARA.get(), LootTable.lootTable()
                .withPool(meat(NaturalistRegistry.BUSHMEAT.get(), 0, 1))
                .withPool(drop(NaturalistRegistry.FUR.get(), 0, 1)));

        add(NaturalistEntityTypes.CATERPILLAR.get(), table(drop(Items.BONE_MEAL, 0, 1)));
        add(NaturalistEntityTypes.CATFISH.get(), table(smeltOnly(NaturalistRegistry.CATFISH.get())));

        add(NaturalistEntityTypes.CLAM.get(), table(pool()
                .add(LootItem.lootTableItem(NaturalistRegistry.CLAM_MEAT.get())
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
                        .apply(SmeltItemFunction.smelted().when(onFire()))
                        .apply(looting(1.0F)))));

        add(NaturalistEntityTypes.CRAB.get(), LootTable.lootTable()
                .withPool(meat(NaturalistRegistry.CRAB_MEAT.get(), 1, 2))
                .withPool(pool()
                        .add(LootItem.lootTableItem(Items.NAUTILUS_SHELL).setWeight(5))
                        .add(EmptyLootItem.emptyItem().setWeight(95))));

        add(NaturalistEntityTypes.DEER.get(), LootTable.lootTable()
                .withPool(drop(Items.LEATHER, 0, 2))
                .withPool(meat(NaturalistRegistry.VENISON.get(), 1, 2))
                .withPool(drop(NaturalistRegistry.ANTLER.get(), 0, 1)));

        add(NaturalistEntityTypes.DESERT_SCORPION.get(), table(scorpionPool()));

        add(NaturalistEntityTypes.DRAGONFLY.get(), table(pool()
                .when(killedByFrog())
                .add(LootItem.lootTableItem(NaturalistRegistry.AZURE_FROGLASS.get()).when(variant(0)))
                .add(LootItem.lootTableItem(NaturalistRegistry.VERDANT_FROGLASS.get()).when(variant(1)))
                .add(LootItem.lootTableItem(NaturalistRegistry.CRIMSON_FROGLASS.get()).when(variant(2)))));

        add(NaturalistEntityTypes.DUCK.get(), LootTable.lootTable()
                .withPool(drop(Items.FEATHER, 0, 2))
                .withPool(smeltLooting(NaturalistRegistry.DUCK.get(), 1.0F)));

        add(NaturalistEntityTypes.ELEPHANT.get(), LootTable.lootTable()
                .withPool(drop(NaturalistRegistry.HIDE.get(), 3, 5))
                .withPool(drop(NaturalistRegistry.BUSHMEAT.get(), 1, 3)));

        add(NaturalistEntityTypes.FIREFLY.get(), table(pool()
                .add(LootItem.lootTableItem(NaturalistRegistry.GLOW_GOOP.get())
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
                        .apply(looting(1.0F)))));

        add(NaturalistEntityTypes.GIANT_ISOPOD.get(), table(drop(Items.BONE_MEAL, 0, 1)));

        add(NaturalistEntityTypes.GIRAFFE.get(), LootTable.lootTable()
                .withPool(meat(NaturalistRegistry.BUSHMEAT.get(), 1, 2))
                .withPool(countOnly(Items.HAY_BLOCK, 0, 1)));

        add(NaturalistEntityTypes.GREAT_WHITE_SHARK.get(), table(countOnly(NaturalistRegistry.TOOTH.get(), 2, 5)));
        add(NaturalistEntityTypes.HEDGEHOG.get(), table(smeltLooting(NaturalistRegistry.MORSEL.get(), 2.0F)));
        add(NaturalistEntityTypes.HIPPO.get(), table(countOnly(Items.MELON_SLICE, 1, 5)));

        add(NaturalistEntityTypes.JELLYFISH.get(), table(pool()
                .add(LootItem.lootTableItem(Items.SLIME_BALL)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 1.0F)))
                        .setWeight(2))
                .add(EmptyLootItem.emptyItem().setWeight(8))));

        add(NaturalistEntityTypes.JUNGLE_SCORPION.get(), table(scorpionPool()));

        add(NaturalistEntityTypes.KOMODO_DRAGON.get(), LootTable.lootTable()
                .withPool(drop(NaturalistRegistry.HIDE.get(), 0, 1))
                .withPool(smeltLooting(NaturalistRegistry.BUSHMEAT.get(), 1.0F)));

        add(NaturalistEntityTypes.LION.get(), LootTable.lootTable()
                .withPool(drop(NaturalistRegistry.TOOTH.get(), 1, 3))
                .withPool(drop(NaturalistRegistry.FUR.get(), 1, 2)));

        add(NaturalistEntityTypes.LIZARD.get(), LootTable.lootTable()
                .withPool(drop(Items.BONE_MEAL, 0, 1))
                .withPool(drop(NaturalistRegistry.MORSEL.get(), 0, 1)));

        add(NaturalistEntityTypes.LIZARD_TAIL.get(), table(smeltOnly(NaturalistRegistry.LIZARD_TAIL.get())));

        add(NaturalistEntityTypes.MAMMOTH.get(), LootTable.lootTable()
                .withPool(drop(NaturalistRegistry.FAT.get(), 2, 3))
                .withPool(drop(NaturalistRegistry.FUR.get(), 2, 3))
                .withPool(meat(NaturalistRegistry.MAMMOTH_MEAT.get(), 2, 3)));

        add(NaturalistEntityTypes.MOLE.get(), table(pool()
                .add(LootItem.lootTableItem(Items.DIRT)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
                        .apply(looting(1.0F)))
                .add(LootItem.lootTableItem(NaturalistRegistry.FUR.get())
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
                        .apply(looting(1.0F)))));

        add(NaturalistEntityTypes.OSTRICH.get(), table(pool()
                .add(LootItem.lootTableItem(NaturalistRegistry.DRUMSTICK.get())
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
                        .apply(SmeltItemFunction.smelted().when(onFire()))
                        .apply(looting(1.0F)))
                .add(LootItem.lootTableItem(Items.FEATHER)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
                        .apply(looting(1.0F)))));

        add(NaturalistEntityTypes.PIRANHA.get(), LootTable.lootTable()
                .withPool(smeltOnly(NaturalistRegistry.PIRANHA.get()))
                .withPool(pool()
                        .when(LootItemRandomChanceCondition.randomChance(0.05F))
                        .add(LootItem.lootTableItem(Items.BONE_MEAL))));

        add(NaturalistEntityTypes.RAT.get(), LootTable.lootTable()
                .withPool(drop(NaturalistRegistry.FUR.get(), 0, 2))
                .withPool(pool().add(LootItem.lootTableItem(NaturalistRegistry.MORSEL.get()).apply(looting(1.0F)))));

        add(NaturalistEntityTypes.RAY.get(), table(meat(NaturalistRegistry.CRAB_MEAT.get(), 1, 2)));

        add(NaturalistEntityTypes.RHINO.get(), LootTable.lootTable()
                .withPool(drop(NaturalistRegistry.HIDE.get(), 1, 3))
                .withPool(drop(NaturalistRegistry.BUSHMEAT.get(), 1, 3)));

        add(NaturalistEntityTypes.SNAIL.get(), LootTable.lootTable()
                .withPool(pool().add(LootItem.lootTableItem(NaturalistRegistry.SNAIL_SHELL.get())))
                .withPool(drop(Items.SLIME_BALL, 0, 1)));

        add(NaturalistEntityTypes.SNAKE.get(), LootTable.lootTable()
                .withPool(pool().add(LootItem.lootTableItem(NaturalistRegistry.TOOTH.get())
                        .when(LootItemRandomChanceCondition.randomChance(0.4F))
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
                        .apply(looting(1.0F))))
                .withPool(drop(NaturalistRegistry.MORSEL.get(), 0, 1)));

        add(NaturalistEntityTypes.TIGER.get(), table(pool()
                .add(LootItem.lootTableItem(NaturalistRegistry.TOOTH.get())
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
                        .apply(looting(1.0F)))
                .add(LootItem.lootTableItem(NaturalistRegistry.FUR.get())
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
                        .apply(looting(1.0F)))));

        add(NaturalistEntityTypes.TORTOISE.get(), table(drop(Items.SCUTE, 0, 1)));

        add(NaturalistEntityTypes.TURKEY.get(), LootTable.lootTable()
                .withPool(drop(Items.FEATHER, 0, 2))
                .withPool(smeltLooting(NaturalistRegistry.DRUMSTICK.get(), 1.0F)));

        add(NaturalistEntityTypes.VULTURE.get(), LootTable.lootTable()
                .withPool(drop(Items.FEATHER, 0, 2))
                .withPool(drop(Items.ROTTEN_FLESH, 0, 2))
                .withPool(drop(NaturalistRegistry.MORSEL.get(), 0, 1)));

        add(NaturalistEntityTypes.WHALE.get(), LootTable.lootTable()
                .withPool(pool()
                        .add(LootItem.lootTableItem(Items.COD)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
                                .apply(looting(1.0F)))
                        .add(LootItem.lootTableItem(Items.SALMON)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
                                .apply(looting(1.0F)))
                        .add(LootItem.lootTableItem(Items.TROPICAL_FISH)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
                                .apply(looting(1.0F))))
                .withPool(drop(NaturalistRegistry.FAT.get(), 2, 5)));

        add(NaturalistEntityTypes.ZEBRA.get(), LootTable.lootTable()
                .withPool(drop(Items.LEATHER, 0, 2))
                .withPool(meat(NaturalistRegistry.BUSHMEAT.get(), 1, 2)));
    }

    private static LootTable.Builder table(LootPool.Builder pool) {
        return LootTable.lootTable().withPool(pool);
    }

    private static LootPool.Builder pool() {
        return LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0F))
                .setBonusRolls(ConstantValue.exactly(0.0F));
    }

    private static LootItemFunction.Builder looting(float max) {
        return LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, max));
    }

    private static LootPool.Builder drop(ItemLike item, int min, int max) {
        return pool().add(LootItem.lootTableItem(item)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)))
                .apply(looting(1.0F)));
    }

    private static LootPool.Builder meat(ItemLike item, int min, int max) {
        return pool().add(LootItem.lootTableItem(item)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)))
                .apply(SmeltItemFunction.smelted().when(onFire()))
                .apply(looting(1.0F)));
    }

    private static LootPool.Builder smeltOnly(ItemLike item) {
        return pool().add(LootItem.lootTableItem(item)
                .apply(SmeltItemFunction.smelted().when(onFire())));
    }

    private static LootPool.Builder smeltLooting(ItemLike item, float max) {
        return pool().add(LootItem.lootTableItem(item)
                .apply(SmeltItemFunction.smelted().when(onFire()))
                .apply(looting(max)));
    }

    private static LootPool.Builder countOnly(ItemLike item, int min, int max) {
        return pool().add(LootItem.lootTableItem(item)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max))));
    }

    private static LootPool.Builder scorpionPool() {
        return pool()
                .when(LootItemRandomChanceCondition.randomChance(0.12F))
                .add(LootItem.lootTableItem(NaturalistRegistry.SCORPION_POISON_GLAND.get())
                        .apply(looting(1.0F)));
    }

    private static LootItemCondition.Builder variant(int variant) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("Variant", variant);
        return LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
                EntityPredicate.Builder.entity().nbt(new NbtPredicate(tag)));
    }

    private static LootItemCondition.Builder onFire() {
        return LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE);
    }

    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
        return Stream.of(
                NaturalistEntityTypes.ALLIGATOR.get(),
                NaturalistEntityTypes.ANGLERFISH.get(),
                NaturalistEntityTypes.ANT.get(),
                NaturalistEntityTypes.BASS.get(),
                NaturalistEntityTypes.BEAR.get(),
                NaturalistEntityTypes.BIRD.get(),
                NaturalistEntityTypes.BLACK_BEAR.get(),
                NaturalistEntityTypes.BLOBFISH.get(),
                NaturalistEntityTypes.BOAR.get(),
                NaturalistEntityTypes.BUTTERFLY.get(),
                NaturalistEntityTypes.CAPYBARA.get(),
                NaturalistEntityTypes.CATERPILLAR.get(),
                NaturalistEntityTypes.CATFISH.get(),
                NaturalistEntityTypes.CLAM.get(),
                NaturalistEntityTypes.CRAB.get(),
                NaturalistEntityTypes.DEER.get(),
                NaturalistEntityTypes.DESERT_SCORPION.get(),
                NaturalistEntityTypes.DRAGONFLY.get(),
                NaturalistEntityTypes.DUCK.get(),
                NaturalistEntityTypes.ELEPHANT.get(),
                NaturalistEntityTypes.FIREFLY.get(),
                NaturalistEntityTypes.GIANT_ISOPOD.get(),
                NaturalistEntityTypes.GIRAFFE.get(),
                NaturalistEntityTypes.GREAT_WHITE_SHARK.get(),
                NaturalistEntityTypes.HEDGEHOG.get(),
                NaturalistEntityTypes.HIPPO.get(),
                NaturalistEntityTypes.JELLYFISH.get(),
                NaturalistEntityTypes.JUNGLE_SCORPION.get(),
                NaturalistEntityTypes.KOMODO_DRAGON.get(),
                NaturalistEntityTypes.LION.get(),
                NaturalistEntityTypes.LIZARD.get(),
                NaturalistEntityTypes.LIZARD_TAIL.get(),
                NaturalistEntityTypes.MAMMOTH.get(),
                NaturalistEntityTypes.MOLE.get(),
                NaturalistEntityTypes.OSTRICH.get(),
                NaturalistEntityTypes.PIRANHA.get(),
                NaturalistEntityTypes.RAT.get(),
                NaturalistEntityTypes.RAY.get(),
                NaturalistEntityTypes.RHINO.get(),
                NaturalistEntityTypes.SNAIL.get(),
                NaturalistEntityTypes.SNAKE.get(),
                NaturalistEntityTypes.TIGER.get(),
                NaturalistEntityTypes.TORTOISE.get(),
                NaturalistEntityTypes.TURKEY.get(),
                NaturalistEntityTypes.VULTURE.get(),
                NaturalistEntityTypes.WHALE.get(),
                NaturalistEntityTypes.ZEBRA.get()
        );
    }
}
