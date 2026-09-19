package com.crispytwig.naturalist.world.entity.animal.bird;

import com.crispytwig.naturalist.tags.NaturalistItemTags;
import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.NaturalistConfig;
import com.crispytwig.naturalist.world.entity.animal.NaturalistAnimal;
import com.crispytwig.naturalist.world.entity.DyeableAnimal;
import com.crispytwig.naturalist.world.entity.FollowingPet;
import com.crispytwig.naturalist.world.entity.ai.goal.FollowAdultGoal;
import com.crispytwig.naturalist.world.entity.ai.goal.PetFollowOwnerGoal;
import com.crispytwig.naturalist.world.entity.variant.DataDrivenVariantAnimal;
import com.crispytwig.naturalist.world.entity.variant.MobVariant;
import com.crispytwig.naturalist.registry.NaturalistMobVariants;
import com.crispytwig.naturalist.registry.NaturalistSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.parrot.ShoulderRidingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.crispytwig.naturalist.world.entity.SmoothAnimationState;
import org.jspecify.annotations.NonNull;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public class Bird extends ShoulderRidingEntity implements DyeableAnimal, FollowingPet, DataDrivenVariantAnimal {
    //region Data
    private static final Predicate<ItemStack> TAME_FOOD = stack -> stack.is(NaturalistItemTags.BIRD_FOOD_ITEMS);
    private static final EntityDataAccessor<String> DATA_VARIANT = SynchedEntityData.defineId(Bird.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> DATA_DYE = SynchedEntityData.defineId(Bird.class, EntityDataSerializers.INT);
    private static final Vec3 HEAD_ATTACHMENT = new Vec3(0.0D, -0.05D, 0.0D);

    private boolean followingOwner = true;
    public float flap;
    public float flapSpeed;
    public float oFlapSpeed;
    public float oFlap;
    private float flapping = 1.0F;
    private float nextFlap = 1.0F;

    private float flyPitch;
    private float flyPitchO;

    public final SmoothAnimationState idleAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState walkAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState flyAnimationState = new SmoothAnimationState();

    public Bird(@NotNull EntityType<? extends ShoulderRidingEntity> entityType, @NotNull Level level) {
        super(entityType, level);
        this.moveControl = new FlyingMoveControl(this, 10, false);
        this.setPathfindingMalus(PathType.FIRE_IN_NEIGHBOR, -1.0F);
        this.setPathfindingMalus(PathType.FIRE, -1.0F);
        this.setPathfindingMalus(PathType.COCOA, -1.0F);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Animal.createAnimalAttributes().add(Attributes.MAX_HEALTH, 6.0D).add(Attributes.FLYING_SPEED, 0.8F).add(Attributes.MOVEMENT_SPEED, 0.2D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT, NaturalistMobVariants.BIRD_AMERICAN_ROBIN.identifier().toString());
        builder.define(DATA_DYE, -1);
    }

    @Override
    public ResourceKey<MobVariant> getDefaultVariant() {
        return NaturalistMobVariants.BIRD_AMERICAN_ROBIN;
    }

    @Override
    public Identifier getFallbackVariantTexture() {
        return Naturalist.location("textures/entity/bird/american_robin.png");
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
    public void addAdditionalSaveData(@NotNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        this.saveVariant(output);
        DyeableAnimal.saveDye(this, output);
        FollowingPet.savePet(this, output);
    }

    @Override
    public void readAdditionalSaveData(@NotNull ValueInput input) {
        super.readAdditionalSaveData(input);
        this.loadVariant(input);
        DyeableAnimal.loadDye(this, input);
        FollowingPet.loadPet(this, input);
    }
    //endregion

    //region Spawning
    public static boolean checkBirdSpawnRules(EntityType<Bird> entityType, @NotNull LevelAccessor state, EntitySpawnReason type, @NotNull BlockPos pos, RandomSource random) {
        return state.getBlockState(pos.below()).is(BlockTags.PARROTS_SPAWNABLE_ON) && isBrightEnoughToSpawn(state, pos);
    }

    @Override
    public @NonNull SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull EntitySpawnReason spawnType, @Nullable SpawnGroupData spawnGroupData) {
        this.selectVariantForSpawn(level);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public boolean canMate(@NotNull Animal otherAnimal) {
        return false;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverLevel, @NotNull AgeableMob ageableMob) {
        return null;
    }

    @Override
    public boolean isBaby() {
        return false;
    }
    //endregion

    //region Behavior
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new BirdEatSeedsGoal(this));
        this.goalSelector.addGoal(2, new MountOnOwnersHeadGoal(this));
        this.goalSelector.addGoal(3, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(3, new PetFollowOwnerGoal(this, 1.5D, 5.0F, 1.0F));
        this.goalSelector.addGoal(4, new BirdAvoidPlayerGoal(this));
        this.goalSelector.addGoal(5, new BirdWanderGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new BirdFlockGoal(this, 1.0D, 6.0F, 12.0F));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));

    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, level);
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(true);
        navigation.getNodeEvaluator().setCanPassDoors(true);
        return navigation;
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    protected void doPush(@NotNull Entity entity) {
    }

    @Override
    public boolean causeFallDamage(double fallDistance, float multiplier, @NotNull DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, @NotNull BlockState state, @NotNull BlockPos pos) {
    }

    public boolean isFlying() {
        return !this.onGround();
    }

    @Override
    protected boolean isFlapping() {
        return this.flyDist > this.nextFlap;
    }

    @Override
    protected void onFlap() {
        this.playSound(NaturalistSoundEvents.BIRD_FLY.get(), 0.15F, 1.0F);
        this.nextFlap = this.flyDist + this.flapSpeed / 2.0F;
    }

    @Override
    public boolean hurtServer(@NotNull ServerLevel level, @NotNull DamageSource source, float amount) {
        if (this.isInvulnerableTo(level, source)) {
            return false;
        } else {
            this.setOrderedToSit(false);
            return super.hurtServer(level, source, amount);
        }
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
        if (this.isTame() && this.isOwnedBy(player)) {
            if (TAME_FOOD.test(stack) && this.getHealth() < this.getMaxHealth()) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                this.heal(1.0F);
                if (this.getHealth() == this.getMaxHealth()) {
                    this.spawnTamingParticles(true);
                }
                return InteractionResult.SUCCESS;
            } else if (!this.isFlying()) {
                if (!this.level().isClientSide()) {
                    this.setOrderedToSit(!this.isOrderedToSit());
                }
                return InteractionResult.SUCCESS;
            }

        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getVehicle() instanceof Player player) {
            this.rideOnHead(player);
        }
        if (this.level().isClientSide()) {
            this.tickFlyPitch();
            this.setupAnimationStates();
        }
    }

    private void tickFlyPitch() {
        this.flyPitchO = this.flyPitch;
        float target = this.onGround() ? 0.0F : this.getXRot() * (Mth.PI / 360F) - (float) this.getDeltaMovement().y * 3.0F;
        this.flyPitch += (target - this.flyPitch) * 0.3F;
    }

    public float getFlyPitch(float partialTick) {
        return Mth.lerp(partialTick, this.flyPitchO, this.flyPitch);
    }

    private void setupAnimationStates() {
        boolean flying;
        boolean walking;
        if (this.isPassenger()) {
            flying = !Objects.requireNonNull(Objects.requireNonNull(this.getVehicle())).onGround();
            walking = false;
        } else if (this.isInSittingPose()) {
            flying = false;
            walking = false;
        } else if (this.isFlying()) {
            flying = true;
            walking = false;
        } else {
            flying = false;
            walking = NaturalistAnimal.isVisiblyMoving(this);
        }
        this.flyAnimationState.animateWhen(flying, this.tickCount);
        this.walkAnimationState.animateWhen(walking, this.tickCount);
        this.idleAnimationState.animateWhen(!flying && !walking, this.tickCount);
    }

    private void rideOnHead(@NotNull Player player) {
        this.setYRot(player.getYRot());
        this.yRotO = this.getYRot();
        this.setYBodyRot(player.yBodyRot);
        this.yBodyRotO = this.yBodyRot;
        this.setYHeadRot(player.getYHeadRot());
        this.yHeadRotO = this.yHeadRot;

        if (this.level().isClientSide()) {
            Vec3 movement = player.getDeltaMovement();
            if (NaturalistConfig.isBirdHeadSlowFallingEnabled() && movement.y < 0.0D && !player.onGround() && !player.getAbilities().flying && !player.isFallFlying()) {
                player.setDeltaMovement(movement.multiply(1.0D, 0.6D, 1.0D));
            }
        } else if (player.isCrouching() || !player.isAlive() || player.isSpectator()) {
            this.stopRiding();
            notifyCarrier(player);
        }

        if (player.hasEffect(MobEffects.SLOW_FALLING)) {
            player.resetFallDistance();
        }
    }

    private static void notifyCarrier(@NotNull Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.connection.send(new ClientboundSetPassengersPacket(player));
        }
    }

    @Override
    public @NotNull Vec3 getVehicleAttachmentPoint(@NotNull Entity vehicle) {
        return HEAD_ATTACHMENT;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.calculateFlapping();
    }

    private void calculateFlapping() {
        this.oFlap = this.flap;
        this.oFlapSpeed = this.flapSpeed;
        this.flapSpeed += (float)(!this.onGround() && !this.isPassenger() ? 4 : -1) * 0.3F;
        this.flapSpeed = Mth.clamp(this.flapSpeed, 0.0F, 1.0F);
        if (!this.onGround() && this.flapping < 1.0F) {
            this.flapping = 1.0F;
        }

        this.flapping *= 0.9F;
        Vec3 vec3 = this.getDeltaMovement();
        if (!this.onGround() && vec3.y < 0.0D) {
            this.setDeltaMovement(vec3.multiply(1.0D, 0.6D, 1.0D));
        }

        this.flap += this.flapping * 2.0F;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return NaturalistSoundEvents.BIRD_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return NaturalistSoundEvents.BIRD_DEATH.get();
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        if (this.level().isDarkOutside()) {
            return null;
        }
        return switch (this.getVariantLocation().getPath()) {
            case "blue_jay", "stellers_jay" -> NaturalistSoundEvents.BIRD_AMBIENT_BLUEJAY.get();
            case "northern_cardinal" -> NaturalistSoundEvents.BIRD_AMBIENT_CARDINAL.get();
            case "carolina_chickadee", "tufted_titmouse" -> NaturalistSoundEvents.BIRD_AMBIENT_FINCH.get();
            case "red_winged_blackbird" -> NaturalistSoundEvents.BIRD_AMBIENT_CANARY.get();
            case "white_throated_sparrow" -> NaturalistSoundEvents.BIRD_AMBIENT_SPARROW.get();
            default -> NaturalistSoundEvents.BIRD_AMBIENT_ROBIN.get();
        };
    }

    @Override
    public void playAmbientSound() {
        super.playAmbientSound();
        if (this.level() instanceof ServerLevel serverLevel && !this.level().isDarkOutside()) {
            float f = (float)level().getRandom().nextInt(4) / 24.0f;
            serverLevel.sendParticles(ParticleTypes.NOTE, this.getX(), this.getY() + 1, this.getZ(), 0, f, 0.0, 0.0, 1.0);
        }
    }

    static class BirdWanderGoal extends WaterAvoidingRandomFlyingGoal {
        private final @NotNull Bird bird;

        public BirdWanderGoal(@NotNull Bird mob, double speedModifier) {
            super(mob, speedModifier);
            this.bird = mob;
        }

        @Nullable
        protected Vec3 getPosition() {
            Vec3 vec3 = null;
            if (this.mob.isInWater()) {
                vec3 = LandRandomPos.getPos(this.mob, 15, 15);
            }

            if (this.mob.getRandom().nextFloat() >= this.probability) {
                vec3 = this.getTreePos();
            }

            return vec3 == null ? super.getPosition() : vec3;
        }

        @Nullable
        private Vec3 getTreePos() {
            BlockPos mobPos = this.mob.blockPosition();
            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
            BlockPos.MutableBlockPos mutable1 = new BlockPos.MutableBlockPos();

            for(BlockPos pos : BlockPos.betweenClosed(Mth.floor(this.mob.getX() - 3.0D), Mth.floor(this.mob.getY() - 6.0D), Mth.floor(this.mob.getZ() - 3.0D), Mth.floor(this.mob.getX() + 3.0D), Mth.floor(this.mob.getY() + 6.0D), Mth.floor(this.mob.getZ() + 3.0D))) {
                if (!mobPos.equals(pos)) {
                    BlockState blockstate = this.mob.level().getBlockState(mutable1.setWithOffset(pos, Direction.DOWN));
                    boolean flag = blockstate.getBlock() instanceof LeavesBlock || blockstate.is(BlockTags.LOGS);
                    if (flag && this.mob.level().isEmptyBlock(pos) && this.mob.level().isEmptyBlock(mutable.setWithOffset(pos, Direction.UP))) {
                        return Vec3.atBottomCenterOf(pos);
                    }
                }
            }

            return null;
        }

        @Override
        public boolean canUse() {
            return !this.bird.isTame() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return !this.bird.isTame() && super.canContinueToUse();
        }
    }

    static class BirdFlockGoal extends FollowAdultGoal {
        private final @NotNull Bird bird;

        public BirdFlockGoal(@NotNull Bird mob, double speedModifier, float stopDistance, float areaSize) {
            super(mob, speedModifier, stopDistance, areaSize);
            this.bird = mob;
        }

        @Override
        public boolean canUse() {
            return !this.bird.isTame() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return !this.bird.isTame() && super.canContinueToUse();
        }
    }

    static class BirdAvoidPlayerGoal extends Goal {
        private static final float MAX_DIST = 8.0F;
        private final @NotNull Bird bird;
        private final TargetingConditions avoidTargeting;
        @Nullable
        private Player toAvoid;
        @Nullable
        private Path fleePath;

        public BirdAvoidPlayerGoal(@NotNull Bird bird) {
            this.bird = bird;
            this.avoidTargeting = TargetingConditions.forCombat().range(MAX_DIST)
                    .selector((entity, level) -> EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity) && !entity.isDiscrete());
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (this.bird.isTame()) {
                return false;
            }
            this.toAvoid = getServerLevel(this.bird).getNearestPlayer(this.avoidTargeting, this.bird);
            if (this.toAvoid == null) {
                return false;
            }
            Vec3 flee = this.getFleePosition();
            if (flee == null) {
                return false;
            }
            this.fleePath = this.bird.getNavigation().createPath(flee.x, flee.y, flee.z, 0);
            return this.fleePath != null;
        }

        @Override
        public boolean canContinueToUse() {
            return !this.bird.isTame() && !this.bird.getNavigation().isDone();
        }

        @Override
        public void start() {
            this.bird.getNavigation().moveTo(this.fleePath, 1.4D);
        }

        @Override
        public void stop() {
            this.toAvoid = null;
        }

        @Override
        public void tick() {
            if (this.toAvoid != null) {
                this.bird.getNavigation().setSpeedModifier(this.bird.distanceToSqr(this.toAvoid) < 49.0D ? 1.8D : 1.4D);
            }
        }

        @Nullable
        private Vec3 getFleePosition() {
            Vec3 away = this.bird.position().subtract(Objects.requireNonNull(this.toAvoid).position());
            Vec3 pos = HoverRandomPos.getPos(this.bird, 16, 7, away.x, away.z, (float) (Math.PI / 2), 3, 1);
            return pos != null ? pos : AirAndWaterRandomPos.getPos(this.bird, 16, 4, -2, away.x, away.z, (float) (Math.PI / 2));
        }
    }

    static class BirdEatSeedsGoal extends Goal {
        private final @NotNull Bird bird;
        @Nullable
        private ItemEntity targetSeeds;
        private int eatCooldown;

        public BirdEatSeedsGoal(@NotNull Bird bird) {
            this.bird = bird;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (this.bird.isTame()) {
                return false;
            }
            this.targetSeeds = this.findSeeds();
            return this.targetSeeds != null;
        }

        @Override
        public boolean canContinueToUse() {
            return !this.bird.isTame() && this.targetSeeds != null && this.targetSeeds.isAlive()
                    && TAME_FOOD.test(this.targetSeeds.getItem());
        }

        @Override
        public void stop() {
            this.targetSeeds = null;
            this.bird.getNavigation().stop();
        }

        @Override
        public void tick() {
            if (this.targetSeeds == null) {
                return;
            }
            this.bird.getLookControl().setLookAt(this.targetSeeds, 30.0F, 30.0F);
            this.bird.getNavigation().moveTo(this.targetSeeds, 1.0D);
            if (this.eatCooldown > 0) {
                this.eatCooldown--;
            } else if (this.bird.distanceToSqr(this.targetSeeds) < 1.5D) {
                this.eatSeed();
            }
        }

        private void eatSeed() {
            this.eatCooldown = 20;
            Level level = this.bird.level();
            ItemStack stack = Objects.requireNonNull(this.targetSeeds).getItem();
            if (!this.bird.isSilent()) {
                level.playSound(null, this.bird.getX(), this.bird.getY(), this.bird.getZ(),
                        NaturalistSoundEvents.BIRD_EAT.get(), this.bird.getSoundSource(),
                        1.0F, 1.0F + (this.bird.random.nextFloat() - this.bird.random.nextFloat()) * 0.2F);
            }
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, ItemStackTemplate.fromNonEmptyStack(stack)),
                        this.targetSeeds.getX(), this.targetSeeds.getY() + 0.15D, this.targetSeeds.getZ(),
                        8, 0.15D, 0.1D, 0.15D, 0.02D);
            }
            Entity thrower = this.targetSeeds.getOwner();
            stack.shrink(1);
            if (stack.isEmpty()) {
                this.targetSeeds.discard();
            }
            if (thrower instanceof Player player && this.bird.random.nextInt(10) == 0) {
                this.bird.tame(player);
                level.broadcastEntityEvent(this.bird, (byte) 7);
            } else {
                level.broadcastEntityEvent(this.bird, (byte) 6);
            }
        }

        @Nullable
        private ItemEntity findSeeds() {
            List<ItemEntity> list = this.bird.level().getEntitiesOfClass(ItemEntity.class,
                    this.bird.getBoundingBox().inflate(8.0D, 4.0D, 8.0D),
                    item -> item.isAlive() && TAME_FOOD.test(item.getItem()));
            return list.isEmpty() ? null : list.getFirst();
        }
    }

    static class MountOnOwnersHeadGoal extends Goal {
        private final @NotNull Bird bird;

        public MountOnOwnersHeadGoal(@NotNull Bird bird) {
            this.bird = bird;
        }

        @Override
        public boolean canUse() {
            if (!this.bird.isTame() || this.bird.isOrderedToSit() || this.bird.isPassenger() || this.bird.isLeashed()) {
                return false;
            }
            if (!(this.bird.getOwner() instanceof Player player)) {
                return false;
            }
            return !player.isSpectator()
                    && !player.getAbilities().flying
                    && !player.isInWater()
                    && !player.isCrouching()
                    && player.getPassengers().isEmpty()
                    && this.bird.getBoundingBox().intersects(player.getBoundingBox());
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void start() {
            if (this.bird.getOwner() instanceof Player player) {
                this.bird.getNavigation().stop();
                if (this.bird.startRiding(player)) {
                    notifyCarrier(player);
                }
            }
        }
    }
    //endregion

}
