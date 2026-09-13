package com.crispytwig.naturalist.world.entity.animal.elephant;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.registry.NaturalistEntityTypes;
import com.crispytwig.naturalist.registry.NaturalistSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Mammoth extends Elephant {
    //region Data
    public Mammoth(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Animal.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 120.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.ATTACK_DAMAGE, 14.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 1.5D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.85D)
                .add(Attributes.FOLLOW_RANGE, 15.0D)
                .add(Attributes.STEP_HEIGHT, 1.0D);
    }

    @Override
    public Identifier getFallbackVariantTexture() {
        return Naturalist.location("textures/entity/mammoth/mammoth.png");
    }
    //endregion

    //region Spawning
    public static boolean checkMammothSpawnRules(EntityType<? extends Animal> entityType, LevelAccessor level, EntitySpawnReason spawnType, BlockPos pos, RandomSource random) {
        BlockState state = level.getBlockState(pos.below());
        return (state.is(BlockTags.DIRT) || state.is(BlockTags.SNOW) || state.is(BlockTags.ICE)) && isBrightEnoughToSpawn(level, pos);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverLevel, @NotNull AgeableMob ageableMob) {
        Mammoth baby = NaturalistEntityTypes.MAMMOTH.get().create(serverLevel, EntitySpawnReason.BREEDING);
        if (baby != null) {
            baby.setVariantString(this.getOffspringVariantId(ageableMob, this.random));
        }
        return baby;
    }
    //endregion

    //region Behavior
    @Override
    protected @NotNull Vec3 getPassengerAttachmentPoint(@NotNull Entity entity, @NotNull EntityDimensions dimensions, float partialTick) {
        return new Vec3(0.0, this.getBbHeight() * 1.05, -0.1);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return this.isBaby() ? NaturalistSoundEvents.MAMMOTH_AMBIENT_BABY.get() : NaturalistSoundEvents.MAMMOTH_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return this.isBaby() ? NaturalistSoundEvents.MAMMOTH_HURT_BABY.get() : NaturalistSoundEvents.MAMMOTH_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return this.isBaby() ? NaturalistSoundEvents.MAMMOTH_DEATH_BABY.get() : NaturalistSoundEvents.MAMMOTH_DEATH.get();
    }
    //endregion
}
