package com.crispytwig.naturalist.world.entity.animal.equine;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.registry.NaturalistEntityTypes;
import com.crispytwig.naturalist.registry.NaturalistSoundEvents;
import com.crispytwig.naturalist.world.entity.variant.DataDrivenVariantAnimal;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.equine.AbstractChestedHorse;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.crispytwig.naturalist.world.entity.SmoothAnimationState;

import java.util.Objects;

@SuppressWarnings("unused")
public class Zebra extends AbstractChestedHorse implements DataDrivenVariantAnimal {
    //region Data
    private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.WHEAT, Items.SUGAR, Blocks.HAY_BLOCK.asItem(), Items.APPLE, Items.GOLDEN_CARROT, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE);

    private static final EntityDataAccessor<String> DATA_VARIANT = SynchedEntityData.defineId(Zebra.class, EntityDataSerializers.STRING);

    public final SmoothAnimationState idleAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState walkAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState runAnimationState = new SmoothAnimationState();

    public Zebra(@NotNull EntityType<? extends AbstractChestedHorse> entityType, @NotNull Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT, this.getDefaultVariant().identifier().toString());
    }

    @Override
    public Identifier getFallbackVariantTexture() {
        return Naturalist.location("textures/entity/zebra.png");
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
    protected void randomizeAttributes(@NotNull RandomSource randomSource) {
        Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(this.generateRandomMaxHealth(randomSource));
        Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(this.generateRandomSpeed(randomSource));
        Objects.requireNonNull(this.getAttribute(Attributes.JUMP_STRENGTH)).setBaseValue(this.generateRandomJumpStrength(randomSource));
    }

    protected float generateRandomMaxHealth(@NotNull RandomSource randomSource) {
        return 15.0f + randomSource.nextInt(8) + randomSource.nextInt(9);
    }

    protected double generateRandomJumpStrength(RandomSource randomSource) {
        return 0.4f + randomSource.nextDouble() * 0.1;
    }

    protected double generateRandomSpeed(@NotNull RandomSource randomSource) {
        return (0.5f + randomSource.nextDouble() * 0.3 + randomSource.nextDouble() * 0.3 + randomSource.nextDouble() * 0.3) * 0.25;
    }
    //endregion

    //region Spawning
    @Override
    public boolean canMate(@NotNull Animal otherAnimal) {
        if (otherAnimal == this) {
            return false;
        }
        if (otherAnimal instanceof Zebra zebra) {
            return this.canParent() && zebra.canParent();
        }
        return false;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverLevel, @NotNull AgeableMob ageableMob) {
        AbstractHorse zebra = NaturalistEntityTypes.ZEBRA.get().create(serverLevel, EntitySpawnReason.BREEDING);
        assert zebra != null;
        if (zebra instanceof Zebra baby) {
            baby.setVariantString(this.getOffspringVariantId(ageableMob, this.random));
        }
        this.setOffspringAttributes(ageableMob, zebra);
        return zebra;
    }

    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull EntitySpawnReason spawnType, @Nullable SpawnGroupData spawnGroupData) {
        this.selectVariantForSpawn(level);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }
    //endregion

    //region Behavior
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25));
        this.goalSelector.addGoal(1, new RunAroundLikeCrazyGoal(this, 1.6));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0, AbstractHorse.class));
        this.goalSelector.addGoal(3, new ZebraTemptGoal(this, 1.25, FOOD_ITEMS, true));
        this.goalSelector.addGoal(4, new ZebraAvoidPlayersGoal(this, 16.0f, 1.6D, 1.6D));
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.0));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.7));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.96f;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return NaturalistSoundEvents.ZEBRA_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return NaturalistSoundEvents.ZEBRA_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return NaturalistSoundEvents.ZEBRA_DEATH.get();
    }

    @Override
    @Nullable
    protected SoundEvent getEatingSound() {
        return NaturalistSoundEvents.ZEBRA_EAT.get();
    }

    @Override
    protected SoundEvent getAngrySound() {
        return NaturalistSoundEvents.ZEBRA_ANGRY.get();
    }

    @Override
    protected void playGallopSound(@NotNull SoundType soundType) {
        super.playGallopSound(soundType);
        if (this.random.nextInt(10) == 0) {
            this.playSound(NaturalistSoundEvents.ZEBRA_BREATHE.get(), soundType.getVolume() * 0.6f, soundType.getPitch());
        }
    }

    @Override
    protected void playJumpSound() {
        this.playSound(NaturalistSoundEvents.ZEBRA_JUMP.get(), 0.4F, 1.0F);
    }

    @Override
    protected void playChestEquipsSound() {
        this.playSound(SoundEvents.MULE_CHEST, 1.0f, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
    }

    static class ZebraAvoidPlayersGoal extends AvoidEntityGoal<Player> {
        private final Zebra zebra;

        public ZebraAvoidPlayersGoal(Zebra zebra, float maxDistance, double walkSpeed, double sprintSpeed) {
            super(zebra, Player.class, maxDistance, walkSpeed, sprintSpeed, EntitySelector.NO_CREATIVE_OR_SPECTATOR::test);
            this.zebra = zebra;
        }

        @Override
        public boolean canUse() {
            return !this.zebra.isTamed() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return !this.zebra.isTamed() && super.canContinueToUse();
        }
    }

    static class ZebraTemptGoal extends TemptGoal {
        private final Zebra zebra;

        public ZebraTemptGoal(Zebra zebra, double speedModifier, Ingredient items, boolean canScare) {
            super(zebra, speedModifier, items, canScare);
            this.zebra = zebra;
        }

        @Override
        protected boolean canScare() {
            return super.canScare() && !this.zebra.isTamed();
        }
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
        double horizontalSpeedSqr = this.getDeltaMovement().horizontalDistanceSqr();
        boolean moving = horizontalSpeedSqr > 1.0E-6;
        boolean running = this.isSprinting() || horizontalSpeedSqr > 0.01;

        this.walkAnimationState.animateWhen(moving && !running, this.tickCount);
        this.runAnimationState.animateWhen(moving && running, this.tickCount);
        this.idleAnimationState.animateWhen(!moving, this.tickCount);
    }
    //endregion
}
