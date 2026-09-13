package com.crispytwig.naturalist.mixin;

import com.crispytwig.naturalist.accessor.ExtendedLivingRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntityRenderState.class)
public class LivingEntityRenderStateMixin implements ExtendedLivingRenderState {
    @Unique
    private boolean naturalist$shoulderFlap;
    @Unique
    private float naturalist$leanPitch;
    @Unique
    private float naturalist$leanRoll;
    @Unique
    private boolean naturalist$diggingOutMole;

    @Override
    public boolean naturalist$shoulderFlap() {
        return this.naturalist$shoulderFlap;
    }

    @Override
    public void naturalist$setShoulderFlap(boolean flap) {
        this.naturalist$shoulderFlap = flap;
    }

    @Override
    public float naturalist$leanPitch() {
        return this.naturalist$leanPitch;
    }

    @Override
    public float naturalist$leanRoll() {
        return this.naturalist$leanRoll;
    }

    @Override
    public void naturalist$setLean(float pitch, float roll) {
        this.naturalist$leanPitch = pitch;
        this.naturalist$leanRoll = roll;
    }

    @Override
    public boolean naturalist$isDiggingOutMole() {
        return this.naturalist$diggingOutMole;
    }

    @Override
    public void naturalist$setDiggingOutMole(boolean digging) {
        this.naturalist$diggingOutMole = digging;
    }
}
