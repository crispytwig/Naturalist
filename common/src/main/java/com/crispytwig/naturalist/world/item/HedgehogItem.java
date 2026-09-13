package com.crispytwig.naturalist.world.item;

import com.crispytwig.naturalist.registry.NaturalistEntityTypes;
import com.crispytwig.naturalist.world.entity.animal.hedgehog.Hedgehog;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.Supplier;

public class HedgehogItem extends CaughtMobWithVariantsItem {
    private static final float MAX_HEALTH = 18.0F;
    private static final Set<String> THROW_ENCHANTMENT_KEYS = Set.of(
            "enchantment.minecraft.unbreaking", "enchantment.minecraft.thorns", "enchantment.minecraft.punch",
            "enchantment.minecraft.flame", "enchantment.minecraft.looting", "enchantment.minecraft.loyalty");

    public HedgehogItem(Supplier<? extends EntityType<?>> entitySupplier, Supplier<? extends Fluid> fluidSupplier, Supplier<? extends SoundEvent> soundSupplier, String tooltipPrefix, String[] variantNames, Properties properties) {
        super(entitySupplier, fluidSupplier, soundSupplier, tooltipPrefix, variantNames, properties);
    }

    public static boolean isThrowEnchantment(Enchantment enchantment) {
        return enchantment.description().getContents() instanceof TranslatableContents contents
                && THROW_ENCHANTMENT_KEYS.contains(contents.getKey());
    }

    private static float getStoredHealth(ItemStack stack) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        return tag.getFloatOr("Health", MAX_HEALTH);
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack stack) {
        return getStoredHealth(stack) < MAX_HEALTH;
    }

    @Override
    public int getBarWidth(@NotNull ItemStack stack) {
        return Math.round(13.0F * Mth.clamp(getStoredHealth(stack) / MAX_HEALTH, 0.0F, 1.0F));
    }

    @Override
    public int getBarColor(@NotNull ItemStack stack) {
        return Mth.hsvToRgb(Mth.clamp(getStoredHealth(stack) / MAX_HEALTH, 0.0F, 1.0F) / 3.0F, 1.0F, 1.0F);
    }

    @Override
    public void checkExtraContent(@Nullable LivingEntity user, @NotNull Level level, @NotNull ItemStack containerStack, @NotNull BlockPos pos) {
        if (level instanceof ServerLevel serverLevel) {
            Entity entity = NaturalistEntityTypes.HEDGEHOG.get().spawn(serverLevel, containerStack, user, pos, EntitySpawnReason.BUCKET, true, false);
            if (entity instanceof Hedgehog hedgehog) {
                hedgehog.loadFromHandTag(containerStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag());
                hedgehog.setNoGravity(false);
                hedgehog.setFromHand(true);
                hedgehog.setThrowEnchantments(containerStack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY));
            }
            level.gameEvent(user, GameEvent.ENTITY_PLACE, pos);
        }
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE).getType() != HitResult.Type.MISS) {
            return super.use(level, player, hand);
        }
        if (level instanceof ServerLevel serverLevel) {
            Hedgehog hedgehog = NaturalistEntityTypes.HEDGEHOG.get().create(serverLevel, EntitySpawnReason.SPAWN_ITEM_USE);
            if (hedgehog == null) {
                return InteractionResult.PASS;
            }
            Vec3 look = player.getLookAngle();
            hedgehog.snapTo(player.getX() + look.x * 0.6, player.getEyeY() - 0.3 + look.y * 0.6, player.getZ() + look.z * 0.6, player.getYRot(), 0.0F);
            hedgehog.loadFromHandTag(stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag());
            if (stack.has(DataComponents.CUSTOM_NAME)) {
                hedgehog.setCustomName(stack.getHoverName());
            }
            hedgehog.setNoGravity(false);
            hedgehog.setFromHand(true);
            hedgehog.setThrowEnchantments(stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY));
            hedgehog.setThrower(player.getUUID());
            hedgehog.setRolling(true);
            hedgehog.setDeltaMovement(look.scale(1.4).add(player.getDeltaMovement().multiply(1.0, 0.0, 1.0)));
            serverLevel.addFreshEntity(hedgehog);
        }
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.NEUTRAL, 0.5F, 0.9F);
        player.awardStat(Stats.ITEM_USED.get(this));
        player.getCooldowns().addCooldown(stack, 20);
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        return InteractionResult.SUCCESS;
    }
}
