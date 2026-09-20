package com.crispytwig.naturalist.world.entity.animal;

import org.jspecify.annotations.Nullable;

import java.util.function.Predicate;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.minecraft.server.level.ServerLevel;

public abstract class NaturalistAnimal extends Animal {
    protected NaturalistAnimal(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @SuppressWarnings("unused")
    public static boolean checkNaturalistAnimalSpawnRules(EntityType<? extends Animal> entityType, LevelAccessor level, EntitySpawnReason spawnType, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).is(BlockTags.SUBSTRATE_OVERWORLD) && isBrightEnoughToSpawn(level, pos);
    }

    public static boolean isVisiblyMoving(Entity entity) {
        return entity.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6;
    }

    public static float defaultVoicePitch(RandomSource random) {
        return (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F;
    }

    public static void leaveWater(Mob mob) {
        Path path = mob.getNavigation().getPath();
        if (!mob.level().isClientSide() && mob.getNavigation().isInProgress() && mob.isInWater() && path != null) {
            int nodeIndex = Mth.clamp(path.getNextNodeIndex(), 0, path.getNodeCount() - 1);
            if (mob.level().getFluidState(path.getNodePos(nodeIndex)).isEmpty()) {
                Vec3 look = mob.getLookAngle();
                mob.addDeltaMovement(new Vec3(look.x() * 0.025, 0.05, look.z() * 0.025));
            }
        }
    }

    public static void freezeInPlace(Mob mob) {
        mob.getNavigation().stop();
        mob.setZza(0.0F);
        mob.setXxa(0.0F);
        mob.setDeltaMovement(0.0D, mob.getDeltaMovement().y, 0.0D);
    }

    public static @Nullable Player findVisiblePlayer(Mob mob, double range, AABB area, Predicate<Player> selector) {
        for (Player player : mob.level().players()) {
            if (!area.contains(player.getX(), player.getY(), player.getZ()) || !player.canBeSeenByAnyone() || !selector.test(player)) {
                continue;
            }
            double visibility = mob.level() instanceof ServerLevel serverLevel
                    ? player.getVisibilityPercent(serverLevel, mob)
                    : 1.0D;
            double visibilityDistance = Math.max(range * visibility, 2.0D);
            if (mob.distanceToSqr(player.getX(), player.getY(), player.getZ()) <= visibilityDistance * visibilityDistance && mob.hasLineOfSight(player)) {
                return player;
            }
        }
        return null;
    }

    public static boolean hasVisiblePlayer(Mob mob, double range, AABB area, Predicate<Player> selector) {
        return findVisiblePlayer(mob, range, area, selector) != null;
    }

    public static SoundEvent eatingSound(ItemStack stack) {
        Consumable consumable = stack.get(DataComponents.CONSUMABLE);
        return (consumable != null ? consumable.sound() : SoundEvents.GENERIC_EAT).value();
    }

    public static double babyKnockbackStrength(LivingEntity entity, double strength) {
        return entity.isBaby() ? strength / Math.max(1.0 - entity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE), 0.01) : strength;
    }

    public static boolean isBlockedByItem(LivingEntity defender, DamageSource source, float damage) {
        ItemStack blockingWith = defender.getItemBlockingWith();
        if (blockingWith == null) {
            return false;
        }
        BlocksAttacks blocksAttacks = blockingWith.get(DataComponents.BLOCKS_ATTACKS);
        if (blocksAttacks == null || blocksAttacks.bypassedBy().map(tag -> tag.contains(source.typeHolder())).orElse(false)) {
            return false;
        }
        Vec3 sourcePosition = source.getSourcePosition();
        double angle = Math.PI;
        if (sourcePosition != null) {
            Vec3 viewVector = defender.calculateViewVector(0.0F, defender.getYHeadRot());
            Vec3 vectorTo = sourcePosition.subtract(defender.position());
            vectorTo = new Vec3(vectorTo.x, 0.0, vectorTo.z).normalize();
            angle = Math.acos(vectorTo.dot(viewVector));
        }
        return blocksAttacks.resolveBlockedDamage(source, damage, angle) > 0.0F;
    }
}
