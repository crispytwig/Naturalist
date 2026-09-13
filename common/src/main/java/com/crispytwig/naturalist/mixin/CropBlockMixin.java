package com.crispytwig.naturalist.mixin;

import com.crispytwig.naturalist.world.entity.animal.snail.Snail;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CropBlock.class)
public class CropBlockMixin {
    @Inject(at = @At(value = "HEAD"), method = "randomTick", cancellable = true)
    @SuppressWarnings("unused")
    public void naturalist$onRandomTick(BlockState state, ServerLevel level, @NotNull BlockPos pos, RandomSource random, CallbackInfo ci) {
        if (!level.getEntitiesOfClass(Snail.class, new AABB(pos).inflate(2.0D)).isEmpty() && random.nextBoolean()) {
            ci.cancel();
        }
    }
}
