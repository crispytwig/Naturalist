package com.crispytwig.naturalist.registry;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.platform.registry.DeferredHolder;
import com.crispytwig.naturalist.platform.registry.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.decoration.PaintingVariant;

public class NaturalistPaintings {
    public static final DeferredRegister<PaintingVariant> PAINTING_VARIANTS = DeferredRegister.create(BuiltInRegistries.PAINTING_VARIANT, Naturalist.MOD_ID);

    public static final DeferredHolder<PaintingVariant, PaintingVariant> BEAR_NECESSITIES = PAINTING_VARIANTS.register("bear_necessities", () -> new PaintingVariant(64, 64));
}
