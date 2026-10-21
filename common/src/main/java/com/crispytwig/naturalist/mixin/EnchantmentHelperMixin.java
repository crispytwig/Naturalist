package com.crispytwig.naturalist.mixin;

import com.crispytwig.naturalist.world.item.HedgehogItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    @Inject(method = "getAvailableEnchantmentResults", at = @At("HEAD"), cancellable = true)
    private static void naturalist$hedgehogTableEnchantments(int power, ItemStack stack, boolean allowTreasure, CallbackInfoReturnable<List<EnchantmentInstance>> cir) {
        if (!(stack.getItem() instanceof HedgehogItem)) {
            return;
        }
        List<EnchantmentInstance> results = new ArrayList<>();
        for (Enchantment enchantment : BuiltInRegistries.ENCHANTMENT) {
            if ((enchantment.isTreasureOnly() && !allowTreasure) || !enchantment.isDiscoverable() || !HedgehogItem.isThrowEnchantment(enchantment)) {
                continue;
            }
            for (int level = enchantment.getMaxLevel(); level > enchantment.getMinLevel() - 1; --level) {
                if (power >= enchantment.getMinCost(level) && power <= enchantment.getMaxCost(level)) {
                    results.add(new EnchantmentInstance(enchantment, level));
                    break;
                }
            }
        }
        cir.setReturnValue(results);
    }
}
