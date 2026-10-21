package com.crispytwig.naturalist.world.level;

import com.crispytwig.naturalist.world.entity.MobPart;

import org.jetbrains.annotations.Nullable;

public interface MultipartLevel {
    void naturalist$addMobPart(MobPart part);

    void naturalist$removeMobPart(MobPart part);

    @Nullable
    MobPart naturalist$getMobPart(int id);
}
