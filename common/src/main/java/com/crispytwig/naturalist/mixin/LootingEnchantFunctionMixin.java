package com.crispytwig.naturalist.mixin;

import com.crispytwig.naturalist.world.entity.animal.hedgehog.Hedgehog;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LootingEnchantFunction.class)
public class LootingEnchantFunctionMixin {
    @ModifyVariable(method = "run(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/storage/loot/LootContext;)Lnet/minecraft/world/item/ItemStack;",
            at = @At("STORE"), ordinal = 0)
    private int naturalist$addHedgehogLooting(int level, ItemStack stack, LootContext context) {
        if (context.getParamOrNull(LootContextParams.DIRECT_KILLER_ENTITY) instanceof Hedgehog hedgehog) {
            level += hedgehog.getThrowEnchantmentLevel(Enchantments.MOB_LOOTING);
        }
        return level;
    }
}
