package com.crispytwig.naturalist.world.entity;

import com.crispytwig.naturalist.world.item.ItemHelper;
import com.crispytwig.naturalist.world.entity.variant.DataDrivenVariantAnimal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.item.ItemStack;

public interface VariantBucketable extends Bucketable, DataDrivenVariantAnimal {
    @Override
    default void saveToBucketTag(ItemStack stack) {
        Bucketable.saveDefaultDataToBucketTag((Mob & Bucketable) this, stack);
        ItemHelper.updateTag(stack, this::saveVariant);
        CompoundTag custom = ItemHelper.copyTag(stack);
        this.saveVariant(custom);
        ItemHelper.mergeTag(stack, custom);
    }

    @Override
    default void loadFromBucketTag(CompoundTag tag) {
        Bucketable.loadDefaultDataFromBucketTag((Mob & Bucketable) this, tag);
        this.loadVariant(tag);
    }
}
