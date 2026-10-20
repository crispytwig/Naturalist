package com.crispytwig.naturalist.mixin;

import com.crispytwig.naturalist.world.item.HedgehogItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public class EnchantmentMixin {
    @Inject(method = "canEnchant", at = @At("HEAD"), cancellable = true)
    private void naturalist$hedgehogCanEnchant(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        this.naturalist$applyHedgehogWhitelist(stack, cir);
    }

    @Inject(method = "isSupportedItem", at = @At("HEAD"), cancellable = true)
    private void naturalist$hedgehogIsSupportedItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        this.naturalist$applyHedgehogWhitelist(stack, cir);
    }

    @Inject(method = "isPrimaryItem", at = @At("HEAD"), cancellable = true)
    private void naturalist$hedgehogIsPrimaryItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        this.naturalist$applyHedgehogWhitelist(stack, cir);
    }

    @Unique
    private void naturalist$applyHedgehogWhitelist(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack.getItem() instanceof HedgehogItem) {
            cir.setReturnValue(HedgehogItem.isThrowEnchantment((Enchantment) (Object) this));
        }
    }
}
