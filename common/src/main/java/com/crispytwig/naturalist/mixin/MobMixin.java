package com.crispytwig.naturalist.mixin;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.NaturalistConfig;
import com.crispytwig.naturalist.world.entity.animal.firefly.Firefly;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity {
    @Unique
    private static final Identifier naturalist$babyHealthId = Identifier.fromNamespaceAndPath("naturalist", "baby_health");
    @Unique
    private static final double naturalist$babyHealthScale = 1.70D;
    @Unique
    private static final double naturalist$babyHealthMaxRatio = 0.75D;
    @Unique
    private static final double naturalist$babyHealthStep = 2.0D;
    @Unique
    private static final double naturalist$babyHealthEpsilon = 1.0E-4D;

    @Unique
    private static final Set<String> naturalist$excluded = Set.of("carried_food", "dirt_trail", "duck_egg", "lizard_tail");

    @Unique
    private Boolean naturalist$wasBaby;

    @Unique
    private boolean naturalist$isNaturalistMob() {
        Identifier key = BuiltInRegistries.ENTITY_TYPE.getKey(this.getType());
        return key.getNamespace().equals(Naturalist.MOD_ID) && !naturalist$excluded.contains(key.getPath());
    }

    @Unique
    private static double naturalist$babyMaxHealth(double adultMax) {
        double raw = Math.min(naturalist$babyHealthScale * Math.sqrt(adultMax), adultMax * naturalist$babyHealthMaxRatio);
        double stepped = Math.round(raw / naturalist$babyHealthStep) * naturalist$babyHealthStep;
        double ceiling = Math.max(naturalist$babyHealthStep,
                Math.floor((adultMax - naturalist$babyHealthEpsilon) / naturalist$babyHealthStep) * naturalist$babyHealthStep);
        return Math.clamp(stepped, naturalist$babyHealthStep, ceiling);
    }

    protected MobMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "aiStep", at = @At("TAIL"))
    private void naturalist$updateBabyHealth(CallbackInfo ci) {
        if (this.level().isClientSide() || !this.naturalist$isNaturalistMob()) {
            return;
        }

        boolean baby = this.isBaby();
        if (this.naturalist$wasBaby != null && this.naturalist$wasBaby == baby) {
            return;
        }
        this.naturalist$wasBaby = baby;

        AttributeInstance maxHealth = this.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealth == null) {
            return;
        }

        AttributeModifier existing = maxHealth.getModifier(naturalist$babyHealthId);
        if (this.isBaby()) {
            double adultMax = existing == null ? this.getMaxHealth() : this.getMaxHealth() / (1.0D + existing.amount());
            if (adultMax <= 0.0D) {
                return;
            }
            double amount = naturalist$babyMaxHealth(adultMax) / adultMax - 1.0D;
            if (existing != null && Math.abs(existing.amount() - amount) < naturalist$babyHealthEpsilon) {
                return;
            }
            if (existing != null) {
                maxHealth.removeModifier(naturalist$babyHealthId);
            }
            maxHealth.addTransientModifier(new AttributeModifier(naturalist$babyHealthId, amount, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
            this.setHealth(Math.min(this.getHealth(), this.getMaxHealth()));
        } else if (existing != null) {
            float healthFraction = this.getMaxHealth() > 0.0F ? this.getHealth() / this.getMaxHealth() : 1.0F;
            maxHealth.removeModifier(naturalist$babyHealthId);
            this.setHealth(this.getMaxHealth() * healthFraction);
        }
    }

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void naturalist$clearSwingLatch(CallbackInfo ci) {
        if (this.naturalist$isNaturalistMob()) {
            this.updateSwingTime();
        }
    }

    @Inject(method = "doHurtTarget", at = @At("HEAD"))
    @SuppressWarnings("unused")
    private void naturalist$onDoHurtTarget(ServerLevel level, Entity target, CallbackInfoReturnable<Boolean> cir) {
        if (BuiltInRegistries.ENTITY_TYPE.getKey(this.getType()).equals(BuiltInRegistries.ENTITY_TYPE.getKey(EntityTypes.FROG))
                && target instanceof Firefly) {
            this.addEffect(new MobEffectInstance(MobEffects.GLOWING, 60));
        }
    }

    @Inject(method = "checkDespawn", at = @At("HEAD"), cancellable = true)
    private void naturalist$checkDespawnMixin(CallbackInfo ci) {
        if (!this.naturalist$isNaturalistMob()) {
            return;
        }

        if (NaturalistConfig.isRemoved(this.getType())) {
            this.remove(Entity.RemovalReason.DISCARDED);
            ci.cancel();
        }
    }
}
