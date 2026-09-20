package com.crispytwig.naturalist.world.entity.animal.tortoise;

import com.crispytwig.naturalist.tags.NaturalistBlockTags;
import com.crispytwig.naturalist.tags.NaturalistItemTags;
import com.crispytwig.naturalist.world.entity.animal.NaturalistAnimal;
import com.crispytwig.naturalist.world.entity.EggLayingAnimal;
import com.crispytwig.naturalist.world.entity.FollowingPet;
import com.crispytwig.naturalist.world.entity.HidingAnimal;
import com.crispytwig.naturalist.world.entity.ai.goal.PetFollowOwnerGoal;
import com.crispytwig.naturalist.world.entity.ai.goal.EggLayingBreedGoal;
import com.crispytwig.naturalist.world.entity.ai.goal.HideGoal;
import com.crispytwig.naturalist.world.entity.ai.goal.LayEggGoal;
import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.registry.NaturalistEntityTypes;
import com.crispytwig.naturalist.registry.NaturalistMobVariants;
import com.crispytwig.naturalist.world.level.block.TortoiseEggBlock;
import com.crispytwig.naturalist.registry.NaturalistRegistry;
import com.crispytwig.naturalist.registry.NaturalistSoundEvents;
import com.crispytwig.naturalist.world.entity.variant.DataDrivenVariantAnimal;
import com.crispytwig.naturalist.world.entity.variant.MobVariant;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TurtleEggBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.crispytwig.naturalist.world.entity.SmoothAnimationState;

import java.util.function.Predicate;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public class Tortoise extends TamableAnimal implements HidingAnimal, EggLayingAnimal, FollowingPet, DataDrivenVariantAnimal {
    //region Data
    public static final String[] VARIANT_NAMES = {"brown", "green", "black"};

    private static final Predicate<ItemStack> TEMPT_ITEMS = stack -> stack.is(NaturalistItemTags.TORTOISE_TEMPT_ITEMS);

    private static final EntityDataAccessor<String> VARIANT_ID = SynchedEntityData.defineId(Tortoise.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> HAS_EGG = SynchedEntityData.defineId(Tortoise.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> LAYING_EGG = SynchedEntityData.defineId(Tortoise.class, EntityDataSerializers.BOOLEAN);

    private boolean followingOwner = true;
    int layEggCounter;

    public final SmoothAnimationState idleAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState walkAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState sitAnimationState = SmoothAnimationState.pose();
    public final SmoothAnimationState digAnimationState = SmoothAnimationState.pose();
    public final SmoothAnimationState hideAnimationState = SmoothAnimationState.pose();
    public final SmoothAnimationState hurtAnimationState = SmoothAnimationState.instant();

    public Tortoise(@NotNull EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Animal.createAnimalAttributes().add(Attributes.MOVEMENT_SPEED, 0.17f).add(Attributes.MAX_HEALTH, 30.0).add(Attributes.ATTACK_DAMAGE, 2.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.6);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(VARIANT_ID, NaturalistMobVariants.TORTOISE_BROWN.identifier().toString());
        builder.define(HAS_EGG, false);
        builder.define(LAYING_EGG, false);
    }

    @Override
    public ResourceKey<MobVariant> getDefaultVariant() {
        return NaturalistMobVariants.TORTOISE_BROWN;
    }

    @Override
    public String[] getLegacyVariantNames() {
        return VARIANT_NAMES;
    }

    @Override
    public Identifier getFallbackVariantTexture() {
        return Naturalist.location("textures/entity/tortoise/brown.png");
    }

    @Override
    public String getVariantString() {
        return this.entityData.get(VARIANT_ID);
    }

    @Override
    public void setVariantString(String location) {
        this.entityData.set(VARIANT_ID, location);
    }

    @Override
    public boolean hasEgg() {
        return this.entityData.get(HAS_EGG);
    }

    @Override
    public void setHasEgg(boolean hasEgg) {
        this.entityData.set(HAS_EGG, hasEgg);
    }

    @Override
    public boolean isLayingEgg() {
        return this.entityData.get(LAYING_EGG);
    }

    @Override
    public void setLayingEgg(boolean isLayingEgg) {
        this.entityData.set(LAYING_EGG, isLayingEgg);
    }

    @Override
    public int getLayEggCounter() {
        return this.layEggCounter;
    }

    @Override
    public void setLayEggCounter(int layEggCounter) {
        this.layEggCounter = layEggCounter;
    }

    @Override
    public Block getEggBlock() {
        return NaturalistRegistry.TORTOISE_EGG.get();
    }

    @Override
    public BlockState createEggBlockState(int eggCount) {
        return this.getEggBlock().defaultBlockState()
                .setValue(TurtleEggBlock.EGGS, eggCount)
                .setValue(TortoiseEggBlock.VARIANT, this.getLegacyVariantIndex());
    }

    @Override
    public TagKey<Block> getEggLayableBlockTag() {
        return NaturalistBlockTags.TORTOISE_EGG_LAYABLE_ON;
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
    protected void addAdditionalSaveData(@NotNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        this.saveVariant(output);
        output.putBoolean("HasEgg", this.hasEgg());
        FollowingPet.savePet(this, output);
    }

    @Override
    protected void readAdditionalSaveData(@NotNull ValueInput input) {
        super.readAdditionalSaveData(input);
        this.loadVariant(input);
        this.setHasEgg(input.getBooleanOr("HasEgg", false));
        FollowingPet.loadPet(this, input);
    }
    //endregion

    //region Spawning
    @Override
    public @NotNull SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull EntitySpawnReason reason, @Nullable SpawnGroupData spawnData) {
        this.selectVariantForSpawn(level);
        return super.finalizeSpawn(level, difficulty, reason, spawnData);
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return TEMPT_ITEMS.test(stack);
    }

    @Override
    public boolean canMate(@NotNull Animal otherAnimal) {
        if (!this.isTame()) {
            return false;
        }
        if (!(otherAnimal instanceof Tortoise tortoise)) {
            return false;
        }
        return tortoise.isTame() && super.canMate(otherAnimal);
    }

    @Override
    public boolean canFallInLove() {
        return super.canFallInLove() && !this.hasEgg();
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverLevel, @NotNull AgeableMob ageableMob) {
        Tortoise tortoise = NaturalistEntityTypes.TORTOISE.get().create(serverLevel, EntitySpawnReason.BREEDING);
        if (ageableMob instanceof Tortoise tortoiseParent) {
            assert tortoise != null;
            tortoise.setVariantString(this.getOffspringVariantId(tortoiseParent, this.random));
            tortoise.setOwnerReference(this.random.nextBoolean() ? tortoiseParent.getOwnerReference() : this.getOwnerReference());
        }
        return tortoise;
    }
    //endregion

    //region Behavior
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new EggLayingBreedGoal<>(this, 1.0));
        this.goalSelector.addGoal(1, new LayEggGoal<>(this, 1.0));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(1, new HideGoal<>(this));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.0, TEMPT_ITEMS, false));
        this.goalSelector.addGoal(3, new PetFollowOwnerGoal(this, 1.0, 10.0f, 5.0f));
        this.goalSelector.addGoal(4, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 10.0f));
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.96f;
    }

    @Override
    public double getFluidJumpThreshold() {
        return 0.4;
    }

    @Override
    public boolean canHide() {
        if (this.isTame()) {
            return false;
        }
        AABB area = this.getBoundingBox().inflate(5.0D, 3.0D, 5.0D);
        if (this.level() instanceof ServerLevel serverLevel) {
            List<Player> players = serverLevel.getNearbyPlayers(TargetingConditions.forNonCombat().range(5.0D).selector((livingEntity, level) -> EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingEntity) && !livingEntity.isDiscrete() && !livingEntity.isHolding(TEMPT_ITEMS)), this, area);
            return !players.isEmpty();
        }
        return !this.level().getEntitiesOfClass(Player.class, area, player -> EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(player)
                && !player.isDiscrete() && !player.isHolding(TEMPT_ITEMS) && this.distanceToSqr(player) <= 25.0D).isEmpty();
    }

    @Override
    public void knockback(double strength, double x, double z) {
        if (this.isBaby()) {
            super.knockback(strength / Math.max(1.0 - this.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE), 0.01), x, z);
        } else {
            super.knockback(this.isInSittingPose() || this.canHide() ? strength / 4 : strength, x, z);
        }
    }

    @Override
    public boolean hurtServer(@NotNull ServerLevel level, @NotNull DamageSource source, float amount) {
        return super.hurtServer(level, source, this.canHide() ? amount * 0.8F : amount);
    }

    @Override
    public void setTame(boolean tamed, boolean applyTameEffects) {
        super.setTame(tamed, applyTameEffects);
        if (tamed) {
            Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(30.0);
            this.setHealth(30.0f);
        } else {
            Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(20.0);
        }
        Objects.requireNonNull(this.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(4.0);
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        InteractionResult interactionResult;
        ItemStack itemStack = player.getItemInHand(hand);
        InteractionResult whistle = FollowingPet.tryWhistle(this, player, hand);
        if (whistle != null) {
            return whistle;
        }
        if (this.level().isClientSide()) {
            if (this.isTame() && this.isOwnedBy(player)) {
                return InteractionResult.SUCCESS;
            }
            if (this.isFood(itemStack) && (this.getHealth() < this.getMaxHealth() || !this.isTame())) {
                return InteractionResult.SUCCESS;
            }

            return InteractionResult.PASS;
        }
        if (this.isTame()) {
            if (this.isOwnedBy(player)) {
                if (this.isFood(itemStack) && this.getHealth() < this.getMaxHealth()) {
                    this.usePlayerItem(player, hand, itemStack);
                    this.heal(3.0F);
                    return InteractionResult.CONSUME;
                }
                InteractionResult interactionResult2 = super.mobInteract(player, hand);
                if (!interactionResult2.consumesAction() || this.isBaby()) {
                    this.setOrderedToSit(!this.isOrderedToSit());
                }
                return interactionResult2;
            }
        } else if (this.isFood(itemStack)) {
            this.usePlayerItem(player, hand, itemStack);
            if (this.random.nextInt(3) == 0) {
                this.tame(player);
                this.setOrderedToSit(true);
                this.level().broadcastEntityEvent(this, (byte)7);
            } else {
                this.level().broadcastEntityEvent(this, (byte)6);
            }
            this.setPersistenceRequired();
            return InteractionResult.CONSUME;
        }
        if ((interactionResult = super.mobInteract(player, hand)).consumesAction()) {
            this.setPersistenceRequired();
        }
        return interactionResult;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        BlockPos pos = this.blockPosition();
        if (this.isAlive() && this.isLayingEgg() && this.layEggCounter >= 1 && this.layEggCounter % 5 == 0 && this.level().getBlockState(pos.below()).is(this.getEggLayableBlockTag())) {
            this.level().levelEvent(2001, pos, Block.getId(this.level().getBlockState(pos.below())));
        }
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return this.isBaby() ? NaturalistSoundEvents.TORTOISE_AMBIENT_BABY.get() : NaturalistSoundEvents.TORTOISE_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return this.isBaby() ? NaturalistSoundEvents.TORTOISE_HURT_BABY.get() : NaturalistSoundEvents.TORTOISE_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return this.isBaby() ? NaturalistSoundEvents.TORTOISE_DEATH_BABY.get() : NaturalistSoundEvents.TORTOISE_DEATH.get();
    }

    @Override
    public float getVoicePitch() {
        return NaturalistAnimal.defaultVoicePitch(this.random);
    }
    //endregion

    //region Animation
    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.setupAnimationStates();
        }
    }

    private void setupAnimationStates() {
        boolean sitting = this.isInSittingPose();
        boolean layingEgg = this.isLayingEgg();
        boolean moving = NaturalistAnimal.isVisiblyMoving(this);
        this.hurtAnimationState.animateWhen(this.hurtTime > 0, this.tickCount);
        this.hideAnimationState.animateWhen(this.canHide(), this.tickCount);
        this.sitAnimationState.animateWhen(sitting, this.tickCount);
        this.digAnimationState.animateWhen(!sitting && layingEgg, this.tickCount);
        this.walkAnimationState.animateWhen(!sitting && !layingEgg && moving, this.tickCount);
        this.idleAnimationState.animateWhen(!sitting && !layingEgg && !moving, this.tickCount);
    }
    //endregion
}
