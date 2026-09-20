package com.crispytwig.naturalist.server.entity.mob;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.NaturalistConfig;
import com.crispytwig.naturalist.server.entity.base.*;
import com.crispytwig.naturalist.server.entity.variant.DataDrivenVariantAnimal;
import com.crispytwig.naturalist.server.entity.ai.goal.EggLayingBreedGoal;
import com.crispytwig.naturalist.server.entity.ai.goal.LayEggGoal;
import com.crispytwig.naturalist.registry.NaturalistEntityTypes;
import com.crispytwig.naturalist.registry.NaturalistRegistry;
import com.crispytwig.naturalist.registry.NaturalistSoundEvents;
import com.crispytwig.naturalist.registry.NaturalistTags;
import com.crispytwig.naturalist.server.entity.climbing.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.crispytwig.naturalist.server.entity.util.SmoothAnimationState;

import java.util.*;

@SuppressWarnings("unused")
public class Snail extends NaturalistAnimal implements Catchable, HidingAnimal, EggLayingAnimal, DataDrivenVariantAnimal, SurfaceCrawler {
    //region Data
    private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.BEETROOT);

    private static final EntityDataAccessor<String> DATA_VARIANT = SynchedEntityData.defineId(Snail.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> DATA_COLOR = SynchedEntityData.defineId(Snail.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> FROM_HAND = SynchedEntityData.defineId(Snail.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HAS_EGG = SynchedEntityData.defineId(Snail.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> LAYING_EGG = SynchedEntityData.defineId(Snail.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Vector3f> ATTACH_NORMAL = SynchedEntityData.defineId(Snail.class, EntityDataSerializers.VECTOR3);

    int layEggCounter;
    int slimeBallTime;

    private static final float BODY_LOOK_FOLLOW = -0.5F;
    private static final float LOOK_LAG_BLEND = 0.15F;
    private static final float TAIL_LOOK_BLEND = 0.15F;
    private static final int HIDE_END_TICKS = 16;

    private final SurfaceClimbing climbing = new SurfaceClimbing(this, ATTACH_NORMAL);
    private boolean wasHiding;
    private int hideEndTicks;
    private float bodyLookYaw;
    private float bodyLookYawO;
    private float tailLookYaw;
    private float tailLookYawO;

    public final SmoothAnimationState idleAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState crawlAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState hideAnimationState = SmoothAnimationState.instant();
    public final SmoothAnimationState hideEndAnimationState = SmoothAnimationState.instant();

    public Snail(@NotNull EntityType<? extends NaturalistAnimal> type, Level level) {
        super(type, level);
        this.moveControl = new SurfaceCrawlerMoveControl(this);
        this.slimeBallTime = this.random.nextInt(1200) + 12000;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 3.0D).add(Attributes.MOVEMENT_SPEED, 0.05F);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT, this.getDefaultVariant().location().toString());
        builder.define(FROM_HAND, false);
        builder.define(DATA_COLOR, Color.BROWN.getId());
        builder.define(HAS_EGG, false);
        builder.define(LAYING_EGG, false);
        builder.define(ATTACH_NORMAL, new Vector3f(0.0F, 1.0F, 0.0F));
    }

    @Override
    public ResourceLocation getFallbackVariantTexture() {
        return Naturalist.location("textures/entity/snail/brown.png");
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
    public boolean hasEgg() {
        return this.entityData.get(HAS_EGG);
    }

    @Override
    public void setHasEgg(boolean hasEgg) {
        this.entityData.set(HAS_EGG, hasEgg);
    }

    @Override
    public Block getEggBlock() {
        return NaturalistRegistry.SNAIL_EGGS.get();
    }

    @Override
    public TagKey<Block> getEggLayableBlockTag() {
        return NaturalistTags.BlockTags.ALLIGATOR_EGG_LAYABLE_ON;
    }

    @Override
    public boolean isLayingEgg() {
        return this.entityData.get(LAYING_EGG);
    }

    @Override
    public void setLayingEgg(boolean isLayingEgg) {
        this.layEggCounter = isLayingEgg ? 1 : 0;
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

    public Color getSnailColor() {
        return Snail.Color.BY_ID[this.entityData.get(DATA_COLOR)];
    }

    public void setSnailColor(Snail.Color color) {
        this.entityData.set(DATA_COLOR, color.getId());
    }

    public DyeColor getColor() {
        return DyeColor.byId(this.entityData.get(DATA_COLOR));
    }

    public void setColor(@NotNull DyeColor color) {
        this.entityData.set(DATA_COLOR, color.getId());
    }

    @Override
    public boolean fromHand() {
        return this.entityData.get(FROM_HAND);
    }

    @Override
    public void setFromHand(boolean fromHand) {
        this.entityData.set(FROM_HAND, fromHand);
    }

    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.fromHand();
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        this.saveVariant(compound);
        compound.putBoolean("FromHand", this.fromHand());
        compound.putByte("Color", (byte)this.getSnailColor().getId());
        compound.putBoolean("HasEgg", this.hasEgg());
        this.climbing.save(compound);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.loadVariant(compound);
        this.setFromHand(compound.getBoolean("FromHand"));
        this.setSnailColor(Color.BY_ID[compound.getInt("Color")]);
        this.setHasEgg(compound.getBoolean("HasEgg"));
        this.climbing.load(compound);
    }

    @Override
    public void saveToHandTag(@NotNull ItemStack stack) {
        Catchable.saveDefaultDataToHandTag(this, stack);
        CompoundTag compoundTag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        this.saveVariant(compoundTag);
        compoundTag.putInt("Color", this.getSnailColor().getId());
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(compoundTag));
    }

    @Override
    public void loadFromHandTag(@NotNull CompoundTag tag) {
        Catchable.loadDefaultDataFromHandTag(this, tag);
        this.loadVariant(tag);
        if (tag.contains("Color", 3)) {
            int i = tag.getInt("Color");
            if (i >= 0 && i < Snail.Color.BY_ID.length) {
                this.setSnailColor(Snail.Color.BY_ID[i]);
            }
        } else {
            this.setSnailColor(Snail.Color.BROWN);
        }
    }

    @Override
    public @NotNull ItemStack getCaughtItemStack() {
        return new ItemStack(NaturalistRegistry.SNAIL.get());
    }

    @Override
    public SoundEvent getPickupSound() {
        return null;
    }

    public enum Color {
        WHITE(0, "white"),
        ORANGE(1, "orange"),
        MAGENTA(2, "magenta"),
        LIGHT_BLUE(3, "light_blue"),
        YELLOW(4, "yellow"),
        LIME(5, "lime"),
        PINK(6, "pink"),
        GRAY(7, "gray"),
        LIGHT_GRAY(8, "light_gray"),
        CYAN(9, "cyan"),
        PURPLE(10, "purple"),
        BLUE(11, "blue"),
        BROWN(12, "brown"),
        GREEN(13, "green"),
        RED(14, "red"),
        BLACK(15, "black");

        public static final Snail.Color[] BY_ID = Arrays.stream(values()).sorted(Comparator.comparingInt(Snail.Color::getId)).toArray(Snail.Color[]::new);
        private final int id;
        private final String name;

        Color(int j, String string2) {
            this.id = j;
            this.name = string2;
        }

        public int getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        public static Snail.Color getTypeById(int id) {
            for (Snail.Color type : values()) {
                if (type.id == id) return type;
            }
            return Snail.Color.BROWN;
        }
    }
    //endregion

    //region Spawning
    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return FOOD_ITEMS.test(stack);
    }

    @Override
    public boolean canFallInLove() {
        return super.canFallInLove() && !this.hasEgg();
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverLevel, @NotNull AgeableMob ageableMob) {
        Snail baby = NaturalistEntityTypes.SNAIL.get().create(serverLevel);
        if (baby != null) {
            baby.setVariantString(this.getOffspringVariantId(ageableMob, this.random));
        }
        return baby;
    }

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        if (spawnType != MobSpawnType.BUCKET) {
            this.selectVariantForSpawn(level);
        }
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }
    //endregion

    //region Behavior
    @Override
    protected void registerGoals() {
        super.registerGoals();

        this.goalSelector.addGoal(1, new EggLayingBreedGoal<>(this, 1.0));
        this.goalSelector.addGoal(1, new LayEggGoal<>(this, 1.0));
        this.goalSelector.addGoal(2, new SnailStrollGoal(this, 0.9D, 0.0F));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        Item item = itemStack.getItem();
        label90: {
                if (!(item instanceof DyeItem dyeItem)) {
                    break label90;
                }

            DyeColor dyeColor = dyeItem.getDyeColor();
                if (dyeColor != this.getColor()) {
                    this.setColor(dyeColor);
                    if (!player.getAbilities().instabuild) {
                        itemStack.shrink(1);
                    }

                    return InteractionResult.SUCCESS;
                }
            }
        return Catchable.catchAnimal(player, hand, this, true).orElse(super.mobInteract(player, hand));
    }

    @Override
    public boolean canHide() {
        List<Player> players = this.level().getNearbyPlayers(TargetingConditions.forNonCombat().range(5.0D).selector(EntitySelector.NO_CREATIVE_OR_SPECTATOR::test), this, this.getBoundingBox().inflate(5.0D, 3.0D, 5.0D));
        return !players.isEmpty();
    }

    @Override
    public void knockback(double strength, double x, double z) {
        super.knockback(this.canHide() ? strength / 4 : strength, x, z);
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        return super.hurt(source, this.canHide() ? amount * 0.8F : amount);
    }

    @Override
    public void travel(@NotNull Vec3 vec3) {
        if (this.canHide() || this.isImmobile()) {
            this.climbing.halt();
        }
        if (!this.isEffectiveAi() || !this.climbing.travel()) {
            super.travel(vec3);
        }
        if (this.canHide()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0, 1, 0));
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.canHide() || this.isImmobile()) {
            this.jumping = false;
            this.xxa = 0.0F;
            this.zza = 0.0F;
        }
        this.checkCrush();
        if (!this.level().isClientSide && this.isAlive() && !this.isBaby() && --this.slimeBallTime <= 0) {
            this.playSound(SoundEvents.SLIME_SQUISH_SMALL, 1.0F, NaturalistAnimal.defaultVoicePitch(this.random));
            this.spawnAtLocation(Items.SLIME_BALL);
            this.slimeBallTime = this.random.nextInt(1200) + 12000;
        }
    }

    private void checkCrush() {
        if (this.level().isClientSide || !NaturalistConfig.isSnailCrushingEnabled()
                || !this.onGround() || this.hasCustomName() || this.fromHand() || this.isPersistenceRequired()) {
            return;
        }
        List<Player> players = this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(0.2D, 0.5D, 0.2D),
                player -> !player.isSpectator() && !player.onGround() && player.getY() > this.getY() && !hasFeatherFalling(player));
        if (!players.isEmpty()) {
            this.hurt(this.damageSources().playerAttack(players.getFirst()), this.getMaxHealth() * 2.0F);
        }
    }

    private static boolean hasFeatherFalling(Player player) {
        Holder<Enchantment> featherFalling = player.level().registryAccess()
                .lookupOrThrow(Registries.ENCHANTMENT)
                .getOrThrow(Enchantments.FEATHER_FALLING);
        return EnchantmentHelper.getEnchantmentLevel(featherFalling, player) > 0;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return NaturalistSoundEvents.SNAIL_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return NaturalistSoundEvents.SNAIL_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return NaturalistSoundEvents.SNAIL_CRUSH.get();
    }

    static class SnailStrollGoal extends WaterAvoidingRandomStrollGoal {
        private boolean surfaceTarget;

        public SnailStrollGoal(PathfinderMob mob, double speedModifier, float probability) {
            super(mob, speedModifier, probability);
            this.forceTrigger = true;
            this.interval = 1;
        }

        @Nullable
        @Override
        protected Vec3 getPosition() {
            this.surfaceTarget = false;
            if (this.mob.getRandom().nextFloat() < 0.3F) {
                Vec3 surface = this.findSurfacePosition();
                if (surface != null) {
                    this.surfaceTarget = true;
                    return surface;
                }
            }
            return super.getPosition();
        }

        @Nullable
        private Vec3 findSurfacePosition() {
            RandomSource random = this.mob.getRandom();
            BlockPos origin = this.mob.blockPosition();
            for (int attempt = 0; attempt < 10; attempt++) {
                BlockPos pos = origin.offset(random.nextInt(13) - 6, random.nextInt(9) - 4, random.nextInt(13) - 6);
                if (!this.mob.level().getBlockState(pos).getCollisionShape(this.mob.level(), pos).isEmpty()) {
                    continue;
                }
                for (Direction direction : Direction.values()) {
                    BlockPos support = pos.relative(direction);
                    if (this.mob.level().getBlockState(support).isFaceSturdy(this.mob.level(), support, direction.getOpposite())) {
                        return Vec3.atCenterOf(pos);
                    }
                }
            }
            return null;
        }

        @Override
        public void start() {
            if (this.surfaceTarget && this.mob.getNavigation() instanceof CrawlerNavigation navigation) {
                navigation.crawlTo(BlockPos.containing(this.wantedX, this.wantedY, this.wantedZ), this.speedModifier);
            } else {
                super.start();
            }
        }
    }
    //endregion

    //region Climbing
    @Override
    public SurfaceClimbing getClimbing() {
        return this.climbing;
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new CrawlerNavigation(this, level);
    }

    @Override
    public void tick() {
        super.tick();
        this.climbing.tick();
        if (this.level().isClientSide) {
            this.updateLookLag();
            this.setupAnimationStates();
        }
    }

    private void updateLookLag() {
        this.bodyLookYawO = this.bodyLookYaw;
        this.tailLookYawO = this.tailLookYaw;
        float target = 0.0F;
        if (!this.walkAnimation.isMoving()) {
            target = Mth.wrapDegrees(this.getYHeadRot() - this.yBodyRot) * BODY_LOOK_FOLLOW;
        }
        this.bodyLookYaw = Mth.rotLerp(LOOK_LAG_BLEND, this.bodyLookYaw, target);
        this.tailLookYaw = Mth.rotLerp(TAIL_LOOK_BLEND, this.tailLookYaw, this.bodyLookYaw);
    }

    public float getBodyLookYaw(float partialTick) {
        return Mth.rotLerp(partialTick, this.bodyLookYawO, this.bodyLookYaw);
    }

    public float getTailLookYaw(float partialTick) {
        return Mth.rotLerp(partialTick, this.tailLookYawO, this.tailLookYaw);
    }

    @Override
    public boolean onClimbable() {
        return false;
    }

    @Override
    protected float getJumpPower() {
        return 0.0F;
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, @NotNull DamageSource source) {
        return false;
    }

    public boolean isClimbing() {
        return this.climbing.isOnSide();
    }

    public boolean isHidden() {
        return this.canHide() || this.isDeadOrDying();
    }
    //endregion

    //region Animation
    private void setupAnimationStates() {
        boolean hidden = this.isHidden();
        if (this.wasHiding && !hidden) {
            this.hideEndTicks = HIDE_END_TICKS;
        }
        this.wasHiding = hidden;
        boolean hideEnd = !hidden && this.hideEndTicks > 0;
        if (this.hideEndTicks > 0) {
            --this.hideEndTicks;
        }
        this.hideAnimationState.animateWhen(hidden, this.tickCount);
        this.hideEndAnimationState.animateWhen(hideEnd, this.tickCount);

        boolean locomotion = !hidden && !hideEnd;
        boolean crawling = locomotion && (NaturalistAnimal.isVisiblyMoving(this) || this.isClimbing());
        this.crawlAnimationState.animateWhen(crawling, this.tickCount);
        this.idleAnimationState.animateWhen(locomotion && !crawling, this.tickCount);
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
