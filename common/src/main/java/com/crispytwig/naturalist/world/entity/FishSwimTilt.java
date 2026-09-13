package com.crispytwig.naturalist.world.entity;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class FishSwimTilt {
    private static final double PITCH_MIN = 0.01D;
    private static final double PITCH_MAX = 0.05D;
    private static final float PITCH_CLAMP = 55.0F;
    private static final float MAX_TILT = 25.0F;
    private static final float TILT_DECAY = 0.85F;
    private static final float PITCH_LERP = 0.2F;

    public float prevTilt;
    public float tilt;

    public float prevSwimPitch;
    public float swimPitch;

    public void tick(Entity entity) {
        this.updateTilt(entity);
        this.updateSwimPitch(entity);
    }

    private void updateTilt(Entity entity) {
        this.prevTilt = this.tilt;
        if (entity.isInWater()) {
            float turn = Mth.degreesDifference(entity.getYRot(), entity.yRotO);
            if (Math.abs(turn) > 1.0F) {
                if (Math.abs(this.tilt) < MAX_TILT) {
                    this.tilt -= Math.signum(turn);
                }
            } else if (this.tilt != 0.0F) {
                float sign = Math.signum(this.tilt);
                this.tilt -= sign * TILT_DECAY;
                if (this.tilt * sign < 0.0F) {
                    this.tilt = 0.0F;
                }
            }
        } else {
            this.tilt = 0.0F;
        }
    }

    private void updateSwimPitch(Entity entity) {
        this.prevSwimPitch = this.swimPitch;
        float target = 0.0F;
        if (entity.isInWater()) {
            double dx = entity.getX() - entity.xo;
            double dy = entity.getY() - entity.yo;
            double dz = entity.getZ() - entity.zo;
            double horizontal = Math.sqrt(dx * dx + dz * dz);
            double speed = Math.sqrt(horizontal * horizontal + dy * dy);
            float speedFactor = (float) Mth.clamp((speed - PITCH_MIN) / (PITCH_MAX - PITCH_MIN), 0.0D, 1.0D);
            if (speedFactor > 0.0F) {
                float angle = (float) (-(Mth.atan2(dy, horizontal) * (180.0D / Math.PI)));
                target = Mth.clamp(angle, -PITCH_CLAMP, PITCH_CLAMP) * speedFactor;
            }
        }
        this.swimPitch += (target - this.swimPitch) * PITCH_LERP;
    }

    public float getTilt(float partialTick) {
        return Mth.lerp(partialTick, this.prevTilt, this.tilt);
    }

    public float getSwimPitch(float partialTick) {
        return Mth.lerp(partialTick, this.prevSwimPitch, this.swimPitch);
    }
}
