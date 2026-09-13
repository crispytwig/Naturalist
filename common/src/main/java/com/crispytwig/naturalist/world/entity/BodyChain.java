package com.crispytwig.naturalist.world.entity;

import net.minecraft.util.Mth;

public class BodyChain {
    private final float renderYawEase;
    private final float[] yawGains;
    private final float[] pitchGains;
    private final float rollPerYaw;
    private final float maxRoll;
    private final float rollEase;

    private final float[] segYaw;
    private final float[] segYawO;
    private final float[] segPitch;
    private final float[] segPitchO;
    private float renderYaw;
    private float renderYawO;
    private float roll;
    private float rollO;
    private boolean initialized;

    public BodyChain(float renderYawEase, float[] yawGains, float[] pitchGains, float rollPerYaw, float maxRoll, float rollEase) {
        this.renderYawEase = renderYawEase;
        this.yawGains = yawGains;
        this.pitchGains = pitchGains;
        this.rollPerYaw = rollPerYaw;
        this.maxRoll = maxRoll;
        this.rollEase = rollEase;
        this.segYaw = new float[yawGains.length];
        this.segYawO = new float[yawGains.length];
        this.segPitch = new float[pitchGains.length];
        this.segPitchO = new float[pitchGains.length];
    }

    public void tick(float bodyYaw, float bodyPitch, float targetPitch) {
        if (!this.initialized) {
            this.initialized = true;
            this.renderYaw = this.renderYawO = bodyYaw;
            for (int i = 0; i < this.segYaw.length; i++) {
                this.segYaw[i] = this.segYawO[i] = bodyYaw;
            }
        }
        this.renderYawO = this.renderYaw;
        this.rollO = this.roll;
        System.arraycopy(this.segYaw, 0, this.segYawO, 0, this.segYaw.length);
        System.arraycopy(this.segPitch, 0, this.segPitchO, 0, this.segPitch.length);

        this.renderYaw += Mth.wrapDegrees(bodyYaw - this.renderYaw) * this.renderYawEase;
        for (int i = 0; i < this.segYaw.length; i++) {
            float yawReference = i == 0 ? bodyYaw : i == 1 ? this.renderYaw : this.segYaw[i - 1];
            this.segYaw[i] += Mth.wrapDegrees(yawReference - this.segYaw[i]) * this.yawGains[i];
            float pitchReference = i == 0 ? targetPitch : i == 1 ? bodyPitch : this.segPitch[i - 1];
            this.segPitch[i] += (pitchReference - this.segPitch[i]) * this.pitchGains[i];
        }

        float rollTarget = Mth.clamp(-Mth.wrapDegrees(this.renderYaw - this.renderYawO) * this.rollPerYaw, -this.maxRoll, this.maxRoll);
        this.roll += (rollTarget - this.roll) * this.rollEase;
    }

    public float getRenderYaw() {
        return this.renderYaw;
    }

    public float getRenderYaw(float partialTick) {
        return Mth.rotLerp(partialTick, this.renderYawO, this.renderYaw);
    }

    public float getRoll(float partialTick) {
        return Mth.lerp(partialTick, this.rollO, this.roll);
    }

    public float getSegmentYawOffset(int index, float partialTick) {
        float current = Mth.rotLerp(partialTick, this.segYawO[index], this.segYaw[index]);
        float reference = index <= 1
                ? this.getRenderYaw(partialTick)
                : Mth.rotLerp(partialTick, this.segYawO[index - 1], this.segYaw[index - 1]);
        return Mth.wrapDegrees(current - reference);
    }

    public float getSegmentPitchOffset(int index, float partialTick, float bodyPitch) {
        float current = Mth.lerp(partialTick, this.segPitchO[index], this.segPitch[index]);
        float reference = index <= 1
                ? bodyPitch
                : Mth.lerp(partialTick, this.segPitchO[index - 1], this.segPitch[index - 1]);
        return current - reference;
    }
}
