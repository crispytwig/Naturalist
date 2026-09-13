package com.crispytwig.naturalist.world.entity.animal.bear;

import net.minecraft.world.entity.animal.Animal;
import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.registry.NaturalistSoundEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlackBear extends Bear {
    //region Behavior
    public BlackBear(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes().add(Attributes.MAX_HEALTH, 24.0D)
                .add(Attributes.FOLLOW_RANGE, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5D)
                .add(Attributes.STEP_HEIGHT, 1.0D);
    }

    @Override
    public Identifier getFallbackVariantTexture() {
        return Naturalist.location("textures/entity/black_bear/black_bear.png");
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return this.isSleeping() ? NaturalistSoundEvents.BLACK_BEAR_SLEEP.get() : NaturalistSoundEvents.BLACK_BEAR_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return NaturalistSoundEvents.BLACK_BEAR_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return NaturalistSoundEvents.BLACK_BEAR_DEATH.get();
    }

    @Override
    protected SoundEvent getEatSound() {
        return NaturalistSoundEvents.BLACK_BEAR_EAT.get();
    }

    @Override
    protected SoundEvent getSniffSound() {
        return NaturalistSoundEvents.BLACK_BEAR_SNIFF.get();
    }

    @Nullable
    @Override
    protected SoundEvent getAttackSound() {
        return NaturalistSoundEvents.BLACK_BEAR_ATTACK.get();
    }

    @Override
    protected void playEatSound() {
    }
    //endregion
}
