package com.crispytwig.naturalist.world.entity.animal.seahorse;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.registry.NaturalistEntityTypes;
import com.crispytwig.naturalist.registry.NaturalistMobVariants;
import com.crispytwig.naturalist.registry.NaturalistRegistry;
import com.crispytwig.naturalist.tags.NaturalistItemTags;
import com.crispytwig.naturalist.world.entity.AquaticBodyTilt;
import com.crispytwig.naturalist.world.entity.SmoothAnimationState;
import com.crispytwig.naturalist.world.entity.ai.goal.RangedSwimGoal;
import com.crispytwig.naturalist.world.entity.animal.BucketableAquaticAnimal;
import com.crispytwig.naturalist.world.entity.variant.MobVariant;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

@SuppressWarnings("unused")
public class Seahorse extends BucketableAquaticAnimal {
    //region Data
    private static final EntityDataAccessor<Boolean> PREGNANT = SynchedEntityData.defineId(Seahorse.class, EntityDataSerializers.BOOLEAN);

    public static final String[] VARIANT_NAMES = {
            "white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray",
            "light_gray", "cyan", "purple", "blue", "brown", "green", "red", "black"
    };
    private static final ResourceKey<MobVariant> DEFAULT_VARIANT = NaturalistMobVariants.createKey(NaturalistMobVariants.registryFor("seahorse"), "white");

    public final SmoothAnimationState swimAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState flopAnimationState = new SmoothAnimationState();
    public final AquaticBodyTilt bodyTilt = new AquaticBodyTilt(new AquaticBodyTilt.Settings(
            new AquaticBodyTilt.Speed(4.0F, 0.3F, 2.5F, 8.0F, 0.24F, 0.25F),
            new AquaticBodyTilt.Pitching(10.0F, 67.5F, 3.0F, 0.0F, 0.0F, 0.0F),
            new AquaticBodyTilt.Turning(6.0F, 1.1F, 20.0F, 1.8F, 25.0F),
            new AquaticBodyTilt.Segment(0.9F, 9.0F),
            new AquaticBodyTilt.Segment(0.35F, 5.0F),
            new AquaticBodyTilt.Segment(0.8F, 2.5F),
            new AquaticBodyTilt.Segment(0.0F, 0.0F),
            0.85F, 0.4F, 2.0F));

    private int pregnancyTicks;
    private int broodRemaining;
    private int birthCooldown;
    private DyeColor partnerColor = DyeColor.WHITE;

    public Seahorse(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new SmoothSwimmingMoveControl<>(this, 85, 10, 0.5F, 0.0F, false);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Animal.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 3.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.1D)
                .add(Attributes.TEMPT_RANGE, 16.0D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(PREGNANT, false);
    }

    @Override
    public ResourceKey<MobVariant> getDefaultVariant() {
        return DEFAULT_VARIANT;
    }

    @Override
    public String[] getLegacyVariantNames() {
        return VARIANT_NAMES;
    }

    @Override
    public Identifier getFallbackVariantTexture() {
        return Naturalist.location("textures/entity/seahorse/white.png");
    }

    @Override
    protected float getBabySpawnChance() {
        return 0.05F;
    }

    @Override
    protected float getFoodHealAmount() {
        return 1.0F;
    }

    public boolean isPregnant() {
        return this.entityData.get(PREGNANT);
    }

    private DyeColor getColor() {
        return DyeColor.byName(this.getVariantLocation().getPath(), DyeColor.WHITE);
    }

    @Override
    public int getMaxAirSupply() {
        return 200;
    }

    @Override
    public void baseTick() {
        int air = this.getAirSupply();
        super.baseTick();
        if (!this.isAlive() || this.isInWater()) {
            this.setAirSupply(this.getMaxAirSupply());
            return;
        }
        this.setAirSupply(air - 1);
        if (this.getAirSupply() == -20) {
            this.setAirSupply(0);
            if (this.level() instanceof ServerLevel serverLevel) {
                this.hurtServer(serverLevel, this.damageSources().drown(), 2.0F);
            }
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("PregnancyTicks", this.pregnancyTicks);
        output.putInt("BroodRemaining", this.broodRemaining);
        output.putString("PartnerColor", this.partnerColor.getName());
    }

    @Override
    public void readAdditionalSaveData(@NotNull ValueInput input) {
        super.readAdditionalSaveData(input);
        this.pregnancyTicks = input.getIntOr("PregnancyTicks", 0);
        this.broodRemaining = input.getIntOr("BroodRemaining", 0);
        this.partnerColor = DyeColor.byName(input.getStringOr("PartnerColor", ""), DyeColor.WHITE);
        this.entityData.set(PREGNANT, this.broodRemaining > 0);
    }

    @Override
    public void saveToBucketTag(@NotNull ItemStack stack) {
        super.saveToBucketTag(stack);
        CustomData.update(DataComponents.BUCKET_ENTITY_DATA, stack, tag -> {
            tag.putInt("PregnancyTicks", this.pregnancyTicks);
            tag.putInt("BroodRemaining", this.broodRemaining);
            tag.putString("PartnerColor", this.partnerColor.getName());
        });
    }

    @Override
    public void loadFromBucketTag(@NotNull CompoundTag tag) {
        super.loadFromBucketTag(tag);
        this.pregnancyTicks = tag.getIntOr("PregnancyTicks", 0);
        this.broodRemaining = tag.getIntOr("BroodRemaining", 0);
        this.partnerColor = DyeColor.byName(tag.getStringOr("PartnerColor", ""), DyeColor.WHITE);
        this.entityData.set(PREGNANT, this.broodRemaining > 0);
    }

    @Override
    public @NotNull ItemStack getBucketItemStack() {
        return new ItemStack(NaturalistRegistry.SEAHORSE_BUCKET.get());
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return SoundEvents.COD_HURT;
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return SoundEvents.COD_HURT;
    }

    @Override
    public float getVoicePitch() {
        return 0.8F + this.random.nextFloat() * 0.4F;
    }
    //endregion

    //region Spawning
    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return stack.is(NaturalistItemTags.SEAHORSE_FOOD_ITEMS);
    }

    @Override
    public boolean canFallInLove() {
        return !this.isPregnant() && super.canFallInLove();
    }

    @Override
    public void spawnChildFromBreeding(@NotNull ServerLevel level, @NotNull Animal partner) {
        this.finalizeSpawnChildFromBreeding(level, partner, null);
        this.partnerColor = ((Seahorse) partner).getColor();
        this.pregnancyTicks = 100;
        this.broodRemaining = Mth.randomBetweenInclusive(this.random, 4, 8);
        this.birthCooldown = 0;
        this.entityData.set(PREGNANT, true);
    }
    //endregion

    //region Behavior
    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new WaterBoundPathNavigation(this, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Player.class, 6.0F, 1.5D, 2.0D, entity -> EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity) && !this.isFood(entity.getMainHandItem()) && !this.isFood(entity.getOffhandItem())));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Axolotl.class, 6.0F, 1.5D, 2.0D));
        this.goalSelector.addGoal(1, new TemptGoal(this, 1.0D, this::isFood, false));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(4, new FlockGoal(this));
        this.goalSelector.addGoal(5, new RangedSwimGoal(this, 20, 4));
    }

    @Override
    protected void customServerAiStep(@NotNull ServerLevel level) {
        super.customServerAiStep(level);
        if (!this.isPregnant()) {
            return;
        }
        if (this.pregnancyTicks > 0) {
            this.pregnancyTicks--;
            return;
        }
        if (--this.birthCooldown > 0) {
            return;
        }
        this.birthCooldown = 8;
        Seahorse baby = NaturalistEntityTypes.SEAHORSE.get().create(level, EntitySpawnReason.BREEDING);
        if (baby != null) {
            DyeColor color = this.random.nextFloat() < 0.2F
                    ? DyeColor.getMixedColor(level, this.getColor(), this.partnerColor)
                    : this.random.nextBoolean() ? this.getColor() : this.partnerColor;
            baby.setAge(BABY_START_AGE);
            baby.setVariantString(Naturalist.location(color.getName()).toString());
            baby.setPersistenceRequired();
            baby.snapTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
            level.addFreshEntity(baby);
            this.playSound(SoundEvents.ITEM_FRAME_ADD_ITEM, 1.0F, 1.0F);
        }
        if (--this.broodRemaining <= 0) {
            this.entityData.set(PREGNANT, false);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            boolean inWater = this.isInWater();
            this.bodyTilt.tick(this);
            this.swimAnimationState.animateWhen(inWater, this.tickCount);
            this.flopAnimationState.animateWhen(!inWater, this.tickCount);
        }
    }

    @Override
    public void aiStep() {
        if (!this.isInWater() && this.onGround() && this.verticalCollision) {
            this.setDeltaMovement(this.getDeltaMovement().add(Mth.randomBetween(this.random, -0.05F, 0.05F), 0.4F, Mth.randomBetween(this.random, -0.05F, 0.05F)));
            this.setOnGround(false);
            this.needsSync = true;
            this.playSound(SoundEvents.COD_FLOP, this.getSoundVolume(), this.getVoicePitch());
        }
        super.aiStep();
    }
    //endregion

    //region Goals
    static class FlockGoal extends Goal {
        private final Seahorse seahorse;
        private Vec3 center;

        FlockGoal(Seahorse seahorse) {
            this.seahorse = seahorse;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if ((this.seahorse.getUUID().hashCode() & Integer.MAX_VALUE) % 10 == 0 || !this.seahorse.isInWater() || this.seahorse.getRandom().nextInt(reducedTickDelay(40)) != 0) {
                return false;
            }
            List<Seahorse> neighbors = this.seahorse.level().getEntitiesOfClass(Seahorse.class, this.seahorse.getBoundingBox().inflate(8.0D), other -> other != this.seahorse);
            if (neighbors.isEmpty() || neighbors.size() > 4) {
                return false;
            }
            Vec3 sum = Vec3.ZERO;
            for (Seahorse neighbor : neighbors) {
                sum = sum.add(neighbor.position());
            }
            this.center = sum.scale(1.0D / neighbors.size());
            return this.seahorse.position().distanceToSqr(this.center) > 4.0D;
        }

        @Override
        public boolean canContinueToUse() {
            return !this.seahorse.getNavigation().isDone();
        }

        @Override
        public void start() {
            this.seahorse.getNavigation().moveTo(this.center.x, this.center.y, this.center.z, 1.0D);
        }
    }
    //endregion
}
