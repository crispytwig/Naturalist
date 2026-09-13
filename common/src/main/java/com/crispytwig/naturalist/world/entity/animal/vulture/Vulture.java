package com.crispytwig.naturalist.world.entity.animal.vulture;

import com.crispytwig.naturalist.tags.NaturalistBlockTags;
import com.crispytwig.naturalist.tags.NaturalistEntityTypeTags;
import net.minecraft.world.entity.animal.Animal;
import com.crispytwig.naturalist.world.entity.animal.NaturalistAnimal;
import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.world.entity.ai.goal.FlyingWanderGoal;
import com.crispytwig.naturalist.world.entity.variant.DataDrivenVariantAnimal;
import com.crispytwig.naturalist.registry.NaturalistSoundEvents;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.crispytwig.naturalist.world.entity.SmoothAnimationState;

import java.util.*;

@SuppressWarnings("unused")
public class Vulture extends PathfinderMob implements DataDrivenVariantAnimal {
    //region Data
    private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.ROTTEN_FLESH);
    private static final int PERCH_COOLDOWN_AFTER_HURT = 200;
    private static final EntityDataAccessor<String> DATA_VARIANT = SynchedEntityData.defineId(Vulture.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> PERCHED = SynchedEntityData.defineId(Vulture.class, EntityDataSerializers.BOOLEAN);

    private int ticksSinceEaten;
    private int perchCooldown;
    @Nullable
    private BlockPos perchTarget;

    public final SmoothAnimationState flyAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState sitAnimationState = new SmoothAnimationState();

    public Vulture(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new FlyingMoveControl<>(this, 20, true);
        this.setCanPickUpLoot(true);
        this.setPathfindingMalus(PathType.FIRE_IN_NEIGHBOR, -1.0F);
        this.setPathfindingMalus(PathType.WATER, -1.0F);
        this.setPathfindingMalus(PathType.WATER_BORDER, 16.0F);
        this.setPathfindingMalus(PathType.COCOA, -1.0F);
        this.setPathfindingMalus(PathType.FENCE, -1.0F);
        this.setPathfindingMalus(PathType.DAMAGING_IN_NEIGHBOR, 0.0F);
        this.setPathfindingMalus(PathType.DAMAGING, 0.0F);
    }

    public static Builder createAttributes() {
        return Animal.createAnimalAttributes().add(Attributes.MAX_HEALTH, 14.0F).add(Attributes.FLYING_SPEED, 0.6F).add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.ATTACK_DAMAGE, 4.0F);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT, this.getDefaultVariant().identifier().toString());
        builder.define(PERCHED, false);
    }

    @Override
    public Identifier getFallbackVariantTexture() {
        return Naturalist.location("textures/entity/vulture.png");
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

    @Nullable
    public BlockPos getPerchTarget() {
        return this.perchTarget;
    }

    public void setPerchTarget(@Nullable BlockPos pos) {
        this.perchTarget = pos;
    }

    public boolean isPerched() {
        return this.entityData.get(PERCHED);
    }

    public void setPerched(boolean perched) {
        this.entityData.set(PERCHED, perched);
    }

    public boolean canPerch() {
        return this.perchCooldown <= 0;
    }
    //endregion

    //region Spawning
    public static boolean checkVultureSpawnRules(EntityType<Vulture> entityType, LevelAccessor state, EntitySpawnReason type, @NotNull BlockPos pos, RandomSource random) {
        return state.getBlockState(pos.below()).is(NaturalistBlockTags.VULTURES_SPAWNABLE_ON) && state.getRawBrightness(pos, 0) > 8;
    }

    @Override
    public boolean isBaby() {
        return false;
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
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new VultureFleePlayerGoal(this, 16.0F, 1.4D));
        this.goalSelector.addGoal(1, new VultureAttackGoal(this, 1.2F, true));
        this.goalSelector.addGoal(2, new VultureSearchForFoodGoal(this, 1.2F, FOOD_ITEMS, 12, 24));
        this.goalSelector.addGoal(2, new VulturePerchOnCactusGoal(this, 1.0D, 32, 32));
        this.goalSelector.addGoal(3, new HighAltitudeFlyingWanderGoal(this));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Monster.class, 10, false, false, (entity, level) -> entity.is(NaturalistEntityTypeTags.VULTURE_HOSTILES) && !FOOD_ITEMS.test(this.getMainHandItem())));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, false, false, (entity, level) -> entity.getHealth() <= 6 && !entity.getMainHandItem().isEmpty() && !FOOD_ITEMS.test(this.getMainHandItem())));
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        VulturePathNavigation navigation = new VulturePathNavigation(this, level);
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(true);
        navigation.getNodeEvaluator().setCanPassDoors(true);
        return navigation;
    }

    @Override
    public boolean causeFallDamage(double fallDistance, float multiplier, @NotNull DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, @NotNull BlockState state, @NotNull BlockPos pos) {
    }

    @Override
    public boolean onClimbable() {
        return false;
    }

    public boolean isFlying() {
        return !this.onGround();
    }

    @Override
    public boolean hurtServer(@NotNull ServerLevel level, @NotNull DamageSource source, float amount) {
        boolean hurt = super.hurtServer(level, source, amount);
        if (hurt) {
            this.startle(source.getSourcePosition());
        }
        return hurt;
    }

    @Override
    public boolean doHurtTarget(@NotNull ServerLevel level, @NotNull Entity target) {
        boolean shouldHurt = true;
        float knockback = (float)this.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
        DamageSource damageSource = target.damageSources().mobAttack(this);
        float damage = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        if (shouldHurt == target.hurtServer(level, damageSource, damage)) {
            if (knockback > 0.0f && target instanceof LivingEntity) {
                ((LivingEntity)target).knockback(knockback * 0.5f, Mth.sin(this.getYRot() * Mth.DEG_TO_RAD), -Mth.cos(this.getYRot() * Mth.DEG_TO_RAD), damageSource, damage);
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.6, 1.0, 0.6));
            }
            this.setLastHurtMob(target);
        }
        return shouldHurt;
    }

    @Override
    public boolean canBeAffected(MobEffectInstance effect) {
        if (effect.is(MobEffects.HUNGER)) {
            return false;
        }
        return super.canBeAffected(effect);
    }

    @Override
    public boolean isInvulnerableTo(@NotNull ServerLevel level, @NotNull DamageSource source) {
        return source.equals(this.damageSources().cactus()) || super.isInvulnerableTo(level, source);
    }

    public void startle(@Nullable Vec3 fleeFrom) {
        this.perchCooldown = PERCH_COOLDOWN_AFTER_HURT;
        this.setPerched(false);
        double dx = 0.0D;
        double dz = 0.0D;
        if (fleeFrom != null) {
            Vec3 away = new Vec3(this.getX() - fleeFrom.x, 0.0D, this.getZ() - fleeFrom.z);
            if (away.lengthSqr() > 1.0E-4D) {
                away = away.normalize();
                dx = away.x * 0.4D;
                dz = away.z * 0.4D;
            }
        }
        this.setDeltaMovement(dx, 0.5D, dz);
        this.needsSync = true;
    }

    @Override
    protected boolean canDispenserEquipIntoSlot(@NotNull EquipmentSlot slot) {
        return !FOOD_ITEMS.test(this.getMainHandItem());
    }

    @Override
    public boolean canHoldItem(@NotNull ItemStack stack) {
        return FOOD_ITEMS.test(stack) && !FOOD_ITEMS.test(this.getMainHandItem());
    }

    @Override
    protected void pickUpItem(@NotNull ServerLevel level, @NotNull ItemEntity itemEntity) {
        ItemStack itemstack = itemEntity.getItem();
        if (this.canHoldItem(itemstack)) {
            if (!this.getMainHandItem().isEmpty() && !FOOD_ITEMS.test(this.getMainHandItem())) {
                this.dropItemStack(this.getMainHandItem());
            }
            this.onItemPickup(itemEntity);
            this.setItemSlot(EquipmentSlot.MAINHAND, itemstack);
            this.take(itemEntity, itemstack.getCount());
            itemEntity.discard();
            this.ticksSinceEaten = 0;
        }
    }

    private void dropItemStack(ItemStack stack) {
        ItemEntity itementity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), stack);
        this.level().addFreshEntity(itementity);
    }

    @Override
    protected void dropAllDeathLoot(@NotNull ServerLevel level, @NotNull DamageSource damageSource) {
        super.dropAllDeathLoot(level, damageSource);

        if (!this.level().isClientSide()) {
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                ItemStack itemStack = this.getItemBySlot(slot);
                if (!itemStack.isEmpty()) {
                    this.spawnAtLocation(level, itemStack);
                    this.setItemSlot(slot, ItemStack.EMPTY);
                }
            }
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
        boolean perched = this.isPerched();
        this.sitAnimationState.animateWhen(perched, this.tickCount);
        this.flyAnimationState.animateWhen(!perched, this.tickCount);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        ProfilerFiller profiler = Profiler.get();
        profiler.push("looting");
        if (this.level() instanceof ServerLevel serverLevel && this.canPickUpLoot() && this.isAlive() && !this.dead && serverLevel.getGameRules().get(GameRules.MOB_GRIEFING)) {
            for(ItemEntity itementity : this.level().getEntitiesOfClass(ItemEntity.class, this.getBoundingBox().inflate(1.0D, 1.0D, 1.0D))) {
                if (!itementity.isRemoved() && !itementity.getItem().isEmpty() && this.wantsToPickUp(serverLevel, itementity.getItem())) {
                    this.pickUpItem(serverLevel, itementity);
                }
            }
        }
        profiler.pop();
        if (!this.level().isClientSide() && this.isAlive() && this.isEffectiveAi()) {
            ++this.ticksSinceEaten;
            if (this.perchCooldown > 0) {
                --this.perchCooldown;
            }
            ItemStack stack = this.getItemBySlot(EquipmentSlot.MAINHAND);
            if (stack.get(DataComponents.FOOD) != null) {
                if (this.ticksSinceEaten > 600) {
                    ItemStack finishedStack = stack.finishUsingItem(this.level(), this);
                    if (!finishedStack.isEmpty()) {
                        this.setItemSlot(EquipmentSlot.MAINHAND, finishedStack);
                    }
                    this.ticksSinceEaten = 0;
                } else if (this.ticksSinceEaten > 560 && this.random.nextFloat() < 0.1f) {
                    this.playSound(NaturalistAnimal.eatingSound(stack), 1.0f, 1.0f);
                    this.level().broadcastEntityEvent(this, (byte)45);
                }
            }
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        super.handleEntityEvent(id);
        if (id == 45) {
            ItemStack itemStack = this.getItemBySlot(EquipmentSlot.MAINHAND);
            if (!itemStack.isEmpty()) {
                for (int i = 0; i < 8; ++i) {
                    Vec3 vec3 = new Vec3(((double)this.random.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, 0.0).xRot(-this.getXRot() * Mth.DEG_TO_RAD).yRot(-this.getYRot() * Mth.DEG_TO_RAD);
                    this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, ItemStackTemplate.fromNonEmptyStack(itemStack)), this.getX() + this.getLookAngle().x / 2.0, this.getY(), this.getZ() + this.getLookAngle().z / 2.0, vec3.x, vec3.y + 0.05, vec3.z);
                }
            }
        }
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return NaturalistSoundEvents.VULTURE_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return NaturalistSoundEvents.VULTURE_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return NaturalistSoundEvents.VULTURE_DEATH.get();
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState state) {
    }

    static class VulturePathNavigation extends FlyingPathNavigation {
        public VulturePathNavigation(@NotNull Mob mob, Level level) {
            super(mob, level);
        }

        @Override
        public boolean isStableDestination(@NotNull BlockPos pos) {
            return super.isStableDestination(pos) && this.mob.level().getBlockState(pos).is(NaturalistBlockTags.VULTURE_PERCH_BLOCKS);
        }
    }

    static class VultureAttackGoal extends MeleeAttackGoal {
        public VultureAttackGoal(@NotNull PathfinderMob mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
            super(mob, speedModifier, followingTargetEvenIfNotSeen);
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.mob.getTarget();
            if (target instanceof Player player) {
                if (player.getHealth() > 6 || player.getMainHandItem().isEmpty())  {
                    return false;
                }
            }
            return this.mob.getMainHandItem().isEmpty() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity target = this.mob.getTarget();
            if (target instanceof Player player) {
                if (player.getHealth() > 6 || player.getMainHandItem().isEmpty())  {
                    return false;
                }
            }
            return this.mob.getMainHandItem().isEmpty() && super.canContinueToUse();
        }

        @Override
        protected void checkAndPerformAttack(@NotNull LivingEntity enemy) {
            if (this.mob.isWithinMeleeAttackRange(enemy) && this.isTimeToAttack()) {
                this.resetAttackCooldown();
                this.mob.swing(InteractionHand.MAIN_HAND);
                if (!(enemy instanceof Player)) {
                    this.mob.doHurtTarget(getServerLevel(this.mob), enemy);
                }
                if (enemy instanceof Player && this.mob.getMainHandItem().isEmpty() && !enemy.getMainHandItem().isEmpty()) {
                    this.mob.setItemSlot(EquipmentSlot.MAINHAND, enemy.getMainHandItem().split(1));
                    Level level = this.mob.level();
                    level.playSound(null, mob.getX(), mob.getY(), mob.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.NEUTRAL, 0.2F, ((level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    this.mob.setTarget(null);
                    this.mob.setAggressive(false);
                }
            }
        }
    }

    static class VultureFleePlayerGoal extends Goal {
        private final Vulture vulture;
        private final double speedModifier;
        private final float detectRange;
        private final TargetingConditions fleeConditions;
        @Nullable
        private Player toAvoid;
        private int recalcTimer;
        private static final int RECALC_INTERVAL = 20;

        public VultureFleePlayerGoal(@NotNull Vulture vulture, float detectRange, double speedModifier) {
            this.vulture = vulture;
            this.detectRange = detectRange;
            this.speedModifier = speedModifier;
            this.fleeConditions = TargetingConditions.forNonCombat().range(detectRange).selector((entity, level) -> EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity));
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (this.vulture.getTarget() != null) {
                return false;
            }
            this.toAvoid = getServerLevel(this.vulture).getNearestPlayer(this.fleeConditions, this.vulture);
            return this.toAvoid != null;
        }

        @Override
        public boolean canContinueToUse() {
            return this.toAvoid != null && this.toAvoid.isAlive()
                    && this.vulture.distanceToSqr(this.toAvoid) < (double) (this.detectRange * this.detectRange);
        }

        @Override
        public void start() {
            this.vulture.startle(Objects.requireNonNull(this.toAvoid).position());
            this.recalcTimer = RECALC_INTERVAL;
            this.fleeAway();
        }

        @Override
        public void tick() {
            if (--this.recalcTimer <= 0) {
                this.recalcTimer = RECALC_INTERVAL;
                this.fleeAway();
            }
        }

        @Override
        public void stop() {
            this.toAvoid = null;
        }

        private void fleeAway() {
            Vec3 fleeTo = DefaultRandomPos.getPosAway(this.vulture, 16, 7, Objects.requireNonNull(this.toAvoid).position());
            if (fleeTo != null && this.toAvoid.distanceToSqr(fleeTo) >= this.toAvoid.distanceToSqr(this.vulture)
                    && this.vulture.getNavigation().moveTo(fleeTo.x, fleeTo.y, fleeTo.z, this.speedModifier)) {
                return;
            }
            Vec3 away = new Vec3(this.vulture.getX() - this.toAvoid.getX(), 0.0D, this.vulture.getZ() - this.toAvoid.getZ());
            away = away.lengthSqr() < 1.0E-4D ? this.vulture.getViewVector(1.0F) : away.normalize();
            this.vulture.getMoveControl().setWantedPosition(
                    this.vulture.getX() + away.x * 12.0D,
                    this.vulture.getY() + 8.0D,
                    this.vulture.getZ() + away.z * 12.0D,
                    this.speedModifier);
        }
    }

    static class VultureSearchForFoodGoal extends Goal {
        private final Vulture mob;
        private final double speedModifier;
        private final double horizontalSearchRange;
        private final double verticalSearchRange;
        private final Ingredient ingredient;

        public VultureSearchForFoodGoal(Vulture mob, double speedModifier, Ingredient ingredient, double horizontalSearchRange, double verticalSearchRange) {
            this.setFlags(EnumSet.of(Flag.MOVE));
            this.mob = mob;
            this.speedModifier = speedModifier;
            this.ingredient = ingredient;
            this.horizontalSearchRange = horizontalSearchRange;
            this.verticalSearchRange = verticalSearchRange;
        }

        @Override
        public boolean canUse() {
            if (!Vulture.FOOD_ITEMS.test(mob.getMainHandItem())) {
                List<ItemEntity> list = mob.level().getEntitiesOfClass(ItemEntity.class, mob.getBoundingBox().inflate(horizontalSearchRange, verticalSearchRange, horizontalSearchRange), itemEntity -> ingredient.test(itemEntity.getItem()));
                return !list.isEmpty() && !Vulture.FOOD_ITEMS.test(mob.getMainHandItem());
            }
            return false;
        }

        @Override
        public void tick() {
            List<ItemEntity> list = mob.level().getEntitiesOfClass(ItemEntity.class, mob.getBoundingBox().inflate(horizontalSearchRange, verticalSearchRange, horizontalSearchRange), itemEntity -> ingredient.test(itemEntity.getItem()));
            if (!Vulture.FOOD_ITEMS.test(mob.getMainHandItem()) && !list.isEmpty()) {
                mob.getNavigation().moveTo(list.getFirst(), speedModifier);
            }

        }

        @Override
        public void start() {
            List<ItemEntity> list = mob.level().getEntitiesOfClass(ItemEntity.class, mob.getBoundingBox().inflate(horizontalSearchRange, verticalSearchRange, horizontalSearchRange), itemEntity -> ingredient.test(itemEntity.getItem()));
            if (!list.isEmpty()) {
                mob.getNavigation().moveTo(list.getFirst(), speedModifier);
            }
        }
    }

    static class VulturePerchOnCactusGoal extends MoveToBlockGoal {
        private final Vulture vulture;
        private final int horizontalRange;
        private final int verticalRange;
        private int perchTime;
        private static final int MAX_PERCH_TIME = 300;
        private static final int MAX_INITIAL_DELAY = 1200;
        private static final double SNAP_DISTANCE_SQR = 0.04D;
        private final Set<BlockPos> reservedByOthers = new HashSet<>();

        public VulturePerchOnCactusGoal(@NotNull Vulture vulture, double speedModifier, int searchRange, int verticalSearchRange) {
            super(vulture, speedModifier, searchRange, verticalSearchRange);
            this.vulture = vulture;
            this.horizontalRange = searchRange;
            this.verticalRange = verticalSearchRange;
            this.nextStartTick = vulture.getRandom().nextInt(MAX_INITIAL_DELAY);
        }

        @Override
        protected boolean isValidTarget(@NotNull LevelReader level, @NotNull BlockPos pos) {
            if (this.reservedByOthers.contains(pos)) {
                return false;
            }
            return level.getBlockState(pos).is(Blocks.CACTUS) && level.getBlockState(pos.above()).isAir();
        }

        @Override
        public boolean canUse() {
            if (this.vulture.getTarget() != null || !this.vulture.canPerch() || Vulture.FOOD_ITEMS.test(this.vulture.getMainHandItem())) {
                return false;
            }
            if (this.nextStartTick <= 0) {
                this.refreshReservations();
            }
            return super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return this.perchTime < MAX_PERCH_TIME && this.vulture.getTarget() == null && this.vulture.canPerch() && super.canContinueToUse();
        }

        @Override
        public void start() {
            super.start();
            this.perchTime = 0;
            this.vulture.setPerchTarget(this.blockPos.immutable());
        }

        @Override
        public void stop() {
            super.stop();
            this.perchTime = 0;
            this.vulture.setPerchTarget(null);
            this.vulture.setPerched(false);
        }

        @Override
        public void tick() {
            super.tick();
            boolean reached = this.isReachedTarget();
            this.vulture.setPerched(reached);
            if (reached) {
                Vec3 perch = new Vec3(this.blockPos.getX() + 0.5D, this.blockPos.getY() + 1, this.blockPos.getZ() + 0.5D);
                Vec3 offset = perch.subtract(this.vulture.position());
                this.vulture.getNavigation().stop();
                if (offset.lengthSqr() > SNAP_DISTANCE_SQR) {
                    this.vulture.setDeltaMovement(offset.scale(0.25D));
                } else {
                    this.vulture.setPos(perch.x, perch.y, perch.z);
                    this.vulture.setDeltaMovement(Vec3.ZERO);
                }
                ++this.perchTime;
            }
        }

        private void refreshReservations() {
            this.reservedByOthers.clear();
            List<Vulture> others = this.vulture.level().getEntitiesOfClass(Vulture.class,
                    this.vulture.getBoundingBox().inflate(this.horizontalRange * 2.0D, this.verticalRange * 2.0D, this.horizontalRange * 2.0D),
                    other -> other != this.vulture && other.getPerchTarget() != null);
            for (Vulture other : others) {
                this.reservedByOthers.add(other.getPerchTarget());
            }
        }
    }

    static class HighAltitudeFlyingWanderGoal extends FlyingWanderGoal {
        private int preferredAltitude = -1;

        private static final int MIN_ALTITUDE_ABOVE_GROUND = 8;
        private static final int MAX_ALTITUDE_ABOVE_GROUND = 20;

        private static final int ALTITUDE_MARGIN_FROM_BUILD_LIMIT = 32;

        private int wanderAttemptsUntilAltitudeChange = 0;
        private static final int ALTITUDE_CHANGE_INTERVAL_MIN = 16;
        private static final int ALTITUDE_CHANGE_INTERVAL_RANGE = 16;

        public HighAltitudeFlyingWanderGoal(Vulture mob) {
            super(mob);
        }

        @Override
        public Vec3 findPos() {
            Vec3 lookVec = mob.getViewVector(0.0F);

            if (preferredAltitude < 0 || wanderAttemptsUntilAltitudeChange <= 0) {
                BlockPos mobPos = mob.blockPosition();
                Level level = mob.level();

                int groundY = level.getHeight(Types.WORLD_SURFACE, mobPos.getX(), mobPos.getZ());
                int minAltitude = groundY + MIN_ALTITUDE_ABOVE_GROUND;
                int maxAltitude = Math.min(groundY + MAX_ALTITUDE_ABOVE_GROUND, level.getMaxY() - ALTITUDE_MARGIN_FROM_BUILD_LIMIT);
                preferredAltitude = Mth.nextInt(mob.getRandom(), minAltitude, maxAltitude);
                wanderAttemptsUntilAltitudeChange = ALTITUDE_CHANGE_INTERVAL_MIN + mob.getRandom().nextInt(ALTITUDE_CHANGE_INTERVAL_RANGE);
            } else {
                wanderAttemptsUntilAltitudeChange--;
            }

            double offsetX = lookVec.x * 8 + mob.getRandom().nextInt(5) - 2;
            double offsetZ = lookVec.z * 8 + mob.getRandom().nextInt(5) - 2;

            return new Vec3(mob.getX() + offsetX, preferredAltitude, mob.getZ() + offsetZ);
        }
    }
    //endregion

}
