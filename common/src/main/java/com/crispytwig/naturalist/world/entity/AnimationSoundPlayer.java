package com.crispytwig.naturalist.world.entity;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public final class AnimationSoundPlayer {
    private static final float NOT_PLAYING = -1.0F;
    private static final float BEFORE_FIRST_CUE = -1.0E-4F;

    private final List<Entry> entries = new ArrayList<>();

    public AnimationSoundPlayer add(AnimationState state, AnimationSoundTrack track) {
        this.entries.add(new Entry(state, track));
        return this;
    }

    public void tick(LivingEntity entity) {
        for (Entry entry : this.entries) {
            entry.tick(entity);
        }
    }

    private static final class Entry {
        private final AnimationState state;
        private final AnimationSoundTrack track;
        private float lastSeconds = NOT_PLAYING;

        private Entry(AnimationState state, AnimationSoundTrack track) {
            this.state = state;
            this.track = track;
        }

        private void tick(LivingEntity entity) {
            if (!this.state.isStarted()) {
                this.lastSeconds = NOT_PLAYING;
                return;
            }
            long millis = this.state instanceof SmoothAnimationState smooth
                    ? smooth.getAccumulatedTime()
                    : this.state.getTimeInMillis(entity.tickCount);
            float now = millis / 1000.0F;
            float previous = this.lastSeconds;
            this.lastSeconds = now;
            if (previous == NOT_PLAYING || now < previous) {
                previous = BEFORE_FIRST_CUE;
            }
            if (now <= previous) {
                return;
            }
            for (AnimationSoundTrack.Cue cue : this.track.cues()) {
                if (this.crossed(cue.time(), previous, now)) {
                    this.play(entity, cue);
                }
            }
        }

        private boolean crossed(float cueTime, float previous, float now) {
            if (!this.track.looping()) {
                return previous < cueTime && cueTime <= now;
            }
            float length = this.track.lengthSeconds();
            if (length <= 0.0F) {
                return false;
            }
            return Mth.floor((now - cueTime) / length) > Mth.floor((previous - cueTime) / length);
        }

        private void play(LivingEntity entity, AnimationSoundTrack.Cue cue) {
            entity.level().playLocalSound(entity.getX(), entity.getY(), entity.getZ(), cue.sound().get(),
                    entity.getSoundSource(), cue.volume().get(entity), cue.pitch().get(entity), false);
        }
    }
}
