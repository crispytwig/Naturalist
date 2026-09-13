package com.crispytwig.naturalist.world.entity;

import com.crispytwig.naturalist.world.item.ItemHelper;
import com.crispytwig.naturalist.registry.NaturalistRegistry;
import com.crispytwig.naturalist.world.item.BugNetItem;
import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

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
            hand.set(DataComponents.CUSTOM_NAME, mob.getCustomName());
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
        hand.set(DataComponents.CUSTOM_DATA, CustomData.of(compoundTag));
    }

    static void loadDefaultDataFromHandTag(@NotNull Mob mob, @Nullable CompoundTag tag) {
        if (tag == null) return;
        tag.getBoolean("NoAI").ifPresent(mob::setNoAi);
        tag.getBoolean("Silent").ifPresent(mob::setSilent);
        tag.getBoolean("NoGravity").ifPresent(mob::setNoGravity);
        tag.getBoolean("Glowing").ifPresent(mob::setGlowingTag);
        tag.getBoolean("Invulnerable").ifPresent(mob::setInvulnerable);
        tag.getFloat("Health").ifPresent(mob::setHealth);
    }

    static <T extends TamableAnimal & FollowingPet> void saveTamableDataToHandTag(@NotNull T entity, @NotNull CompoundTag tag) {
        EntityReference<LivingEntity> owner = entity.getOwnerReference();
        if (entity.isTame() && owner != null) {
            tag.putBoolean("Tame", true);
            tag.store("Owner", UUIDUtil.CODEC, owner.getUUID());
            tag.putBoolean("FollowingOwner", entity.isFollowingOwner());
            tag.putBoolean("Sitting", entity.isOrderedToSit());
        }
    }

    static <T extends TamableAnimal & FollowingPet> void loadTamableDataFromHandTag(@NotNull T entity, @NotNull CompoundTag tag) {
        Optional<java.util.UUID> owner = tag.read("Owner", UUIDUtil.CODEC);
        if (tag.getBooleanOr("Tame", false) && owner.isPresent()) {
            entity.setOwnerReference(EntityReference.of(owner.get()));
            entity.setTame(true, true);
            entity.setFollowingOwner(tag.getBooleanOr("FollowingOwner", false));
            entity.setOrderedToSit(tag.getBooleanOr("Sitting", false));
        }
    }

    static <T extends LivingEntity & Catchable> @NotNull Optional<InteractionResult> catchAnimal(Player player, @NotNull InteractionHand hand, T entity, boolean needsNet) {
        ItemStack itemStack = player.getItemInHand(hand);
        if ((needsNet ? itemStack.getItem().equals(NaturalistRegistry.CAPTURE_NET.get()) : itemStack.isEmpty()) && entity.isAlive()) {
            if (needsNet) {
                BugNetItem.swing(entity.level(), player);
                BugNetItem.playCaughtEffects(entity.level(), entity);
            }
            ItemStack caughtItemStack = entity.getCaughtItemStack();
            entity.saveToHandTag(caughtItemStack);
            if (needsNet) {
                itemStack.hurtAndBreak(1, player, hand);
            }
            if (player.getInventory().add(caughtItemStack)) {
                entity.discard();
                return Optional.of(InteractionResult.SUCCESS);
            }
            else {
                ItemHelper.spawnItemOnEntity(player, caughtItemStack);
            }
            player.playSound(SoundEvents.ITEM_PICKUP, 0.3F, 1.0F);
            if (!entity.level().isClientSide()) {
                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer)player, caughtItemStack);
            }
            entity.discard();
            return Optional.of(InteractionResult.SUCCESS);
        } else {
            return Optional.empty();
        }
    }
}
