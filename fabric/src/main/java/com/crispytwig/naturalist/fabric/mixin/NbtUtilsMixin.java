package com.crispytwig.naturalist.fabric.mixin;

import com.crispytwig.naturalist.world.level.LegacyIds;
import net.minecraft.core.HolderGetter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NbtUtils.class)
public class NbtUtilsMixin {
    @Inject(method = "readBlockState", at = @At("HEAD"))
    private static void naturalist$remapLegacyBlockId(HolderGetter<Block> blockGetter, CompoundTag tag, CallbackInfoReturnable<BlockState> cir) {
        if (tag.contains("Name", Tag.TAG_STRING)) {
            String renamed = LegacyIds.BLOCKS.get(tag.getString("Name"));
            if (renamed != null) {
                tag.putString("Name", renamed);
            }
        }
    }
}
