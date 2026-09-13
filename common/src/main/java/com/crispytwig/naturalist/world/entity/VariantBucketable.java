package com.crispytwig.naturalist.world.entity;

import com.crispytwig.naturalist.world.entity.variant.DataDrivenVariantAnimal;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Bucketable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public interface VariantBucketable extends Bucketable, DataDrivenVariantAnimal {
    @Override
    default void saveToBucketTag(ItemStack stack) {
        Bucketable.saveDefaultDataToBucketTag((Mob & Bucketable) this, stack);
        CustomData.update(DataComponents.BUCKET_ENTITY_DATA, stack, this::saveVariant);
        CompoundTag custom = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        this.saveVariant(custom);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(custom));
    }

    @Override
    default void loadFromBucketTag(CompoundTag tag) {
        Bucketable.loadDefaultDataFromBucketTag((Mob & Bucketable) this, tag);
        this.loadVariant(tag);
    }
}
