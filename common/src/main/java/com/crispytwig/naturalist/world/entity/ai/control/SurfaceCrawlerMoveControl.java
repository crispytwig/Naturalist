package com.crispytwig.naturalist.world.entity.ai.control;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;
import com.crispytwig.naturalist.world.entity.SurfaceClimbing;
import com.crispytwig.naturalist.world.entity.SurfaceCrawler;

public class SurfaceCrawlerMoveControl extends MoveControl {
    private final SurfaceCrawler crawler;
    private Vec3 lastPos = Vec3.ZERO;
    private Vec3 lastWanted = Vec3.ZERO;
    private int stuckTicks;
    private int climbTicks;
    private int stuckCycles;
    private Vec3 progressAnchor = Vec3.ZERO;
    private int progressTicks;

    public <T extends Mob & SurfaceCrawler> SurfaceCrawlerMoveControl(T mob) {
        super(mob);
        this.crawler = mob;
    }

    public void giveUp() {
        this.operation = Operation.WAIT;
        this.stuckTicks = 0;
        this.climbTicks = 0;
        this.stuckCycles = 0;
        this.progressAnchor = this.mob.position();
        this.progressTicks = 0;
        this.mob.getNavigation().stop();
        this.crawler.getClimbing().halt();
    }

    @Override
    public void tick() {
        SurfaceClimbing climbing = this.crawler.getClimbing();
        if (this.operation != Operation.MOVE_TO || !climbing.isAttached()) {
            this.stuckTicks = 0;
            climbing.halt();
            return;
        }

        Vec3 wanted = new Vec3(this.wantedX, this.wantedY, this.wantedZ);
        if (!wanted.equals(this.lastWanted)) {
            this.lastWanted = wanted;
            this.stuckTicks = 0;
            this.climbTicks = 0;
            this.stuckCycles = 0;
            this.progressAnchor = this.mob.position();
            this.progressTicks = 0;
        }

        Vec3 delta = wanted.subtract(this.mob.position());
        double distance = delta.length();
        if (distance < 0.1D) {
            this.operation = Operation.WAIT;
            this.stuckTicks = 0;
            climbing.halt();
            return;
        }

        this.trackProgress();

        Vec3 normal = climbing.getNormal();
        boolean onWall = Math.abs(normal.y) < 0.5D;
        Vec3 tangent;
        if (onWall && delta.dot(normal) < 0.0D) {
            tangent = new Vec3(0.0D, 1.0D, 0.0D);
        } else {
            tangent = SurfaceClimbing.projectOntoPlane(delta, normal);
            if (onWall && tangent.y > 0.0D) {
                tangent = new Vec3(tangent.x, 0.0D, tangent.z);
            }
            if (tangent.lengthSqr() < distance * distance * 0.04D) {
                if (onWall) {
                    tangent = new Vec3(0.0D, -1.0D, 0.0D);
                } else {
                    tangent = SurfaceClimbing.projectOntoPlane(new Vec3(0.0D, 1.0D, 0.0D), normal);
                    if (tangent.lengthSqr() < 0.01D) {
                        tangent = new Vec3(delta.x, 0.0D, delta.z);
                    }
                }
            }
        }
        if (this.climbTicks > 0) {
            this.climbTicks--;
            Vec3 climb = SurfaceClimbing.projectOntoPlane(new Vec3(0.0D, 1.0D, 0.0D), normal);
            if (climb.lengthSqr() > 0.01D) {
                tangent = climb;
            } else {
                Vec3 slide = normal.cross(tangent);
                if (slide.lengthSqr() > 1.0E-6D) {
                    tangent = slide;
                }
            }
        }
        if (tangent.lengthSqr() < 1.0E-7D) {
            climbing.halt();
            return;
        }

        tangent = tangent.normalize();
        double speed = this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED);
        this.mob.setSpeed((float) speed);
        climbing.setDesired(tangent, speed, delta);

        if (tangent.horizontalDistanceSqr() > 1.0E-4D) {
            float yaw = (float) (Mth.atan2(tangent.z, tangent.x) * Mth.RAD_TO_DEG) - 90.0F;
            this.mob.setYRot(this.rotlerp(this.mob.getYRot(), yaw, 20.0F));
            this.mob.yBodyRot = this.mob.getYRot();
        }
    }

    private void trackProgress() {
        Vec3 pos = this.mob.position();
        if (++this.progressTicks >= 20) {
            if (pos.distanceToSqr(this.progressAnchor) < 0.01D) {
                this.crawler.getClimbing().suppressClimbing(40);
                this.giveUp();
                return;
            }
            this.progressAnchor = pos;
            this.progressTicks = 0;
        }
        if (pos.distanceToSqr(this.lastPos) < 1.0E-4D) {
            if (++this.stuckTicks > 8 && this.climbTicks <= 0) {
                this.stuckTicks = 0;
                if (this.crawler.getClimbing().isOnSide()) {
                    if (++this.stuckCycles >= 2) {
                        this.crawler.getClimbing().suppressClimbing(40);
                        this.giveUp();
                    } else {
                        this.climbTicks = 24;
                    }
                } else {
                    this.crawler.getClimbing().suppressClimbing(40);
                    this.giveUp();
                }
            }
        } else {
            this.stuckTicks = 0;
        }
        this.lastPos = pos;
    }
}
