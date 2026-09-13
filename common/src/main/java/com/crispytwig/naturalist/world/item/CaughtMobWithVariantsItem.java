package com.crispytwig.naturalist.world.item;

import net.minecraft.sounds.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.material.*;

import java.util.function.*;

public class CaughtMobWithVariantsItem extends CaughtMobItem {
    public CaughtMobWithVariantsItem(Supplier<? extends EntityType<?>> entitySupplier, Supplier<? extends Fluid> fluidSupplier, Supplier<? extends SoundEvent> soundSupplier, String tooltipPrefix, String[] variantNames, Properties properties) {
        super(entitySupplier, fluidSupplier, soundSupplier, tooltipPrefix, variantNames, properties);
    }
}
