package com.crispytwig.naturalist.world.item;

import org.jetbrains.annotations.Nullable;
import net.minecraft.world.level.Level;
import com.crispytwig.naturalist.world.entity.animal.snail.Snail;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public class SnailItem extends CaughtEntityItem {
    public SnailItem(Supplier<? extends EntityType<?>> entitySupplier, Supplier<? extends SoundEvent> soundSupplier, Properties properties) {
        super(entitySupplier, soundSupplier, properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level context, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        CompoundTag tag = ItemHelper.copyTag(stack);
        if (tag.contains("Color", 3)) {
            Snail.Color color = Snail.Color.byId(tag.getInt("Color"));
            tooltip.add(Component.translatable(String.format("item.minecraft.firework_star.%s", color.toString().toLowerCase())).withStyle(ChatFormatting.GRAY));
        }
    }
}
