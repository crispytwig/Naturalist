package com.crispytwig.naturalist.mixin;

import com.crispytwig.naturalist.world.entity.ai.goal.WolfDigOutMoleGoal;
import com.crispytwig.naturalist.world.entity.WolfMoleDigging;
import com.crispytwig.naturalist.world.entity.animal.mole.Mole;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Wolf.class)
public abstract class WolfMixin extends TamableAnimal implements WolfMoleDigging {
    @Unique
    private static final byte naturalist$startDiggingOutMole = 101;
    @Unique
    private static final byte naturalist$stopDiggingOutMole = 102;

    @Unique
    private boolean naturalist$diggingOutMole;

    protected WolfMixin(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(at = @At("TAIL"), method = "registerGoals")
    @SuppressWarnings("unused")
    private void naturalist$registerGoals(CallbackInfo ci) {
        this.goalSelector.addGoal(3, new WolfDigOutMoleGoal((Wolf) (Object) this));
        this.targetSelector.addGoal(4, new NonTameRandomTargetGoal<>(this, Mole.class, false, null));
    }

    @Inject(at = @At("HEAD"), method = "handleEntityEvent", cancellable = true)
    @SuppressWarnings("unused")
    private void naturalist$handleEntityEvent(byte id, CallbackInfo ci) {
        if (id == naturalist$startDiggingOutMole || id == naturalist$stopDiggingOutMole) {
            this.naturalist$diggingOutMole = id == naturalist$startDiggingOutMole;
            ci.cancel();
        }
    }

    @Override
    public boolean naturalist$isDiggingOutMole() {
        return this.naturalist$diggingOutMole;
    }

    @Override
    public void naturalist$setDiggingOutMole(boolean digging) {
        if (this.naturalist$diggingOutMole == digging) {
            return;
        }
        this.naturalist$diggingOutMole = digging;
        if (!this.level().isClientSide()) {
            this.level().broadcastEntityEvent(this, digging ? naturalist$startDiggingOutMole : naturalist$stopDiggingOutMole);
        }
    }
}
