package com.crispytwig.naturalist.server.entity.mob;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.registry.NaturalistRegistry;
import com.crispytwig.naturalist.registry.NaturalistSoundEvents;
import com.crispytwig.naturalist.server.entity.variant.DataDrivenVariantAnimal;
import com.crispytwig.naturalist.server.util.ItemHelper;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.crispytwig.naturalist.server.entity.util.FishSwimTilt;
import com.crispytwig.naturalist.server.entity.util.SmoothAnimationState;

@SuppressWarnings("unused")
public class Bass extends AbstractSchoolingFish implements DataDrivenVariantAnimal {
    //region Data
    private static final EntityDataAccessor<String> DATA_VARIANT = SynchedEntityData.defineId(Bass.class, EntityDataSerializers.STRING);

    public static final String[] VARIANT_NAMES = {"bass", "bass_medium", "bass_large"};

    public final SmoothAnimationState swimAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState flopAnimationState = new SmoothAnimationState();

    public final FishSwimTilt swimTilt = new FishSwimTilt();

    private static final int EAT_COOLDOWN = 5000;
    private static final float GROW_CHANCE = 0.2F;
    private int eatCooldown;

    private int groupSizeTarget;

    public Bass(EntityType<? extends AbstractSchoolingFish> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new SmoothSwimmingMoveControl(this, 1000, 5, 0.02F, 0.1F, false);
        this.lookControl = new SmoothSwimmingLookControl(this, 5);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 3.0D).add(Attributes.ATTACK_DAMAGE, 1.0D);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return distanceToClosestPlayer > 16384.0D && super.removeWhenFarAway(distanceToClosestPlayer);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT, this.getDefaultVariant().location().toString());
    }

    @Override
    public ResourceLocation getFallbackVariantTexture() {
        return Naturalist.location("textures/entity/bass.png");
    }

    @Override
    public String[] getLegacyVariantNames() {
        return VARIANT_NAMES;
    }

    @Override
    public String getVariantString() {
        return this.entityData.get(DATA_VARIANT);
    }

    @Override
    public void setVariantString(String location) {
        this.entityData.set(DATA_VARIANT, location);
        this.moveControl = new SmoothSwimmingMoveControl(this, 1000, this.isLargeVariant() ? 10 : 5, 0.02F, 0.1F, false);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        this.saveVariant(compound);
        compound.putInt("EatCooldown", this.eatCooldown);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.loadVariant(compound);
        this.eatCooldown = compound.getInt("EatCooldown");
    }

    @Override
    public @NotNull ItemStack getBucketItemStack() {
        return new ItemStack(NaturalistRegistry.BASS_BUCKET.get());
    }

    @Override
    public void saveToBucketTag(@NotNull ItemStack stack) {
        super.saveToBucketTag(stack);
        CustomData.update(DataComponents.BUCKET_ENTITY_DATA, stack, this::saveVariant);
        CompoundTag custom = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        this.saveVariant(custom);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(custom));
    }

    @Override
    public void loadFromBucketTag(@NotNull CompoundTag tag) {
        super.loadFromBucketTag(tag);
        this.loadVariant(tag);
    }

    public boolean isMediumVariant() {
        return this.getVariantLocation().equals(Naturalist.location("bass_medium"));
    }

    public boolean isLargeVariant() {
        return this.getVariantLocation().equals(Naturalist.location("bass_large"));
    }

    public int getSizeTier() {
        if (this.isLargeVariant()) {
            return 2;
        }
        if (this.isMediumVariant()) {
            return 1;
        }
        return 0;
    }

    public boolean canSchool() {
        return this.getSizeTier() < 2;
    }

    public boolean canHunt() {
        return this.getSizeTier() > 0 && this.eatCooldown <= 0;
    }

    public boolean canEatTarget(Bass prey) {
        return prey != this && prey.isAlive() && prey.getSizeTier() < this.getSizeTier();
    }

    //endregion

    //region Spawning
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        if (spawnType == MobSpawnType.BUCKET) {
            return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
        }
        if (spawnGroupData instanceof BassGroupData group) {
            this.setVariantString(group.variant);
            this.groupSizeTarget = group.targetCount;
            return super.finalizeSpawn(level, difficulty, spawnType, group);
        }
        this.selectVariantForSpawn(level);
        this.groupSizeTarget = this.rollGroupSizeTarget();
        super.finalizeSpawn(level, difficulty, spawnType, null);
        return new BassGroupData(this, this.getVariantString(), this.groupSizeTarget);
    }

    private int rollGroupSizeTarget() {
        RandomSource random = this.getRandom();
        return switch (this.getSizeTier()) {
            case 2 -> 1;
            case 1 -> 2 + random.nextInt(2);
            default -> 3 + random.nextInt(4);
        };
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 6;
    }

    @Override
    public boolean isMaxGroupSizeReached(int size) {
        return this.groupSizeTarget > 0 && size >= this.groupSizeTarget;
    }

    private static class BassGroupData extends AbstractSchoolingFish.SchoolSpawnGroupData {
        private final String variant;
        private final int targetCount;

        private BassGroupData(AbstractSchoolingFish leader, String variant, int targetCount) {
            super(leader);
            this.variant = variant;
            this.targetCount = targetCount;
        }
    }
    //endregion

    //region Behavior
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Player.class, 6.0F, 1.0D, 1.5D));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Axolotl.class, 6.0F, 1.0D, 1.5D));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Catfish.class, 8.0F, 1.0D, 1.5D));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Bass.class, 10.0F, 1.3D, 1.7D, (living) -> living instanceof Bass other && other.getSizeTier() > this.getSizeTier()));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.4D, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Bass.class, 10, true, false, (living) -> living instanceof Bass prey && Bass.this.canEatTarget(prey)) {
            @Override
            public boolean canUse() {
                return Bass.this.canHunt() && super.canUse();
            }
        });
    }

    @Override
    public @NotNull AbstractSchoolingFish startFollowing(@NotNull AbstractSchoolingFish leader) {
        if (this.canSchool() && leader instanceof Bass other && other.canSchool() && other.getSizeTier() == this.getSizeTier()) {
            return super.startFollowing(leader);
        }
        return leader;
    }

    @Override
    public void stopFollowing() {
        if (this.isFollower()) {
            super.stopFollowing();
        }
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity target) {
        if (!this.level().isClientSide && target instanceof Bass prey && this.canEatTarget(prey)) {
            boolean wasMediumEatingSmall = this.getSizeTier() == 1 && prey.getSizeTier() == 0;
            boolean grew = wasMediumEatingSmall && this.getRandom().nextFloat() < GROW_CHANCE;
            devour(this, prey, !grew);
            this.eatCooldown = EAT_COOLDOWN;
            this.setTarget(null);
            if (grew) {
                this.growIntoLarge();
            }
            return true;
        }
        return super.doHurtTarget(target);
    }

    public static void devour(Mob predator, Bass prey, boolean dropBoneMeal) {
        if (predator.level() instanceof ServerLevel serverLevel) {
            ItemParticleOption bassParticle = new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(NaturalistRegistry.BASS.get()));
            for (int i = 0; i < 16; i++) {
                serverLevel.sendParticles(bassParticle, prey.getRandomX(1.0D), prey.getRandomY(), prey.getRandomZ(1.0D), 1, 0.0D, 0.0D, 0.0D, 0.05D);
            }
            if (dropBoneMeal) {
                ItemHelper.spawnItemOnEntity(prey, new ItemStack(Items.BONE_MEAL));
            }
        }
        predator.playSound(SoundEvents.PARROT_EAT, 0.7F, 1.2F + predator.getRandom().nextFloat() * 0.1F);
        prey.discard();
    }

    private void growIntoLarge() {
        this.setVariantString(Naturalist.location("bass_large").toString());
        if (this.isFollower()) {
            this.stopFollowing();
        }
    }

    @Override
    public int getMaxSchoolSize() {
        return 5;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SALMON_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return SoundEvents.SALMON_HURT;
    }

    @Override
    protected @NotNull SoundEvent getFlopSound() {
        return NaturalistSoundEvents.BASS_FLOP.get();
    }
    //endregion

    //region Animation
    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            this.setupAnimationStates();
            this.swimTilt.tick(this);
        } else if (this.eatCooldown > 0) {
            this.eatCooldown--;
        }
    }

    private void setupAnimationStates() {
        boolean inWater = this.isInWater();
        this.flopAnimationState.animateWhen(!inWater, this.tickCount);
        this.swimAnimationState.animateWhen(inWater, this.tickCount);
    }
    //endregion

    @Override
    protected void doPush(@NotNull Entity entity) {
    }

    @Override
    public boolean canUsePortal(boolean allowPassengers) {
        return false;
    }
}
