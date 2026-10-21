package com.crispytwig.naturalist.world.item.crafting;

import com.crispytwig.naturalist.registry.NaturalistRecipes;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import org.jetbrains.annotations.NotNull;

public class NbtShapelessRecipe extends ShapelessRecipe {
    public NbtShapelessRecipe(ShapelessRecipe base) {
        super(base.getId(), base.getGroup(), base.category(), base.getResultItem(null), base.getIngredients());
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return NaturalistRecipes.SHAPELESS_NBT_SERIALIZER.get();
    }

    static CompoundTag parseNbt(JsonElement element) {
        try {
            return TagParser.parseTag(element.isJsonPrimitive() ? element.getAsString() : element.toString());
        } catch (CommandSyntaxException e) {
            throw new JsonParseException("Invalid result nbt: " + e.getMessage());
        }
    }

    public static class Serializer implements RecipeSerializer<NbtShapelessRecipe> {
        @Override
        public @NotNull NbtShapelessRecipe fromJson(@NotNull ResourceLocation id, @NotNull JsonObject json) {
            ShapelessRecipe base = RecipeSerializer.SHAPELESS_RECIPE.fromJson(id, json);
            ShapelessRecipe withNbt = base;
            JsonObject result = GsonHelper.getAsJsonObject(json, "result");
            if (result.has("nbt")) {
                ItemStack stack = base.getResultItem(null).copy();
                stack.getOrCreateTag().merge(parseNbt(result.get("nbt")));
                withNbt = new ShapelessRecipe(id, base.getGroup(), base.category(), stack, base.getIngredients());
            }
            return new NbtShapelessRecipe(withNbt);
        }

        @Override
        public @NotNull NbtShapelessRecipe fromNetwork(@NotNull ResourceLocation id, @NotNull FriendlyByteBuf buf) {
            return new NbtShapelessRecipe(RecipeSerializer.SHAPELESS_RECIPE.fromNetwork(id, buf));
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buf, @NotNull NbtShapelessRecipe recipe) {
            RecipeSerializer.SHAPELESS_RECIPE.toNetwork(buf, recipe);
        }
    }
}
