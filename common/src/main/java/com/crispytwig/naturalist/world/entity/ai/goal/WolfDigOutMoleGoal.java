package com.crispytwig.naturalist.world.entity.ai.goal;

import com.crispytwig.naturalist.world.entity.WolfMoleDigging;
import com.crispytwig.naturalist.world.entity.animal.NaturalistAnimal;
import com.crispytwig.naturalist.world.entity.animal.mole.Mole;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumSet;

public class WolfDigOutMoleGoal extends Goal {
    private static final int DIG_DURATION = 50;
    private static final double START_RANGE_SQR = 4.0D;
    private static final double CONTINUE_RANGE_SQR = 6.25D;

    private final Wolf wolf;
    private Mole mole;
    private BlockState ground;
    private int digTicks;

    public WolfDigOutMoleGoal(Wolf wolf) {
        this.wolf = wolf;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        return this.wolf.getTarget() instanceof Mole target
                && target.isAlive() && target.canBeDugOut()
                && this.wolf.onGround()
                && this.wolf.distanceToSqr(target) <= START_RANGE_SQR;
    }

    @Override
    public boolean canContinueToUse() {
        return this.digTicks < DIG_DURATION
                && this.mole != null && this.mole.isAlive() && this.mole.canBeDugOut()
                && this.wolf.getTarget() == this.mole
                && this.wolf.distanceToSqr(this.mole) <= CONTINUE_RANGE_SQR;
    }

    @Override
    public void start() {
        this.mole = (Mole) this.wolf.getTarget();
        this.digTicks = 0;
        this.ground = resolveGround();
        ((WolfMoleDigging) this.wolf).naturalist$setDiggingOutMole(true);
    }

    @Override
    public void stop() {
        ((WolfMoleDigging) this.wolf).naturalist$setDiggingOutMole(false);
        this.wolf.getNavigation().stop();
        this.mole = null;
        this.digTicks = 0;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        NaturalistAnimal.freezeInPlace(this.wolf);
        this.wolf.getLookControl().setLookAt(this.mole.getX(), this.mole.getY(), this.mole.getZ());
        this.mole.holdForDigging();
        this.spawnDigEffects();
        if (++this.digTicks >= DIG_DURATION) {
            this.mole.popOut(this.wolf);
        }
    }

    private BlockState resolveGround() {
        BlockPos groundPos = this.mole.blockPosition().below();
        BlockState state = this.wolf.level().getBlockState(groundPos);
        if (state.isAir() || state.getCollisionShape(this.wolf.level(), groundPos).isEmpty()) {
            return Blocks.DIRT.defaultBlockState();
        }
        return state;
    }

    private void spawnDigEffects() {
        if (!(this.wolf.level() instanceof ServerLevel level)) {
            return;
        }
        double x = this.mole.getX();
        double y = this.mole.getY() + 0.1D;
        double z = this.mole.getZ();
        if (this.digTicks % 5 == 0) {
            level.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, this.ground), x, y, z, 12, 0.2D, 0.05D, 0.2D, 0.15D);
        }
        if (this.digTicks % 4 == 0) {
            SoundType soundType = this.ground.getSoundType();
            float pitch = soundType.getPitch() * (0.7F + this.wolf.getRandom().nextFloat() * 0.2F);
            level.playSound(null, x, y, z, soundType.getHitSound(), SoundSource.NEUTRAL, 0.9F, pitch);
        }
    }
}
