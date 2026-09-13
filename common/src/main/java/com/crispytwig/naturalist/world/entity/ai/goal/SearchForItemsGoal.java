package com.crispytwig.naturalist.world.entity.ai.goal;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class SearchForItemsGoal extends Goal {
    private final PathfinderMob mob;
    private final double speedModifier;
    private final double horizontalSearchRange;
    private final double verticalSearchRange;
    private final Predicate<ItemStack> itemPredicate;

    public SearchForItemsGoal(PathfinderMob mob, double speedModifier, Predicate<ItemStack> itemPredicate, double horizontalSearchRange, double verticalSearchRange) {
        this.setFlags(EnumSet.of(Flag.MOVE));
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.itemPredicate = itemPredicate;
        this.horizontalSearchRange = horizontalSearchRange;
        this.verticalSearchRange = verticalSearchRange;
    }

    public SearchForItemsGoal(PathfinderMob mob, double speedModifier, Ingredient ingredient, double horizontalSearchRange, double verticalSearchRange) {
        this(mob, speedModifier, (Predicate<ItemStack>) ingredient::test, horizontalSearchRange, verticalSearchRange);
    }

    private List<ItemEntity> findItems() {
        return mob.level().getEntitiesOfClass(ItemEntity.class, mob.getBoundingBox().inflate(horizontalSearchRange, verticalSearchRange, horizontalSearchRange), itemEntity -> itemPredicate.test(itemEntity.getItem()));
    }

    @Override
    public boolean canUse() {
        if (mob.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
            List<ItemEntity> list = this.findItems();
            return !list.isEmpty() && mob.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty();
        }
        return false;
    }

    @Override
    public void tick() {
        List<ItemEntity> list = this.findItems();
        if (mob.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty() && !list.isEmpty()) {
            mob.getNavigation().moveTo(list.getFirst(), speedModifier);
        }

    }

    @Override
    public void start() {
        List<ItemEntity> list = this.findItems();
        if (!list.isEmpty()) {
            mob.getNavigation().moveTo(list.getFirst(), speedModifier);
        }

    }
}
