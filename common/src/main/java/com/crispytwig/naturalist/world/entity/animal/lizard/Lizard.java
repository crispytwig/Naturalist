package com.crispytwig.naturalist.world.entity.animal.lizard;

import com.crispytwig.naturalist.tags.NaturalistItemTags;
import net.minecraft.world.entity.animal.Animal;
import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.registry.NaturalistEntityTypes;
import com.crispytwig.naturalist.registry.NaturalistMobVariants;
import com.crispytwig.naturalist.registry.NaturalistSoundEvents;
import com.crispytwig.naturalist.world.entity.ai.goal.PetFollowOwnerGoal;
import com.crispytwig.naturalist.world.entity.animal.NaturalistAnimal;
import com.crispytwig.naturalist.world.entity.DyeableAnimal;
import com.crispytwig.naturalist.world.entity.FollowingPet;
import com.crispytwig.naturalist.world.entity.variant.DataDrivenVariantAnimal;
import com.crispytwig.naturalist.world.entity.variant.MobVariant;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.crispytwig.naturalist.world.entity.SmoothAnimationState;

import java.util.function.Predicate;
import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("unused")
public class Lizard extends TamableAnimal implements DyeableAnimal, FollowingPet, DataDrivenVariantAnimal {
    //region Data
    public static final String[] VARIANT_NAMES = {"green", "brown", "beardie", "leopard_gecko"};

    private static final String DEFAULT_VARIANT_ID = "naturalist:green";

    private static final Predicate<ItemStack> TEMPT_INGREDIENT = stack -> stack.is(NaturalistItemTags.LIZARD_TEMPT_ITEMS);
    private static final EntityDataAccessor<String> VARIANT_ID = SynchedEntityData.defineId(Lizard.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> HAS_TAIL = SynchedEntityData.defineId(Lizard.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_DYE = SynchedEntityData.defineId(Lizard.class, EntityDataSerializers.INT);

    private boolean followingOwner = true;
    private int tailRegrowCooldown = 0;

    public final SmoothAnimationState walkAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState sitAnimationState = SmoothAnimationState.pose();

    public Lizard(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes().add(Attributes.MOVEMENT_SPEED, 0.3f).add(Attributes.MAX_HEALTH, 8.0).add(Attributes.ATTACK_DAMAGE, 2.0);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(VARIANT_ID, DEFAULT_VARIANT_ID);
        builder.define(HAS_TAIL, true);
        builder.define(DATA_DYE, -1);
    }

    @Override
    public ResourceKey<MobVariant> getDefaultVariant() {
        return NaturalistMobVariants.createKey(NaturalistMobVariants.registryFor("lizard"), "green");
    }

    @Override
    public String[] getLegacyVariantNames() {
        return VARIANT_NAMES;
    }

    @Override
    public Identifier getFallbackVariantTexture() {
        return Naturalist.location("textures/entity/lizard/green.png");
    }

    @Override
    public String getVariantString() {
        return this.entityData.get(VARIANT_ID);
    }

    @Override
    public void setVariantString(String location) {
        this.entityData.set(VARIANT_ID, location);
    }

    public boolean hasTail() {
        return this.entityData.get(HAS_TAIL);
    }

    public void setHasTail(boolean hasTail) {
        this.entityData.set(HAS_TAIL, hasTail);
    }

    @Override
    public boolean isFollowingOwner() {
        return this.followingOwner;
    }

    @Override
    public void setFollowingOwner(boolean following) {
        this.followingOwner = following;
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
    public void setTame(boolean tamed, boolean applyTameEffects) {
        super.setTame(tamed, applyTameEffects);
        if (tamed) {
            Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(20.0);
            this.setHealth(20.0f);
        } else {
            Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(8.0);
        }
        Objects.requireNonNull(this.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(4.0);
    }

    @Override
    protected void addAdditionalSaveData(@NotNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        this.saveVariant(output);
        output.putBoolean("HasTail", this.hasTail());
        DyeableAnimal.saveDye(this, output);
        FollowingPet.savePet(this, output);
    }

    @Override
    protected void readAdditionalSaveData(@NotNull ValueInput input) {
        super.readAdditionalSaveData(input);
        this.loadVariant(input);
        this.setHasTail(input.getBooleanOr("HasTail", false));
        DyeableAnimal.loadDye(this, input);
        FollowingPet.loadPet(this, input);
    }
    //endregion

    //region Spawning
    @Override
    public @NotNull SpawnGroupData finalizeSpawn(ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull EntitySpawnReason reason, @Nullable SpawnGroupData spawnData) {
        this.selectVariantForSpawn(level);
        return super.finalizeSpawn(level, difficulty, reason, spawnData);
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverLevel, @NotNull AgeableMob ageableMob) {
        return null;
    }
    //endregion

    //region Behavior
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(3, new LizardTemptGoal(this, 0.6, TEMPT_INGREDIENT, true));
        this.goalSelector.addGoal(6, new PetFollowOwnerGoal(this, 1.0, 10.0f, 2.0f));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
    }

    @Override
    public boolean hurtServer(@NotNull ServerLevel level, @NotNull DamageSource source, float amount) {
        if (this.hasTail() && this.getHealth() <= this.getMaxHealth() / 2) {
            this.setHasTail(false);
            this.playSound(SoundEvents.SLIME_SQUISH, 1.0f, 1.0f);
            LizardTail lizardTail = NaturalistEntityTypes.LIZARD_TAIL.get().create(level, EntitySpawnReason.MOB_SUMMONED);
            if (lizardTail != null) {
                lizardTail.setVariantString(this.getVariantString());
                lizardTail.setPos(this.getX(), this.getY(), this.getZ());
                this.level().addFreshEntity(lizardTail);
            }
            for (Mob mob : this.level().getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(8.0), entity -> entity.getTarget() == this)) {
                mob.setTarget(lizardTail);
            }
            this.tailRegrowCooldown = 12000;
        }
        return super.hurtServer(level, source, amount);
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
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
        if (this.level().isClientSide()) {
            return (this.isOwnedBy(player) || this.isTame() || TEMPT_INGREDIENT.test(stack) && !this.isTame()) ? InteractionResult.CONSUME : InteractionResult.PASS;
        }
        if (this.isTame()) {
            if (TEMPT_INGREDIENT.test(stack) && this.getHealth() < this.getMaxHealth()) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                this.heal(5);
                if (!this.hasTail() && this.getHealth() >= this.getMaxHealth()) {
                    this.setHasTail(true);
                    this.playSound(SoundEvents.SLIME_SQUISH, 1.0f, 1.0f);
                }
                return InteractionResult.SUCCESS;
            }
            InteractionResult interactionResult = super.mobInteract(player, hand);
            if (interactionResult.consumesAction() && !this.isBaby() || !this.isOwnedBy(player)) return interactionResult;
            this.setOrderedToSit(!this.isOrderedToSit());
            this.jumping = false;
            this.navigation.stop();
            this.setTarget(null);
            return InteractionResult.SUCCESS;
        }
        if (!TEMPT_INGREDIENT.test(stack)) return super.mobInteract(player, hand);
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        if (this.random.nextInt(3) == 0) {
            this.tame(player);
            this.navigation.stop();
            this.setTarget(null);
            this.setOrderedToSit(true);
            this.level().broadcastEntityEvent(this, (byte)7);
            return InteractionResult.SUCCESS;
        } else {
            this.level().broadcastEntityEvent(this, (byte)6);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.hasTail() && !this.level().isClientSide()) {
            if (this.tailRegrowCooldown > 0) {
                --this.tailRegrowCooldown;
            } else {
                this.playSound(SoundEvents.SLIME_SQUISH, 1.0f, 1.0f);
                this.setHasTail(true);
            }
        }
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return NaturalistSoundEvents.LIZARD_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return NaturalistSoundEvents.LIZARD_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return NaturalistSoundEvents.LIZARD_DEATH.get();
    }

    static class LizardTemptGoal extends TemptGoal {
        @Nullable
        private Player selectedPlayer;
        private final Lizard lizard;

        public LizardTemptGoal(Lizard lizard, double speedModifier, Predicate<ItemStack> ingredient, boolean canScare) {
            super(lizard, speedModifier, ingredient, canScare);
            this.lizard = lizard;
        }

        @Override
        public void tick() {
            super.tick();
            if (this.selectedPlayer == null && this.mob.getRandom().nextInt(this.adjustedTickDelay(600)) == 0) {
                this.selectedPlayer = this.player;
            } else if (this.mob.getRandom().nextInt(this.adjustedTickDelay(500)) == 0) {
                this.selectedPlayer = null;
            }
        }

        @Override
        protected boolean canScare() {
            if (this.selectedPlayer != null && this.selectedPlayer.equals(this.player)) {
                return false;
            }
            return super.canScare();
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !this.lizard.isTame();
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
        boolean sitting = this.isInSittingPose();
        this.sitAnimationState.animateWhen(sitting, this.tickCount);
        this.walkAnimationState.animateWhen(!sitting && NaturalistAnimal.isVisiblyMoving(this), this.tickCount);
    }
    //endregion

    @Override
    protected void doPush(@NotNull Entity entity) {
    }
}
