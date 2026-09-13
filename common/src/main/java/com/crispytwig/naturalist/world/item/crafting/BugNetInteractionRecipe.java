package com.crispytwig.naturalist.world.item.crafting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import com.crispytwig.naturalist.registry.NaturalistRecipes;

import java.util.Optional;

@SuppressWarnings("unused")
public record BugNetInteractionRecipe(EntityType<?> entityType, Optional<Ingredient> ingredient, ItemStackTemplate result) implements Recipe<RecipeInput> {
    public static final MapCodec<BugNetInteractionRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity_type").forGetter(BugNetInteractionRecipe::entityType),
                    Ingredient.CODEC.optionalFieldOf("ingredient").forGetter(BugNetInteractionRecipe::ingredient),
                    ItemStackTemplate.CODEC.fieldOf("result").forGetter(BugNetInteractionRecipe::result)
            ).apply(instance, BugNetInteractionRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, BugNetInteractionRecipe> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.registry(Registries.ENTITY_TYPE), BugNetInteractionRecipe::entityType,
            Ingredient.OPTIONAL_CONTENTS_STREAM_CODEC, BugNetInteractionRecipe::ingredient,
            ItemStackTemplate.STREAM_CODEC, BugNetInteractionRecipe::result,
            BugNetInteractionRecipe::new
    );

    public static final RecipeSerializer<BugNetInteractionRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);

    public ItemStack dropStack() {
        return this.result.create();
    }

    public int findIngredientSlot(@NotNull Player player) {
        if (ingredient.isEmpty()) {
            return -1;
        }
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            if (ingredient.get().test(player.getInventory().getItem(i))) {
                return i;
            }
        }
        return -1;
    }

    public boolean hasIngredient(@NotNull Player player) {
        return ingredient.isEmpty() || findIngredientSlot(player) >= 0;
    }

    public void consumeIngredient(@NotNull Player player) {
        if (ingredient.isEmpty() || player.getAbilities().instabuild) {
            return;
        }
        int slot = findIngredientSlot(player);
        if (slot >= 0) {
            player.getInventory().removeItem(slot, 1);
        }
    }

    @Override
    public boolean matches(@NotNull RecipeInput input, @NotNull Level level) {
        return false;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull RecipeInput input) {
        return this.dropStack();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public @NotNull String group() {
        return "";
    }

    @Override
    public @NotNull PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public @NotNull RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    @Override
    public @NotNull RecipeSerializer<BugNetInteractionRecipe> getSerializer() {
        return NaturalistRecipes.BUG_NET_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<BugNetInteractionRecipe> getType() {
        return NaturalistRecipes.BUG_NET.get();
    }
}
