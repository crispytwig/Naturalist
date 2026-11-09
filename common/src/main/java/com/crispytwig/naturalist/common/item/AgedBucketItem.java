package com.crispytwig.naturalist.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class AgedBucketItem extends NaturalistBucketItem {
    public AgedBucketItem(EntityType<?> entityType, Fluid content, SoundEvent emptySound, Properties properties, boolean allowMidWater, @Nullable String tooltipPrefix, @Nullable String[] variantNames) {
        super(entityType, content, emptySound, properties, allowMidWater, tooltipPrefix, variantNames);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext context, @NotNull TooltipDisplay display, @NotNull Consumer<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, context, display, tooltip, flag);
        tooltip.accept(Component.translatable(stack.getOrDefault(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY).copyTag().getIntOr("Age", 0) < 0 ? "tooltip.naturalist.baby" : "tooltip.naturalist.adult").withStyle(ChatFormatting.BLUE));
    }
}
