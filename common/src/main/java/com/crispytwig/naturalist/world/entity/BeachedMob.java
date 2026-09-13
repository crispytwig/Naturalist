package com.crispytwig.naturalist.world.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public final class BeachedMob {
    private BeachedMob() {}

    public static int tickFlopping(Mob mob, int flopCooldown, @Nullable SoundEvent sound) {
        if (mob.isInWater() || !mob.onGround() || !mob.verticalCollisionBelow) {
            return flopCooldown;
        }
        if (flopCooldown > 0) {
            return flopCooldown - 1;
        }
        flopTowardWater(mob, sound);
        return 15 + mob.getRandom().nextInt(25);
    }

    public static void flopTowardWater(Mob mob, @Nullable SoundEvent sound) {
        Vec3 dir = findNearestWaterDirection(mob);
        double dx;
        double dz;
        if (dir != null) {
            Vec3 jittered = dir.yRot((mob.getRandom().nextFloat() - 0.5F) * 0.35F);
            dx = jittered.x * 0.45D;
            dz = jittered.z * 0.45D;
            float yaw = (float) (Mth.atan2(jittered.z, jittered.x) * Mth.RAD_TO_DEG) - 90.0F;
            mob.setYRot(yaw);
            mob.yBodyRot = yaw;
            mob.yHeadRot = yaw;
        } else {
            dx = (mob.getRandom().nextFloat() * 2.0F - 1.0F) * 0.3D;
            dz = (mob.getRandom().nextFloat() * 2.0F - 1.0F) * 0.3D;
        }
        mob.setDeltaMovement(mob.getDeltaMovement().add(dx, 0.5D, dz));
        mob.setOnGround(false);
        mob.needsSync = true;
        if (sound != null) {
            mob.playSound(sound, 2.0F, 0.6F);
        }
    }

    @Nullable
    public static Vec3 findNearestWaterDirection(Mob mob) {
        BlockPos origin = mob.blockPosition();
        BlockPos best = null;
        double bestDist = Double.MAX_VALUE;
        for (BlockPos pos : BlockPos.betweenClosed(origin.offset(-16, -8, -16), origin.offset(16, 1, 16))) {
            if (mob.level().getFluidState(pos).is(FluidTags.WATER)) {
                double dist = pos.distSqr(origin);
                if (dist < bestDist) {
                    bestDist = dist;
                    best = pos.immutable();
                }
            }
        }
        if (best == null) {
            return null;
        }
        Vec3 dir = new Vec3(best.getX() + 0.5D - mob.getX(), 0.0D, best.getZ() + 0.5D - mob.getZ());
        return dir.lengthSqr() < 1.0E-4D ? null : dir.normalize();
    }
}
