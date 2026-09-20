package com.crispytwig.naturalist.world.entity.animal.scorpion;

import com.crispytwig.naturalist.tags.NaturalistBlockTags;
import com.crispytwig.naturalist.tags.NaturalistEntityTypeTags;
import com.crispytwig.naturalist.registry.NaturalistRegistry;
import com.crispytwig.naturalist.registry.NaturalistSoundEvents;
import com.crispytwig.naturalist.world.entity.animal.NaturalistAnimal;
import com.crispytwig.naturalist.world.entity.NocturnalHostile;
import com.crispytwig.naturalist.world.entity.AnimationTimer;
import com.crispytwig.naturalist.world.entity.AnimationSoundPlayer;
import com.crispytwig.naturalist.world.entity.AnimationSoundTrack;
import com.crispytwig.naturalist.world.entity.SmoothAnimationState;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Scorpion extends Animal implements NocturnalHostile {
    //region Data
    private final AnimationTimer attackAnimTimer = new AnimationTimer(15);

    public final SmoothAnimationState idleAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState walkAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState runAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState attackAnimationState = SmoothAnimationState.instant();

    private static final AnimationSoundTrack.SoundParam STEP_VOLUME = entity -> 0.63F;
    private static final AnimationSoundTrack.SoundParam SOUND_VOLUME = entity -> ((Scorpion) entity).getSoundVolume();
    private static final AnimationSoundTrack.SoundParam VOICE_PITCH = LivingEntity::getVoicePitch;

    private static final AnimationSoundTrack WALK_SOUNDS = AnimationSoundTrack.builder(0.5F, true)
            .at(0.0F, NaturalistSoundEvents.SCORPION_STEP, STEP_VOLUME, VOICE_PITCH)
            .at(0.13F, NaturalistSoundEvents.SCORPION_STEP, STEP_VOLUME, VOICE_PITCH)
            .at(0.38F, NaturalistSoundEvents.SCORPION_STEP, STEP_VOLUME, VOICE_PITCH)
            .build();
    private static final AnimationSoundTrack RUN_SOUNDS = AnimationSoundTrack.builder(0.25F, true)
            .at(0.0F, NaturalistSoundEvents.SCORPION_STEP, STEP_VOLUME, VOICE_PITCH)
            .at(0.06F, NaturalistSoundEvents.SCORPION_STEP, STEP_VOLUME, VOICE_PITCH)
            .at(0.19F, NaturalistSoundEvents.SCORPION_STEP, STEP_VOLUME, VOICE_PITCH)
            .build();
    private static final AnimationSoundTrack ATTACK_SOUNDS = AnimationSoundTrack.builder(0.75F, false)
            .at(0.08F, NaturalistSoundEvents.SCORPION_ATTACK, SOUND_VOLUME, VOICE_PITCH)
            .build();

    private final AnimationSoundPlayer animationSounds = new AnimationSoundPlayer()
            .add(this.walkAnimationState, WALK_SOUNDS)
            .add(this.runAnimationState, RUN_SOUNDS)
            .add(this.attackAnimationState, ATTACK_SOUNDS);

    protected Scorpion(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }
    //endregion

    //region Spawning
    public static boolean checkScorpionSpawnRules(EntityType<? extends Animal> entityType, LevelAccessor level, EntitySpawnReason spawnType, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).is(NaturalistBlockTags.SCORPIONS_SPAWNABLE_ON) && isBrightEnoughToSpawn(level, pos);
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return false;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob mob) {
        return null;
    }
    //endregion

    //region Behavior
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.35D, true));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, true,
                (entity, level) -> entity.is(NaturalistEntityTypeTags.SCORPION_HOSTILES)));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, true,
                (player, level) -> this.canHuntPlayers() && this.isInDarkness()));
    }

    protected boolean canHuntPlayers() {
        return true;
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        if (target instanceof Player && this.level().getDifficulty() == Difficulty.PEACEFUL) {
            return;
        }
        super.setTarget(target);
    }

    @Override
    public boolean doHurtTarget(@NotNull ServerLevel level, @NotNull Entity entity) {
        boolean result = super.doHurtTarget(level, entity);
        if (result && entity instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(MobEffects.POISON, 120), this);
        }
        return result;
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(NaturalistRegistry.LIZARD_TAIL.get()) && this.getHealth() < this.getMaxHealth()) {
            this.heal(1.0F);
            this.playSound(SoundEvents.GENERIC_EAT.value(), 1.0F, 1.0F);
            stack.consume(1, player);
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void customServerAiStep(@NotNull ServerLevel level) {
        super.customServerAiStep(level);
        this.setAggressive(this.getTarget() != null);
    }

    @Override
    protected float getSoundVolume() {
        return 0.7F;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return NaturalistSoundEvents.SCORPION_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return NaturalistSoundEvents.SCORPION_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return NaturalistSoundEvents.SCORPION_DEATH.get();
    }
    //endregion

    //region Animation
    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.setupAnimationStates();
            this.animationSounds.tick(this);
        }
    }

    private void setupAnimationStates() {
        boolean moving = NaturalistAnimal.isVisiblyMoving(this);

        this.attackAnimationState.animateWhen(this.attackAnimTimer.tick(this.isSwinging()), this.tickCount);

        this.walkAnimationState.animateWhen(moving && !this.isAggressive(), this.tickCount);
        this.runAnimationState.animateWhen(moving && this.isAggressive(), this.tickCount);
        this.idleAnimationState.animateWhen(!moving, this.tickCount);
    }
    //endregion
}
