package com.crispytwig.naturalist.world.entity.ai.goal;

import com.crispytwig.naturalist.world.entity.FollowingPet;
import com.crispytwig.naturalist.world.entity.IKMount;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;

public class PetFollowOwnerGoal extends FollowOwnerGoal {
    private final TamableAnimal mob;
    private final FollowingPet pet;
    private final double speedModifier;
    private int recalcCooldown;

    public PetFollowOwnerGoal(TamableAnimal mob, double speedModifier, float startDistance, float stopDistance) {
        super(mob, speedModifier, startDistance, stopDistance);
        this.mob = mob;
        this.pet = (FollowingPet) mob;
        this.speedModifier = speedModifier;
    }

    @Override
    public boolean canUse() {
        return this.pet.isFollowingOwner() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return this.pet.isFollowingOwner() && super.canContinueToUse();
    }

    @Override
    public void tick() {
        if (!(this.mob instanceof IKMount)) {
            super.tick();
            return;
        }
        LivingEntity owner = this.mob.getOwner();
        if (owner == null) {
            return;
        }
        this.mob.getLookControl().setLookAt(owner, 10.0F, (float) this.mob.getMaxHeadXRot());
        if (--this.recalcCooldown <= 0) {
            this.recalcCooldown = this.adjustedTickDelay(10);
            if (!this.mob.isLeashed() && !this.mob.isPassenger()) {
                this.mob.getNavigation().moveTo(owner, this.speedModifier);
            }
        }
    }
}
