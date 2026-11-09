package com.crispytwig.naturalist.common.entity;

public interface MultipartMob {
    MobPart[] getMobParts();

    float getSegmentPitchOffset(int index, float partialTick);

    float getSegmentYawOffset(int index, float partialTick);
}
