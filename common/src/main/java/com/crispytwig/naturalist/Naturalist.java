package com.crispytwig.naturalist;

import com.mojang.logging.LogUtils;
import com.crispytwig.naturalist.compat.fieldguide.FieldGuidePlugin;
import com.crispytwig.naturalist.platform.Services;
import com.crispytwig.naturalist.registry.NaturalistBlockEntities;
import com.crispytwig.naturalist.registry.NaturalistCreativeTab;
import com.crispytwig.naturalist.registry.NaturalistEntityTypes;
import com.crispytwig.naturalist.registry.NaturalistFeatures;
import com.crispytwig.naturalist.registry.NaturalistMenus;
import com.crispytwig.naturalist.registry.NaturalistMobEffects;
import com.crispytwig.naturalist.registry.NaturalistMobVariants;
import com.crispytwig.naturalist.registry.NaturalistParticleTypes;
import com.crispytwig.naturalist.registry.NaturalistPotions;
import com.crispytwig.naturalist.registry.NaturalistRecipes;
import com.crispytwig.naturalist.registry.NaturalistRegistry;
import com.crispytwig.naturalist.registry.NaturalistSoundEvents;
import com.crispytwig.naturalist.world.entity.animal.NaturalistAnimal;
import com.crispytwig.naturalist.world.entity.animal.alligator.Alligator;
import com.crispytwig.naturalist.world.entity.animal.fish.Anglerfish;
import com.crispytwig.naturalist.world.entity.animal.ant.Ant;
import com.crispytwig.naturalist.world.entity.animal.fish.Bass;
import com.crispytwig.naturalist.world.entity.animal.bear.Bear;
import com.crispytwig.naturalist.world.entity.animal.bird.Bird;
import com.crispytwig.naturalist.world.entity.animal.bear.BlackBear;
import com.crispytwig.naturalist.world.entity.animal.fish.Blobfish;
import com.crispytwig.naturalist.world.entity.animal.boar.Boar;
import com.crispytwig.naturalist.world.entity.animal.butterfly.Butterfly;
import com.crispytwig.naturalist.world.entity.animal.capybara.Capybara;
import com.crispytwig.naturalist.world.entity.animal.butterfly.Caterpillar;
import com.crispytwig.naturalist.world.entity.animal.fish.Catfish;
import com.crispytwig.naturalist.world.entity.animal.clam.Clam;
import com.crispytwig.naturalist.world.entity.animal.crab.Crab;
import com.crispytwig.naturalist.world.entity.animal.deer.Deer;
import com.crispytwig.naturalist.world.entity.animal.scorpion.DesertScorpion;
import com.crispytwig.naturalist.world.entity.animal.dragonfly.Dragonfly;
import com.crispytwig.naturalist.world.entity.animal.duck.Duck;
import com.crispytwig.naturalist.world.entity.animal.elephant.Elephant;
import com.crispytwig.naturalist.world.entity.animal.firefly.Firefly;
import com.crispytwig.naturalist.world.entity.animal.giantisopod.GiantIsopod;
import com.crispytwig.naturalist.world.entity.animal.giraffe.Giraffe;
import com.crispytwig.naturalist.world.entity.animal.greatwhiteshark.GreatWhiteShark;
import com.crispytwig.naturalist.world.entity.animal.hedgehog.Hedgehog;
import com.crispytwig.naturalist.world.entity.animal.hippo.Hippo;
import com.crispytwig.naturalist.world.entity.animal.jellyfish.Jellyfish;
import com.crispytwig.naturalist.world.entity.animal.scorpion.JungleScorpion;
import com.crispytwig.naturalist.world.entity.animal.komododragon.KomodoDragon;
import com.crispytwig.naturalist.world.entity.animal.lion.Lion;
import com.crispytwig.naturalist.world.entity.animal.lizard.Lizard;
import com.crispytwig.naturalist.world.entity.animal.lizard.LizardTail;
import com.crispytwig.naturalist.world.entity.animal.elephant.Mammoth;
import com.crispytwig.naturalist.world.entity.animal.mole.Mole;
import com.crispytwig.naturalist.world.entity.animal.ostrich.Ostrich;
import com.crispytwig.naturalist.world.entity.animal.fish.Piranha;
import com.crispytwig.naturalist.world.entity.animal.rat.Rat;
import com.crispytwig.naturalist.world.entity.animal.fish.Ray;
import com.crispytwig.naturalist.world.entity.animal.rhino.Rhino;
import com.crispytwig.naturalist.world.entity.animal.scorpion.Scorpion;
import com.crispytwig.naturalist.world.entity.animal.snail.Snail;
import com.crispytwig.naturalist.world.entity.animal.snake.Snake;
import com.crispytwig.naturalist.world.entity.animal.starfish.Starfish;
import com.crispytwig.naturalist.world.entity.animal.tiger.Tiger;
import com.crispytwig.naturalist.world.entity.animal.tortoise.Tortoise;
import com.crispytwig.naturalist.world.entity.animal.turkey.Turkey;
import com.crispytwig.naturalist.world.entity.animal.vulture.Vulture;
import com.crispytwig.naturalist.world.entity.animal.whale.Whale;
import com.crispytwig.naturalist.world.entity.animal.equine.Zebra;
import com.crispytwig.naturalist.world.item.CaughtMobItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.animal.fish.WaterAnimal;
import net.minecraft.world.item.DispensibleContainerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.function.Supplier;

public final class Naturalist {
    public static final String MOD_ID = "naturalist";
    public static final Logger LOGGER = LogUtils.getLogger();

    private Naturalist() {
    }

    public static Identifier location(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public static void bootstrap() {
        touch(NaturalistSoundEvents.SOUND_EVENTS);
        touch(NaturalistParticleTypes.PARTICLE_TYPES);
        touch(NaturalistEntityTypes.ENTITY_TYPES);
        touch(NaturalistFeatures.FEATURES);
        NaturalistRegistry.init();
        touch(NaturalistBlockEntities.BLOCK_ENTITY_TYPES);
        touch(NaturalistMenus.MENUS);
        touch(NaturalistMobEffects.MOB_EFFECTS);
        touch(NaturalistPotions.POTIONS);
        touch(NaturalistRecipes.RECIPE_TYPES);
        touch(NaturalistRecipes.RECIPE_SERIALIZERS);
        touch(NaturalistCreativeTab.CREATIVE_MODE_TABS);
        NaturalistMobVariants.bootstrap();

        if (Services.PLATFORM.isModLoaded("fieldguide")) {
            FieldGuidePlugin.register();
        }
    }

    private static void touch(Object registry) {
    }

    @FunctionalInterface
    public interface AttributeRegistrar {
        void register(EntityType<? extends LivingEntity> type, AttributeSupplier.Builder builder);
    }

    @FunctionalInterface
    public interface SpawnPlacementRegistrar {
        <T extends Mob> void register(EntityType<T> type, SpawnPlacementType placementType, Heightmap.Types heightmap, SpawnPlacements.SpawnPredicate<T> predicate);
    }

    @FunctionalInterface
    public interface BrewingRegistrar {
        void addMix(Holder<Potion> input, Item ingredient, Holder<Potion> output);
    }

    public static void createAttributes(AttributeRegistrar r) {
        r.register(NaturalistEntityTypes.SNAIL.get(), Snail.createAttributes());
        r.register(NaturalistEntityTypes.BEAR.get(), Bear.createAttributes());
        r.register(NaturalistEntityTypes.BUTTERFLY.get(), Butterfly.createAttributes());
        r.register(NaturalistEntityTypes.FIREFLY.get(), Firefly.createAttributes());
        r.register(NaturalistEntityTypes.SNAKE.get(), Snake.createAttributes());
        r.register(NaturalistEntityTypes.CRAB.get(), Crab.createAttributes());
        r.register(NaturalistEntityTypes.DEER.get(), Deer.createAttributes());
        r.register(NaturalistEntityTypes.BIRD.get(), Bird.createAttributes());
        r.register(NaturalistEntityTypes.CATERPILLAR.get(), Caterpillar.createAttributes());
        r.register(NaturalistEntityTypes.RHINO.get(), Rhino.createAttributes());
        r.register(NaturalistEntityTypes.LION.get(), Lion.createAttributes());
        r.register(NaturalistEntityTypes.ELEPHANT.get(), Elephant.createAttributes());
        r.register(NaturalistEntityTypes.MAMMOTH.get(), Mammoth.createAttributes());
        r.register(NaturalistEntityTypes.ZEBRA.get(), Zebra.createBaseHorseAttributes());
        r.register(NaturalistEntityTypes.GIRAFFE.get(), Giraffe.createAttributes());
        r.register(NaturalistEntityTypes.HIPPO.get(), Hippo.createAttributes());
        r.register(NaturalistEntityTypes.VULTURE.get(), Vulture.createAttributes());
        r.register(NaturalistEntityTypes.BOAR.get(), Boar.createAttributes());
        r.register(NaturalistEntityTypes.DRAGONFLY.get(), Dragonfly.createAttributes());
        r.register(NaturalistEntityTypes.CATFISH.get(), Catfish.createAttributes());
        r.register(NaturalistEntityTypes.ANGLERFISH.get(), Anglerfish.createAttributes());
        r.register(NaturalistEntityTypes.RAY.get(), Ray.createAttributes());
        r.register(NaturalistEntityTypes.BLOBFISH.get(), Blobfish.createAttributes());
        r.register(NaturalistEntityTypes.PIRANHA.get(), Piranha.createAttributes());
        r.register(NaturalistEntityTypes.ALLIGATOR.get(), Alligator.createAttributes());
        r.register(NaturalistEntityTypes.BASS.get(), Bass.createAttributes());
        r.register(NaturalistEntityTypes.LIZARD.get(), Lizard.createAttributes());
        r.register(NaturalistEntityTypes.LIZARD_TAIL.get(), LizardTail.createAttributes());
        r.register(NaturalistEntityTypes.TORTOISE.get(), Tortoise.createAttributes());
        r.register(NaturalistEntityTypes.DUCK.get(), Duck.createAttributes());
        r.register(NaturalistEntityTypes.STARFISH.get(), Starfish.createAttributes());
        r.register(NaturalistEntityTypes.CLAM.get(), Clam.createAttributes());
        r.register(NaturalistEntityTypes.GIANT_ISOPOD.get(), GiantIsopod.createAttributes());
        r.register(NaturalistEntityTypes.JELLYFISH.get(), Jellyfish.createAttributes());
        r.register(NaturalistEntityTypes.WHALE.get(), Whale.createAttributes());
        r.register(NaturalistEntityTypes.ANT.get(), Ant.createAttributes());
        r.register(NaturalistEntityTypes.MOLE.get(), Mole.createAttributes());
        r.register(NaturalistEntityTypes.RAT.get(), Rat.createAttributes());
        r.register(NaturalistEntityTypes.BLACK_BEAR.get(), BlackBear.createAttributes());
        r.register(NaturalistEntityTypes.TIGER.get(), Tiger.createAttributes());
        r.register(NaturalistEntityTypes.KOMODO_DRAGON.get(), KomodoDragon.createAttributes());
        r.register(NaturalistEntityTypes.OSTRICH.get(), Ostrich.createAttributes());
        r.register(NaturalistEntityTypes.DESERT_SCORPION.get(), DesertScorpion.createAttributes());
        r.register(NaturalistEntityTypes.JUNGLE_SCORPION.get(), JungleScorpion.createAttributes());
        r.register(NaturalistEntityTypes.GREAT_WHITE_SHARK.get(), GreatWhiteShark.createAttributes());
        r.register(NaturalistEntityTypes.TURKEY.get(), Turkey.createAttributes());
        r.register(NaturalistEntityTypes.CAPYBARA.get(), Capybara.createAttributes());
        r.register(NaturalistEntityTypes.HEDGEHOG.get(), Hedgehog.createAttributes());
    }

    public static void registerSpawnPlacements(SpawnPlacementRegistrar registrar) {
        SpawnPlacementRegistrar r = new SpawnPlacementRegistrar() {
            @Override
            public <T extends Mob> void register(EntityType<T> type, SpawnPlacementType placementType, Heightmap.Types heightmap, SpawnPlacements.SpawnPredicate<T> predicate) {
                registrar.register(type, placementType, heightmap, (t, level, spawnType, pos, random) ->
                        !NaturalistConfig.isRemoved(t) && predicate.test(t, level, spawnType, pos, random));
            }
        };

        r.register(NaturalistEntityTypes.SNAIL.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, NaturalistAnimal::checkNaturalistAnimalSpawnRules);
        r.register(NaturalistEntityTypes.BEAR.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, NaturalistAnimal::checkNaturalistAnimalSpawnRules);
        r.register(NaturalistEntityTypes.BUTTERFLY.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Butterfly::checkButterflySpawnRules);
        r.register(NaturalistEntityTypes.FIREFLY.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Firefly::checkFireflySpawnRules);
        r.register(NaturalistEntityTypes.SNAKE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Snake::checkSnakeSpawnRules);
        r.register(NaturalistEntityTypes.CRAB.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Crab::checkCrabSpawnRules);
        r.register(NaturalistEntityTypes.DEER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, NaturalistAnimal::checkNaturalistAnimalSpawnRules);
        r.register(NaturalistEntityTypes.BIRD.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Bird::checkBirdSpawnRules);
        r.register(NaturalistEntityTypes.RHINO.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, NaturalistAnimal::checkNaturalistAnimalSpawnRules);
        r.register(NaturalistEntityTypes.LION.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, NaturalistAnimal::checkNaturalistAnimalSpawnRules);
        r.register(NaturalistEntityTypes.ELEPHANT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, NaturalistAnimal::checkNaturalistAnimalSpawnRules);
        r.register(NaturalistEntityTypes.MAMMOTH.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mammoth::checkMammothSpawnRules);
        r.register(NaturalistEntityTypes.ZEBRA.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, NaturalistAnimal::checkNaturalistAnimalSpawnRules);
        r.register(NaturalistEntityTypes.GIRAFFE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, NaturalistAnimal::checkNaturalistAnimalSpawnRules);
        r.register(NaturalistEntityTypes.HIPPO.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Hippo::checkHippoSpawnRules);
        r.register(NaturalistEntityTypes.VULTURE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Vulture::checkVultureSpawnRules);
        r.register(NaturalistEntityTypes.BOAR.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, NaturalistAnimal::checkNaturalistAnimalSpawnRules);
        r.register(NaturalistEntityTypes.DRAGONFLY.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Dragonfly::checkDragonflySpawnRules);
        r.register(NaturalistEntityTypes.CATFISH.get(), SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
        r.register(NaturalistEntityTypes.ANGLERFISH.get(), SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Anglerfish::checkAnglerfishSpawnRules);
        r.register(NaturalistEntityTypes.RAY.get(), SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
        r.register(NaturalistEntityTypes.BLOBFISH.get(), SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Blobfish::checkBlobfishSpawnRules);
        r.register(NaturalistEntityTypes.PIRANHA.get(), SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Piranha::checkPiranhaSpawnRules);
        r.register(NaturalistEntityTypes.ALLIGATOR.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Alligator::checkAlligatorSpawnRules);
        r.register(NaturalistEntityTypes.BASS.get(), SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
        r.register(NaturalistEntityTypes.LIZARD.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, NaturalistAnimal::checkNaturalistAnimalSpawnRules);
        r.register(NaturalistEntityTypes.TORTOISE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, NaturalistAnimal::checkNaturalistAnimalSpawnRules);
        r.register(NaturalistEntityTypes.DUCK.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Duck::checkDuckSpawnRules);
        r.register(NaturalistEntityTypes.STARFISH.get(), SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Starfish::checkStarfishSpawnRules);
        r.register(NaturalistEntityTypes.CLAM.get(), SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Clam::checkClamSpawnRules);
        r.register(NaturalistEntityTypes.GIANT_ISOPOD.get(), SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, GiantIsopod::checkGiantIsopodSpawnRules);
        r.register(NaturalistEntityTypes.JELLYFISH.get(), SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
        r.register(NaturalistEntityTypes.WHALE.get(), SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Whale::checkWhaleSpawnRules);
        r.register(NaturalistEntityTypes.MOLE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, NaturalistAnimal::checkNaturalistAnimalSpawnRules);
        r.register(NaturalistEntityTypes.RAT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, NaturalistAnimal::checkNaturalistAnimalSpawnRules);
        r.register(NaturalistEntityTypes.BLACK_BEAR.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, NaturalistAnimal::checkNaturalistAnimalSpawnRules);
        r.register(NaturalistEntityTypes.TIGER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, NaturalistAnimal::checkNaturalistAnimalSpawnRules);
        r.register(NaturalistEntityTypes.KOMODO_DRAGON.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, KomodoDragon::checkKomodoDragonSpawnRules);
        r.register(NaturalistEntityTypes.OSTRICH.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, NaturalistAnimal::checkNaturalistAnimalSpawnRules);
        r.register(NaturalistEntityTypes.DESERT_SCORPION.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Scorpion::checkScorpionSpawnRules);
        r.register(NaturalistEntityTypes.JUNGLE_SCORPION.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Scorpion::checkScorpionSpawnRules);
        r.register(NaturalistEntityTypes.GREAT_WHITE_SHARK.get(), SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, GreatWhiteShark::checkGreatWhiteSharkSpawnRules);
        r.register(NaturalistEntityTypes.TURKEY.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, NaturalistAnimal::checkNaturalistAnimalSpawnRules);
        r.register(NaturalistEntityTypes.CAPYBARA.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, NaturalistAnimal::checkNaturalistAnimalSpawnRules);
        r.register(NaturalistEntityTypes.HEDGEHOG.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, NaturalistAnimal::checkNaturalistAnimalSpawnRules);
    }

    public static void registerPotionMixes(BrewingRegistrar r) {
        r.addMix(Potions.AWKWARD, NaturalistRegistry.ANTLER.get(), potion(NaturalistPotions.FOREST_DASHER));
        r.addMix(potion(NaturalistPotions.FOREST_DASHER), Items.REDSTONE, potion(NaturalistPotions.LONG_FOREST_DASHER));
        r.addMix(potion(NaturalistPotions.FOREST_DASHER), Items.GLOWSTONE_DUST, potion(NaturalistPotions.STRONG_FOREST_DASHER));
        r.addMix(Potions.AWKWARD, NaturalistRegistry.SCORPION_POISON_GLAND.get(), potion(NaturalistPotions.ANTIVENOM));
    }

    private static Holder<Potion> potion(Supplier<Potion> potion) {
        return BuiltInRegistries.POTION.wrapAsHolder(potion.get());
    }

    public static void registerDispenserBehaviors() {
        DispenserBlock.registerProjectileBehavior(NaturalistRegistry.DUCK_EGG.get());

        DispenseItemBehavior bucketDispenseBehavior = new DefaultDispenseItemBehavior() {
            private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

            public @NotNull ItemStack execute(@NotNull BlockSource source, ItemStack stack) {
                BlockPos blockPos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
                Level level = source.level();
                if (stack.getItem() instanceof DispensibleContainerItem dispensibleContainerItem) {
                    if (dispensibleContainerItem.emptyContents(null, level, blockPos, null)) {
                        dispensibleContainerItem.checkExtraContent(null, level, stack, blockPos);
                        return new ItemStack(Items.BUCKET);
                    }
                }
                return this.defaultDispenseItemBehavior.dispense(source, stack);
            }
        };
        DispenserBlock.registerBehavior(NaturalistRegistry.BASS_BUCKET.get(), bucketDispenseBehavior);
        DispenserBlock.registerBehavior(NaturalistRegistry.CATFISH_BUCKET.get(), bucketDispenseBehavior);
        DispenserBlock.registerBehavior(NaturalistRegistry.STARFISH_BUCKET.get(), bucketDispenseBehavior);
        DispenserBlock.registerBehavior(NaturalistRegistry.GIANT_ISOPOD_BUCKET.get(), bucketDispenseBehavior);
        DispenserBlock.registerBehavior(NaturalistRegistry.JELLYFISH_BUCKET.get(), bucketDispenseBehavior);
        DispenserBlock.registerBehavior(NaturalistRegistry.ANGLERFISH_BUCKET.get(), bucketDispenseBehavior);
        DispenserBlock.registerBehavior(NaturalistRegistry.RAY_BUCKET.get(), bucketDispenseBehavior);

        DispenserBlock.registerBehavior(NaturalistRegistry.SNAIL.get(), new DefaultDispenseItemBehavior() {
            public @NotNull ItemStack execute(@NotNull BlockSource source, @NotNull ItemStack stack) {
                Direction direction = source.state().getValue(DispenserBlock.FACING);
                BlockPos blockPos = source.pos().relative(direction);
                ServerLevel serverLevel = source.level();

                EntityType<Snail> entityType = NaturalistEntityTypes.SNAIL.get();
                if (entityType.spawn(serverLevel, blockPos, EntitySpawnReason.DISPENSER) != null) {
                    stack.shrink(1);
                }
                return stack;
            }
        });

        DispenserBlock.registerBehavior(NaturalistRegistry.BUTTERFLY.get(), new DefaultDispenseItemBehavior() {
            public @NotNull ItemStack execute(@NotNull BlockSource source, @NotNull ItemStack stack) {
                Direction direction = source.state().getValue(DispenserBlock.FACING);
                BlockPos blockPos = source.pos().relative(direction);
                ServerLevel serverLevel = source.level();

                EntityType<Butterfly> entityType = NaturalistEntityTypes.BUTTERFLY.get();
                if (entityType.spawn(serverLevel, blockPos, EntitySpawnReason.DISPENSER) != null) {
                    stack.shrink(1);
                }
                return stack;
            }
        });

        DispenserBlock.registerBehavior(NaturalistRegistry.CRAB.get(), new DefaultDispenseItemBehavior() {
            public @NotNull ItemStack execute(@NotNull BlockSource source, @NotNull ItemStack stack) {
                Direction direction = source.state().getValue(DispenserBlock.FACING);
                BlockPos blockPos = source.pos().relative(direction);
                Level level = source.level();
                if (stack.getItem() instanceof CaughtMobItem caughtMobItem) {
                    caughtMobItem.checkExtraContent(null, level, stack, blockPos);
                    stack.shrink(1);
                }
                return stack;
            }
        });
    }
}
