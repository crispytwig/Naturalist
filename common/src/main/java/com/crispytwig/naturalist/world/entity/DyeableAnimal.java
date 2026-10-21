package com.crispytwig.naturalist.world.entity;

import com.crispytwig.naturalist.tags.NaturalistItemTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface DyeableAnimal {
    @Nullable DyeColor getDyeColor();

    void setDyeColor(@Nullable DyeColor color);

    default boolean isDyeableBy(Player player) {
        return this instanceof TamableAnimal tamable && tamable.isTame() && tamable.isOwnedBy(player);
    }

    static void saveDye(DyeableAnimal animal, CompoundTag tag) {
        DyeColor color = animal.getDyeColor();
        tag.putInt("DyeColor", color == null ? -1 : color.getId());
    }

    static void loadDye(DyeableAnimal animal, CompoundTag tag) {
        if (tag.contains("DyeColor")) {
            int id = tag.getInt("DyeColor");
            animal.setDyeColor(id < 0 ? null : DyeColor.byId(id));
        }
    }

    static <T extends LivingEntity & DyeableAnimal> Optional<InteractionResult> tryDye(T animal, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() instanceof DyeItem dyeItem && animal.isDyeableBy(player)) {
            DyeColor color = dyeItem.getDyeColor();
            if (color != animal.getDyeColor()) {
                if (!animal.level().isClientSide) {
                    animal.setDyeColor(color);
                    animal.playSound(SoundEvents.DYE_USE, 1.0F, 1.0F);
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                }
                return Optional.of(InteractionResult.sidedSuccess(animal.level().isClientSide));
            }
        }
        return Optional.empty();
    }

    static <T extends LivingEntity & DyeableAnimal> Optional<InteractionResult> tryClearDye(T animal, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (animal.getDyeColor() != null && stack.is(NaturalistItemTags.SHEARS) && animal.isDyeableBy(player)) {
            if (!animal.level().isClientSide) {
                animal.setDyeColor(null);
                animal.playSound(SoundEvents.SHEEP_SHEAR, 1.0F, 1.0F);
                stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
            }
            return Optional.of(InteractionResult.sidedSuccess(animal.level().isClientSide));
        }
        return Optional.empty();
    }
}
