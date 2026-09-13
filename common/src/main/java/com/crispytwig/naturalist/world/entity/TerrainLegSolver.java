package com.crispytwig.naturalist.world.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Optional;

public class TerrainLegSolver {
    public final Leg backLeft;
    public final Leg backRight;
    public final Leg frontLeft;
    public final Leg frontRight;

    public final Leg[] legs;

    public float renderPitch;
    public float renderRoll;

    public TerrainLegSolver(float forward, float side, float range) {
        this.legs = new Leg[]{
                new Leg(-forward, side, range),
                new Leg(-forward, -side, range),
                new Leg(forward, side, range),
                new Leg(forward, -side, range)
        };
        this.backLeft = this.legs[0];
        this.backRight = this.legs[1];
        this.frontLeft = this.legs[2];
        this.frontRight = this.legs[3];
    }

    public void update(LivingEntity entity, float scale) {
        this.update(entity, entity.yBodyRot, scale);
    }

    public void update(LivingEntity entity, float yaw, float scale) {
        double sideTheta = yaw * Mth.DEG_TO_RAD;
        double sideX = Math.cos(sideTheta) * scale;
        double sideZ = Math.sin(sideTheta) * scale;
        double forwardTheta = sideTheta + Mth.HALF_PI;
        double forwardX = Math.cos(forwardTheta) * scale;
        double forwardZ = Math.sin(forwardTheta) * scale;
        for (Leg leg : this.legs) {
            leg.update(entity, sideX, sideZ, forwardX, forwardZ, scale);
        }
    }

    public static final class Leg {
        private static final float SMOOTHING = 0.2F;

        public final float forward;
        public final float side;
        private final float range;
        private float height;
        private float prevHeight;

        public Leg(float forward, float side, float range) {
            this.forward = forward;
            this.side = side;
            this.range = range;
        }

        public float getHeight(float delta) {
            return this.prevHeight + (this.height - this.prevHeight) * delta;
        }

        public void update(LivingEntity entity, double sideX, double sideZ, double forwardX, double forwardZ, float scale) {
            this.prevHeight = this.height;
            double x = entity.getX() + sideX * this.side + forwardX * this.forward;
            double z = entity.getZ() + sideZ * this.side + forwardZ * this.forward;
            this.height = Mth.clamp(this.settle(entity, x, entity.getY() - 1, z, this.height), -this.range * scale, this.range * scale);
        }

        private float settle(LivingEntity entity, double x, double y, double z, float height) {
            BlockPos pos = new BlockPos((int) Math.floor(x), (int) Math.floor(y + 0.001), (int) Math.floor(z));
            Vec3 vec3 = new Vec3(x, y, z);
            float dist = this.getDistance(entity.level(), pos, vec3);
            float lastDistance = dist;
            while (lastDistance == 1.0F) {
                pos = pos.below();
                lastDistance = this.getDistance(entity.level(), pos, vec3);
                dist += lastDistance;
            }
            return ((entity.onGround() && height <= dist) || height > 0.0F) ? Mth.lerp(SMOOTHING, height, dist) : height;
        }

        private float getDistance(Level level, BlockPos pos, Vec3 position) {
            if (pos.getY() < level.getMinY()) {
                return 0.0F;
            }
            BlockState state = level.getBlockState(pos);
            VoxelShape shape = state.getCollisionShape(level, pos);
            if (shape.isEmpty()) {
                return 1.0F;
            }
            Optional<Vec3> closest = shape.closestPointTo(new Vec3(position.x % 1.0D, position.y, position.z % 1.0D));
            if (closest.isEmpty()) {
                return 1.0F;
            }
            float closestY = Math.min((float) closest.get().y, 1.0F);
            return position.y < 0.0 ? closestY : 1.0F - closestY;
        }
    }
}
