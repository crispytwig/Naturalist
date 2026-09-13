package com.crispytwig.naturalist.mixin;

import com.crispytwig.naturalist.world.entity.ai.goal.AttackEggGoal;
import com.crispytwig.naturalist.registry.NaturalistRegistry;
import com.crispytwig.naturalist.registry.NaturalistSoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Zombie.class)
public class ZombieMixin extends Monster {
    protected ZombieMixin(@NotNull EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(at = @At("HEAD"), method = "registerGoals")
    @SuppressWarnings("unused")
    private void registerGoals(CallbackInfo info) {
        this.goalSelector.addGoal(2, new AttackEggGoal(NaturalistRegistry.ALLIGATOR_EGG.get(), NaturalistSoundEvents.GATOR_EGG_BREAK.get(), this, 1.0D, 3));
        this.goalSelector.addGoal(2, new AttackEggGoal(NaturalistRegistry.TORTOISE_EGG.get(), NaturalistSoundEvents.TORTOISE_EGG_BREAK.get(), this, 1.0D, 3));
        this.goalSelector.addGoal(2, new AttackEggGoal(NaturalistRegistry.OSTRICH_EGG.get(), NaturalistSoundEvents.OSTRICH_EGG_BREAK.get(), this, 1.0D, 3));
    }
}
