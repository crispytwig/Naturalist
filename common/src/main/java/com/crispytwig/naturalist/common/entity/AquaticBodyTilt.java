package com.crispytwig.naturalist.common.entity;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class AquaticBodyTilt {
    private static final float TICKS_PER_SECOND = 20.0F;

    public record Speed(float rate, float min, float max, float groundGain, float verticalGain, float scale) {
    }

    public record Pitching(float gain, float max, float rate, float tailGain, float maxTail, float tailRate) {
    }

    public record Turning(float yawRate, float rollGain, float maxRoll, float rollRate, float segmentLimit) {
    }

    public record Segment(float gain, float rate) {
    }

    public record Settings(Speed speed, Pitching pitching, Turning turning, Segment head, Segment body, Segment tail, Segment fluke, float headRollStabilize, float tailRollFollow, float tailRollRate) {
    }

    public static final class Channel {
        private float value;
        private float previous;

        private void approach(float target, float blend) {
            this.previous = this.value;
            this.value += (target - this.value) * blend;
        }

        private void set(float target) {
            this.previous = this.value;
            this.value = target;
        }

        public float get(float partialTick) {
            return Mth.lerp(partialTick, this.previous, this.value);
        }

        public float radians(float partialTick) {
            return this.get(partialTick) * Mth.DEG_TO_RAD;
        }
    }

    public final Channel animSpeed = new Channel();
    public final Channel turnYaw = new Channel();
    public final Channel pitch = new Channel();
    public final Channel roll = new Channel();
    public final Channel tailPitch = new Channel();
    public final Channel headYaw = new Channel();
    public final Channel bodyYaw = new Channel();
    public final Channel tailYaw = new Channel();
    public final Channel flukeYaw = new Channel();
    public final Channel headRoll = new Channel();
    public final Channel tailRoll = new Channel();

    private final Settings settings;
    private boolean initialized;
    private float yawSmooth;
    private float tailPitchSmooth;

    public AquaticBodyTilt(Settings settings) {
        this.settings = settings;
    }

    public void tick(LivingEntity entity) {
        float verticalSpeed = (float) (entity.getY() - entity.yo) * TICKS_PER_SECOND;
        float wet = entity.isInWater() ? 1.0F : 0.0F;
        float yaw = entity.yBodyRot;

        Speed speed = this.settings.speed();
        float speedTarget = Mth.clamp(speed.min() + (float) Mth.length(entity.getX() - entity.xo, entity.getZ() - entity.zo) * TICKS_PER_SECOND * speed.groundGain() + Math.abs(verticalSpeed) * speed.verticalGain(), speed.min(), speed.max()) * speed.scale();
        if (!this.initialized) {
            this.initialized = true;
            this.animSpeed.set(speedTarget);
            this.yawSmooth = yaw;
        }
        this.animSpeed.approach(speedTarget, blend(speed.rate()));

        Pitching pitching = this.settings.pitching();
        this.pitch.approach(wet * Mth.clamp(-verticalSpeed * pitching.gain(), -pitching.max(), pitching.max()), blend(pitching.rate()));
        this.tailPitchSmooth += (this.pitch.value - this.tailPitchSmooth) * blend(pitching.tailRate());
        this.tailPitch.set(Mth.clamp(-(this.pitch.value - this.tailPitchSmooth) * pitching.tailGain(), -pitching.maxTail(), pitching.maxTail()));

        Turning turning = this.settings.turning();
        float yawLead = Mth.wrapDegrees(yaw - this.yawSmooth);
        this.yawSmooth += yawLead * blend(turning.yawRate());
        this.turnYaw.set(Mth.wrapDegrees(this.yawSmooth - yaw));
        this.roll.approach(wet * Mth.clamp(yawLead * turning.rollGain(), -turning.maxRoll(), turning.maxRoll()), blend(turning.rollRate()));

        this.approachYaw(this.headYaw, yawLead, this.settings.head());
        this.approachYaw(this.bodyYaw, -yawLead, this.settings.body());
        this.approachYaw(this.tailYaw, -yawLead, this.settings.tail());
        this.approachYaw(this.flukeYaw, -yawLead, this.settings.fluke());

        this.headRoll.approach(this.roll.value * -this.settings.headRollStabilize(), blend(this.settings.head().rate()));
        this.tailRoll.approach(this.roll.value * this.settings.tailRollFollow(), blend(this.settings.tailRollRate()));
    }

    private void approachYaw(Channel channel, float yawLead, Segment segment) {
        float limit = this.settings.turning().segmentLimit();
        channel.approach(limit * (float) Math.atan(yawLead * segment.gain() / limit * Mth.HALF_PI) / Mth.HALF_PI, blend(segment.rate()));
    }

    private static float blend(float rate) {
        return 1.0F - (float) Math.exp(-rate / TICKS_PER_SECOND);
    }
}
