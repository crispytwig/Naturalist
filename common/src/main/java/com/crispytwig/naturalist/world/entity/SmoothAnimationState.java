package com.crispytwig.naturalist.world.entity;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;

public class SmoothAnimationState extends AnimationState {
    public static final float ACTIVE_THRESHOLD = 0.05F;
    private static final float STOP_THRESHOLD = 0.001F;
    private static final float DEFAULT_LERP_SPEED = 0.5F;
    private static final float POSE_LERP_SPEED = 0.25F;
    private static final float INSTANT_LERP_SPEED = 1.0F;

    private final float lerpSpeed;
    private float factorO;
    private float factor;

    public SmoothAnimationState() {
        this(DEFAULT_LERP_SPEED);
    }

    public SmoothAnimationState(float lerpSpeed) {
        this.lerpSpeed = lerpSpeed;
    }

    public static SmoothAnimationState pose() {
        return new SmoothAnimationState(POSE_LERP_SPEED);
    }

    public static SmoothAnimationState instant() {
        return new SmoothAnimationState(INSTANT_LERP_SPEED);
    }

    @Override
    public void animateWhen(boolean condition, int tickCount) {
        this.factorO = this.factor;
        this.factor = Mth.clamp(this.factor + ((condition ? 1.0F : 0.0F) - this.factor) * this.lerpSpeed, 0.0F, 1.0F);
        if (condition) {
            this.startIfStopped(tickCount);
        } else if (this.factor < STOP_THRESHOLD) {
            this.stop();
        }
    }

    public float factor(float partialTick) {
        return Mth.lerp(partialTick, this.factorO, this.factor);
    }

}
