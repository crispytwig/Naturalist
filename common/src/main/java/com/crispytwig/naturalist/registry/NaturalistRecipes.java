package com.crispytwig.naturalist.registry;

import com.crispytwig.naturalist.world.item.crafting.NbtShapelessRecipe;
import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.world.item.crafting.BugNetInteractionRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import com.crispytwig.naturalist.platform.registry.DeferredHolder;
import com.crispytwig.naturalist.platform.registry.DeferredRegister;

public class NaturalistRecipes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, Naturalist.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, Naturalist.MOD_ID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<BugNetInteractionRecipe>> BUG_NET = RECIPE_TYPES.register("net",
            () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return "naturalist:net";
                }
            });

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> BUG_NET_SERIALIZER = RECIPE_SERIALIZERS.register("net",
            BugNetInteractionRecipe.Serializer::new);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> SHAPELESS_NBT_SERIALIZER = RECIPE_SERIALIZERS.register("crafting_shapeless_nbt",
            NbtShapelessRecipe.Serializer::new);
}
