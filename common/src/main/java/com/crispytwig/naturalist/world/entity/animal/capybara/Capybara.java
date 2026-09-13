package com.crispytwig.naturalist.world.entity.animal.capybara;

import com.crispytwig.naturalist.tags.NaturalistItemTags;
import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.registry.NaturalistEntityTypes;
import com.crispytwig.naturalist.registry.NaturalistSoundEvents;
import com.crispytwig.naturalist.world.entity.ai.goal.DistancedFollowParentGoal;
import com.crispytwig.naturalist.world.entity.ai.goal.PetFollowOwnerGoal;
import com.crispytwig.naturalist.world.entity.ai.goal.SleepGoal;
import com.crispytwig.naturalist.world.entity.DyeableAnimal;
import com.crispytwig.naturalist.world.entity.FollowingPet;
import com.crispytwig.naturalist.world.entity.animal.NaturalistAnimal;
import com.crispytwig.naturalist.world.entity.SleepingAnimal;
import com.crispytwig.naturalist.world.entity.SmoothAnimationState;
import com.crispytwig.naturalist.world.entity.variant.DataDrivenVariantAnimal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.function.Predicate;
import java.util.Optional;

@SuppressWarnings("unused")
public class Capybara extends TamableAnimal implements DyeableAnimal, FollowingPet, SleepingAnimal, DataDrivenVariantAnimal {
    //region Data
    private static final Predicate<ItemStack> FOOD_ITEMS = stack -> stack.is(NaturalistItemTags.CAPYBARA_FOOD_ITEMS);

    private static final EntityDataAccessor<String> DATA_VARIANT = SynchedEntityData.defineId(Capybara.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> DATA_DYE = SynchedEntityData.defineId(Capybara.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> SLEEPING = SynchedEntityData.defineId(Capybara.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SLEEP_POSE = SynchedEntityData.defineId(Capybara.class, EntityDataSerializers.BOOLEAN);

    private boolean followingOwner = true;

    public final SmoothAnimationState idleAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState walkAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState runAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState swimAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState sitAnimationState = SmoothAnimationState.pose();
    public final SmoothAnimationState sleepAnimationState = SmoothAnimationState.pose();
    public final SmoothAnimationState sleep2AnimationState = SmoothAnimationState.pose();

    public Capybara(@NotNull EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes().add(Attributes.MAX_HEALTH, 14.0).add(Attributes.MOVEMENT_SPEED, 0.2);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT, this.getDefaultVariant().identifier().toString());
        builder.define(DATA_DYE, -1);
        builder.define(SLEEPING, false);
        builder.define(SLEEP_POSE, false);
    }

    @Override
    public Identifier getFallbackVariantTexture() {
        return Naturalist.location("textures/entity/capybara/capybara.png");
    }

    @Override
    public String getVariantString() {
        return this.entityData.get(DATA_VARIANT);
    }

    @Override
    public void setVariantString(String location) {
        this.entityData.set(DATA_VARIANT, location);
    }

    @Nullable
    @Override
    public DyeColor getDyeColor() {
        int id = this.entityData.get(DATA_DYE);
        return id < 0 ? null : DyeColor.byId(id);
    }

    @Override
    public void setDyeColor(@Nullable DyeColor color) {
        this.entityData.set(DATA_DYE, color == null ? -1 : color.getId());
    }

    @Override
    public boolean isFollowingOwner() {
        return this.followingOwner;
    }

    @Override
    public void setFollowingOwner(boolean following) {
        this.followingOwner = following;
    }

    @Override
    public boolean isSleeping() {
        return this.entityData.get(SLEEPING);
    }

    @Override
    public void setSleeping(boolean sleeping) {
        if (sleeping && !this.isSleeping()) {
            this.entityData.set(SLEEP_POSE, this.random.nextBoolean());
        }
        this.entityData.set(SLEEPING, sleeping);
    }

    @Override
    public boolean canSleep() {
        long dayTime = this.level().getDefaultClockTime() % 24000;
        return dayTime > 6000 && dayTime < 13000 && this.onGround() && !this.isInWater()
                && !this.isOrderedToSit() && !this.isInLove() && this.getLastHurtByMob() == null;
    }

    public boolean hasAltSleepPose() {
        return this.entityData.get(SLEEP_POSE);
    }

    @Override
    protected void addAdditionalSaveData(@NotNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        this.saveVariant(output);
        DyeableAnimal.saveDye(this, output);
        FollowingPet.savePet(this, output);
    }

    @Override
    protected void readAdditionalSaveData(@NotNull ValueInput input) {
        super.readAdditionalSaveData(input);
        this.loadVariant(input);
        DyeableAnimal.loadDye(this, input);
        FollowingPet.loadPet(this, input);
    }
    //endregion

    //region Spawning
    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return FOOD_ITEMS.test(stack);
    }

    @Nullable
    @Override
    public Capybara getBreedOffspring(@NotNull ServerLevel serverLevel, @NotNull AgeableMob ageableMob) {
        Capybara baby = NaturalistEntityTypes.CAPYBARA.get().create(serverLevel, EntitySpawnReason.BREEDING);
        if (baby != null) {
            baby.setVariantString(this.getOffspringVariantId(ageableMob, this.random));
            if (this.isTame()) {
                baby.setOwnerReference(this.getOwnerReference());
                baby.setTame(true, true);
                baby.setDyeColor(this.getDyeColor() == null ? DyeColor.RED : this.getDyeColor());
            }
        }
        return baby;
    }

    @Override
    public @NonNull SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull EntitySpawnReason spawnType, @Nullable SpawnGroupData spawnGroupData) {
        this.selectVariantForSpawn(level);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }
    //endregion

    //region Behavior
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.4));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.2, FOOD_ITEMS, false));
        this.goalSelector.addGoal(4, new DistancedFollowParentGoal(this, 1.1, 16.0, 8.0, 5.0));
        this.goalSelector.addGoal(4, new PetFollowOwnerGoal(this, 1.2, 10.0F, 3.0F));
        this.goalSelector.addGoal(5, new SleepGoal<>(this));
        this.goalSelector.addGoal(6, new RandomSwimmingGoal(this, 1.0, 10));
        this.goalSelector.addGoal(6, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    public void tame(@NotNull Player player) {
        super.tame(player);
        if (this.getDyeColor() == null) {
            this.setDyeColor(DyeColor.RED);
        }
    }

    @Override
    public boolean canMate(@NotNull Animal otherAnimal) {
        return this.isTame() && otherAnimal instanceof Capybara capybara && capybara.isTame() && super.canMate(otherAnimal);
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.9F;
    }

    @Override
    public boolean causeFallDamage(double fallDistance, float multiplier, @NotNull DamageSource source) {
        return false;
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        InteractionResult whistle = FollowingPet.tryWhistle(this, player, hand);
        if (whistle != null) {
            return whistle;
        }
        Optional<InteractionResult> dyeResult = DyeableAnimal.tryClearDye(this, player, hand);
        if (dyeResult.isEmpty()) {
            dyeResult = DyeableAnimal.tryDye(this, player, hand);
        }
        if (dyeResult.isPresent()) {
            return dyeResult.get();
        }
        ItemStack stack = player.getItemInHand(hand);
        if (!this.isTame()) {
            if (this.isFood(stack)) {
                if (!this.level().isClientSide()) {
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                    if (this.random.nextInt(3) != 0) {
                        this.tame(player);
                        this.level().broadcastEntityEvent(this, (byte) 7);
                    } else {
                        this.level().broadcastEntityEvent(this, (byte) 6);
                    }
                }
                return InteractionResult.SUCCESS;
            }
            return super.mobInteract(player, hand);
        }
        if (this.isOwnedBy(player)) {
            if (this.isFood(stack) && this.getHealth() < this.getMaxHealth()) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                this.heal(2.0F);
                return InteractionResult.SUCCESS;
            }
            if (stack.isEmpty()) {
                this.setOrderedToSit(!this.isOrderedToSit());
                this.jumping = false;
                this.navigation.stop();
                return InteractionResult.SUCCESS;
            }
        }
        return super.mobInteract(player, hand);
    }

    @Override
    protected int getBaseExperienceReward(@NotNull ServerLevel level) {
        return 1 + this.random.nextInt(3);
    }

    @Override
    public void customServerAiStep(@NotNull ServerLevel level) {
        super.customServerAiStep(level);
        if (this.getMoveControl().hasWanted()) {
            this.setSprinting(this.getMoveControl().getSpeedModifier() >= 1.3);
        } else {
            this.setSprinting(false);
        }
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
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.setupAnimationStates();
        }
    }

    private void setupAnimationStates() {
        boolean sleeping = this.isSleeping();
        boolean sitting = !sleeping && this.isInSittingPose();
        boolean swimming = !sitting && !sleeping && this.isInWater();
        boolean moving = NaturalistAnimal.isVisiblyMoving(this);
        boolean grounded = !sitting && !sleeping && !swimming && moving;
        this.sitAnimationState.animateWhen(sitting, this.tickCount);
        this.sleepAnimationState.animateWhen(sleeping && !this.hasAltSleepPose(), this.tickCount);
        this.sleep2AnimationState.animateWhen(sleeping && this.hasAltSleepPose(), this.tickCount);
        this.swimAnimationState.animateWhen(swimming, this.tickCount);
        this.walkAnimationState.animateWhen(grounded && !this.isSprinting(), this.tickCount);
        this.runAnimationState.animateWhen(grounded && this.isSprinting(), this.tickCount);
        this.idleAnimationState.animateWhen(!sitting && !sleeping && !swimming && !moving, this.tickCount);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return this.isSleeping() ? NaturalistSoundEvents.CAPYBARA_SLEEP.get() : NaturalistSoundEvents.CAPYBARA_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return NaturalistSoundEvents.CAPYBARA_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return NaturalistSoundEvents.CAPYBARA_DEATH.get();
    }

    @Override
    public float getVoicePitch() {
        return NaturalistAnimal.defaultVoicePitch(this.random);
    }
    //endregion
}
