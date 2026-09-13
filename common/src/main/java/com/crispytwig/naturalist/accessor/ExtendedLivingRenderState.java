package com.crispytwig.naturalist.accessor;

public interface ExtendedLivingRenderState {
    boolean naturalist$shoulderFlap();

    void naturalist$setShoulderFlap(boolean flap);

    float naturalist$leanPitch();

    float naturalist$leanRoll();

    void naturalist$setLean(float pitch, float roll);

    boolean naturalist$isDiggingOutMole();

    void naturalist$setDiggingOutMole(boolean digging);
}
