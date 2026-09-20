package com.crispytwig.naturalist.world.entity.animal.dragonfly;

import com.crispytwig.naturalist.tags.NaturalistBlockTags;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.core.particles.PowerParticleOption;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.registry.NaturalistMobVariants;
import com.crispytwig.naturalist.world.entity.variant.DataDrivenVariantAnimal;
import com.crispytwig.naturalist.world.entity.variant.MobVariant;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import com.crispytwig.naturalist.registry.NaturalistSoundEvents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import com.crispytwig.naturalist.world.entity.SmoothAnimationState;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class Dragonfly extends PathfinderMob implements DataDrivenVariantAnimal {
    //region Data
    public static final String[] VARIANT_NAMES = {"blue", "green", "red"};

    private static final String DEFAULT_VARIANT_ID = "naturalist:blue";

    private static final EntityDataAccessor<String> VARIANT_ID = SynchedEntityData.defineId(Dragonfly.class, EntityDataSerializers.STRING);

    @Nullable
    private BlockPos targetPosition;
    private int hoverTicks;

    public final SmoothAnimationState flyAnimationState = new SmoothAnimationState();

    public Dragonfly(@NotNull EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        this.setHoverTicks(30);
        this.setNoGravity(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes().add(Attributes.MAX_HEALTH, 6.0);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(VARIANT_ID, DEFAULT_VARIANT_ID);
    }

    @Override
    public ResourceKey<MobVariant> getDefaultVariant() {
        return NaturalistMobVariants.createKey(NaturalistMobVariants.registryFor("dragonfly"), "blue");
    }

    @Override
    public String[] getLegacyVariantNames() {
        return VARIANT_NAMES;
    }

    @Override
    public Identifier getFallbackVariantTexture() {
        return Naturalist.location("textures/entity/dragonfly/blue_dragonfly.png");
    }

    @Override
    public String getVariantString() {
        return this.entityData.get(VARIANT_ID);
    }

    @Override
    public void setVariantString(String location) {
        this.entityData.set(VARIANT_ID, location);
    }

    public int getHoverTicks() {
        return hoverTicks;
    }

    public void setHoverTicks(int hoverTicks) {
        this.hoverTicks = hoverTicks;
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
    //endregion

    //region Spawning
    public static boolean checkDragonflySpawnRules(EntityType<? extends Dragonfly> type, ServerLevelAccessor level, EntitySpawnReason reason, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).is(NaturalistBlockTags.DRAGONFLIES_SPAWNABLE_ON) && level.getRawBrightness(pos, 0) > 8;
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason reason, @Nullable SpawnGroupData spawnData) {
        this.selectVariantForSpawn(level);
        return super.finalizeSpawn(level, difficulty, reason, spawnData);
    }
    //endregion

    //region Behavior
    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(@NotNull Entity entity) {
    }

    @Override
    protected void pushEntities() {
    }

    public boolean causeFallDamage(double fallDistance, float multiplier, @NotNull DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, @NotNull BlockState state, @NotNull BlockPos pos) {
    }

    @Override
    protected Entity.@NotNull MovementEmission getMovementEmission() {
        return Entity.MovementEmission.EVENTS;
    }

    @Override
    protected void doWaterSplashEffect() {
    }

    @Override
    public float getWalkTargetValue(@NotNull BlockPos blockPos) {
        if (this.level().getBlockState(blockPos).isAir() && this.level().isWaterAt(blockPos.below(2))) {
            return 10.0F;
        }
        return 0.0F;
    }

    @Override
    public boolean isInvertedHealAndHarm() {
        return true;
    }

    @Override
    protected @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(Items.CHORUS_FRUIT)) {
            this.playSound(SoundEvents.GENERIC_EAT.value());
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            if (!this.level().isClientSide()) {
                AreaEffectCloud areaEffectCloud = new AreaEffectCloud(this.level(), this.getX(), this.getY(), this.getZ());
                areaEffectCloud.setOwner(this);
                areaEffectCloud.setCustomParticle(PowerParticleOption.create(ParticleTypes.DRAGON_BREATH, 1.0F));
                areaEffectCloud.setRadius(0.5f);
                areaEffectCloud.setDuration(200);
                areaEffectCloud.addEffect(new MobEffectInstance(MobEffects.INSTANT_DAMAGE, 1, 1));
                areaEffectCloud.setPos(this.getX(), this.getY(), this.getZ());
                this.level().addFreshEntity(areaEffectCloud);
            }
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player , hand);
    }

    @Override
    public void tick() {
        super.tick();
        this.setDeltaMovement(this.getDeltaMovement().multiply(1.0, 0.6, 1.0));
        if (this.level().isClientSide()) {
            this.setupAnimationStates();
        }
    }

    @Override
    protected void customServerAiStep(@NotNull ServerLevel level) {
        super.customServerAiStep(level);
        if (!(this.targetPosition == null || this.level().isEmptyBlock(this.targetPosition) && this.targetPosition.getY() > this.level().getMinY())) {
            this.targetPosition = null;
        }
        if (this.getHoverTicks() > 0) {
            this.setHoverTicks(Math.max(0, this.getHoverTicks() - 1));
        } else if (this.targetPosition == null || this.targetPosition.closerToCenterThan(this.position(), 2.0)) {
            Vec3 randomPos = RandomPos.generateRandomPos(this, () -> new BlockPos(
                    (int)(this.getX() + this.random.nextInt(7) - this.random.nextInt(7)),
                    (int)(this.getY() + this.random.nextInt(6) - 2.0),
                    (int)(this.getZ() + this.random.nextInt(7) - this.random.nextInt(7))
            ));
            assert randomPos != null;
            this.targetPosition = BlockPos.containing(randomPos);
            this.setHoverTicks(15);
        }
        if (this.targetPosition != null && this.getHoverTicks() <= 0) {
            Vec3 vec32 = getVec3();
            this.setDeltaMovement(vec32);
            this.zza = 5.0f;
            float h = Mth.wrapDegrees((float) (Mth.atan2(vec32.z, vec32.x) * Mth.RAD_TO_DEG) - 90.0f - this.getYRot());
            this.setYRot(this.getYRot() + h);
        }
    }

    private @NotNull Vec3 getVec3() {
        assert this.targetPosition != null;
        double x = this.targetPosition.getX() + 0.5 - this.getX();
        assert this.targetPosition != null;
        double y = this.targetPosition.getY() + 0.1 - this.getY();
        assert this.targetPosition != null;
        double z = this.targetPosition.getZ() + 0.5 - this.getZ();
        Vec3 vec3 = this.getDeltaMovement();
        return vec3.add((Math.signum(x) * 0.5 - vec3.x) * 0.1f, (Math.signum(y) * 0.7f - vec3.y) * 0.1f, (Math.signum(z) * 0.5 - vec3.z) * 0.1f);
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState state) {
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return NaturalistSoundEvents.DRAGONFLY_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return NaturalistSoundEvents.DRAGONFLY_DEATH.get();
    }
    //endregion

    //region Animation
    private void setupAnimationStates() {
        this.flyAnimationState.animateWhen(true, this.tickCount);
    }
    //endregion
}
