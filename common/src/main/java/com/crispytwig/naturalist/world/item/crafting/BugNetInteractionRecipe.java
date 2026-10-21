package com.crispytwig.naturalist.world.item.crafting;

import com.crispytwig.naturalist.registry.NaturalistRecipes;
import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record BugNetInteractionRecipe(ResourceLocation id, EntityType<?> entityType, Optional<Ingredient> ingredient, ItemStack dropStack) implements Recipe<Container> {
    public int findIngredientSlot(@NotNull Player player) {
        if (this.ingredient.isEmpty()) {
            return -1;
        }
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            if (this.ingredient.get().test(player.getInventory().getItem(i))) {
                return i;
            }
        }
        return -1;
    }

    public boolean hasIngredient(@NotNull Player player) {
        return this.ingredient.isEmpty() || this.findIngredientSlot(player) >= 0;
    }

    public void consumeIngredient(@NotNull Player player) {
        if (this.ingredient.isEmpty() || player.getAbilities().instabuild) {
            return;
        }
        int slot = this.findIngredientSlot(player);
        if (slot >= 0) {
            player.getInventory().removeItem(slot, 1);
        }
    }

    @Override
    public boolean matches(@NotNull Container container, @NotNull Level level) {
        return false;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull Container container, @NotNull RegistryAccess registryAccess) {
        return this.dropStack.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
        return this.dropStack;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return this.id;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return NaturalistRecipes.BUG_NET_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return NaturalistRecipes.BUG_NET.get();
    }

    public static class Serializer implements RecipeSerializer<BugNetInteractionRecipe> {
        @Override
        public @NotNull BugNetInteractionRecipe fromJson(@NotNull ResourceLocation id, @NotNull JsonObject json) {
            ResourceLocation typeId = new ResourceLocation(GsonHelper.getAsString(json, "entity_type"));
            EntityType<?> entityType = BuiltInRegistries.ENTITY_TYPE.getOptional(typeId)
                    .orElseThrow(() -> new IllegalArgumentException("Unknown entity type " + typeId + " in recipe " + id));
            Optional<Ingredient> ingredient = json.has("ingredient")
                    ? Optional.of(Ingredient.fromJson(json.get("ingredient")))
                    : Optional.empty();
            ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            return new BugNetInteractionRecipe(id, entityType, ingredient, result);
        }

        @Override
        public @NotNull BugNetInteractionRecipe fromNetwork(@NotNull ResourceLocation id, @NotNull FriendlyByteBuf buf) {
            EntityType<?> entityType = BuiltInRegistries.ENTITY_TYPE.get(buf.readResourceLocation());
            Optional<Ingredient> ingredient = buf.readBoolean() ? Optional.of(Ingredient.fromNetwork(buf)) : Optional.empty();
            return new BugNetInteractionRecipe(id, entityType, ingredient, buf.readItem());
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buf, @NotNull BugNetInteractionRecipe recipe) {
            buf.writeResourceLocation(BuiltInRegistries.ENTITY_TYPE.getKey(recipe.entityType()));
            buf.writeBoolean(recipe.ingredient().isPresent());
            recipe.ingredient().ifPresent(ingredient -> ingredient.toNetwork(buf));
            buf.writeItem(recipe.dropStack());
        }
    }
}
