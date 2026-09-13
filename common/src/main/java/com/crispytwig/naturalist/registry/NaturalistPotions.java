package com.crispytwig.naturalist.registry;

import com.crispytwig.naturalist.Naturalist;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import com.crispytwig.naturalist.platform.registry.DeferredHolder;
import com.crispytwig.naturalist.platform.registry.DeferredRegister;

public class NaturalistPotions {
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(BuiltInRegistries.POTION, Naturalist.MOD_ID);

    public static final DeferredHolder<Potion, Potion> FOREST_DASHER = POTIONS.register("forest_dasher",
            () -> new Potion("forest_dasher", new MobEffectInstance(MobEffects.SPEED, 400, 1), new MobEffectInstance(MobEffects.WEAKNESS, 400, 0)));

    public static final DeferredHolder<Potion, Potion> LONG_FOREST_DASHER = POTIONS.register("long_forest_dasher",
            () -> new Potion("forest_dasher", new MobEffectInstance(MobEffects.SPEED, 800, 1), new MobEffectInstance(MobEffects.WEAKNESS, 800, 0)));

    public static final DeferredHolder<Potion, Potion> STRONG_FOREST_DASHER = POTIONS.register("strong_forest_dasher",
            () -> new Potion("forest_dasher", new MobEffectInstance(MobEffects.SPEED, 400, 2), new MobEffectInstance(MobEffects.WEAKNESS, 400, 1)));

    public static final DeferredHolder<Potion, Potion> ANTIVENOM = POTIONS.register("antivenom",
            () -> new Potion("antivenom", new MobEffectInstance(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(NaturalistMobEffects.ANTIVENOM.get()), 1)));
}
