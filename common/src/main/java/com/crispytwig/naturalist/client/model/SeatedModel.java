package com.crispytwig.naturalist.client.model;

import com.mojang.blaze3d.vertex.PoseStack;

public interface SeatedModel {
    void translateToSeat(PoseStack poseStack);

    default float seatHeight() {
        return 0.5F;
    }

    default float seatZRot() {
        return 0.0F;
    }
}
