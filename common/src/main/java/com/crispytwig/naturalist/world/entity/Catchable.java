package com.crispytwig.naturalist.world.entity;

import java.util.Optional;

import com.crispytwig.naturalist.world.item.ItemHelper;
import com.crispytwig.naturalist.registry.NaturalistRegistry;
import com.crispytwig.naturalist.world.item.CaptureNetItem;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface
Catchable {
    boolean fromHand();

    void setFromHand(boolean fromHand);

    void saveToHandTag(ItemStack stack);

    void loadFromHandTag(CompoundTag tag);

    ItemStack getCaughtItemStack();

    @Nullable SoundEvent getPickupSound();

    static void saveDefaultDataToHandTag(@NotNull Mob mob, @NotNull ItemStack hand) {
        CompoundTag compoundTag = new CompoundTag();
        if (mob.hasCustomName()) {
            hand.setHoverName(mob.getCustomName());
        }

        if (mob.isNoAi()) {
            compoundTag.putBoolean("NoAI", mob.isNoAi());
        }

        if (mob.isSilent()) {
            compoundTag.putBoolean("Silent", mob.isSilent());
        }

        if (mob.isNoGravity()) {
            compoundTag.putBoolean("NoGravity", mob.isNoGravity());
        }

        if (mob.hasGlowingTag()) {
            compoundTag.putBoolean("Glowing", true);
        }

        if (mob.isInvulnerable()) {
            compoundTag.putBoolean("Invulnerable", mob.isInvulnerable());
        }

        compoundTag.putFloat("Health", mob.getHealth());
        ItemHelper.mergeTag(hand, compoundTag);
    }

    static void loadDefaultDataFromHandTag(@NotNull Mob mob, @Nullable CompoundTag tag) {
        if (tag == null) return;
        if (tag.contains("NoAI")) {
            mob.setNoAi(tag.getBoolean("NoAI"));
        }

        if (tag.contains("Silent")) {
            mob.setSilent(tag.getBoolean("Silent"));
        }

        if (tag.contains("NoGravity")) {
            mob.setNoGravity(tag.getBoolean("NoGravity"));
        }

        if (tag.contains("Glowing")) {
            mob.setGlowingTag(tag.getBoolean("Glowing"));
        }

        if (tag.contains("Invulnerable")) {
            mob.setInvulnerable(tag.getBoolean("Invulnerable"));
        }

        if (tag.contains("Health", 99)) {
            mob.setHealth(tag.getFloat("Health"));
        }

    }

    static <T extends TamableAnimal & FollowingPet> void saveTamableDataToHandTag(@NotNull T entity, @NotNull CompoundTag tag) {
        if (entity.isTame() && entity.getOwnerUUID() != null) {
            tag.putBoolean("Tame", true);
            tag.putUUID("Owner", entity.getOwnerUUID());
            tag.putBoolean("FollowingOwner", entity.isFollowingOwner());
            tag.putBoolean("Sitting", entity.isOrderedToSit());
        }
    }

    static <T extends TamableAnimal & FollowingPet> void loadTamableDataFromHandTag(@NotNull T entity, @NotNull CompoundTag tag) {
        if (tag.getBoolean("Tame") && tag.hasUUID("Owner")) {
            entity.setOwnerUUID(tag.getUUID("Owner"));
            entity.setTame(true);
            entity.setFollowingOwner(tag.getBoolean("FollowingOwner"));
            entity.setOrderedToSit(tag.getBoolean("Sitting"));
        }
    }

    static <T extends LivingEntity & Catchable> @NotNull Optional<InteractionResult> catchAnimal(Player player, @NotNull InteractionHand hand, T entity, boolean needsNet) {
        ItemStack itemStack = player.getItemInHand(hand);
        if ((needsNet ? itemStack.getItem().equals(NaturalistRegistry.CAPTURE_NET.get()) : itemStack.isEmpty()) && entity.isAlive()) {
            if (needsNet) {
                CaptureNetItem.swing(entity.level(), player);
                CaptureNetItem.playCaughtEffects(entity.level(), entity);
            }
            ItemStack caughtItemStack = entity.getCaughtItemStack();
            entity.saveToHandTag(caughtItemStack);
            if (needsNet) {
                itemStack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
            }
            if (player.getInventory().add(caughtItemStack)) {
                entity.discard();
                return Optional.of(InteractionResult.SUCCESS);
            }
            else {
                ItemHelper.spawnItemOnEntity(player, caughtItemStack);
            }
            player.playSound(SoundEvents.ITEM_PICKUP, 0.3F, 1.0F);
            if (!entity.level().isClientSide) {
                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer)player, caughtItemStack);
            }
            entity.discard();
            return Optional.of(InteractionResult.SUCCESS);
        } else {
            return Optional.empty();
        }
    }
}
