package com.crispytwig.naturalist.world.entity.ai.goal;

import com.crispytwig.naturalist.world.entity.EggLayingAnimal;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class LayEggGoal<T extends Animal & EggLayingAnimal> extends MoveToBlockGoal {
    private final @NotNull T animal;
    public LayEggGoal(@NotNull T animal, double speedModifier) {
        super(animal, speedModifier, 16);
        this.animal = animal;
    }

    @Override
    public boolean canUse() {
        if (this.animal.hasEgg()) {
            return super.canUse();
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && this.animal.hasEgg();
    }

    @Override
    public void tick() {
        super.tick();
        BlockPos pos = this.animal.blockPosition();

        if (this.animal instanceof TamableAnimal ta && ta.isInSittingPose()) {
            this.animal.setLayingEgg(false);
            return;
        }

        if (!this.animal.isInWater() && this.isReachedTarget()) {
            if (this.animal.getLayEggCounter() < 1) {
                this.animal.setLayingEgg(true);
            }
            else if (this.animal.getLayEggCounter() > this.adjustedTickDelay(60)) {
                Level level = this.animal.level();
                level.playSound(null, pos, SoundEvents.TURTLE_LAY_EGG,
                        SoundSource.BLOCKS, 0.3f,
                        0.9f + level.getRandom().nextFloat() * 0.2f);

                BlockState eggState = this.animal.createEggBlockState(this.animal.getRandom().nextInt(4) + 1);

                BlockPos eggPos = this.blockPos.above();
                level.setBlock(eggPos, eggState, 3);
                this.animal.onEggLaid(eggPos);
                this.animal.setHasEgg(false);
                this.animal.setLayingEgg(false);
                this.animal.setInLoveTime(600);
            }

            if (this.animal.isLayingEgg()) {
                this.animal.setLayEggCounter(this.animal.getLayEggCounter() + 1);
            }
        }
    }

    @Override
    protected boolean isValidTarget(LevelReader level, BlockPos pos) {
        return level.isEmptyBlock(pos.above()) && level.getBlockState(pos).is(this.animal.getEggLayableBlockTag());
    }
}
