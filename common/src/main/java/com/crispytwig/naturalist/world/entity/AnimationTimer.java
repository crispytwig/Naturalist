package com.crispytwig.naturalist.world.entity;

public class AnimationTimer {
    private final int durationTicks;
    private int ticksLeft;

    public AnimationTimer(int durationTicks) {
        this.durationTicks = durationTicks;
    }

    public boolean tick(boolean trigger) {
        if (trigger && this.ticksLeft <= 0) {
            this.ticksLeft = this.durationTicks;
        } else if (this.ticksLeft > 0) {
            this.ticksLeft--;
        }
        return this.ticksLeft > 0;
    }
}
