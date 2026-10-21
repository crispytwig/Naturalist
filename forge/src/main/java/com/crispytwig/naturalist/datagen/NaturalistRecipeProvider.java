package com.crispytwig.naturalist.datagen;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.registry.NaturalistRecipes;
import com.crispytwig.naturalist.registry.NaturalistRegistry;
import com.crispytwig.naturalist.tags.NaturalistItemTags;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class NaturalistRecipeProvider extends RecipeProvider {
    private static final TagKey<Item> C_EGGS = TagKey.create(Registries.ITEM, new ResourceLocation("c", "eggs"));

    public NaturalistRecipeProvider(PackOutput output) {
        super(output);
    }

    private record Cook(ItemLike raw, ItemLike cooked, int smoking, int campfire, boolean unlock) {
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> writer) {
        cooking(writer);
        stoneFamilies(writer);
        froglassPanes(writer);
        snailShellDyes(writer);
        misc(writer);
        vanillaOverrides(writer);
        netCatches(writer);
    }

    //region Cooking
    private void cooking(Consumer<FinishedRecipe> writer) {
        List<Cook> foods = List.of(
                new Cook(NaturalistRegistry.ANGLERFISH.get(), NaturalistRegistry.COOKED_ANGLERFISH.get(), 100, 600, true),
                new Cook(NaturalistRegistry.BASS.get(), NaturalistRegistry.COOKED_BASS.get(), 100, 600, true),
                new Cook(NaturalistRegistry.BLOBFISH.get(), NaturalistRegistry.COOKED_BLOBFISH.get(), 100, 600, true),
                new Cook(NaturalistRegistry.BUSHMEAT.get(), NaturalistRegistry.COOKED_BUSHMEAT.get(), 100, 600, false),
                new Cook(NaturalistRegistry.CATFISH.get(), NaturalistRegistry.COOKED_CATFISH.get(), 100, 600, true),
                new Cook(NaturalistRegistry.CLAM_MEAT.get(), NaturalistRegistry.COOKED_CLAM_MEAT.get(), 100, 600, false),
                new Cook(NaturalistRegistry.CRAB_MEAT.get(), NaturalistRegistry.COOKED_CRAB_MEAT.get(), 100, 600, false),
                new Cook(NaturalistRegistry.DRUMSTICK.get(), NaturalistRegistry.COOKED_DRUMSTICK.get(), 100, 600, true),
                new Cook(NaturalistRegistry.DUCK.get(), NaturalistRegistry.COOKED_DUCK.get(), 200, 200, true),
                new Cook(NaturalistRegistry.LIZARD_TAIL.get(), NaturalistRegistry.COOKED_LIZARD_TAIL.get(), 100, 600, true),
                new Cook(NaturalistRegistry.MAMMOTH_MEAT.get(), NaturalistRegistry.COOKED_MAMMOTH_MEAT.get(), 100, 600, true),
                new Cook(NaturalistRegistry.PIRANHA.get(), NaturalistRegistry.COOKED_PIRANHA.get(), 100, 600, true),
                new Cook(NaturalistRegistry.VENISON.get(), NaturalistRegistry.COOKED_VENISON.get(), 100, 600, true));

        for (Cook food : foods) {
            String name = getItemName(food.cooked());
            Consumer<FinishedRecipe> smelting = food.unlock()
                    ? output(writer, false, "food/" + name, cookingUnlock(getHasName(food.raw()), has(food.raw()), name), false)
                    : plain(writer);
            cookTriple(writer, smelting, Ingredient.of(food.raw()), has(food.raw()), food.cooked(), food.smoking(), food.campfire());
        }

        cookTriple(writer, output(writer, false, "food/cooked_egg", cookingUnlock("has_eggs", has(NaturalistItemTags.EGGS), "cooked_egg"), false),
                Ingredient.of(C_EGGS), has(C_EGGS), NaturalistRegistry.COOKED_EGG.get(), 200, 200);
        cookTriple(writer, output(writer, false, "naturalist/glow_goop", cookingUnlock(getHasName(Items.GLOW_BERRIES), has(Items.GLOW_BERRIES), "glow_goop"), false),
                Ingredient.of(Items.GLOW_BERRIES), has(Items.GLOW_BERRIES), NaturalistRegistry.GLOW_GOOP.get(), 200, 200);

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(NaturalistRegistry.HIDE.get()), RecipeCategory.MISC, Items.LEATHER, 0.1F, 200)
                .unlockedBy(getHasName(NaturalistRegistry.HIDE.get()), has(NaturalistRegistry.HIDE.get()))
                .save(plain(writer), id("leather_from_smelting_hide"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(NaturalistRegistry.SHELLSTONE.get()), RecipeCategory.BUILDING_BLOCKS, NaturalistRegistry.SMOOTH_SHELLSTONE.get(), 0.1F, 200)
                .unlockedBy(getHasName(NaturalistRegistry.SHELLSTONE.get()), has(NaturalistRegistry.SHELLSTONE.get()))
                .save(legacyUnlock(writer, "smooth_limestone", NaturalistRegistry.SHELLSTONE.get(), NaturalistRegistry.SMOOTH_SHELLSTONE.get()), id("smooth_shellstone"));
    }

    private void cookTriple(Consumer<FinishedRecipe> writer, Consumer<FinishedRecipe> smeltingWriter, Ingredient input, CriterionTriggerInstance trigger, ItemLike result, int smokingTime, int campfireTime) {
        String name = getItemName(result);
        SimpleCookingRecipeBuilder.smelting(input, RecipeCategory.FOOD, result, 0.35F, 200)
                .unlockedBy("has_ingredient", trigger)
                .save(smeltingWriter, id(name));
        SimpleCookingRecipeBuilder.smoking(input, RecipeCategory.FOOD, result, 0.35F, smokingTime)
                .unlockedBy("has_ingredient", trigger)
                .save(plain(writer), id(name + "_from_smoking"));
        SimpleCookingRecipeBuilder.campfireCooking(input, RecipeCategory.FOOD, result, 0.35F, campfireTime)
                .unlockedBy("has_ingredient", trigger)
                .save(plain(writer), id(name + "_from_campfire_cooking"));
    }
    //endregion

    //region Shellstone
    private void stoneFamilies(Consumer<FinishedRecipe> writer) {
        stoneFamily(writer, NaturalistRegistry.SHELLSTONE.get(), NaturalistRegistry.SHELLSTONE_SLAB.get(),
                NaturalistRegistry.SHELLSTONE_STAIRS.get(), NaturalistRegistry.SHELLSTONE_WALL.get(), "limestone");
        stoneFamily(writer, NaturalistRegistry.SHELLSTONE_BRICKS.get(), NaturalistRegistry.SHELLSTONE_BRICK_SLAB.get(),
                NaturalistRegistry.SHELLSTONE_BRICK_STAIRS.get(), NaturalistRegistry.SHELLSTONE_BRICK_WALL.get(), "limestone_brick");
        stoneFamily(writer, NaturalistRegistry.CUT_SHELLSTONE.get(), NaturalistRegistry.CUT_SHELLSTONE_SLAB.get(),
                NaturalistRegistry.CUT_SHELLSTONE_STAIRS.get(), NaturalistRegistry.CUT_SHELLSTONE_WALL.get(), "cut_limestone");
        stoneFamily(writer, NaturalistRegistry.SMOOTH_SHELLSTONE.get(), NaturalistRegistry.SMOOTH_SHELLSTONE_SLAB.get(),
                NaturalistRegistry.SMOOTH_SHELLSTONE_STAIRS.get(), NaturalistRegistry.SMOOTH_SHELLSTONE_WALL.get(), "smooth_limestone");

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, NaturalistRegistry.SHELLSTONE.get(), 8)
                .pattern("CS").pattern("SC")
                .define('C', Items.CALCITE)
                .define('S', NaturalistRegistry.SNAIL_SHELL.get())
                .unlockedBy("has_shell", has(NaturalistRegistry.SNAIL_SHELL.get()))
                .save(output(writer, false, "naturalist/limestone",
                        unlock("has_shell", has(NaturalistRegistry.SNAIL_SHELL.get()), id("shellstone")), true), id("shellstone"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, NaturalistRegistry.SHELLSTONE_BRICKS.get())
                .pattern("##").pattern("##")
                .define('#', NaturalistRegistry.SHELLSTONE.get())
                .unlockedBy(getHasName(NaturalistRegistry.SHELLSTONE.get()), has(NaturalistRegistry.SHELLSTONE.get()))
                .save(legacyUnlock(writer, "limestone_bricks", NaturalistRegistry.SHELLSTONE.get(), NaturalistRegistry.SHELLSTONE_BRICKS.get()), id("shellstone_bricks"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, NaturalistRegistry.CUT_SHELLSTONE.get())
                .pattern("#").pattern("#")
                .define('#', NaturalistRegistry.SHELLSTONE_SLAB.get())
                .unlockedBy(getHasName(NaturalistRegistry.SHELLSTONE_SLAB.get()), has(NaturalistRegistry.SHELLSTONE_SLAB.get()))
                .save(legacyUnlock(writer, "cut_limestone", NaturalistRegistry.SHELLSTONE_SLAB.get(), NaturalistRegistry.CUT_SHELLSTONE.get()), id("cut_shellstone"));
    }

    private void stoneFamily(Consumer<FinishedRecipe> writer, ItemLike base, ItemLike slab, ItemLike stairs, ItemLike wall, String legacyName) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, slab, 6)
                .pattern("###")
                .define('#', base)
                .unlockedBy(getHasName(base), has(base))
                .save(legacyUnlock(writer, legacyName + "_slab", base, slab), id(getItemName(slab)));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, stairs, 4)
                .pattern("#  ").pattern("## ").pattern("###")
                .define('#', base)
                .unlockedBy(getHasName(base), has(base))
                .save(legacyUnlock(writer, legacyName + "_stairs", base, stairs), id(getItemName(stairs)));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, wall, 6)
                .pattern("###").pattern("###")
                .define('#', base)
                .unlockedBy(getHasName(base), has(base))
                .save(legacyUnlock(writer, legacyName + "_wall", base, wall), id(getItemName(wall)));

        stonecut(writer, base, slab, 2);
        stonecut(writer, base, stairs, 1);
        stonecut(writer, base, wall, 1);
    }

    private void stonecut(Consumer<FinishedRecipe> writer, ItemLike base, ItemLike result, int count) {
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(base), RecipeCategory.BUILDING_BLOCKS, result, count)
                .unlockedBy(getHasName(base), has(base))
                .save(plain(writer), id(getItemName(result) + "_from_stonecutting"));
    }
    //endregion

    //region Froglass
    private void froglassPanes(Consumer<FinishedRecipe> writer) {
        froglassPane(writer, NaturalistRegistry.AZURE_FROGLASS.get(), NaturalistRegistry.AZURE_FROGLASS_PANE.get());
        froglassPane(writer, NaturalistRegistry.CRIMSON_FROGLASS.get(), NaturalistRegistry.CRIMSON_FROGLASS_PANE.get());
        froglassPane(writer, NaturalistRegistry.VERDANT_FROGLASS.get(), NaturalistRegistry.VERDANT_FROGLASS_PANE.get());
    }

    private void froglassPane(Consumer<FinishedRecipe> writer, ItemLike glass, ItemLike pane) {
        String name = getItemName(pane);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, pane, 16)
                .pattern("###").pattern("###")
                .define('#', glass)
                .unlockedBy("has_glass", has(glass))
                .save(output(writer, true, "naturalist/" + name, unlock("has_glass", has(glass), id(name)), false), id(name));
    }
    //endregion

    //region Snail shells
    private void snailShellDyes(Consumer<FinishedRecipe> writer) {
        for (DyeColor color : DyeColor.values()) {
            CompoundTag nbt = new CompoundTag();
            nbt.putInt("Color", color.getId());
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, NaturalistRegistry.SNAIL_SHELL.get())
                    .requires(NaturalistRegistry.SNAIL_SHELL.get())
                    .requires(DyeItem.byColor(color))
                    .unlockedBy(getHasName(NaturalistRegistry.SNAIL_SHELL.get()), has(NaturalistRegistry.SNAIL_SHELL.get()))
                    .save(recipe -> writer.accept(new NbtResult(recipe, nbt)), id("snail_shell_" + color.getName()));
        }
    }
    //endregion

    //region Misc
    private void misc(Consumer<FinishedRecipe> writer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, NaturalistRegistry.CAPTURE_NET.get())
                .pattern("  #").pattern(" #S").pattern("#SS")
                .define('#', Items.BAMBOO)
                .define('S', Items.STRING)
                .unlockedBy(getHasName(Items.BAMBOO), has(Items.BAMBOO))
                .save(output(writer, false, "naturalist/capture_net",
                        unlockFirst(getHasName(Items.BAMBOO), has(Items.BAMBOO), id("capture_net")), true), id("capture_net"));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, NaturalistRegistry.KNAPSACK.get())
                .pattern("H H").pattern(" H ")
                .define('H', NaturalistRegistry.HIDE.get())
                .unlockedBy(getHasName(NaturalistRegistry.HIDE.get()), has(NaturalistRegistry.HIDE.get()))
                .save(output(writer, false, "naturalist/knapsack",
                        unlockFirst(getHasName(NaturalistRegistry.HIDE.get()), has(NaturalistRegistry.HIDE.get()), id("knapsack")), true), id("knapsack"));

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, NaturalistRegistry.PLUSH_BEAR.get())
                .pattern(" F ").pattern("FWF").pattern(" F ")
                .define('F', Items.STRING)
                .define('W', ItemTags.WOOL)
                .unlockedBy("has_bear_fur", has(NaturalistRegistry.FUR.get()))
                .save(output(writer, false, "naturalist/plush_bear",
                        unlock("has_bear_fur", has(NaturalistRegistry.FUR.get()), id("plush_bear")), false), id("plush_bear"));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, NaturalistRegistry.WHISTLE.get())
                .pattern(" C ").pattern(" S ")
                .define('C', Items.COPPER_INGOT)
                .define('S', Items.STRING)
                .unlockedBy(getHasName(Items.COPPER_INGOT), has(Items.COPPER_INGOT))
                .save(output(writer, false, "naturalist/whistle",
                        unlockFirst(getHasName(Items.COPPER_INGOT), has(Items.COPPER_INGOT), id("whistle")), true), id("whistle"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, Items.SPECTRAL_ARROW, 2)
                .pattern("###").pattern("#A#").pattern("###")
                .define('#', NaturalistRegistry.GLOW_GOOP.get())
                .define('A', Items.ARROW)
                .group("naturalist:spectral_arrow")
                .unlockedBy(getHasName(NaturalistRegistry.GLOW_GOOP.get()), has(NaturalistRegistry.GLOW_GOOP.get()))
                .save(output(writer, false, "naturalist/spectral_arrow_from_glow_goop",
                        unlock(getHasName(NaturalistRegistry.GLOW_GOOP.get()), has(NaturalistRegistry.GLOW_GOOP.get()), id("spectral_arrow_from_glow_goop")), false),
                        id("spectral_arrow_from_glow_goop"));

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, Items.CAKE)
                .pattern("AAA").pattern("BEB").pattern("CCC")
                .define('A', Items.MILK_BUCKET)
                .define('B', Items.SUGAR)
                .define('C', Items.WHEAT)
                .define('E', NaturalistRegistry.DUCK_EGG.get())
                .group("naturalist:cake")
                .unlockedBy(getHasName(NaturalistRegistry.DUCK_EGG.get()), has(NaturalistRegistry.DUCK_EGG.get()))
                .save(output(writer, false, "food/cake",
                        unlock(getHasName(NaturalistRegistry.DUCK_EGG.get()), has(NaturalistRegistry.DUCK_EGG.get()), mc("cake")), false), id("cake"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.PUMPKIN_PIE)
                .requires(Items.PUMPKIN)
                .requires(Items.SUGAR)
                .requires(NaturalistRegistry.DUCK_EGG.get())
                .group("naturalist:pumpkin_pie")
                .unlockedBy(getHasName(NaturalistRegistry.DUCK_EGG.get()), has(NaturalistRegistry.DUCK_EGG.get()))
                .save(plain(writer), id("pumpkin_pie"));
    }

    private void vanillaOverrides(Consumer<FinishedRecipe> writer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, Items.CAKE)
                .pattern("AAA").pattern("BEB").pattern("CCC")
                .define('A', Items.MILK_BUCKET)
                .define('B', Items.SUGAR)
                .define('C', Items.WHEAT)
                .define('E', Items.EGG)
                .group("naturalist:cake")
                .unlockedBy(getHasName(Items.EGG), has(Items.EGG))
                .save(plain(writer), mc("cake"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.LEATHER)
                .pattern("##").pattern("##")
                .define('#', Items.RABBIT_HIDE)
                .group("naturalist:leather")
                .unlockedBy(getHasName(Items.RABBIT_HIDE), has(Items.RABBIT_HIDE))
                .save(plain(writer), mc("leather"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.PUMPKIN_PIE)
                .requires(Items.PUMPKIN)
                .requires(Items.SUGAR)
                .requires(Items.EGG)
                .group("naturalist:pumpkin_pie")
                .unlockedBy(getHasName(Items.EGG), has(Items.EGG))
                .save(plain(writer), mc("pumpkin_pie"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, Items.SPECTRAL_ARROW, 2)
                .pattern(" # ").pattern("#X#").pattern(" # ")
                .define('#', Items.GLOWSTONE_DUST)
                .define('X', Items.ARROW)
                .group("naturalist:spectral_arrow")
                .unlockedBy(getHasName(Items.GLOWSTONE_DUST), has(Items.GLOWSTONE_DUST))
                .save(plain(writer), mc("spectral_arrow"));
    }

    private void netCatches(Consumer<FinishedRecipe> writer) {
        writer.accept(new NetCatch(id("catch_bee"), EntityType.BEE, Optional.empty(), Items.DIRT));
        writer.accept(new NetCatch(id("catch_cod"), EntityType.COD, Optional.of(Ingredient.of(Items.WATER_BUCKET)), Items.COD_BUCKET));
    }
    //endregion

    //region Output
    private static ResourceLocation id(String path) {
        return Naturalist.location(path);
    }

    private static ResourceLocation mc(String path) {
        return new ResourceLocation(path);
    }

    private static Consumer<FinishedRecipe> plain(Consumer<FinishedRecipe> writer) {
        return recipe -> writer.accept(new Result(recipe, false, null, null));
    }

    private static Consumer<FinishedRecipe> output(Consumer<FinishedRecipe> writer, boolean bookFields, String advancementPath, Advancement.Builder advancement, boolean telemetryField) {
        JsonObject json = advancement.serializeToJson();
        if (!telemetryField) {
            json.remove("sends_telemetry_event");
        }
        return recipe -> writer.accept(new Result(recipe, bookFields, id("recipes/" + advancementPath), json));
    }

    private static Consumer<FinishedRecipe> legacyUnlock(Consumer<FinishedRecipe> writer, String advancementName, ItemLike trigger, ItemLike result) {
        return output(writer, false, "naturalist/" + advancementName, unlock(getHasName(trigger), has(trigger), id(getItemName(result))), true);
    }

    private static Advancement.Builder unlock(String criterion, CriterionTriggerInstance trigger, ResourceLocation recipe, ResourceLocation... extraRewards) {
        AdvancementRewards.Builder rewards = AdvancementRewards.Builder.recipe(recipe);
        for (ResourceLocation extra : extraRewards) {
            rewards.addRecipe(extra);
        }
        return Advancement.Builder.recipeAdvancement()
                .parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT)
                .addCriterion(criterion, trigger)
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipe))
                .rewards(rewards)
                .requirements(RequirementsStrategy.OR);
    }

    private static Advancement.Builder unlockFirst(String criterion, CriterionTriggerInstance trigger, ResourceLocation recipe) {
        return unlock(criterion, trigger, recipe).requirements(new String[][]{{"has_the_recipe", criterion}});
    }

    private static Advancement.Builder cookingUnlock(String criterion, CriterionTriggerInstance trigger, String name) {
        return unlock(criterion, trigger, id(name), id(name + "_from_smoking"), id(name + "_from_campfire_cooking"));
    }

    private record Result(FinishedRecipe recipe, boolean bookFields, @Nullable ResourceLocation advancementId,
                          @Nullable JsonObject advancement) implements FinishedRecipe {
        @Override
        public void serializeRecipeData(JsonObject json) {
            this.recipe.serializeRecipeData(json);
            if (!this.bookFields) {
                json.remove("category");
                json.remove("show_notification");
            }
        }

        @Override
        public ResourceLocation getId() {
            return this.recipe.getId();
        }

        @Override
        public RecipeSerializer<?> getType() {
            return this.recipe.getType();
        }

        @Override
        public @Nullable JsonObject serializeAdvancement() {
            return this.advancement;
        }

        @Override
        public @Nullable ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }

    private record NbtResult(FinishedRecipe recipe, CompoundTag nbt) implements FinishedRecipe {
        @Override
        public void serializeRecipeData(JsonObject json) {
            this.recipe.serializeRecipeData(json);
            json.getAsJsonObject("result").add("nbt", NbtOps.INSTANCE.convertTo(JsonOps.INSTANCE, this.nbt));
        }

        @Override
        public ResourceLocation getId() {
            return this.recipe.getId();
        }

        @Override
        public RecipeSerializer<?> getType() {
            return NaturalistRecipes.SHAPELESS_NBT_SERIALIZER.get();
        }

        @Override
        public @Nullable JsonObject serializeAdvancement() {
            return null;
        }

        @Override
        public @Nullable ResourceLocation getAdvancementId() {
            return null;
        }
    }

    private record NetCatch(ResourceLocation id, EntityType<?> entityType, Optional<Ingredient> ingredient,
                            Item result) implements FinishedRecipe {
        @Override
        public void serializeRecipeData(JsonObject json) {
            json.addProperty("entity_type", BuiltInRegistries.ENTITY_TYPE.getKey(this.entityType).toString());
            this.ingredient.ifPresent(ingredient -> json.add("ingredient", ingredient.toJson()));
            JsonObject result = new JsonObject();
            result.addProperty("item", BuiltInRegistries.ITEM.getKey(this.result).toString());
            json.add("result", result);
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return NaturalistRecipes.BUG_NET_SERIALIZER.get();
        }

        @Override
        public @Nullable JsonObject serializeAdvancement() {
            return null;
        }

        @Override
        public @Nullable ResourceLocation getAdvancementId() {
            return null;
        }
    }
    //endregion
}
