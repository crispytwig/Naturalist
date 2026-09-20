package com.crispytwig.naturalist.mixin;

import com.crispytwig.naturalist.server.entity.ai.goal.WolfDigOutMoleGoal;
import com.crispytwig.naturalist.server.entity.base.WolfMoleDigging;
import com.crispytwig.naturalist.server.entity.mob.Deer;
import com.crispytwig.naturalist.server.entity.mob.Mole;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Wolf.class)
public abstract class WolfMixin extends TamableAnimal implements WolfMoleDigging {
    @Unique
    private static final EntityDataAccessor<Boolean> naturalist$DIGGING_OUT_MOLE =
            SynchedEntityData.defineId(Wolf.class, EntityDataSerializers.BOOLEAN);

    protected WolfMixin(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(at = @At("TAIL"), method = "defineSynchedData")
    @SuppressWarnings("unused")
    private void naturalist$defineSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(naturalist$DIGGING_OUT_MOLE, false);
    }

    @Inject(at = @At("TAIL"), method = "registerGoals")
    @SuppressWarnings("unused")
    private void naturalist$registerGoals(CallbackInfo ci) {
        this.goalSelector.addGoal(3, new WolfDigOutMoleGoal((Wolf) (Object) this));
        this.targetSelector.addGoal(4, new NonTameRandomTargetGoal<>(this, Mole.class, false, null));
        this.targetSelector.addGoal(4, new NonTameRandomTargetGoal<>(this, Deer.class, false, null));
    }

    @Override
    public boolean naturalist$isDiggingOutMole() {
        return this.entityData.get(naturalist$DIGGING_OUT_MOLE);
    }

    @Override
    public void naturalist$setDiggingOutMole(boolean digging) {
        this.entityData.set(naturalist$DIGGING_OUT_MOLE, digging);
    }
}
