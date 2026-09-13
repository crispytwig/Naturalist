package com.crispytwig.naturalist.datagen;

import com.crispytwig.naturalist.registry.NaturalistEntityTypes;
import com.crispytwig.naturalist.registry.NaturalistRegistry;
import net.minecraft.advancements.criterion.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jspecify.annotations.NonNull;

import java.util.stream.Stream;

public class NaturalistEntityLootProvider extends EntityLootSubProvider {
    private final HolderLookup.Provider lookupProvider;

    protected NaturalistEntityLootProvider(HolderLookup.Provider registries) {
        super(FeatureFlags.REGISTRY.allFlags(), registries);
        this.lookupProvider = registries;
    }

    @Override
    public void generate() {
        add(NaturalistEntityTypes.BASS.get(), fishTable(NaturalistRegistry.BASS.get().asItem()));
        add(NaturalistEntityTypes.CATFISH.get(), fishTable(NaturalistRegistry.CATFISH.get().asItem()));
        add(NaturalistEntityTypes.LIZARD_TAIL.get(), fishTable(NaturalistRegistry.LIZARD_TAIL.get()));

        add(NaturalistEntityTypes.BIRD.get(), featherTable());

        add(NaturalistEntityTypes.BUTTERFLY.get(), simpleLootingTable(Items.BONE_MEAL, 0, 1));
        add(NaturalistEntityTypes.CATERPILLAR.get(), simpleLootingTable(Items.BONE_MEAL, 0, 1));
        add(NaturalistEntityTypes.BEAR.get(), simpleLootingTable(NaturalistRegistry.FUR.get(), 1, 2));
        add(NaturalistEntityTypes.TORTOISE.get(), simpleLootingTable(Items.TURTLE_SCUTE, 0, 1));

        add(NaturalistEntityTypes.HIPPO.get(), LootTable.lootTable().withPool(
                LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(Items.MELON_SLICE)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))))
        ));

        add(NaturalistEntityTypes.FIREFLY.get(), LootTable.lootTable().withPool(
                LootPool.lootPool().setRolls(ConstantValue.exactly(1)).setBonusRolls(ConstantValue.exactly(0))
                        .add(LootItem.lootTableItem(NaturalistRegistry.GLOW_GOOP.get())
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.lookupProvider, UniformGenerator.between(0, 1))))
        ));

        add(NaturalistEntityTypes.BOAR.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(Items.PORKCHOP)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
                                .apply(SmeltItemFunction.smelted().when(onFire()))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.lookupProvider, UniformGenerator.between(0, 1)))))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(Items.LEATHER)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 2)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.lookupProvider, UniformGenerator.between(0, 1)))))
        );

        add(NaturalistEntityTypes.DEER.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).setBonusRolls(ConstantValue.exactly(0))
                        .add(LootItem.lootTableItem(Items.LEATHER)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 2)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.lookupProvider, UniformGenerator.between(0, 1)))))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).setBonusRolls(ConstantValue.exactly(0))
                        .add(LootItem.lootTableItem(NaturalistRegistry.VENISON.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                                .apply(SmeltItemFunction.smelted().when(onFire()))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.lookupProvider, UniformGenerator.between(0, 1)))))
        );

        add(NaturalistEntityTypes.DUCK.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(Items.FEATHER)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 2)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.lookupProvider, UniformGenerator.between(0, 1)))))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(NaturalistRegistry.DUCK.get())
                                .apply(SmeltItemFunction.smelted().when(onFire()))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.lookupProvider, UniformGenerator.between(0, 1)))))
        );

        add(NaturalistEntityTypes.ELEPHANT.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(Items.LEATHER)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 5)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.lookupProvider, UniformGenerator.between(0, 1)))))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(NaturalistRegistry.BUSHMEAT.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.lookupProvider, UniformGenerator.between(0, 1)))))
        );

        add(NaturalistEntityTypes.LION.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(Items.LEATHER)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 1)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.lookupProvider, UniformGenerator.between(0, 1)))))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(NaturalistRegistry.FUR.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.lookupProvider, UniformGenerator.between(0, 1)))))
        );

        add(NaturalistEntityTypes.RHINO.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(Items.LEATHER)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.lookupProvider, UniformGenerator.between(0, 1)))))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(NaturalistRegistry.BUSHMEAT.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.lookupProvider, UniformGenerator.between(0, 1)))))
        );

        add(NaturalistEntityTypes.VULTURE.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(Items.FEATHER)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 2)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.lookupProvider, UniformGenerator.between(0, 1)))))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(Items.ROTTEN_FLESH)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 2)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.lookupProvider, UniformGenerator.between(0, 1)))))
        );

        add(NaturalistEntityTypes.GIRAFFE.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).setBonusRolls(ConstantValue.exactly(0))
                        .add(LootItem.lootTableItem(NaturalistRegistry.BUSHMEAT.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                                .apply(SmeltItemFunction.smelted().when(onFire()))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.lookupProvider, UniformGenerator.between(0, 1)))))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(Items.HAY_BLOCK)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 1)))))
        );

        add(NaturalistEntityTypes.SNAIL.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(NaturalistRegistry.SNAIL_SHELL.get())
                                .when(LootItemRandomChanceCondition.randomChance(0.1F))))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).setBonusRolls(ConstantValue.exactly(0))
                        .add(LootItem.lootTableItem(Items.SLIME_BALL)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 1)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.lookupProvider, UniformGenerator.between(0, 1)))))
        );

        CompoundTag variant0 = new CompoundTag();
        variant0.putInt("Variant", 0);
        CompoundTag variant1 = new CompoundTag();
        variant1.putInt("Variant", 1);
        CompoundTag variant2 = new CompoundTag();
        variant2.putInt("Variant", 2);

        add(NaturalistEntityTypes.DRAGONFLY.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).setBonusRolls(ConstantValue.exactly(0))
                        .when(DamageSourceCondition.hasDamageSource(DamageSourcePredicate.Builder.damageType()
                                .source(EntityPredicate.Builder.entity().of(EntityType.FROG))))
                        .add(LootItem.lootTableItem(NaturalistRegistry.AZURE_FROGLASS.get())
                                .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
                                        EntityPredicate.Builder.entity().nbt(new NbtPredicate(variant0)))))
                        .add(LootItem.lootTableItem(NaturalistRegistry.VERDANT_FROGLASS.get())
                                .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
                                        EntityPredicate.Builder.entity().nbt(new NbtPredicate(variant1)))))
                        .add(LootItem.lootTableItem(NaturalistRegistry.CRIMSON_FROGLASS.get())
                                .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
                                        EntityPredicate.Builder.entity().nbt(new NbtPredicate(variant2)))))
                )
        );
    }

    private LootTable.Builder fishTable(ItemLike item) {
        return LootTable.lootTable().withPool(
                LootPool.lootPool().setRolls(ConstantValue.exactly(1)).setBonusRolls(ConstantValue.exactly(0))
                        .add(LootItem.lootTableItem(item)
                                .apply(SmeltItemFunction.smelted().when(onFire())))
        );
    }

    private LootTable.Builder featherTable() {
        return LootTable.lootTable().withPool(
                LootPool.lootPool().setRolls(ConstantValue.exactly(1)).setBonusRolls(ConstantValue.exactly(0))
                        .add(LootItem.lootTableItem(Items.FEATHER)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.lookupProvider, UniformGenerator.between(0, 1))))
        );
    }

    private LootTable.Builder simpleLootingTable(ItemLike item, int min, int max) {
        return LootTable.lootTable().withPool(
                LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(item)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.lookupProvider, UniformGenerator.between(0, 1))))
        );
    }

    private static LootItemEntityPropertyCondition.Builder onFire() {
        return LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
                EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setOnFire(true)));
    }

    @Override
    protected @NonNull Stream<EntityType<?>> getKnownEntityTypes() {
        return Stream.of(
                NaturalistEntityTypes.BASS.get(),
                NaturalistEntityTypes.BEAR.get(),
                NaturalistEntityTypes.BIRD.get(),
                NaturalistEntityTypes.BOAR.get(),
                NaturalistEntityTypes.BUTTERFLY.get(),
                NaturalistEntityTypes.CATERPILLAR.get(),
                NaturalistEntityTypes.CATFISH.get(),
                NaturalistEntityTypes.DEER.get(),
                NaturalistEntityTypes.DRAGONFLY.get(),
                NaturalistEntityTypes.DUCK.get(),
                NaturalistEntityTypes.ELEPHANT.get(),
                NaturalistEntityTypes.FIREFLY.get(),
                NaturalistEntityTypes.GIRAFFE.get(),
                NaturalistEntityTypes.HIPPO.get(),
                NaturalistEntityTypes.LION.get(),
                NaturalistEntityTypes.LIZARD_TAIL.get(),
                NaturalistEntityTypes.RHINO.get(),
                NaturalistEntityTypes.SNAIL.get(),
                NaturalistEntityTypes.TORTOISE.get(),
                NaturalistEntityTypes.VULTURE.get()
        );
    }
}
