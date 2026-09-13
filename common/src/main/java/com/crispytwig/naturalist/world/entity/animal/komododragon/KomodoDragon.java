package com.crispytwig.naturalist.world.entity.animal.komododragon;

import com.crispytwig.naturalist.tags.NaturalistBlockTags;
import com.crispytwig.naturalist.tags.NaturalistEntityTypeTags;
import com.crispytwig.naturalist.tags.NaturalistItemTags;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.registry.NaturalistEntityTypes;
import com.crispytwig.naturalist.registry.NaturalistSoundEvents;
import com.crispytwig.naturalist.world.entity.variant.DataDrivenVariantAnimal;
import com.crispytwig.naturalist.world.entity.ai.goal.BabyHurtByTargetGoal;
import com.crispytwig.naturalist.world.entity.ai.goal.BabyPanicGoal;
import com.crispytwig.naturalist.world.entity.ai.goal.SleepGoal;
import com.crispytwig.naturalist.world.entity.animal.NaturalistAnimal;
import com.crispytwig.naturalist.world.entity.SleepingAnimal;
import com.crispytwig.naturalist.world.entity.AnimationSoundPlayer;
import com.crispytwig.naturalist.world.entity.AnimationSoundTrack;
import com.crispytwig.naturalist.world.entity.SmoothAnimationState;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
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
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.function.Predicate;
import java.util.List;

public class KomodoDragon extends Animal implements SleepingAnimal, DataDrivenVariantAnimal {
    //region Data
    private static final Predicate<ItemStack> FOOD_ITEMS = stack -> stack.is(NaturalistItemTags.KOMODO_DRAGON_FOOD_ITEMS);
    private static final int PLAYER_TARGETING_TIME = 400;

    private static final EntityDataAccessor<String> DATA_VARIANT = SynchedEntityData.defineId(KomodoDragon.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> BASKING = SynchedEntityData.defineId(KomodoDragon.class, EntityDataSerializers.BOOLEAN);

    private int playerNearbyTicks;
    private boolean playerTargeting;

    public final SmoothAnimationState idleAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState walkAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState baskAnimationState = SmoothAnimationState.pose();
    public final SmoothAnimationState biteAnimationState = SmoothAnimationState.instant();
    private static final int BITE_ANIM_TICKS = 7;
    private int biteAnimTicks;
    public final SmoothAnimationState babyTransformAnimationState = SmoothAnimationState.pose();

    private static final AnimationSoundTrack WALK_SOUNDS = AnimationSoundTrack.builder(1.2917F, true)
            .at(0.2917F, NaturalistSoundEvents.KOMODO_DRAGON_STEP, 0.4F, 1.0F)
            .at(0.3333F, NaturalistSoundEvents.KOMODO_DRAGON_STEP, 0.15F, 1.0F)
            .at(0.7917F, NaturalistSoundEvents.KOMODO_DRAGON_STEP, 0.4F, 1.0F)
            .at(0.8333F, NaturalistSoundEvents.KOMODO_DRAGON_STEP, 0.15F, 1.0F)
            .build();
    private static final AnimationSoundTrack BITE_SOUNDS = AnimationSoundTrack.builder(0.3125F, false)
            .at(0.0F, NaturalistSoundEvents.KOMODO_DRAGON_ATTACK, 1.0F, 1.0F)
            .build();

    private final AnimationSoundPlayer animationSounds = new AnimationSoundPlayer()
            .add(this.walkAnimationState, WALK_SOUNDS)
            .add(this.biteAnimationState, BITE_SOUNDS);

    public KomodoDragon(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .add(Attributes.STEP_HEIGHT, 1.0D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT, this.getDefaultVariant().identifier().toString());
        builder.define(BASKING, false);
    }

    @Override
    public Identifier getFallbackVariantTexture() {
        return Naturalist.location("textures/entity/komodo_dragon.png");
    }

    @Override
    public String getVariantString() {
        return this.entityData.get(DATA_VARIANT);
    }

    @Override
    public void setVariantString(String location) {
        this.entityData.set(DATA_VARIANT, location);
    }

    @Override
    public void addAdditionalSaveData(@NotNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        this.saveVariant(output);
    }

    @Override
    public void readAdditionalSaveData(@NotNull ValueInput input) {
        super.readAdditionalSaveData(input);
        this.loadVariant(input);
    }

    @Override
    public boolean isSleeping() {
        return this.entityData.get(BASKING);
    }

    @Override
    public void setSleeping(boolean sleeping) {
        this.entityData.set(BASKING, sleeping);
    }

    public boolean isPlayerTargeting() {
        return this.playerTargeting;
    }
    //endregion

    //region Spawning
    public static boolean checkKomodoDragonSpawnRules(EntityType<? extends Animal> entityType, LevelAccessor level, EntitySpawnReason spawnType, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).is(NaturalistBlockTags.KOMODO_DRAGONS_SPAWNABLE_ON) && isBrightEnoughToSpawn(level, pos);
    }

    @Override
    public @NonNull SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull EntitySpawnReason spawnType, @Nullable SpawnGroupData spawnGroupData) {
        this.selectVariantForSpawn(level);
        if (spawnGroupData == null) {
            spawnGroupData = new AgeableMob.AgeableMobGroupData(0.05F);
        }
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return FOOD_ITEMS.test(stack);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob mob) {
        KomodoDragon baby = NaturalistEntityTypes.KOMODO_DRAGON.get().create(level, EntitySpawnReason.BREEDING);
        if (baby != null) {
            baby.setVariantString(this.getOffspringVariantId(mob, this.random));
        }
        return baby;
    }
    //endregion

    //region Behavior
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.5D, true));
        this.goalSelector.addGoal(2, new BabyPanicGoal(this, 2.0D));
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.25D, FOOD_ITEMS, false));
        this.goalSelector.addGoal(5, new KomodoDragonBaskGoal(this));
        this.goalSelector.addGoal(6, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new BabyHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, true,
                (entity, level) -> entity.is(NaturalistEntityTypeTags.KOMODO_DRAGON_HOSTILES) && !this.isBaby()));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, true,
                (player, level) -> this.isPlayerTargeting() && !this.isBaby()));
    }

    @Override
    public boolean canSleep() {
        return this.level().isBrightOutside() && this.getTarget() == null && !this.level().isWaterAt(this.blockPosition());
    }

    @Override
    public boolean doHurtTarget(@NotNull ServerLevel level, @NotNull Entity entity) {
        boolean result = super.doHurtTarget(level, entity);
        if (result && entity instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(MobEffects.POISON, 100), this);
        }
        return result;
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (this.isFood(stack) && this.getHealth() < this.getMaxHealth()) {
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            this.heal(2.0F);
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.isSleeping() || this.isImmobile()) {
            this.jumping = false;
            this.xxa = 0.0F;
            this.zza = 0.0F;
        }
    }

    @Override
    public void customServerAiStep(@NotNull ServerLevel level) {
        if (this.getMoveControl().hasWanted()) {
            this.setSprinting(this.getMoveControl().getSpeedModifier() >= 1.05D && this.onGround());
        } else {
            this.setSprinting(false);
        }
        if (this.isBaby()) {
            this.playerNearbyTicks = 0;
            this.playerTargeting = false;
            return;
        }
        if (this.tickCount % 10 == 0) {
            List<Player> players = level.getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(16.0D),
                    player -> !player.isCreative() && !player.isSpectator());
            if (players.isEmpty()) {
                this.playerNearbyTicks = 0;
                if (this.getTarget() == null) {
                    this.playerTargeting = false;
                }
            } else if (this.playerNearbyTicks < PLAYER_TARGETING_TIME) {
                this.playerNearbyTicks += 10;
                if (this.playerNearbyTicks >= PLAYER_TARGETING_TIME) {
                    this.playerTargeting = true;
                }
            }
        }
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return NaturalistSoundEvents.KOMODO_DRAGON_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return NaturalistSoundEvents.KOMODO_DRAGON_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return NaturalistSoundEvents.KOMODO_DRAGON_DEATH.get();
    }

    static class KomodoDragonBaskGoal extends SleepGoal<KomodoDragon> {
        private final KomodoDragon komodoDragon;
        private int baskTime;

        public KomodoDragonBaskGoal(KomodoDragon komodoDragon) {
            super(komodoDragon);
            this.komodoDragon = komodoDragon;
        }

        @Override
        public boolean canUse() {
            return super.canUse() && (this.komodoDragon.isSleeping() || this.komodoDragon.getRandom().nextFloat() < 0.001F);
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && --this.baskTime > 0;
        }

        @Override
        public void start() {
            super.start();
            this.baskTime = 160 + this.komodoDragon.getRandom().nextInt(340);
        }
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
        boolean sleeping = this.isSleeping();
        boolean moving = NaturalistAnimal.isVisiblyMoving(this);
        if (this.swinging && this.biteAnimTicks <= 0) {
            this.biteAnimTicks = BITE_ANIM_TICKS;
        } else if (this.biteAnimTicks > 0) {
            this.biteAnimTicks--;
        }
        this.biteAnimationState.animateWhen(this.biteAnimTicks > 0, this.tickCount);
        this.babyTransformAnimationState.animateWhen(this.isBaby(), this.tickCount);
        this.baskAnimationState.animateWhen(sleeping, this.tickCount);
        this.walkAnimationState.animateWhen(!sleeping && moving, this.tickCount);
        this.idleAnimationState.animateWhen(!sleeping && !moving, this.tickCount);
    }
    //endregion
}
