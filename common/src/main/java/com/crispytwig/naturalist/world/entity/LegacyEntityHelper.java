package com.crispytwig.naturalist.world.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public final class LegacyEntityHelper {
    private static final double DEFAULT_ATTACK_REACH = Math.sqrt(2.04F) - 0.6F;
    private static final double PLAYER_SEAT_CORRECTION = -0.25D;

    private LegacyEntityHelper() {
    }

    public static UUID modifierId(ResourceLocation id) {
        return UUID.nameUUIDFromBytes(id.toString().getBytes(StandardCharsets.UTF_8));
    }

    public static boolean isWithinMeleeAttackRange(Mob mob, LivingEntity target) {
        return getAttackBoundingBox(mob).intersects(target.getBoundingBox());
    }

    public static AABB getAttackBoundingBox(Mob mob) {
        Entity vehicle = mob.getVehicle();
        AABB box = mob.getBoundingBox();
        if (vehicle != null) {
            AABB vehicleBox = vehicle.getBoundingBox();
            box = new AABB(Math.min(box.minX, vehicleBox.minX), box.minY, Math.min(box.minZ, vehicleBox.minZ),
                    Math.max(box.maxX, vehicleBox.maxX), box.maxY, Math.max(box.maxZ, vehicleBox.maxZ));
        }
        return box.inflate(DEFAULT_ATTACK_REACH, 0.0D, DEFAULT_ATTACK_REACH);
    }

    public static void positionRider(Entity vehicle, Entity passenger, Entity.MoveFunction moveFunction, Vec3 localAttachment) {
        Vec3 attach = localAttachment.yRot(-vehicle.getYRot() * Mth.DEG_TO_RAD);
        double y = vehicle.getY() + attach.y + passenger.getMyRidingOffset() + (passenger instanceof Player ? PLAYER_SEAT_CORRECTION : 0.0D);
        moveFunction.accept(passenger, vehicle.getX() + attach.x, y, vehicle.getZ() + attach.z);
    }
}
