package com.crispytwig.naturalist.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.TagValueInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TagValueInput.class)
public interface TagValueInputAccessor {
    @Accessor("input")
    CompoundTag naturalist$getInput();
}
