package com.crispytwig.naturalist.world.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.joml.Vector3fc;

import java.util.*;

public class SurfaceClimbing {
    //region Data
    private static final Vec3 UP = new Vec3(0.0D, 1.0D, 0.0D);
    private static final double PROBE_RANGE = 0.06D;
    private static final int GRACE_TICKS = 12;
    private static final double GRADIENT_STEP = 0.2D;
    private static final double WALL_NORMAL_Y = 0.9D;

    private final Mob mob;
    private final EntityDataAccessor<Vector3fc> normalData;

    private Vec3 normal = UP;
    private boolean attached = true;
    private int grace = GRACE_TICKS;
    private int switchCooldown;
    private int stuckTicks;
    private Vec3 nudgeDir = Vec3.ZERO;
    private Vec3 desiredDir = Vec3.ZERO;
    private Vec3 goalDelta = Vec3.ZERO;
    private double desiredSpeed;
    private int groundCooldown;

    private Vec3 renderNormal = UP;
    private Vec3 renderNormalO = UP;
    private Vec3 forward = new Vec3(0.0D, 0.0D, 1.0D);
    private Vec3 forwardO = forward;
    private Vec3 tailForward = forward;
    private Vec3 tailForwardO = forward;
    private Vec3 anchor = Vec3.ZERO;
    private Vec3 anchorO = Vec3.ZERO;
    private Vec3 smoothedVelocity = Vec3.ZERO;
    private final List<AABB> sampleBoxes = new ArrayList<>();

    public SurfaceClimbing(Mob mob, EntityDataAccessor<Vector3fc> normalData) {
        this.mob = mob;
        this.normalData = normalData;
    }

    public void setDesired(Vec3 dir, double speed, Vec3 goalDelta) {
        this.desiredDir = dir;
        this.desiredSpeed = speed;
        this.goalDelta = goalDelta;
    }

    public void halt() {
        this.desiredDir = Vec3.ZERO;
        this.desiredSpeed = 0.0D;
        this.goalDelta = Vec3.ZERO;
    }

    public void suppressClimbing(int ticks) {
        this.groundCooldown = ticks;
    }

    public Vec3 getNormal() {
        return this.normal;
    }

    public boolean isAttached() {
        return this.attached;
    }

    public boolean isOnSide() {
        if (this.mob.level().isClientSide()) {
            return this.mob.getEntityData().get(this.normalData).y() < WALL_NORMAL_Y;
        }
        return this.attached && this.normal.y < WALL_NORMAL_Y;
    }
    //endregion

    //region Physics
    public void tick() {
        if (this.mob.level().isClientSide()) {
            this.tickClient();
            return;
        }
        if (!this.attached && this.mob.onGround() && !this.mob.isInWater() && !this.mob.isInLava()) {
            this.attach(UP);
        }
        if (this.attached) {
            this.mob.resetFallDistance();
        }
        Vec3 synced = this.attached ? this.normal : UP;
        Vector3fc current = this.mob.getEntityData().get(this.normalData);
        if (current.x() != (float) synced.x || current.y() != (float) synced.y || current.z() != (float) synced.z) {
            this.mob.getEntityData().set(this.normalData, synced.toVector3f());
        }
    }

    public boolean travel() {
        if (this.mob.isInWater() || this.mob.isInLava()) {
            this.attached = false;
            return false;
        }
        if (!this.attached) {
            return false;
        }
        Vec3 velocity = this.mob.getDeltaMovement().scale(0.6D).add(this.desiredDir.scale(this.desiredSpeed * 0.16D));
        boolean facePresent = this.probe(this.normal.scale(-1.0D));
        Vec3 intended = facePresent || this.normal.y > 0.5D || this.goalDelta.dot(this.normal) < 0.0D
                ? velocity.add(this.normal.scale(-0.08D))
                : velocity;
        if (this.stuckTicks > 2 && this.normal.y < WALL_NORMAL_Y) {
            if (this.stuckTicks == 3) {
                Vec3 cross = this.normal.cross(this.desiredDir);
                if (cross.lengthSqr() > 1.0E-6D) {
                    Vec3 crossNorm = cross.normalize();
                    boolean plusFree = this.mob.level().noCollision(this.mob,
                            this.mob.getBoundingBox().move(crossNorm.scale(0.35D)));
                    boolean minusFree = this.mob.level().noCollision(this.mob,
                            this.mob.getBoundingBox().move(crossNorm.scale(-0.35D)));
                    if (plusFree) {
                        this.nudgeDir = crossNorm.scale(0.05D);
                    } else if (minusFree) {
                        this.nudgeDir = crossNorm.scale(-0.05D);
                    } else {
                        this.nudgeDir = Vec3.ZERO;
                    }
                }
            }
            if (this.nudgeDir.lengthSqr() > 1.0E-8D) {
                intended = intended.add(this.nudgeDir);
            }
        }
        Vec3 before = this.mob.position();
        this.mob.move(MoverType.SELF, intended);
        Vec3 actual = this.mob.position().subtract(before);
        this.mob.setDeltaMovement(velocity);
        this.updateAttachment(intended, actual);
        if (this.switchCooldown > 0) {
            this.switchCooldown--;
        }
        if (this.groundCooldown > 0) {
            this.groundCooldown--;
        }
        this.updateStuckEscape(actual);
        return true;
    }

    private void updateStuckEscape(Vec3 actual) {
        if (this.desiredSpeed <= 1.0E-3D) {
            this.stuckTicks = 0;
            this.nudgeDir = Vec3.ZERO;
            return;
        }
        if (this.desiredDir.lengthSqr() > 1.0E-4D && actual.dot(this.desiredDir) > 1.0E-4D) {
            this.stuckTicks = 0;
            if (this.nudgeDir.lengthSqr() < 1.0E-8D || actual.dot(this.desiredDir) > 0.005D) {
                this.nudgeDir = Vec3.ZERO;
            }
            return;
        }
        if (actual.lengthSqr() > 1.0E-6D && this.desiredDir.lengthSqr() <= 1.0E-4D) {
            this.stuckTicks = 0;
            this.nudgeDir = Vec3.ZERO;
            return;
        }
        this.stuckTicks++;
        if (this.normal.y > WALL_NORMAL_Y) {
            return;
        }
        if (this.stuckTicks > 10) {
            this.stuckTicks = 0;
            this.nudgeDir = Vec3.ZERO;
            this.attached = false;
        }
    }

    private void updateAttachment(Vec3 intended, Vec3 actual) {
        Vec3 steered = this.findSteeredFace(intended, actual);
        if (steered != null) {
            this.attach(steered);
            return;
        }
        if (this.probe(this.normal.scale(-1.0D))) {
            this.grace = GRACE_TICKS;
            return;
        }
        Vec3 nearest = this.findNearestFace();
        if (nearest != null && (this.groundCooldown <= 0 || nearest.y > WALL_NORMAL_Y)) {
            this.attach(nearest);
            return;
        }
        if (--this.grace <= 0) {
            this.attached = false;
        }
    }

    private void attach(Vec3 newNormal) {
        if (this.attached && newNormal.equals(this.normal)) {
            this.grace = GRACE_TICKS;
            return;
        }
        this.normal = newNormal;
        this.attached = true;
        this.grace = GRACE_TICKS;
        this.switchCooldown = 5;
        this.mob.setDeltaMovement(projectOntoPlane(this.mob.getDeltaMovement(), newNormal));
    }

    private Vec3 findSteeredFace(Vec3 intended, Vec3 actual) {
        if (this.switchCooldown > 0 || this.groundCooldown > 0) {
            return null;
        }
        Vec3 best = null;
        double bestWeight = 0.0D;
        for (int axis = 0; axis < 3; axis++) {
            double wanted = component(intended, axis);
            if (Math.abs(wanted - component(actual, axis)) < 1.0E-7D || Math.abs(wanted) < 1.0E-7D) {
                continue;
            }
            double weight = Math.abs(component(this.desiredDir, axis));
            if (weight < 0.05D || weight <= bestWeight) {
                continue;
            }
            Vec3 candidate = axisVec(axis, -Math.signum(wanted));
            if (candidate.dot(this.normal) > 0.5D) {
                continue;
            }
            if (this.normal.y > WALL_NORMAL_Y && candidate.y == 0.0D && this.canStepOver(candidate)) {
                continue;
            }
            if (!this.probe(candidate.scale(-1.0D))) {
                continue;
            }
            best = candidate;
            bestWeight = weight;
        }
        return best;
    }

    private Vec3 findNearestFace() {
        Vec3 best = null;
        double bestDot = -0.5D;
        for (int axis = 0; axis < 3; axis++) {
            for (int sign = -1; sign <= 1; sign += 2) {
                Vec3 candidate = axisVec(axis, sign);
                if (candidate.equals(this.normal) || !this.probe(candidate.scale(-1.0D))) {
                    continue;
                }
                double dot = candidate.dot(this.normal);
                if (dot > bestDot) {
                    bestDot = dot;
                    best = candidate;
                }
            }
        }
        return best;
    }

    private boolean canStepOver(Vec3 faceNormal) {
        return this.mob.level().noCollision(this.mob, this.mob.getBoundingBox().deflate(1.0E-3D)
                .move(-faceNormal.x * PROBE_RANGE, this.mob.maxUpStep() + 1.0E-3D, -faceNormal.z * PROBE_RANGE));
    }

    private boolean probe(Vec3 dir) {
        return !this.mob.level().noCollision(this.mob, this.mob.getBoundingBox().deflate(1.0E-3D)
                .move(dir.x * PROBE_RANGE, dir.y * PROBE_RANGE, dir.z * PROBE_RANGE));
    }
    //endregion

    //region Smoothing
    private void tickClient() {
        this.renderNormalO = this.renderNormal;
        this.forwardO = this.forward;
        this.tailForwardO = this.tailForward;
        this.anchorO = this.anchor;

        this.collectNearbyBoxes();
        Vec3 center = this.mob.getBoundingBox().getCenter();

        Vec3 synced = new Vec3(this.mob.getEntityData().get(this.normalData));
        Vec3 sampled = this.sampleFieldNormal(center);
        this.renderNormal = lerpUnit(this.renderNormal, sampled.lengthSqr() > 1.0E-4D
                ? sampled.add(synced.scale(0.6D)).normalize()
                : synced, 0.3F);

        Vec3 anchorTarget = Vec3.ZERO;
        if (!this.sampleBoxes.isEmpty()) {
            double dist = this.surfaceDistance(center);
            if (dist < 0.75D) {
                anchorTarget = center.subtract(this.renderNormal.scale(dist)).subtract(this.mob.position());
            }
        }
        this.anchor = this.anchor.lerp(anchorTarget, 0.4F);

        this.smoothedVelocity = this.smoothedVelocity.lerp(new Vec3(this.mob.getX() - this.mob.xo, this.mob.getY() - this.mob.yo, this.mob.getZ() - this.mob.zo), 0.3D);

        Vec3 tangent = projectOntoPlane(this.smoothedVelocity, this.renderNormal);
        if (tangent.lengthSqr() > 2.5E-5D) {
            this.forward = lerpUnit(this.forward, tangent.normalize(), 0.35F);
        } else {
            Vec3 bodyDir = projectOntoPlane(Vec3.directionFromRotation(0.0F, this.mob.yBodyRot), this.renderNormal);
            if (bodyDir.lengthSqr() > 1.0E-4D) {
                this.forward = lerpUnit(this.forward, bodyDir.normalize(), 0.08F);
            }
        }

        this.forward = flattenOnto(this.forward, this.renderNormal);
        this.tailForward = flattenOnto(lerpUnit(this.tailForward, this.forward, 0.5F), this.renderNormal);
    }

    private void collectNearbyBoxes() {
        this.sampleBoxes.clear();
        AABB range = this.mob.getBoundingBox().inflate(0.5D);
        for (BlockPos pos : BlockPos.betweenClosed(
                Mth.floor(range.minX), Mth.floor(range.minY), Mth.floor(range.minZ),
                Mth.floor(range.maxX), Mth.floor(range.maxY), Mth.floor(range.maxZ))) {
            BlockState state = this.mob.level().getBlockState(pos);
            if (state.isAir()) {
                continue;
            }
            VoxelShape shape = state.getCollisionShape(this.mob.level(), pos);
            if (shape.isEmpty()) {
                continue;
            }
            for (AABB part : shape.toAabbs()) {
                AABB world = part.move(pos);
                if (world.intersects(range)) {
                    this.sampleBoxes.add(world);
                }
            }
        }
    }

    private double surfaceDistance(Vec3 point) {
        double best = Double.MAX_VALUE;
        for (AABB box : this.sampleBoxes) {
            best = Math.min(best, box.distanceToSqr(point));
        }
        return Math.sqrt(best);
    }

    private Vec3 sampleFieldNormal(Vec3 center) {
        if (this.sampleBoxes.isEmpty()) {
            return Vec3.ZERO;
        }
        Vec3 gradient = new Vec3(
                this.surfaceDistance(center.add(GRADIENT_STEP, 0.0D, 0.0D)) - this.surfaceDistance(center.add(-GRADIENT_STEP, 0.0D, 0.0D)),
                this.surfaceDistance(center.add(0.0D, GRADIENT_STEP, 0.0D)) - this.surfaceDistance(center.add(0.0D, -GRADIENT_STEP, 0.0D)),
                this.surfaceDistance(center.add(0.0D, 0.0D, GRADIENT_STEP)) - this.surfaceDistance(center.add(0.0D, 0.0D, -GRADIENT_STEP)));
        return gradient.lengthSqr() > 1.0E-6D ? gradient.normalize() : Vec3.ZERO;
    }

    public Vec3 getRenderNormal(float partialTick) {
        return lerpUnit(this.renderNormalO, this.renderNormal, partialTick);
    }

    public Vec3 getRenderForward(float partialTick) {
        return lerpUnit(this.forwardO, this.forward, partialTick);
    }

    public Vec3 getRenderForwardFlattened(float partialTick, Vec3 renderNormal) {
        Vec3 flat = projectOntoPlane(this.getRenderForward(partialTick), renderNormal);
        if (flat.lengthSqr() < 1.0E-6D) {
            flat = projectOntoPlane(new Vec3(0.0D, 0.0D, 1.0D), renderNormal);
            if (flat.lengthSqr() < 1.0E-6D) {
                flat = projectOntoPlane(UP, renderNormal);
            }
        }
        return flat.normalize();
    }

    public Vec3 getRenderAnchor(float partialTick) {
        return this.anchorO.lerp(this.anchor, partialTick);
    }

    public float getTailLag(float partialTick) {
        Vec3 heading = this.getRenderForward(partialTick);
        Vec3 tail = lerpUnit(this.tailForwardO, this.tailForward, partialTick);
        return (float) Mth.atan2(tail.cross(heading).dot(this.getRenderNormal(partialTick)), tail.dot(heading));
    }
    //endregion

    //region Persistence
    public void save(ValueOutput output) {
        if (this.attached) {
            output.putFloat("ClimbNormalX", (float) this.normal.x);
            output.putFloat("ClimbNormalY", (float) this.normal.y);
            output.putFloat("ClimbNormalZ", (float) this.normal.z);
        }
    }

    public void load(ValueInput input) {
        if (input.read("ClimbNormalY", com.mojang.serialization.Codec.FLOAT).isPresent()) {
            Vec3 loaded = new Vec3(input.getFloatOr("ClimbNormalX", 0.0F), input.getFloatOr("ClimbNormalY", 0.0F), input.getFloatOr("ClimbNormalZ", 0.0F));
            this.normal = loaded.lengthSqr() > 1.0E-4D ? loaded.normalize() : UP;
            this.attached = true;
            this.grace = GRACE_TICKS;
        }
    }
    //endregion

    //region Math
    private static double component(Vec3 vec, int axis) {
        return axis == 0 ? vec.x : axis == 1 ? vec.y : vec.z;
    }

    private static Vec3 axisVec(int axis, double sign) {
        return axis == 0 ? new Vec3(sign, 0.0D, 0.0D) : axis == 1 ? new Vec3(0.0D, sign, 0.0D) : new Vec3(0.0D, 0.0D, sign);
    }

    public static Vec3 projectOntoPlane(Vec3 vec, Vec3 planeNormal) {
        return vec.subtract(planeNormal.scale(vec.dot(planeNormal)));
    }

    private static Vec3 flattenOnto(Vec3 vec, Vec3 normal) {
        Vec3 flat = projectOntoPlane(vec, normal);
        return flat.lengthSqr() > 1.0E-6D ? flat.normalize() : vec;
    }

    private static Vec3 lerpUnit(Vec3 from, Vec3 to, float delta) {
        Vec3 mixed = from.lerp(to, delta);
        return mixed.lengthSqr() > 1.0E-8D ? mixed.normalize() : to;
    }
    //endregion
}
