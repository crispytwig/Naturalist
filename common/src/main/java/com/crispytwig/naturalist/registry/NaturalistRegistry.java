package com.crispytwig.naturalist.registry;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.server.block.*;
import com.crispytwig.naturalist.server.entity.mob.Bass;
import com.crispytwig.naturalist.server.entity.mob.Butterfly;
import com.crispytwig.naturalist.server.entity.mob.Crab;
import com.crispytwig.naturalist.server.entity.mob.Hedgehog;
import com.crispytwig.naturalist.server.entity.mob.Rat;
import com.crispytwig.naturalist.server.item.HedgehogItem;
import com.crispytwig.naturalist.server.item.BugNetItem;
import com.crispytwig.naturalist.server.item.QueenAntItem;
import com.crispytwig.naturalist.server.item.DuckEggItem;
import com.crispytwig.naturalist.server.item.KnapsackItem;
import com.crispytwig.naturalist.server.item.WhistleItem;
import com.crispytwig.naturalist.server.item.GlowGoopItem;
import com.crispytwig.naturalist.server.item.CaughtEntityItem;
import com.crispytwig.naturalist.server.item.NaturalistBucketItem;
import com.crispytwig.naturalist.server.item.SnailItem;
import com.crispytwig.naturalist.server.entity.mob.Starfish;
import com.crispytwig.naturalist.server.entity.mob.GiantIsopod;
import com.crispytwig.naturalist.server.entity.mob.Jellyfish;
import com.crispytwig.naturalist.server.entity.mob.Anglerfish;
import com.crispytwig.naturalist.server.entity.mob.Ray;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import com.crispytwig.naturalist.platform.Services;
import com.crispytwig.naturalist.platform.registry.DeferredHolder;
import com.crispytwig.naturalist.platform.registry.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class NaturalistRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, Naturalist.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, Naturalist.MOD_ID);
    public static final List<DeferredHolder<Item, CaughtEntityItem>> CAUGHT_ENTITY_ITEMS = new ArrayList<>();

    public static final DeferredHolder<Item, Item> BUSHMEAT = ITEMS.register("bushmeat", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3F).build())));
    public static final DeferredHolder<Item, Item> COOKED_BUSHMEAT = ITEMS.register("cooked_bushmeat", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationModifier(0.8F).build())));
    public static final DeferredHolder<Item, Item> FUR = ITEMS.register("fur", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> TOOTH = ITEMS.register("tooth", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> FAT = ITEMS.register("fat", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> HIDE = ITEMS.register("hide", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> MORSEL = ITEMS.register("morsel", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3F).build())));

    public static final DeferredHolder<Block, AlligatorEggBlock> ALLIGATOR_EGG = registerBlock("alligator_egg", () -> new AlligatorEggBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TURTLE_EGG)));
    public static final DeferredHolder<Item, DuckEggItem> DUCK_EGG = ITEMS.register("duck_egg", () -> new DuckEggItem(new Item.Properties()));
    public static final DeferredHolder<Block, TortoiseEggBlock> TORTOISE_EGG = registerBlock("tortoise_egg", () -> new TortoiseEggBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TURTLE_EGG)));
    public static final DeferredHolder<Block, OstrichEggBlock> OSTRICH_EGG = registerBlock("ostrich_egg", () -> new OstrichEggBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TURTLE_EGG)));
    public static final DeferredHolder<Item, Item> COOKED_EGG = ITEMS.register("cooked_egg", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.6F).build())));
    public static final DeferredHolder<Block, SnailEggBlock> SNAIL_EGGS = registerBlock("snail_eggs", () -> new SnailEggBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FROGSPAWN)));

    public static final DeferredHolder<Item, Item> ANTLER = ITEMS.register("antler", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Block, GlowGoopBlock> GLOW_GOOP_BLOCK = registerBlockOnly("glow_goop", () -> new GlowGoopBlock(BlockBehaviour.Properties.of().strength(0.5F).replaceable().noOcclusion().noCollission().lightLevel(GlowGoopBlock.LIGHT_EMISSION).sound(SoundType.HONEY_BLOCK)));
    public static final DeferredHolder<Item, GlowGoopItem> GLOW_GOOP = ITEMS.register("glow_goop", () -> new GlowGoopItem(GLOW_GOOP_BLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<Block, TeddyBearBlock> PLUSH_BEAR = registerBlock("plush_bear", () -> new TeddyBearBlock(BlockBehaviour.Properties.of().strength(0.8f).sound(SoundType.WOOL).noOcclusion()));
    public static final DeferredHolder<Item, Item> DUCK = ITEMS.register("duck", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3F).build())));
    public static final DeferredHolder<Item, Item> COOKED_DUCK = ITEMS.register("cooked_duck", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.6F).build())));
    public static final DeferredHolder<Item, Item> VENISON = ITEMS.register("venison", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3F).build())));
    public static final DeferredHolder<Item, Item> COOKED_VENISON = ITEMS.register("cooked_venison", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.8F).build())));
    public static final DeferredHolder<Item, Item> DRUMSTICK = ITEMS.register("drumstick", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3F).build())));
    public static final DeferredHolder<Item, Item> COOKED_DRUMSTICK = ITEMS.register("cooked_drumstick", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.6F).build())));
    public static final DeferredHolder<Item, Item> MAMMOTH_MEAT = ITEMS.register("mammoth_meat", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3F).build())));
    public static final DeferredHolder<Item, Item> COOKED_MAMMOTH_MEAT = ITEMS.register("cooked_mammoth_meat", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationModifier(0.8F).build())));
    public static final DeferredHolder<Item, Item> LIZARD_TAIL = ITEMS.register("lizard_tail", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8F).effect(new MobEffectInstance(MobEffects.POISON, 100, 0), 1.0F).build())));
    public static final DeferredHolder<Item, Item> COOKED_LIZARD_TAIL = ITEMS.register("cooked_lizard_tail", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.6F).build())));
    public static final DeferredHolder<Item, NaturalistBucketItem> CATFISH_BUCKET = ITEMS.register("catfish_bucket", () -> new NaturalistBucketItem(NaturalistEntityTypes.CATFISH.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, NaturalistBucketItem> BASS_BUCKET = ITEMS.register("bass_bucket", () -> new NaturalistBucketItem(NaturalistEntityTypes.BASS.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1), true, null, Bass.VARIANT_NAMES));
    public static final DeferredHolder<Item, NaturalistBucketItem> DUCK_BUCKET = ITEMS.register("duck_bucket", () -> new NaturalistBucketItem(NaturalistEntityTypes.DUCK.get(), Fluids.EMPTY, SoundEvents.BUCKET_EMPTY, new Item.Properties().stacksTo(1).component(DataComponents.BUCKET_ENTITY_DATA, ducklingBucketData())));
    public static final DeferredHolder<Item, Item> CATFISH = ITEMS.register("catfish", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.1F).build())));
    public static final DeferredHolder<Item, Item> COOKED_CATFISH = ITEMS.register("cooked_catfish", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.8F).build())));
    public static final DeferredHolder<Item, Item> BASS = ITEMS.register("bass", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.1F).build())));
    public static final DeferredHolder<Item, Item> COOKED_BASS = ITEMS.register("cooked_bass", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.6F).build())));
    public static final DeferredHolder<Item, Item> ANGLERFISH = ITEMS.register("anglerfish", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.1F).build())));
    public static final DeferredHolder<Item, Item> COOKED_ANGLERFISH = ITEMS.register("cooked_anglerfish", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.6F).build())));
    public static final DeferredHolder<Item, Item> BLOBFISH = ITEMS.register("blobfish", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.1F).build())));
    public static final DeferredHolder<Item, Item> COOKED_BLOBFISH = ITEMS.register("cooked_blobfish", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.6F).build())));
    public static final DeferredHolder<Item, Item> PIRANHA = ITEMS.register("piranha", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.1F).build())));
    public static final DeferredHolder<Item, Item> COOKED_PIRANHA = ITEMS.register("cooked_piranha", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.6F).build())));
    public static final DeferredHolder<Item, Item> CLAM_MEAT = ITEMS.register("clam_meat", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.1F).build())));
    public static final DeferredHolder<Item, Item> COOKED_CLAM_MEAT = ITEMS.register("cooked_clam_meat", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.6F).build())));
    public static final DeferredHolder<Item, Item> CRAB_MEAT = ITEMS.register("crab_meat", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.1F).build())));
    public static final DeferredHolder<Item, Item> COOKED_CRAB_MEAT = ITEMS.register("cooked_crab_meat", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.6F).build())));
    public static final DeferredHolder<Item, CaughtEntityItem> CRAB = ITEMS.register("crab", () -> new CaughtEntityItem(NaturalistEntityTypes.CRAB, NaturalistSoundEvents.CRAB_AMBIENT, "tooltip.naturalist.crab_", Crab.VARIANT_NAMES, new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, BugNetItem> CAPTURE_NET = ITEMS.register("capture_net", () -> new BugNetItem(new Item.Properties().durability(64)));
    public static final DeferredHolder<Item, KnapsackItem> KNAPSACK = ITEMS.register("knapsack", () -> new KnapsackItem(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, WhistleItem> WHISTLE = ITEMS.register("whistle", () -> new WhistleItem(new Item.Properties().stacksTo(1)));
    public static final ResourceKey<JukeboxSong> WILD_ONES_SONG = ResourceKey.create(Registries.JUKEBOX_SONG, Naturalist.location("wild_ones"));
    public static final DeferredHolder<Item, Item> MUSIC_DISC_WILD_ONES = ITEMS.register("music_disc_wild_ones", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(WILD_ONES_SONG)));
    public static final ResourceKey<JukeboxSong> DEATH_BY_HOGS_SONG = ResourceKey.create(Registries.JUKEBOX_SONG, Naturalist.location("death_by_hogs"));
    public static final DeferredHolder<Item, Item> MUSIC_DISC_DEATH_BY_HOGS = ITEMS.register("music_disc_death_by_hogs", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(DEATH_BY_HOGS_SONG)));
    public static final DeferredHolder<Block, ChrysalisBlock> CHRYSALIS_BLOCK = registerBlockOnly("chrysalis", () -> new ChrysalisBlock(BlockBehaviour.Properties.of().randomTicks().strength(0.2F, 3.0F).sound(SoundType.GRASS).noOcclusion().noCollission().pushReaction(PushReaction.DESTROY)));
    public static final DeferredHolder<Item, BlockItem> CHRYSALIS = ITEMS.register("chrysalis", () -> new BlockItem(CHRYSALIS_BLOCK.get(), new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, CaughtEntityItem> CATERPILLAR = ITEMS.register("caterpillar", () -> new CaughtEntityItem(NaturalistEntityTypes.CATERPILLAR, NaturalistSoundEvents.SNAIL_FORWARD, new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, CaughtEntityItem> BUTTERFLY = ITEMS.register("butterfly", () -> new CaughtEntityItem(NaturalistEntityTypes.BUTTERFLY, NaturalistSoundEvents.BIRD_FLY, "tooltip.naturalist.", Butterfly.VARIANT_NAMES, new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, CaughtEntityItem> ANT = ITEMS.register("ant", () -> new CaughtEntityItem(NaturalistEntityTypes.ANT, NaturalistSoundEvents.ANT_AMBIENT, new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, CaughtEntityItem> RAT = ITEMS.register("rat", () -> new CaughtEntityItem(NaturalistEntityTypes.RAT, NaturalistSoundEvents.RAT_AMBIENT, "tooltip.naturalist.rat_", Rat.VARIANT_NAMES, new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, CaughtEntityItem> SCORPION = ITEMS.register("scorpion", () -> new CaughtEntityItem(NaturalistEntityTypes.DESERT_SCORPION, NaturalistSoundEvents.SCORPION_AMBIENT, new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, HedgehogItem> HEDGEHOG = ITEMS.register("hedgehog", () -> new HedgehogItem(NaturalistEntityTypes.HEDGEHOG, NaturalistSoundEvents.HEDGEHOG_AMBIENT, "tooltip.naturalist.hedgehog_", Hedgehog.VARIANT_NAMES, new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, CaughtEntityItem> DRAGONFLY = registerCaughtEntity("dragonfly", NaturalistEntityTypes.DRAGONFLY);
    public static final DeferredHolder<Item, CaughtEntityItem> FIREFLY = registerCaughtEntity("firefly", NaturalistEntityTypes.FIREFLY);
    public static final DeferredHolder<Item, CaughtEntityItem> BEE = registerCaughtEntity("bee", () -> EntityType.BEE);
    public static final DeferredHolder<Item, CaughtEntityItem> SILVERFISH = registerCaughtEntity("silverfish", () -> EntityType.SILVERFISH);
    public static final DeferredHolder<Item, CaughtEntityItem> ENDERMITE = registerCaughtEntity("endermite", () -> EntityType.ENDERMITE);
    public static final DeferredHolder<Item, Item> SCORPION_POISON_GLAND = ITEMS.register("scorpion_poison_gland", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, QueenAntItem> QUEEN_ANT = ITEMS.register("queen_ant", () -> new QueenAntItem(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Block, AntHillBlock> ANT_HILL = registerBlock("ant_hill", () -> new AntHillBlock(BlockBehaviour.Properties.of().strength(0.5F, 0.2F).sound(SoundType.ROOTED_DIRT).randomTicks()));
    public static final DeferredHolder<Block, SnailShellBlock> SNAIL_SHELL_BLOCK = registerBlockOnly("snail_shell", () -> new SnailShellBlock(BlockBehaviour.Properties.of().strength(0.5F).sound(SoundType.CORAL_BLOCK).noOcclusion().pushReaction(PushReaction.DESTROY)));
    public static final DeferredHolder<Item, BlockItem> SNAIL_SHELL = ITEMS.register("snail_shell", () -> new BlockItem(SNAIL_SHELL_BLOCK.get(), new Item.Properties().component(DataComponents.CUSTOM_DATA, SnailShellBlock.colorData(DyeColor.BROWN))));
    public static final DeferredHolder<Item, SnailItem> SNAIL = ITEMS.register("snail", () -> new SnailItem(NaturalistEntityTypes.SNAIL, NaturalistSoundEvents.SNAIL_FORWARD, new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, NaturalistBucketItem> STARFISH_BUCKET = ITEMS.register("starfish_bucket", () -> new NaturalistBucketItem(NaturalistEntityTypes.STARFISH.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1), false, "color.minecraft.", Starfish.VARIANT_NAMES));
    public static final DeferredHolder<Item, NaturalistBucketItem> GIANT_ISOPOD_BUCKET = ITEMS.register("giant_isopod_bucket", () -> new NaturalistBucketItem(NaturalistEntityTypes.GIANT_ISOPOD.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1), false, "tooltip.naturalist.giant_isopod_", GiantIsopod.VARIANT_NAMES));
    public static final DeferredHolder<Item, NaturalistBucketItem> ANGLERFISH_BUCKET = ITEMS.register("anglerfish_bucket", () -> new NaturalistBucketItem(NaturalistEntityTypes.ANGLERFISH.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1), true, "tooltip.naturalist.anglerfish_", Anglerfish.VARIANT_NAMES));
    public static final DeferredHolder<Item, NaturalistBucketItem> JELLYFISH_BUCKET = ITEMS.register("jellyfish_bucket", () -> new NaturalistBucketItem(NaturalistEntityTypes.JELLYFISH.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1), true, "color.minecraft.", Jellyfish.VARIANT_NAMES));
    public static final DeferredHolder<Item, NaturalistBucketItem> RAY_BUCKET = ITEMS.register("ray_bucket", () -> new NaturalistBucketItem(NaturalistEntityTypes.RAY.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1), true, "tooltip.naturalist.ray_", Ray.VARIANT_NAMES));
    public static final DeferredHolder<Item, NaturalistBucketItem> BLOBFISH_BUCKET = ITEMS.register("blobfish_bucket", () -> new NaturalistBucketItem(NaturalistEntityTypes.BLOBFISH.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, NaturalistBucketItem> PIRANHA_BUCKET = ITEMS.register("piranha_bucket", () -> new NaturalistBucketItem(NaturalistEntityTypes.PIRANHA.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Block, StarfishBlock> RED_STARFISH = registerStarfishBlock("red_starfish");
    public static final DeferredHolder<Block, StarfishBlock> ORANGE_STARFISH = registerStarfishBlock("orange_starfish");
    public static final DeferredHolder<Block, StarfishBlock> BLUE_STARFISH = registerStarfishBlock("blue_starfish");
    public static final DeferredHolder<Block, StarfishBlock> PURPLE_STARFISH = registerStarfishBlock("purple_starfish");

    public static final DeferredHolder<Block, TransparentBlock> AZURE_FROGLASS = registerBlock("azure_froglass", () -> new TransparentBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)));
    public static final DeferredHolder<Block, TransparentBlock> VERDANT_FROGLASS = registerBlock("verdant_froglass", () -> new TransparentBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)));
    public static final DeferredHolder<Block, TransparentBlock> CRIMSON_FROGLASS = registerBlock("crimson_froglass", () -> new TransparentBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)));
    public static final DeferredHolder<Block, IronBarsBlock> AZURE_FROGLASS_PANE = registerBlock("azure_froglass_pane", () -> new IronBarsBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS_PANE)));
    public static final DeferredHolder<Block, IronBarsBlock> VERDANT_FROGLASS_PANE = registerBlock("verdant_froglass_pane", () -> new IronBarsBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS_PANE)));
    public static final DeferredHolder<Block, IronBarsBlock> CRIMSON_FROGLASS_PANE = registerBlock("crimson_froglass_pane", () -> new IronBarsBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS_PANE)));
    public static final DeferredHolder<Block, Block> SHELLSTONE = registerBlock("shellstone", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final DeferredHolder<Block, StairBlock> SHELLSTONE_STAIRS = registerBlock("shellstone_stairs", () -> new StairBlock(SHELLSTONE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final DeferredHolder<Block, SlabBlock> SHELLSTONE_SLAB = registerBlock("shellstone_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final DeferredHolder<Block, WallBlock> SHELLSTONE_WALL = registerBlock("shellstone_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final DeferredHolder<Block, Block> SHELLSTONE_BRICKS = registerBlock("shellstone_bricks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final DeferredHolder<Block, StairBlock> SHELLSTONE_BRICK_STAIRS = registerBlock("shellstone_brick_stairs", () -> new StairBlock(SHELLSTONE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final DeferredHolder<Block, SlabBlock> SHELLSTONE_BRICK_SLAB = registerBlock("shellstone_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final DeferredHolder<Block, WallBlock> SHELLSTONE_BRICK_WALL = registerBlock("shellstone_brick_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final DeferredHolder<Block, Block> CUT_SHELLSTONE = registerBlock("cut_shellstone", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final DeferredHolder<Block, StairBlock> CUT_SHELLSTONE_STAIRS = registerBlock("cut_shellstone_stairs", () -> new StairBlock(CUT_SHELLSTONE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final DeferredHolder<Block, SlabBlock> CUT_SHELLSTONE_SLAB = registerBlock("cut_shellstone_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final DeferredHolder<Block, WallBlock> CUT_SHELLSTONE_WALL = registerBlock("cut_shellstone_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final DeferredHolder<Block, Block> SMOOTH_SHELLSTONE = registerBlock("smooth_shellstone", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final DeferredHolder<Block, StairBlock> SMOOTH_SHELLSTONE_STAIRS = registerBlock("smooth_shellstone_stairs", () -> new StairBlock(SMOOTH_SHELLSTONE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final DeferredHolder<Block, SlabBlock> SMOOTH_SHELLSTONE_SLAB = registerBlock("smooth_shellstone_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final DeferredHolder<Block, WallBlock> SMOOTH_SHELLSTONE_WALL = registerBlock("smooth_shellstone_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)));

    public static final DeferredHolder<Item, SpawnEggItem> ALLIGATOR_SPAWN_EGG = ITEMS.register("alligator_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.ALLIGATOR, 6184228, 13810273, new Item.Properties()));
    public static final DeferredHolder<Item, SpawnEggItem> ANGLERFISH_SPAWN_EGG = ITEMS.register("anglerfish_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.ANGLERFISH, 3029818, 15132298, new Item.Properties()));
    public static final DeferredHolder<Item, SpawnEggItem> RAY_SPAWN_EGG = ITEMS.register("ray_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.RAY, 4873579, 12568015, new Item.Properties()));
    public static final DeferredHolder<Item, SpawnEggItem> BLOBFISH_SPAWN_EGG = ITEMS.register("blobfish_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.BLOBFISH, 9142136, 14722214, new Item.Properties()));
    public static final DeferredHolder<Item, SpawnEggItem> PIRANHA_SPAWN_EGG = ITEMS.register("piranha_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.PIRANHA, 7171697, 11549218, new Item.Properties()));
    public static final DeferredHolder<Item, SpawnEggItem> BASS_SPAWN_EGG = ITEMS.register("bass_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.BASS, 8159273, 14729339, new Item.Properties()));
    public static final DeferredHolder<Item, SpawnEggItem> BEAR_SPAWN_EGG = ITEMS.register("bear_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.BEAR, 6569255, 13150577, new Item.Properties()));
    public static final DeferredHolder<Item, SpawnEggItem> BIRD_SPAWN_EGG = ITEMS.register("bird_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.BIRD, 4865860, 16620592, new Item.Properties()));
    public static final DeferredHolder<Item, SpawnEggItem> BOAR_SPAWN_EGG = ITEMS.register("boar_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.BOAR, 6768433, 9854549, new Item.Properties()));
    public static final DeferredHolder<Item, SpawnEggItem> BUTTERFLY_SPAWN_EGG = ITEMS.register("butterfly_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.BUTTERFLY, 15165706, 6828564, new Item.Properties()));
    public static final DeferredHolder<Item, SpawnEggItem> CATFISH_SPAWN_EGG = ITEMS.register("catfish_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.CATFISH, 8416033, 12233092, new Item.Properties()));
    public static final DeferredHolder<Item, SpawnEggItem> CATERPILLAR_SPAWN_EGG = ITEMS.register("caterpillar_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.CATERPILLAR, 3815473, 15647488, new Item.Properties()));
    public static final DeferredHolder<Item, SpawnEggItem> CLAM_SPAWN_EGG = ITEMS.register("clam_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.CLAM, 9138517, 14272931, new Item.Properties()));
    public static final DeferredHolder<Item, SpawnEggItem> CRAB_SPAWN_EGG = ITEMS.register("crab_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.CRAB, 14179386, 15909531, new Item.Properties()));
    public static final DeferredHolder<Item, SpawnEggItem> DEER_SPAWN_EGG = ITEMS.register("deer_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.DEER, 10318165, 14531208, new Item.Properties()));
    public static final DeferredHolder<Item, SpawnEggItem> DRAGONFLY_SPAWN_EGG = ITEMS.register("dragonfly_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.DRAGONFLY, 7507200, 16771840, new Item.Properties()));
    public static final DeferredHolder<Item, SpawnEggItem> DUCK_SPAWN_EGG = ITEMS.register("duck_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.DUCK, 13286315, 2333491, new Item.Properties()));
    public static final DeferredHolder<Item, SpawnEggItem> CAPYBARA_SPAWN_EGG = ITEMS.register("capybara_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.CAPYBARA, 11691058, 5650727, new Item.Properties()));
    public static final DeferredHolder<Item, SpawnEggItem> HEDGEHOG_SPAWN_EGG = ITEMS.register("hedgehog_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.HEDGEHOG, 5783076, 14468251, new Item.Properties()));
    public static final DeferredHolder<Item, SpawnEggItem> ELEPHANT_SPAWN_EGG = ITEMS.register("elephant_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.ELEPHANT, 9539213, 6643034, new Item.Properties()));
    public static final DeferredHolder<Item, SpawnEggItem> MAMMOTH_SPAWN_EGG = ITEMS.register("mammoth_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.MAMMOTH, 5520936, 3153430, new Item.Properties()));
    public static final DeferredHolder<Item, SpawnEggItem> FIREFLY_SPAWN_EGG = ITEMS.register("firefly_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.FIREFLY, 6764577, 16768800, new Item.Properties()));
    public static final DeferredHolder<Item, SpawnEggItem> GIANT_ISOPOD_SPAWN_EGG = ITEMS.register("giant_isopod_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.GIANT_ISOPOD, 7362632, 10125426, new Item.Properties()));
    public static final DeferredHolder<Item, SpawnEggItem> JELLYFISH_SPAWN_EGG = ITEMS.register("jellyfish_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.JELLYFISH, 8900331, 8388736, new Item.Properties()));
    public static final DeferredHolder<Item, SpawnEggItem> GIRAFFE_SPAWN_EGG = ITEMS.register("giraffe_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.GIRAFFE, 14329967, 7619616, new Item.Properties()));
    public static final DeferredHolder<Item, SpawnEggItem> HIPPO_SPAWN_EGG = ITEMS.register("hippo_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.HIPPO, 15702682, 9004386, new Item.Properties()));
    public static final DeferredHolder<Item, SpawnEggItem> LION_SPAWN_EGG = ITEMS.register("lion_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.LION, 14990722, 6699537, new Item.Properties()));
    public static final DeferredHolder<Item, SpawnEggItem> LIZARD_SPAWN_EGG = ITEMS.register("lizard_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.LIZARD, 10853166, 15724462, new Item.Properties()));
    public static final DeferredHolder<Item, SpawnEggItem> RHINO_SPAWN_EGG = ITEMS.register("rhino_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.RHINO, 7626842, 10982025, new Item.Properties()));
    public static final DeferredHolder<Item, SpawnEggItem> SNAKE_SPAWN_EGG = ITEMS.register("snake_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.SNAKE, 8813107, 15524255, new Item.Properties()));
    public static final DeferredHolder<Item, SpawnEggItem> SNAIL_SPAWN_EGG = ITEMS.register("snail_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.SNAIL, 5457209, 8811878, new Item.Properties()));
    public static final DeferredHolder<Item, SpawnEggItem> STARFISH_SPAWN_EGG = ITEMS.register("starfish_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.STARFISH, 14245934, 15909006, new Item.Properties()));
    public static final DeferredHolder<Item, SpawnEggItem> TORTOISE_SPAWN_EGG = ITEMS.register("tortoise_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.TORTOISE, 15724462, 11765582, new Item.Properties()));
    public static final DeferredHolder<Item, SpawnEggItem> VULTURE_SPAWN_EGG = ITEMS.register("vulture_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.VULTURE, 4010022, 15325376, new Item.Properties()));
    public static final DeferredHolder<Item, SpawnEggItem> ZEBRA_SPAWN_EGG = ITEMS.register("zebra_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.ZEBRA, 15263457, 1710104, new Item.Properties()));
    public static final DeferredHolder<Item, SpawnEggItem> WHALE_SPAWN_EGG = ITEMS.register("whale_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.WHALE, 3886172, 9413037, new Item.Properties()));
    public static final DeferredHolder<Item, SpawnEggItem> ANT_SPAWN_EGG = ITEMS.register("ant_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.ANT, 5515292, 3085582, new Item.Properties()));
    public static final DeferredHolder<Item, SpawnEggItem> MOLE_SPAWN_EGG = ITEMS.register("mole_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.MOLE, 5259603, 15443344, new Item.Properties()));
    public static final DeferredHolder<Item, SpawnEggItem> RAT_SPAWN_EGG = ITEMS.register("rat_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.RAT, 4866377, 15968421, new Item.Properties()));
    public static final DeferredHolder<Item, SpawnEggItem> BLACK_BEAR_SPAWN_EGG = ITEMS.register("black_bear_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.BLACK_BEAR, 2500134, 12550772, new Item.Properties()));
    public static final DeferredHolder<Item, SpawnEggItem> TIGER_SPAWN_EGG = ITEMS.register("tiger_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.TIGER, 14257212, 3158062, new Item.Properties()));
    public static final DeferredHolder<Item, SpawnEggItem> KOMODO_DRAGON_SPAWN_EGG = ITEMS.register("komodo_dragon_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.KOMODO_DRAGON, 5590330, 8746320, new Item.Properties()));
    public static final DeferredHolder<Item, SpawnEggItem> OSTRICH_SPAWN_EGG = ITEMS.register("ostrich_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.OSTRICH, 9521716, 14451302, new Item.Properties()));
    public static final DeferredHolder<Item, SpawnEggItem> DESERT_SCORPION_SPAWN_EGG = ITEMS.register("desert_scorpion_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.DESERT_SCORPION, 13544037, 9856322, new Item.Properties()));
    public static final DeferredHolder<Item, SpawnEggItem> JUNGLE_SCORPION_SPAWN_EGG = ITEMS.register("jungle_scorpion_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.JUNGLE_SCORPION, 2960943, 6926420, new Item.Properties()));
    public static final DeferredHolder<Item, SpawnEggItem> GREAT_WHITE_SHARK_SPAWN_EGG = ITEMS.register("great_white_shark_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.GREAT_WHITE_SHARK, 6060149, 15329245, new Item.Properties()));
    public static final DeferredHolder<Item, SpawnEggItem> TURKEY_SPAWN_EGG = ITEMS.register("turkey_spawn_egg", () -> Services.REGISTRY.createSpawnEgg(NaturalistEntityTypes.TURKEY, 4600099, 14122348, new Item.Properties()));

    public static void init() {
    }

    private static CustomData ducklingBucketData() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("Age", -24000);
        return CustomData.of(tag);
    }

    private static DeferredHolder<Item, CaughtEntityItem> registerCaughtEntity(String name, Supplier<? extends EntityType<?>> type) {
        DeferredHolder<Item, CaughtEntityItem> holder = ITEMS.register(name, () -> new CaughtEntityItem(type, () -> SoundEvents.BUNDLE_DROP_CONTENTS, new Item.Properties().stacksTo(1)));
        CAUGHT_ENTITY_ITEMS.add(holder);
        return holder;
    }

    private static <T extends Block> DeferredHolder<Block, T> registerBlock(String name, Supplier<T> block) {
        DeferredHolder<Block, T> holder = BLOCKS.register(name, block);
        ITEMS.register(name, () -> new BlockItem(holder.get(), new Item.Properties()));
        return holder;
    }

    private static <T extends Block> DeferredHolder<Block, T> registerBlockOnly(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    private static DeferredHolder<Block, StarfishBlock> registerStarfishBlock(String name) {
        DeferredHolder<Block, StarfishBlock> holder = BLOCKS.register(name, () -> new StarfishBlock(BlockBehaviour.Properties.of().noCollission().instabreak().sound(SoundType.WET_GRASS).noOcclusion().pushReaction(PushReaction.DESTROY)));
        ITEMS.register(name, () -> new BlockItem(holder.get(), new Item.Properties()));
        return holder;
    }
}
