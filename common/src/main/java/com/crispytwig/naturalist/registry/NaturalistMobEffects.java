package com.crispytwig.naturalist.registry;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.platform.registry.DeferredHolder;
import com.crispytwig.naturalist.platform.registry.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import com.crispytwig.naturalist.world.effect.AntivenomMobEffect;

public class NaturalistMobEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, Naturalist.MOD_ID);

    public static final DeferredHolder<MobEffect, MobEffect> ANTIVENOM = MOB_EFFECTS.register("antivenom", () -> new AntivenomMobEffect(MobEffectCategory.BENEFICIAL, 0x6FD6B2));
}
