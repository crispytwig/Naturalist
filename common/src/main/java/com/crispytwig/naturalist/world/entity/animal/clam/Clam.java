package com.crispytwig.naturalist.world.entity.animal.clam;

import net.minecraft.world.entity.animal.Animal;
import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.registry.NaturalistMobVariants;
import com.crispytwig.naturalist.registry.NaturalistRegistry;
import com.crispytwig.naturalist.registry.NaturalistSoundEvents;
import com.crispytwig.naturalist.world.entity.SmoothAnimationState;
import com.crispytwig.naturalist.world.entity.variant.DataDrivenVariantAnimal;
import com.crispytwig.naturalist.world.entity.variant.MobVariant;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.fish.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("unused")
public class Clam extends WaterAnimal implements DataDrivenVariantAnimal {
    //region Data
    public static final String[] VARIANT_NAMES = {"brown", "white"};

    private static final ResourceKey<MobVariant> DEFAULT_VARIANT = NaturalistMobVariants.createKey(NaturalistMobVariants.registryFor("clam"), "brown");

    private static final EntityDataAccessor<String> DATA_VARIANT = SynchedEntityData.defineId(Clam.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> DATA_OPEN = SynchedEntityData.defineId(Clam.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_HAS_TREASURE = SynchedEntityData.defineId(Clam.class, EntityDataSerializers.BOOLEAN);

    private static final int OPEN_ANIM_TICKS = 25;
    private static final int CLOSE_ANIM_TICKS = 10;

    private int stateTimer;
    private int snapCooldown;
    private int launchPushDelay;

    private boolean openStateInitialised;
    private boolean lastOpenClient;
    private int openAnimTicks;
    private int closeAnimTicks;

    public final SmoothAnimationState idleOpenAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState idleClosedAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState openAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState closeAnimationState = new SmoothAnimationState();

    public Clam(EntityType<? extends WaterAnimal> entityType, Level level) {
        super(entityType, level);
        this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
        this.stateTimer = 200 + this.random.nextInt(201);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.0D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT, DEFAULT_VARIANT.identifier().toString());
        builder.define(DATA_OPEN, false);
        builder.define(DATA_HAS_TREASURE, false);
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
        return Naturalist.location("textures/entity/clam/brown_clam.png");
    }

    @Override
    public String getVariantString() {
        return this.entityData.get(DATA_VARIANT);
    }

    @Override
    public void setVariantString(String location) {
        this.entityData.set(DATA_VARIANT, location);
    }

    public boolean isOpen() {
        return this.entityData.get(DATA_OPEN);
    }

    public void setOpen(boolean open) {
        this.entityData.set(DATA_OPEN, open);
    }

    public boolean hasTreasure() {
        return this.entityData.get(DATA_HAS_TREASURE);
    }

    public void setHasTreasure(boolean hasTreasure) {
        this.entityData.set(DATA_HAS_TREASURE, hasTreasure);
    }

    @Override
    protected void addAdditionalSaveData(@NotNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        this.saveVariant(output);
        output.putBoolean("Open", this.isOpen());
        output.putBoolean("HasTreasure", this.hasTreasure());
    }

    @Override
    protected void readAdditionalSaveData(@NotNull ValueInput input) {
        super.readAdditionalSaveData(input);
        this.loadVariant(input);
        this.setOpen(input.getBooleanOr("Open", false));
        this.setHasTreasure(input.getBooleanOr("HasTreasure", false));
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.hasTreasure();
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return distance > 16384.0D && !this.hasTreasure() && !this.hasCustomName();
    }
    //endregion

    //region Spawning
    public static boolean checkClamSpawnRules(EntityType<? extends Clam> type, ServerLevelAccessor level, EntitySpawnReason reason, BlockPos pos, RandomSource random) {
        return pos.getY() < level.getLevel().getSeaLevel()
                && level.getFluidState(pos).is(FluidTags.WATER)
                && level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull EntitySpawnReason reason, @Nullable SpawnGroupData spawnData) {
        this.selectVariantForSpawn(level);
        if (this.random.nextFloat() < 0.05F) {
            this.setHasTreasure(true);
            this.setItemSlot(EquipmentSlot.MAINHAND, this.randomTreasure());
        }
        return super.finalizeSpawn(level, difficulty, reason, spawnData);
    }

    private ItemStack randomTreasure() {
        int roll = this.random.nextInt(125);
        if (roll < 5) {
            return new ItemStack(Items.HEART_OF_THE_SEA);
        } else if (roll < 35) {
            return new ItemStack(Items.GOLDEN_APPLE);
        } else if (roll < 75) {
            return new ItemStack(Items.ENDER_PEARL);
        }
        return new ItemStack(NaturalistRegistry.CAPTURE_NET.get());
    }
    //endregion

    //region Behavior
    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void customServerAiStep(@NotNull ServerLevel level) {
        super.customServerAiStep(level);
        if (this.snapCooldown > 0) {
            this.snapCooldown--;
        }
        if (this.launchPushDelay > 0 && --this.launchPushDelay == 0) {
            this.launchBurst();
        }
        if (this.isOpen()) {
            if (this.snapCooldown <= 0 && this.hasTreasure() && this.intruderNear(3.5D, 2.0D)) {
                this.slamShut();
                return;
            }
        } else if (this.snapCooldown <= 0 && this.intruderNear(1.0D, 0.5D)) {
            this.snapCooldown = 60;
            this.launch();
            return;
        }
        if (--this.stateTimer > 0) {
            return;
        }
        if (this.isOpen()) {
            this.closeShell();
        } else {
            this.openShell();
        }
    }

    private void openShell() {
        this.setOpen(true);
        this.stateTimer = 100 + this.random.nextInt(101);
    }

    private void closeShell() {
        this.setOpen(false);
        this.stateTimer = 200 + this.random.nextInt(201);
    }

    private void slamShut() {
        this.setOpen(false);
        this.stateTimer = 200 + this.random.nextInt(201);
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), NaturalistSoundEvents.CLAM_LAUNCH.get(), this.getSoundSource(), 1.0F, 1.0F);
    }

    private boolean intruderNear(double horizontal, double vertical) {
        return !this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(horizontal, vertical, horizontal),
                player -> !player.isCreative() && !player.isSpectator() && !player.isShiftKeyDown()).isEmpty();
    }

    private void launch() {
        this.setOpen(true);
        this.stateTimer = 100 + this.random.nextInt(101);
        this.launchPushDelay = 4;
    }

    private void launchBurst() {
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), NaturalistSoundEvents.CLAM_LAUNCH.get(), this.getSoundSource(), 1.0F, 1.0F);
        if (this.level() instanceof ServerLevel server) {
            server.sendParticles(ParticleTypes.BUBBLE_COLUMN_UP, this.getX(), this.getY() + 0.3D, this.getZ(), 30, 0.4D, 0.2D, 0.4D, 0.1D);
            server.sendParticles(ParticleTypes.BUBBLE, this.getX(), this.getY() + 0.3D, this.getZ(), 20, 0.4D, 0.2D, 0.4D, 0.05D);
        }
        List<Player> targets = this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(1.2D, 0.6D, 1.2D),
                player -> !player.isCreative() && !player.isSpectator());
        for (Player target : targets) {
            target.setDeltaMovement(target.getDeltaMovement().add(0.0D, 2.2D, 0.0D));
            target.hurtMarked = true;
        }
    }

    @Override
    public boolean hurtServer(@NotNull ServerLevel level, @NotNull DamageSource source, float amount) {
        if (!this.isOpen()
                && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)
                && !source.is(DamageTypes.DROWN)
                && !source.is(DamageTypes.IN_WALL)
                && !source.is(DamageTypes.LAVA)) {
            return false;
        }
        return super.hurtServer(level, source, amount);
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if (player.isCreative() && player.getItemInHand(hand).is(Items.DEBUG_STICK)) {
            if (!this.level().isClientSide()) {
                this.setOpen(true);
                this.stateTimer = 200;
                this.snapCooldown = 200;
            }
            return InteractionResult.SUCCESS;
        }
        if (this.isOpen() && this.hasTreasure() && !this.getMainHandItem().isEmpty()) {
            if (!this.level().isClientSide()) {
                ItemStack treasure = this.getMainHandItem().copy();
                this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                this.setHasTreasure(false);
                if (!player.addItem(treasure)) {
                    player.drop(treasure, false);
                }
                this.closeShell();
            }
            return InteractionResult.SUCCESS;
        }
        ItemStack inHand = player.getItemInHand(hand);
        if (this.isOpen() && !this.hasTreasure() && !inHand.isEmpty()) {
            if (!this.level().isClientSide()) {
                this.setItemSlot(EquipmentSlot.MAINHAND, inHand.copyWithCount(1));
                this.setHasTreasure(true);
                if (!player.getAbilities().instabuild) {
                    inHand.shrink(1);
                }
            }
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return NaturalistSoundEvents.CLAM_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return NaturalistSoundEvents.CLAM_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return NaturalistSoundEvents.CLAM_DEATH.get();
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
        boolean open = this.isOpen();
        if (!this.openStateInitialised) {
            this.openStateInitialised = true;
            this.lastOpenClient = open;
        } else if (open != this.lastOpenClient) {
            this.lastOpenClient = open;
            if (open) {
                this.openAnimTicks = OPEN_ANIM_TICKS;
                this.closeAnimTicks = 0;
            } else {
                this.closeAnimTicks = CLOSE_ANIM_TICKS;
                this.openAnimTicks = 0;
            }
        } else {
            if (this.openAnimTicks > 0) {
                this.openAnimTicks--;
            }
            if (this.closeAnimTicks > 0) {
                this.closeAnimTicks--;
            }
        }

        boolean opening = this.openAnimTicks > 0;
        boolean closing = this.closeAnimTicks > 0;

        this.openAnimationState.animateWhen(opening, this.tickCount);
        this.closeAnimationState.animateWhen(closing, this.tickCount);
        this.idleOpenAnimationState.animateWhen(!opening && !closing && open, this.tickCount);
        this.idleClosedAnimationState.animateWhen(!opening && !closing && !open, this.tickCount);
    }
    //endregion
}
