package com.crispytwig.naturalist.registry;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.world.level.block.AlligatorEggBlock;
import com.crispytwig.naturalist.world.level.block.AntHillBlock;
import com.crispytwig.naturalist.world.level.block.ChrysalisBlock;
import com.crispytwig.naturalist.world.level.block.GlowGoopBlock;
import com.crispytwig.naturalist.world.level.block.OstrichEggBlock;
import com.crispytwig.naturalist.world.level.block.SnailEggBlock;
import com.crispytwig.naturalist.world.level.block.SnailShellBlock;
import com.crispytwig.naturalist.world.level.block.StarfishBlock;
import com.crispytwig.naturalist.world.level.block.TeddyBearBlock;
import com.crispytwig.naturalist.world.level.block.TortoiseEggBlock;
import com.crispytwig.naturalist.world.entity.animal.fish.Bass;
import com.crispytwig.naturalist.world.entity.animal.butterfly.Butterfly;
import com.crispytwig.naturalist.world.entity.animal.crab.Crab;
import com.crispytwig.naturalist.world.entity.animal.hedgehog.Hedgehog;
import com.crispytwig.naturalist.world.entity.animal.rat.Rat;
import com.crispytwig.naturalist.world.item.HedgehogItem;
import com.crispytwig.naturalist.world.item.BugNetItem;
import com.crispytwig.naturalist.world.item.QueenAntItem;
import com.crispytwig.naturalist.world.item.DuckEggItem;
import com.crispytwig.naturalist.world.item.KnapsackItem;
import com.crispytwig.naturalist.world.item.WhistleItem;
import com.crispytwig.naturalist.world.item.GlowGoopItem;
import com.crispytwig.naturalist.world.item.CaughtMobItem;
import com.crispytwig.naturalist.world.item.CaughtMobWithVariantsItem;
import com.crispytwig.naturalist.world.item.NaturalistBucketItem;
import com.crispytwig.naturalist.world.item.SnailItem;
import com.crispytwig.naturalist.world.entity.animal.starfish.Starfish;
import com.crispytwig.naturalist.world.entity.animal.giantisopod.GiantIsopod;
import com.crispytwig.naturalist.world.entity.animal.jellyfish.Jellyfish;
import com.crispytwig.naturalist.world.entity.animal.fish.Anglerfish;
import com.crispytwig.naturalist.world.entity.animal.fish.Ray;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import com.crispytwig.naturalist.platform.registry.DeferredHolder;
import com.crispytwig.naturalist.platform.registry.DeferredRegister;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class NaturalistRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, Naturalist.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, Naturalist.MOD_ID);

    public static final DeferredHolder<Item, Item> BUSHMEAT = registerItem("bushmeat", new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3F).build()));
    public static final DeferredHolder<Item, Item> COOKED_BUSHMEAT = registerItem("cooked_bushmeat", new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationModifier(0.8F).build()));
    public static final DeferredHolder<Item, Item> FUR = registerItem("fur", new Item.Properties());
    public static final DeferredHolder<Item, Item> TOOTH = registerItem("tooth", new Item.Properties());
    public static final DeferredHolder<Item, Item> FAT = registerItem("fat", new Item.Properties());
    public static final DeferredHolder<Item, Item> HIDE = registerItem("hide", new Item.Properties());
    public static final DeferredHolder<Item, Item> MORSEL = registerItem("morsel", new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3F).build()));

    public static final DeferredHolder<Block, AlligatorEggBlock> ALLIGATOR_EGG = registerBlock("alligator_egg", AlligatorEggBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.TURTLE_EGG));
    public static final DeferredHolder<Item, DuckEggItem> DUCK_EGG = registerItem("duck_egg", DuckEggItem::new, new Item.Properties());
    public static final DeferredHolder<Block, TortoiseEggBlock> TORTOISE_EGG = registerBlock("tortoise_egg", TortoiseEggBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.TURTLE_EGG));
    public static final DeferredHolder<Block, OstrichEggBlock> OSTRICH_EGG = registerBlock("ostrich_egg", OstrichEggBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.TURTLE_EGG));
    public static final DeferredHolder<Item, Item> COOKED_EGG = registerItem("cooked_egg", new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.6F).build()));
    public static final DeferredHolder<Block, SnailEggBlock> SNAIL_EGGS = registerBlock("snail_eggs", SnailEggBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.FROGSPAWN));

    public static final DeferredHolder<Item, Item> ANTLER = registerItem("antler", new Item.Properties());
    public static final DeferredHolder<Block, GlowGoopBlock> GLOW_GOOP_BLOCK = registerBlockOnly("glow_goop", GlowGoopBlock::new, () -> BlockBehaviour.Properties.of().strength(0.5F).replaceable().noOcclusion().noCollision().lightLevel(GlowGoopBlock.LIGHT_EMISSION).sound(SoundType.HONEY_BLOCK));
    public static final DeferredHolder<Item, GlowGoopItem> GLOW_GOOP = registerItem("glow_goop", props -> new GlowGoopItem(GLOW_GOOP_BLOCK.get(), props), new Item.Properties().useItemDescriptionPrefix());
    public static final DeferredHolder<Block, TeddyBearBlock> PLUSH_BEAR = registerBlock("plush_bear", TeddyBearBlock::new, () -> BlockBehaviour.Properties.of().strength(0.8f).sound(SoundType.WOOL).noOcclusion());
    public static final DeferredHolder<Item, Item> DUCK = registerItem("duck", new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3F).build()));
    public static final DeferredHolder<Item, Item> COOKED_DUCK = registerItem("cooked_duck", new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.6F).build()));
    public static final DeferredHolder<Item, Item> VENISON = registerItem("venison", new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3F).build()));
    public static final DeferredHolder<Item, Item> COOKED_VENISON = registerItem("cooked_venison", new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.8F).build()));
    public static final DeferredHolder<Item, Item> DRUMSTICK = registerItem("drumstick", new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3F).build()));
    public static final DeferredHolder<Item, Item> COOKED_DRUMSTICK = registerItem("cooked_drumstick", new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.6F).build()));
    public static final DeferredHolder<Item, Item> MAMMOTH_MEAT = registerItem("mammoth_meat", new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3F).build()));
    public static final DeferredHolder<Item, Item> COOKED_MAMMOTH_MEAT = registerItem("cooked_mammoth_meat", new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationModifier(0.8F).build()));
    public static final DeferredHolder<Item, Item> LIZARD_TAIL = registerItem("lizard_tail", new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8F).build(),
            Consumables.defaultFood().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.POISON, 100, 0), 1.0F)).build()));
    public static final DeferredHolder<Item, Item> COOKED_LIZARD_TAIL = registerItem("cooked_lizard_tail", new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.6F).build()));
    public static final DeferredHolder<Item, NaturalistBucketItem> CATFISH_BUCKET = registerItem("catfish_bucket", props -> new NaturalistBucketItem(NaturalistEntityTypes.CATFISH.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, props), new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, NaturalistBucketItem> BASS_BUCKET = registerItem("bass_bucket", props -> new NaturalistBucketItem(NaturalistEntityTypes.BASS.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, props, true, null, Bass.VARIANT_NAMES), new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, NaturalistBucketItem> DUCK_BUCKET = registerItem("duck_bucket", props -> new NaturalistBucketItem(NaturalistEntityTypes.DUCK.get(), Fluids.EMPTY, SoundEvents.BUCKET_EMPTY, props), new Item.Properties().stacksTo(1).component(DataComponents.BUCKET_ENTITY_DATA, ducklingBucketData()));
    public static final DeferredHolder<Item, Item> CATFISH = registerItem("catfish", new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.1F).build()));
    public static final DeferredHolder<Item, Item> COOKED_CATFISH = registerItem("cooked_catfish", new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.8F).build()));
    public static final DeferredHolder<Item, Item> BASS = registerItem("bass", new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.1F).build()));
    public static final DeferredHolder<Item, Item> COOKED_BASS = registerItem("cooked_bass", new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.6F).build()));
    public static final DeferredHolder<Item, Item> ANGLERFISH = registerItem("anglerfish", new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.1F).build()));
    public static final DeferredHolder<Item, Item> COOKED_ANGLERFISH = registerItem("cooked_anglerfish", new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.6F).build()));
    public static final DeferredHolder<Item, Item> BLOBFISH = registerItem("blobfish", new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.1F).build()));
    public static final DeferredHolder<Item, Item> COOKED_BLOBFISH = registerItem("cooked_blobfish", new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.6F).build()));
    public static final DeferredHolder<Item, Item> PIRANHA = registerItem("piranha", new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.1F).build()));
    public static final DeferredHolder<Item, Item> COOKED_PIRANHA = registerItem("cooked_piranha", new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.6F).build()));
    public static final DeferredHolder<Item, Item> CLAM_MEAT = registerItem("clam_meat", new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.1F).build()));
    public static final DeferredHolder<Item, Item> COOKED_CLAM_MEAT = registerItem("cooked_clam_meat", new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.6F).build()));
    public static final DeferredHolder<Item, Item> CRAB_MEAT = registerItem("crab_meat", new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.1F).build()));
    public static final DeferredHolder<Item, Item> COOKED_CRAB_MEAT = registerItem("cooked_crab_meat", new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.6F).build()));
    public static final DeferredHolder<Item, CaughtMobWithVariantsItem> CRAB = registerItem("crab", props -> new CaughtMobWithVariantsItem(NaturalistEntityTypes.CRAB, () -> Fluids.EMPTY, NaturalistSoundEvents.CRAB_AMBIENT, "tooltip.naturalist.crab_", Crab.VARIANT_NAMES, props), new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, BugNetItem> CAPTURE_NET = registerItem("capture_net", BugNetItem::new, new Item.Properties().durability(64));
    public static final DeferredHolder<Item, KnapsackItem> KNAPSACK = registerItem("knapsack", KnapsackItem::new, new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, WhistleItem> WHISTLE = registerItem("whistle", WhistleItem::new, new Item.Properties().stacksTo(1));
    public static final ResourceKey<JukeboxSong> WILD_ONES_SONG = ResourceKey.create(Registries.JUKEBOX_SONG, Naturalist.location("wild_ones"));
    public static final DeferredHolder<Item, Item> MUSIC_DISC_WILD_ONES = registerItem("music_disc_wild_ones", new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(WILD_ONES_SONG));
    public static final ResourceKey<JukeboxSong> DEATH_BY_HOGS_SONG = ResourceKey.create(Registries.JUKEBOX_SONG, Naturalist.location("death_by_hogs"));
    public static final DeferredHolder<Item, Item> MUSIC_DISC_DEATH_BY_HOGS = registerItem("music_disc_death_by_hogs", new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(DEATH_BY_HOGS_SONG));
    public static final DeferredHolder<Block, ChrysalisBlock> CHRYSALIS_BLOCK = registerBlockOnly("chrysalis", ChrysalisBlock::new, () -> BlockBehaviour.Properties.of().randomTicks().strength(0.2F, 3.0F).sound(SoundType.GRASS).noOcclusion().noCollision().pushReaction(PushReaction.POPPED));
    public static final DeferredHolder<Item, BlockItem> CHRYSALIS = registerItem("chrysalis", props -> new BlockItem(CHRYSALIS_BLOCK.get(), props), new Item.Properties().stacksTo(1).useBlockDescriptionPrefix());
    public static final DeferredHolder<Item, CaughtMobItem> CATERPILLAR = registerItem("caterpillar", props -> new CaughtMobItem(NaturalistEntityTypes.CATERPILLAR, () -> Fluids.EMPTY, NaturalistSoundEvents.SNAIL_FORWARD, props), new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, CaughtMobWithVariantsItem> BUTTERFLY = registerItem("butterfly", props -> new CaughtMobWithVariantsItem(NaturalistEntityTypes.BUTTERFLY, () -> Fluids.EMPTY, NaturalistSoundEvents.BIRD_FLY, "tooltip.naturalist.", Butterfly.VARIANT_NAMES, props), new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, CaughtMobItem> ANT = registerItem("ant", props -> new CaughtMobItem(NaturalistEntityTypes.ANT, () -> Fluids.EMPTY, NaturalistSoundEvents.ANT_AMBIENT, props), new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, CaughtMobWithVariantsItem> RAT = registerItem("rat", props -> new CaughtMobWithVariantsItem(NaturalistEntityTypes.RAT, () -> Fluids.EMPTY, NaturalistSoundEvents.RAT_AMBIENT, "tooltip.naturalist.rat_", Rat.VARIANT_NAMES, props), new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, CaughtMobItem> SCORPION = registerItem("scorpion", props -> new CaughtMobItem(NaturalistEntityTypes.DESERT_SCORPION, () -> Fluids.EMPTY, NaturalistSoundEvents.SCORPION_AMBIENT, props), new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, HedgehogItem> HEDGEHOG = registerItem("hedgehog", props -> new HedgehogItem(NaturalistEntityTypes.HEDGEHOG, () -> Fluids.EMPTY, NaturalistSoundEvents.HEDGEHOG_AMBIENT, "tooltip.naturalist.hedgehog_", Hedgehog.VARIANT_NAMES, props), new Item.Properties().stacksTo(1).enchantable(1));
    public static final DeferredHolder<Item, Item> SCORPION_POISON_GLAND = registerItem("scorpion_poison_gland", new Item.Properties());
    public static final DeferredHolder<Item, QueenAntItem> QUEEN_ANT = registerItem("queen_ant", QueenAntItem::new, new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Block, AntHillBlock> ANT_HILL = registerBlock("ant_hill", AntHillBlock::new, () -> BlockBehaviour.Properties.of().strength(0.5F, 0.2F).sound(SoundType.ROOTED_DIRT).randomTicks());
    public static final DeferredHolder<Block, SnailShellBlock> SNAIL_SHELL_BLOCK = registerBlockOnly("snail_shell", SnailShellBlock::new, () -> BlockBehaviour.Properties.of().strength(0.5F).sound(SoundType.CORAL_BLOCK).noOcclusion().pushReaction(PushReaction.POPPED));
    public static final DeferredHolder<Item, BlockItem> SNAIL_SHELL = registerItem("snail_shell", props -> new BlockItem(SNAIL_SHELL_BLOCK.get(), props), new Item.Properties().useBlockDescriptionPrefix().component(DataComponents.CUSTOM_DATA, SnailShellBlock.colorData(DyeColor.BROWN)));
    public static final DeferredHolder<Item, SnailItem> SNAIL = registerItem("snail", props -> new SnailItem(NaturalistEntityTypes.SNAIL, () -> Fluids.EMPTY, NaturalistSoundEvents.SNAIL_FORWARD, props), new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, NaturalistBucketItem> STARFISH_BUCKET = registerItem("starfish_bucket", props -> new NaturalistBucketItem(NaturalistEntityTypes.STARFISH.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, props, false, "color.minecraft.", Starfish.VARIANT_NAMES), new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, NaturalistBucketItem> GIANT_ISOPOD_BUCKET = registerItem("giant_isopod_bucket", props -> new NaturalistBucketItem(NaturalistEntityTypes.GIANT_ISOPOD.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, props, false, "tooltip.naturalist.giant_isopod_", GiantIsopod.VARIANT_NAMES), new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, NaturalistBucketItem> ANGLERFISH_BUCKET = registerItem("anglerfish_bucket", props -> new NaturalistBucketItem(NaturalistEntityTypes.ANGLERFISH.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, props, true, "tooltip.naturalist.anglerfish_", Anglerfish.VARIANT_NAMES), new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, NaturalistBucketItem> JELLYFISH_BUCKET = registerItem("jellyfish_bucket", props -> new NaturalistBucketItem(NaturalistEntityTypes.JELLYFISH.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, props, true, "color.minecraft.", Jellyfish.VARIANT_NAMES), new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, NaturalistBucketItem> RAY_BUCKET = registerItem("ray_bucket", props -> new NaturalistBucketItem(NaturalistEntityTypes.RAY.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, props, true, "tooltip.naturalist.ray_", Ray.VARIANT_NAMES), new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, NaturalistBucketItem> BLOBFISH_BUCKET = registerItem("blobfish_bucket", props -> new NaturalistBucketItem(NaturalistEntityTypes.BLOBFISH.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, props), new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, NaturalistBucketItem> PIRANHA_BUCKET = registerItem("piranha_bucket", props -> new NaturalistBucketItem(NaturalistEntityTypes.PIRANHA.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, props), new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Block, StarfishBlock> RED_STARFISH = registerStarfishBlock("red_starfish");
    public static final DeferredHolder<Block, StarfishBlock> ORANGE_STARFISH = registerStarfishBlock("orange_starfish");
    public static final DeferredHolder<Block, StarfishBlock> BLUE_STARFISH = registerStarfishBlock("blue_starfish");
    public static final DeferredHolder<Block, StarfishBlock> PURPLE_STARFISH = registerStarfishBlock("purple_starfish");

    public static final DeferredHolder<Block, TransparentBlock> AZURE_FROGLASS = registerBlock("azure_froglass", TransparentBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS));
    public static final DeferredHolder<Block, TransparentBlock> VERDANT_FROGLASS = registerBlock("verdant_froglass", TransparentBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS));
    public static final DeferredHolder<Block, TransparentBlock> CRIMSON_FROGLASS = registerBlock("crimson_froglass", TransparentBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS));
    public static final DeferredHolder<Block, IronBarsBlock> AZURE_FROGLASS_PANE = registerBlock("azure_froglass_pane", IronBarsBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS_PANE));
    public static final DeferredHolder<Block, IronBarsBlock> VERDANT_FROGLASS_PANE = registerBlock("verdant_froglass_pane", IronBarsBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS_PANE));
    public static final DeferredHolder<Block, IronBarsBlock> CRIMSON_FROGLASS_PANE = registerBlock("crimson_froglass_pane", IronBarsBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS_PANE));
    public static final DeferredHolder<Block, Block> SHELLSTONE = registerBlock("shellstone", Block::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE));
    public static final DeferredHolder<Block, StairBlock> SHELLSTONE_STAIRS = registerBlock("shellstone_stairs", props -> new StairBlock(SHELLSTONE.get().defaultBlockState(), props), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE));
    public static final DeferredHolder<Block, SlabBlock> SHELLSTONE_SLAB = registerBlock("shellstone_slab", SlabBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE));
    public static final DeferredHolder<Block, WallBlock> SHELLSTONE_WALL = registerBlock("shellstone_wall", WallBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE));
    public static final DeferredHolder<Block, Block> SHELLSTONE_BRICKS = registerBlock("shellstone_bricks", Block::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE));
    public static final DeferredHolder<Block, StairBlock> SHELLSTONE_BRICK_STAIRS = registerBlock("shellstone_brick_stairs", props -> new StairBlock(SHELLSTONE_BRICKS.get().defaultBlockState(), props), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE));
    public static final DeferredHolder<Block, SlabBlock> SHELLSTONE_BRICK_SLAB = registerBlock("shellstone_brick_slab", SlabBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE));
    public static final DeferredHolder<Block, WallBlock> SHELLSTONE_BRICK_WALL = registerBlock("shellstone_brick_wall", WallBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE));
    public static final DeferredHolder<Block, Block> CUT_SHELLSTONE = registerBlock("cut_shellstone", Block::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE));
    public static final DeferredHolder<Block, StairBlock> CUT_SHELLSTONE_STAIRS = registerBlock("cut_shellstone_stairs", props -> new StairBlock(CUT_SHELLSTONE.get().defaultBlockState(), props), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE));
    public static final DeferredHolder<Block, SlabBlock> CUT_SHELLSTONE_SLAB = registerBlock("cut_shellstone_slab", SlabBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE));
    public static final DeferredHolder<Block, WallBlock> CUT_SHELLSTONE_WALL = registerBlock("cut_shellstone_wall", WallBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE));
    public static final DeferredHolder<Block, Block> SMOOTH_SHELLSTONE = registerBlock("smooth_shellstone", Block::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE));
    public static final DeferredHolder<Block, StairBlock> SMOOTH_SHELLSTONE_STAIRS = registerBlock("smooth_shellstone_stairs", props -> new StairBlock(SMOOTH_SHELLSTONE.get().defaultBlockState(), props), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE));
    public static final DeferredHolder<Block, SlabBlock> SMOOTH_SHELLSTONE_SLAB = registerBlock("smooth_shellstone_slab", SlabBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE));
    public static final DeferredHolder<Block, WallBlock> SMOOTH_SHELLSTONE_WALL = registerBlock("smooth_shellstone_wall", WallBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE));

    public static final DeferredHolder<Item, SpawnEggItem> ALLIGATOR_SPAWN_EGG = registerSpawnEgg("alligator_spawn_egg", NaturalistEntityTypes.ALLIGATOR);
    public static final DeferredHolder<Item, SpawnEggItem> ANGLERFISH_SPAWN_EGG = registerSpawnEgg("anglerfish_spawn_egg", NaturalistEntityTypes.ANGLERFISH);
    public static final DeferredHolder<Item, SpawnEggItem> RAY_SPAWN_EGG = registerSpawnEgg("ray_spawn_egg", NaturalistEntityTypes.RAY);
    public static final DeferredHolder<Item, SpawnEggItem> BLOBFISH_SPAWN_EGG = registerSpawnEgg("blobfish_spawn_egg", NaturalistEntityTypes.BLOBFISH);
    public static final DeferredHolder<Item, SpawnEggItem> PIRANHA_SPAWN_EGG = registerSpawnEgg("piranha_spawn_egg", NaturalistEntityTypes.PIRANHA);
    public static final DeferredHolder<Item, SpawnEggItem> BASS_SPAWN_EGG = registerSpawnEgg("bass_spawn_egg", NaturalistEntityTypes.BASS);
    public static final DeferredHolder<Item, SpawnEggItem> BEAR_SPAWN_EGG = registerSpawnEgg("bear_spawn_egg", NaturalistEntityTypes.BEAR);
    public static final DeferredHolder<Item, SpawnEggItem> BIRD_SPAWN_EGG = registerSpawnEgg("bird_spawn_egg", NaturalistEntityTypes.BIRD);
    public static final DeferredHolder<Item, SpawnEggItem> BOAR_SPAWN_EGG = registerSpawnEgg("boar_spawn_egg", NaturalistEntityTypes.BOAR);
    public static final DeferredHolder<Item, SpawnEggItem> BUTTERFLY_SPAWN_EGG = registerSpawnEgg("butterfly_spawn_egg", NaturalistEntityTypes.BUTTERFLY);
    public static final DeferredHolder<Item, SpawnEggItem> CATFISH_SPAWN_EGG = registerSpawnEgg("catfish_spawn_egg", NaturalistEntityTypes.CATFISH);
    public static final DeferredHolder<Item, SpawnEggItem> CATERPILLAR_SPAWN_EGG = registerSpawnEgg("caterpillar_spawn_egg", NaturalistEntityTypes.CATERPILLAR);
    public static final DeferredHolder<Item, SpawnEggItem> CLAM_SPAWN_EGG = registerSpawnEgg("clam_spawn_egg", NaturalistEntityTypes.CLAM);
    public static final DeferredHolder<Item, SpawnEggItem> CRAB_SPAWN_EGG = registerSpawnEgg("crab_spawn_egg", NaturalistEntityTypes.CRAB);
    public static final DeferredHolder<Item, SpawnEggItem> DEER_SPAWN_EGG = registerSpawnEgg("deer_spawn_egg", NaturalistEntityTypes.DEER);
    public static final DeferredHolder<Item, SpawnEggItem> DRAGONFLY_SPAWN_EGG = registerSpawnEgg("dragonfly_spawn_egg", NaturalistEntityTypes.DRAGONFLY);
    public static final DeferredHolder<Item, SpawnEggItem> DUCK_SPAWN_EGG = registerSpawnEgg("duck_spawn_egg", NaturalistEntityTypes.DUCK);
    public static final DeferredHolder<Item, SpawnEggItem> CAPYBARA_SPAWN_EGG = registerSpawnEgg("capybara_spawn_egg", NaturalistEntityTypes.CAPYBARA);
    public static final DeferredHolder<Item, SpawnEggItem> HEDGEHOG_SPAWN_EGG = registerSpawnEgg("hedgehog_spawn_egg", NaturalistEntityTypes.HEDGEHOG);
    public static final DeferredHolder<Item, SpawnEggItem> ELEPHANT_SPAWN_EGG = registerSpawnEgg("elephant_spawn_egg", NaturalistEntityTypes.ELEPHANT);
    public static final DeferredHolder<Item, SpawnEggItem> MAMMOTH_SPAWN_EGG = registerSpawnEgg("mammoth_spawn_egg", NaturalistEntityTypes.MAMMOTH);
    public static final DeferredHolder<Item, SpawnEggItem> FIREFLY_SPAWN_EGG = registerSpawnEgg("firefly_spawn_egg", NaturalistEntityTypes.FIREFLY);
    public static final DeferredHolder<Item, SpawnEggItem> GIANT_ISOPOD_SPAWN_EGG = registerSpawnEgg("giant_isopod_spawn_egg", NaturalistEntityTypes.GIANT_ISOPOD);
    public static final DeferredHolder<Item, SpawnEggItem> JELLYFISH_SPAWN_EGG = registerSpawnEgg("jellyfish_spawn_egg", NaturalistEntityTypes.JELLYFISH);
    public static final DeferredHolder<Item, SpawnEggItem> GIRAFFE_SPAWN_EGG = registerSpawnEgg("giraffe_spawn_egg", NaturalistEntityTypes.GIRAFFE);
    public static final DeferredHolder<Item, SpawnEggItem> HIPPO_SPAWN_EGG = registerSpawnEgg("hippo_spawn_egg", NaturalistEntityTypes.HIPPO);
    public static final DeferredHolder<Item, SpawnEggItem> LION_SPAWN_EGG = registerSpawnEgg("lion_spawn_egg", NaturalistEntityTypes.LION);
    public static final DeferredHolder<Item, SpawnEggItem> LIZARD_SPAWN_EGG = registerSpawnEgg("lizard_spawn_egg", NaturalistEntityTypes.LIZARD);
    public static final DeferredHolder<Item, SpawnEggItem> RHINO_SPAWN_EGG = registerSpawnEgg("rhino_spawn_egg", NaturalistEntityTypes.RHINO);
    public static final DeferredHolder<Item, SpawnEggItem> SNAKE_SPAWN_EGG = registerSpawnEgg("snake_spawn_egg", NaturalistEntityTypes.SNAKE);
    public static final DeferredHolder<Item, SpawnEggItem> SNAIL_SPAWN_EGG = registerSpawnEgg("snail_spawn_egg", NaturalistEntityTypes.SNAIL);
    public static final DeferredHolder<Item, SpawnEggItem> STARFISH_SPAWN_EGG = registerSpawnEgg("starfish_spawn_egg", NaturalistEntityTypes.STARFISH);
    public static final DeferredHolder<Item, SpawnEggItem> TORTOISE_SPAWN_EGG = registerSpawnEgg("tortoise_spawn_egg", NaturalistEntityTypes.TORTOISE);
    public static final DeferredHolder<Item, SpawnEggItem> VULTURE_SPAWN_EGG = registerSpawnEgg("vulture_spawn_egg", NaturalistEntityTypes.VULTURE);
    public static final DeferredHolder<Item, SpawnEggItem> ZEBRA_SPAWN_EGG = registerSpawnEgg("zebra_spawn_egg", NaturalistEntityTypes.ZEBRA);
    public static final DeferredHolder<Item, SpawnEggItem> WHALE_SPAWN_EGG = registerSpawnEgg("whale_spawn_egg", NaturalistEntityTypes.WHALE);
    public static final DeferredHolder<Item, SpawnEggItem> ANT_SPAWN_EGG = registerSpawnEgg("ant_spawn_egg", NaturalistEntityTypes.ANT);
    public static final DeferredHolder<Item, SpawnEggItem> MOLE_SPAWN_EGG = registerSpawnEgg("mole_spawn_egg", NaturalistEntityTypes.MOLE);
    public static final DeferredHolder<Item, SpawnEggItem> RAT_SPAWN_EGG = registerSpawnEgg("rat_spawn_egg", NaturalistEntityTypes.RAT);
    public static final DeferredHolder<Item, SpawnEggItem> BLACK_BEAR_SPAWN_EGG = registerSpawnEgg("black_bear_spawn_egg", NaturalistEntityTypes.BLACK_BEAR);
    public static final DeferredHolder<Item, SpawnEggItem> TIGER_SPAWN_EGG = registerSpawnEgg("tiger_spawn_egg", NaturalistEntityTypes.TIGER);
    public static final DeferredHolder<Item, SpawnEggItem> KOMODO_DRAGON_SPAWN_EGG = registerSpawnEgg("komodo_dragon_spawn_egg", NaturalistEntityTypes.KOMODO_DRAGON);
    public static final DeferredHolder<Item, SpawnEggItem> OSTRICH_SPAWN_EGG = registerSpawnEgg("ostrich_spawn_egg", NaturalistEntityTypes.OSTRICH);
    public static final DeferredHolder<Item, SpawnEggItem> DESERT_SCORPION_SPAWN_EGG = registerSpawnEgg("desert_scorpion_spawn_egg", NaturalistEntityTypes.DESERT_SCORPION);
    public static final DeferredHolder<Item, SpawnEggItem> JUNGLE_SCORPION_SPAWN_EGG = registerSpawnEgg("jungle_scorpion_spawn_egg", NaturalistEntityTypes.JUNGLE_SCORPION);
    public static final DeferredHolder<Item, SpawnEggItem> GREAT_WHITE_SHARK_SPAWN_EGG = registerSpawnEgg("great_white_shark_spawn_egg", NaturalistEntityTypes.GREAT_WHITE_SHARK);
    public static final DeferredHolder<Item, SpawnEggItem> TURKEY_SPAWN_EGG = registerSpawnEgg("turkey_spawn_egg", NaturalistEntityTypes.TURKEY);

    public static void init() {
    }

    private static CustomData ducklingBucketData() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("Age", -24000);
        return CustomData.of(tag);
    }

    private static ResourceKey<Item> itemKey(String name) {
        return ResourceKey.create(Registries.ITEM, Naturalist.location(name));
    }

    private static ResourceKey<Block> blockKey(String name) {
        return ResourceKey.create(Registries.BLOCK, Naturalist.location(name));
    }

    private static DeferredHolder<Item, Item> registerItem(String name, Item.Properties properties) {
        return registerItem(name, Item::new, properties);
    }

    private static <T extends Item> DeferredHolder<Item, T> registerItem(String name, Function<Item.Properties, T> factory, Item.Properties properties) {
        return ITEMS.register(name, () -> factory.apply(properties.setId(itemKey(name))));
    }

    private static <T extends Mob> DeferredHolder<Item, SpawnEggItem> registerSpawnEgg(String name, Supplier<EntityType<T>> type) {
        return ITEMS.register(name, () -> new SpawnEggItem(new Item.Properties().setId(itemKey(name)).spawnEgg(type.get())));
    }

    private static <T extends Block> DeferredHolder<Block, T> registerBlock(String name, Function<BlockBehaviour.Properties, T> factory, Supplier<BlockBehaviour.Properties> properties) {
        DeferredHolder<Block, T> holder = registerBlockOnly(name, factory, properties);
        registerItem(name, props -> new BlockItem(holder.get(), props), new Item.Properties().useBlockDescriptionPrefix());
        return holder;
    }

    private static <T extends Block> DeferredHolder<Block, T> registerBlockOnly(String name, Function<BlockBehaviour.Properties, T> factory, Supplier<BlockBehaviour.Properties> properties) {
        return BLOCKS.register(name, () -> factory.apply(properties.get().setId(blockKey(name))));
    }

    private static DeferredHolder<Block, StarfishBlock> registerStarfishBlock(String name) {
        return registerBlock(name, StarfishBlock::new, () -> BlockBehaviour.Properties.of().noCollision().instabreak().sound(SoundType.WET_GRASS).noOcclusion().pushReaction(PushReaction.POPPED));
    }
}
