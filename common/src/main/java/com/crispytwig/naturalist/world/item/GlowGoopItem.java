package com.crispytwig.naturalist.world.item;

import com.crispytwig.naturalist.NaturalistClientConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class GlowGoopItem extends BlockItem {
    public GlowGoopItem(Block block, @NotNull Properties properties) {
        super(block, properties);
    }

    @SuppressWarnings("unused")
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull Item.TooltipContext context, @NotNull TooltipDisplay display, @NotNull Consumer<Component> tooltip, @NotNull TooltipFlag flagIn) {
        if (!NaturalistClientConfig.isGlowGoopTooltipEnabled()) {
            return;
        }
        if (Minecraft.getInstance().hasShiftDown()) {
            tooltip.accept(Component.literal("Place up to 3").withStyle(ChatFormatting.GRAY));
            tooltip.accept(Component.literal("in one space!").withStyle(ChatFormatting.GRAY));
        } else {
            tooltip.accept(Component.literal("Hold ").withStyle(ChatFormatting.GRAY).append(Component.literal("Shift").withStyle(ChatFormatting.YELLOW, ChatFormatting.ITALIC)).append(Component.literal(" for info!").withStyle(ChatFormatting.GRAY)));
        }
    }
}
