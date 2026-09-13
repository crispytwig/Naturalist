package com.crispytwig.naturalist.world.entity;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class AnimationSoundTrack {
    @FunctionalInterface
    public interface SoundParam {
        float get(LivingEntity entity);
    }

    public record Cue(float time, Supplier<SoundEvent> sound, SoundParam volume, SoundParam pitch) {
    }

    private final float lengthSeconds;
    private final boolean looping;
    private final List<Cue> cues;

    private AnimationSoundTrack(float lengthSeconds, boolean looping, List<Cue> cues) {
        this.lengthSeconds = lengthSeconds;
        this.looping = looping;
        this.cues = List.copyOf(cues);
    }

    public float lengthSeconds() {
        return this.lengthSeconds;
    }

    public boolean looping() {
        return this.looping;
    }

    public List<Cue> cues() {
        return this.cues;
    }

    public static Builder builder(float lengthSeconds, boolean looping) {
        return new Builder(lengthSeconds, looping);
    }

    public static final class Builder {
        private final float lengthSeconds;
        private final boolean looping;
        private final List<Cue> cues = new ArrayList<>();

        private Builder(float lengthSeconds, boolean looping) {
            this.lengthSeconds = lengthSeconds;
            this.looping = looping;
        }

        public Builder at(float time, Supplier<SoundEvent> sound, float volume, float pitch) {
            return this.at(time, sound, entity -> volume, entity -> pitch);
        }

        public Builder at(float time, Supplier<SoundEvent> sound, SoundParam volume, SoundParam pitch) {
            this.cues.add(new Cue(time, sound, volume, pitch));
            return this;
        }

        public AnimationSoundTrack build() {
            return new AnimationSoundTrack(this.lengthSeconds, this.looping, this.cues);
        }
    }
}
