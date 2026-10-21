package com.crispytwig.naturalist.registry;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.platform.registry.DeferredHolder;
import com.crispytwig.naturalist.platform.registry.DeferredRegister;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;

@SuppressWarnings("unused")
public class NaturalistParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE.key(), Naturalist.MOD_ID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> CAPTURE_NET_SWING = PARTICLE_TYPES.register("capture_net_swing", () -> new SimpleParticleType(false) {});
}
