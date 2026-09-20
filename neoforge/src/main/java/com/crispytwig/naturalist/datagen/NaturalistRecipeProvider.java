package com.crispytwig.naturalist.datagen;

import com.crispytwig.naturalist.registry.NaturalistRegistry;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.MultiRegistryBootstrap;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.crafting.SmokingRecipe;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import com.crispytwig.naturalist.registry.NaturalistPotions;
import com.crispytwig.naturalist.world.item.crafting.BugNetInteractionRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.BrewingRecipeBuilder;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import java.util.Optional;

public class NaturalistRecipeProvider extends RecipeProvider {
    private static final TagKey<Item> C_EGGS = TagKey.create(Registries.ITEM, Identifier.parse("c:eggs"));

    private final RecipeOutput recipes;

    protected NaturalistRecipeProvider(BootstrapContext<Recipe<?>> recipeOutput, BootstrapContext<Advancement> advancementOutput) {
        super(recipeOutput, advancementOutput);
        this.recipes = new RecipeOutput() {
            @Override
            public void accept(ResourceKey<Recipe<?>> id, Recipe<?> recipe, @Nullable AdvancementHolder advancement,
                               net.neoforged.neoforge.common.conditions.ICondition... conditions) {
                NaturalistRecipeProvider.this.output.accept(id, recipe, null, conditions);
            }

            @Override
            public Advancement.Builder advancement() {
                return NaturalistRecipeProvider.this.output.advancement();
            }

            @Override
            public <T> HolderGetter<T> lookup(ResourceKey<? extends Registry<? extends T>> key) {
                return NaturalistRecipeProvider.this.output.lookup(key);
            }

            @Override
            public <T> Stream<Holder.Reference<T>> listContextElements(ResourceKey<? extends Registry<? extends T>> key) {
                return NaturalistRecipeProvider.this.output.listContextElements(key);
            }
        };
    }

    private static ResourceKey<Recipe<?>> id(String path) {
        return ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath("naturalist", path));
    }

    private static ResourceKey<Recipe<?>> mc(String path) {
        return ResourceKey.create(Registries.RECIPE, Identifier.withDefaultNamespace(path));
    }

    private record Cook(ItemLike raw, ItemLike cooked, String name, int smoking, int campfire) {
    }

    @Override
    protected void buildRecipes() {
        cooking();
        stoneFamilies();
        froglassPanes();
        snailShellDyes();
        misc();
        vanillaOverrides();
        brewing();
        netCatches();
    }

    private void cooking() {
        List<Cook> foods = List.of(
                new Cook(NaturalistRegistry.ANGLERFISH.get(), NaturalistRegistry.COOKED_ANGLERFISH.get(), "cooked_anglerfish", 100, 600),
                new Cook(NaturalistRegistry.BASS.get(), NaturalistRegistry.COOKED_BASS.get(), "cooked_bass", 100, 600),
                new Cook(NaturalistRegistry.BLOBFISH.get(), NaturalistRegistry.COOKED_BLOBFISH.get(), "cooked_blobfish", 100, 600),
                new Cook(NaturalistRegistry.BUSHMEAT.get(), NaturalistRegistry.COOKED_BUSHMEAT.get(), "cooked_bushmeat", 100, 600),
                new Cook(NaturalistRegistry.CATFISH.get(), NaturalistRegistry.COOKED_CATFISH.get(), "cooked_catfish", 100, 600),
                new Cook(NaturalistRegistry.CLAM_MEAT.get(), NaturalistRegistry.COOKED_CLAM_MEAT.get(), "cooked_clam_meat", 100, 600),
                new Cook(NaturalistRegistry.CRAB_MEAT.get(), NaturalistRegistry.COOKED_CRAB_MEAT.get(), "cooked_crab_meat", 100, 600),
                new Cook(NaturalistRegistry.DRUMSTICK.get(), NaturalistRegistry.COOKED_DRUMSTICK.get(), "cooked_drumstick", 100, 600),
                new Cook(NaturalistRegistry.DUCK.get(), NaturalistRegistry.COOKED_DUCK.get(), "cooked_duck", 200, 200),
                new Cook(NaturalistRegistry.LIZARD_TAIL.get(), NaturalistRegistry.COOKED_LIZARD_TAIL.get(), "cooked_lizard_tail", 100, 600),
                new Cook(NaturalistRegistry.MAMMOTH_MEAT.get(), NaturalistRegistry.COOKED_MAMMOTH_MEAT.get(), "cooked_mammoth_meat", 100, 600),
                new Cook(NaturalistRegistry.PIRANHA.get(), NaturalistRegistry.COOKED_PIRANHA.get(), "cooked_piranha", 100, 600),
                new Cook(NaturalistRegistry.VENISON.get(), NaturalistRegistry.COOKED_VENISON.get(), "cooked_venison", 100, 600));

        for (Cook food : foods) {
            cookTriple(Ingredient.of(food.raw()), food.cooked(), food.name(), food.smoking(), food.campfire());
        }

        cookTriple(tag(C_EGGS), NaturalistRegistry.COOKED_EGG.get(), "cooked_egg", 200, 200);
        cookTriple(Ingredient.of(Items.GLOW_BERRIES), NaturalistRegistry.GLOW_GOOP.get(), "glow_goop", 200, 200);

        SimpleCookingRecipeBuilder.generic(Ingredient.of(NaturalistRegistry.HIDE.get()), RecipeCategory.MISC,
                        CookingBookCategory.MISC, Items.LEATHER, 0.1F, 200, SmeltingRecipe::new)
                .unlockedBy("has_ingredient", has(NaturalistRegistry.HIDE.get()))
                .save(this.recipes, id("leather_from_smelting_hide"));

        SimpleCookingRecipeBuilder.generic(Ingredient.of(NaturalistRegistry.SHELLSTONE.get()), RecipeCategory.MISC,
                        CookingBookCategory.MISC, NaturalistRegistry.SMOOTH_SHELLSTONE.get(), 0.1F, 200, SmeltingRecipe::new)
                .unlockedBy("has_ingredient", has(NaturalistRegistry.SHELLSTONE.get()))
                .save(this.recipes, id("smooth_shellstone"));
    }

    private void cookTriple(Ingredient input, ItemLike result, String name, int smokingTime, int campfireTime) {
        SimpleCookingRecipeBuilder.generic(input, RecipeCategory.MISC, CookingBookCategory.MISC, result, 0.35F, 200, SmeltingRecipe::new)
                .unlockedBy("has_ingredient", has(result))
                .save(this.recipes, id(name));
        SimpleCookingRecipeBuilder.generic(input, RecipeCategory.MISC, CookingBookCategory.MISC, result, 0.35F, smokingTime, SmokingRecipe::new)
                .unlockedBy("has_ingredient", has(result))
                .save(this.recipes, id(name + "_from_smoking"));
        SimpleCookingRecipeBuilder.generic(input, RecipeCategory.MISC, CookingBookCategory.MISC, result, 0.35F, campfireTime, CampfireCookingRecipe::new)
                .unlockedBy("has_ingredient", has(result))
                .save(this.recipes, id(name + "_from_campfire_cooking"));
    }

    private void stoneFamilies() {
        stoneFamily(NaturalistRegistry.SHELLSTONE.get(), NaturalistRegistry.SHELLSTONE_SLAB.get(),
                NaturalistRegistry.SHELLSTONE_STAIRS.get(), NaturalistRegistry.SHELLSTONE_WALL.get(), "shellstone");
        stoneFamily(NaturalistRegistry.SHELLSTONE_BRICKS.get(), NaturalistRegistry.SHELLSTONE_BRICK_SLAB.get(),
                NaturalistRegistry.SHELLSTONE_BRICK_STAIRS.get(), NaturalistRegistry.SHELLSTONE_BRICK_WALL.get(), "shellstone_brick");
        stoneFamily(NaturalistRegistry.CUT_SHELLSTONE.get(), NaturalistRegistry.CUT_SHELLSTONE_SLAB.get(),
                NaturalistRegistry.CUT_SHELLSTONE_STAIRS.get(), NaturalistRegistry.CUT_SHELLSTONE_WALL.get(), "cut_shellstone");
        stoneFamily(NaturalistRegistry.SMOOTH_SHELLSTONE.get(), NaturalistRegistry.SMOOTH_SHELLSTONE_SLAB.get(),
                NaturalistRegistry.SMOOTH_SHELLSTONE_STAIRS.get(), NaturalistRegistry.SMOOTH_SHELLSTONE_WALL.get(), "smooth_shellstone");

        shaped(RecipeCategory.MISC, NaturalistRegistry.SHELLSTONE_BRICKS.get())
                .pattern("##").pattern("##")
                .define('#', NaturalistRegistry.SHELLSTONE.get())
                .unlockedBy("has_ingredient", has(NaturalistRegistry.SHELLSTONE.get()))
                .save(this.recipes, id("shellstone_bricks"));

        shaped(RecipeCategory.MISC, NaturalistRegistry.CUT_SHELLSTONE.get())
                .pattern("#").pattern("#")
                .define('#', NaturalistRegistry.SHELLSTONE_SLAB.get())
                .unlockedBy("has_ingredient", has(NaturalistRegistry.SHELLSTONE_SLAB.get()))
                .save(this.recipes, id("cut_shellstone"));

        shaped(RecipeCategory.MISC, NaturalistRegistry.SHELLSTONE.get(), 8)
                .pattern("CS").pattern("SC")
                .define('C', Items.CALCITE)
                .define('S', NaturalistRegistry.SNAIL_SHELL.get())
                .unlockedBy("has_ingredient", has(NaturalistRegistry.SNAIL_SHELL.get()))
                .save(this.recipes, id("shellstone"));
    }

    private void stoneFamily(ItemLike base, ItemLike slab, ItemLike stairs, ItemLike wall, String name) {
        shaped(RecipeCategory.MISC, slab, 6).pattern("###")
                .define('#', base).unlockedBy("has_ingredient", has(base))
                .save(this.recipes, id(name + "_slab"));
        shaped(RecipeCategory.MISC, stairs, 4).pattern("#  ").pattern("## ").pattern("###")
                .define('#', base).unlockedBy("has_ingredient", has(base))
                .save(this.recipes, id(name + "_stairs"));
        shaped(RecipeCategory.MISC, wall, 6).pattern("###").pattern("###")
                .define('#', base).unlockedBy("has_ingredient", has(base))
                .save(this.recipes, id(name + "_wall"));

        stonecut(base, slab, 2, name + "_slab");
        stonecut(base, stairs, 1, name + "_stairs");
        stonecut(base, wall, 1, name + "_wall");
    }

    private void stonecut(ItemLike base, ItemLike result, int count, String name) {
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(base), RecipeCategory.MISC, result, count)
                .unlockedBy("has_ingredient", has(base))
                .save(this.recipes, id(name + "_from_stonecutting"));
    }

    private void froglassPanes() {
        froglassPane(NaturalistRegistry.AZURE_FROGLASS.get(), NaturalistRegistry.AZURE_FROGLASS_PANE.get(), "azure_froglass_pane");
        froglassPane(NaturalistRegistry.CRIMSON_FROGLASS.get(), NaturalistRegistry.CRIMSON_FROGLASS_PANE.get(), "crimson_froglass_pane");
        froglassPane(NaturalistRegistry.VERDANT_FROGLASS.get(), NaturalistRegistry.VERDANT_FROGLASS_PANE.get(), "verdant_froglass_pane");
    }

    private void froglassPane(ItemLike glass, ItemLike pane, String name) {
        shaped(RecipeCategory.MISC, pane, 16)
                .pattern("###").pattern("###")
                .define('#', glass)
                .unlockedBy("has_ingredient", has(glass))
                .save(this.recipes, id(name));
    }

    private void snailShellDyes() {
        for (DyeColor color : DyeColor.values()) {
            CompoundTag data = new CompoundTag();
            data.putInt("Color", color.getId());
            ItemStackTemplate result = new ItemStackTemplate(NaturalistRegistry.SNAIL_SHELL.get(),
                    DataComponentPatch.builder().set(DataComponents.CUSTOM_DATA, CustomData.of(data)).build());
            shapeless(RecipeCategory.MISC, result)
                    .requires(NaturalistRegistry.SNAIL_SHELL.get())
                    .requires(Items.DYE.pick(color))
                    .unlockedBy("has_ingredient", has(NaturalistRegistry.SNAIL_SHELL.get()))
                    .save(this.recipes, id("snail_shell_" + color.getName()));
        }
    }

    private void misc() {
        shaped(RecipeCategory.MISC, NaturalistRegistry.CAPTURE_NET.get())
                .pattern("  #").pattern(" #S").pattern("#SS")
                .define('#', Items.BAMBOO).define('S', Items.STRING)
                .unlockedBy("has_ingredient", has(Items.BAMBOO))
                .save(this.recipes, id("capture_net"));

        shaped(RecipeCategory.MISC, NaturalistRegistry.KNAPSACK.get())
                .pattern("H H").pattern(" H ")
                .define('H', NaturalistRegistry.HIDE.get())
                .unlockedBy("has_ingredient", has(NaturalistRegistry.HIDE.get()))
                .save(this.recipes, id("knapsack"));

        shaped(RecipeCategory.MISC, NaturalistRegistry.PLUSH_BEAR.get())
                .pattern(" F ").pattern("FWF").pattern(" F ")
                .define('F', Items.STRING).define('W', ItemTags.WOOL)
                .unlockedBy("has_ingredient", has(ItemTags.WOOL))
                .save(this.recipes, id("plush_bear"));

        shaped(RecipeCategory.MISC, NaturalistRegistry.WHISTLE.get())
                .pattern(" C ").pattern(" S ")
                .define('C', Items.COPPER_INGOT).define('S', Items.STRING)
                .unlockedBy("has_ingredient", has(Items.COPPER_INGOT))
                .save(this.recipes, id("whistle"));

        shaped(RecipeCategory.MISC, Items.SPECTRAL_ARROW, 2)
                .pattern("###").pattern("#A#").pattern("###")
                .define('#', NaturalistRegistry.GLOW_GOOP.get()).define('A', Items.ARROW)
                .group("naturalist:spectral_arrow")
                .unlockedBy("has_ingredient", has(NaturalistRegistry.GLOW_GOOP.get()))
                .save(this.recipes, id("spectral_arrow_from_glow_goop"));

        shaped(RecipeCategory.MISC, Items.CAKE)
                .pattern("AAA").pattern("BEB").pattern("CCC")
                .define('A', Items.MILK_BUCKET).define('B', Items.SUGAR)
                .define('C', Items.WHEAT).define('E', NaturalistRegistry.DUCK_EGG.get())
                .group("naturalist:cake")
                .unlockedBy("has_ingredient", has(NaturalistRegistry.DUCK_EGG.get()))
                .save(this.recipes, id("cake"));

        shapeless(RecipeCategory.MISC, Items.PUMPKIN_PIE)
                .requires(Items.PUMPKIN).requires(Items.SUGAR).requires(NaturalistRegistry.DUCK_EGG.get())
                .group("naturalist:pumpkin_pie")
                .unlockedBy("has_ingredient", has(NaturalistRegistry.DUCK_EGG.get()))
                .save(this.recipes, id("pumpkin_pie"));
    }

    private void vanillaOverrides() {
        shaped(RecipeCategory.MISC, Items.CAKE)
                .pattern("AAA").pattern("BEB").pattern("CCC")
                .define('A', Items.MILK_BUCKET).define('B', Items.SUGAR)
                .define('C', Items.WHEAT).define('E', ItemTags.EGGS)
                .group("naturalist:cake")
                .unlockedBy("has_ingredient", has(ItemTags.EGGS))
                .save(this.recipes, mc("cake"));

        shaped(RecipeCategory.MISC, Items.LEATHER)
                .pattern("##").pattern("##")
                .define('#', Items.RABBIT_HIDE)
                .group("naturalist:leather")
                .unlockedBy("has_ingredient", has(Items.RABBIT_HIDE))
                .save(this.recipes, mc("leather"));

        shapeless(RecipeCategory.MISC, Items.PUMPKIN_PIE)
                .requires(Items.PUMPKIN).requires(Items.SUGAR).requires(ItemTags.EGGS)
                .group("naturalist:pumpkin_pie")
                .unlockedBy("has_ingredient", has(ItemTags.EGGS))
                .save(this.recipes, mc("pumpkin_pie"));

        shaped(RecipeCategory.MISC, Items.SPECTRAL_ARROW, 2)
                .pattern(" # ").pattern("#X#").pattern(" # ")
                .define('#', Items.GLOWSTONE_DUST).define('X', Items.ARROW)
                .group("naturalist:spectral_arrow")
                .unlockedBy("has_ingredient", has(Items.GLOWSTONE_DUST))
                .save(this.recipes, mc("spectral_arrow"));
    }

    private static Holder<Potion> potion(java.util.function.Supplier<Potion> potion) {
        return BuiltInRegistries.POTION.wrapAsHolder(potion.get());
    }

    private void brewing() {
        brewMix(Potions.AWKWARD, NaturalistRegistry.ANTLER.get(), potion(NaturalistPotions.FOREST_DASHER), "awkward_antler");
        brewMix(potion(NaturalistPotions.FOREST_DASHER), Items.REDSTONE, potion(NaturalistPotions.LONG_FOREST_DASHER), "forest_dasher_redstone");
        brewMix(potion(NaturalistPotions.FOREST_DASHER), Items.GLOWSTONE_DUST, potion(NaturalistPotions.STRONG_FOREST_DASHER), "forest_dasher_glowstone_dust");
        brewMix(Potions.AWKWARD, NaturalistRegistry.SCORPION_POISON_GLAND.get(), potion(NaturalistPotions.ANTIVENOM), "awkward_scorpion_poison_gland");
    }

    private void brewMix(Holder<Potion> input, Item reagent, Holder<Potion> result, String name) {
        for (Item container : List.of(Items.POTION, Items.SPLASH_POTION, Items.LINGERING_POTION)) {
            String prefix = BuiltInRegistries.ITEM.getKey(container).getPath();
            BrewingRecipeBuilder.brewingMix(container, input, reagent, result)
                    .save(this.recipes, id("brewing/" + prefix + "_" + name));
        }
    }

    private void netCatches() {
        this.recipes.accept(id("catch_bee"),
                new BugNetInteractionRecipe(EntityTypes.BEE, Optional.empty(), new ItemStackTemplate(Items.DIRT)), null);
        this.recipes.accept(id("catch_cod"),
                new BugNetInteractionRecipe(EntityTypes.COD, Optional.of(Ingredient.of(Items.WATER_BUCKET)),
                        new ItemStackTemplate(Items.COD_BUCKET)), null);
    }

    public static MultiRegistryBootstrap create() {
        return new MultiRegistryBootstrap() {
            @Override
            public Set<ResourceKey<? extends Registry<?>>> requestedRegistries() {
                return Set.of(Registries.RECIPE, Registries.ADVANCEMENT);
            }

            @Override
            public void run(MultiRegistryBootstrap.BootstrapGetter registries) {
                new NaturalistRecipeProvider(registries.get(Registries.RECIPE), registries.get(Registries.ADVANCEMENT)).buildRecipes();
            }
        };
    }
}
