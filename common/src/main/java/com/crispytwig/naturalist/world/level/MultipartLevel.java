package com.crispytwig.naturalist.world.level;

import org.jetbrains.annotations.Nullable;
import com.crispytwig.naturalist.world.entity.MobPart;

public interface MultipartLevel {
    void naturalist$addMobPart(MobPart part);

    void naturalist$removeMobPart(MobPart part);

    @Nullable
    MobPart naturalist$getMobPart(int id);
}
