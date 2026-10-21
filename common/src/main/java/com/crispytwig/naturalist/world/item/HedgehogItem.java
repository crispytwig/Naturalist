package com.crispytwig.naturalist.world.item;

import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import com.crispytwig.naturalist.registry.NaturalistEntityTypes;
import com.crispytwig.naturalist.world.entity.animal.hedgehog.Hedgehog;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class HedgehogItem extends CaughtEntityItem {
    private static final float MAX_HEALTH = 18.0F;

    public HedgehogItem(Supplier<? extends EntityType<?>> entitySupplier, Supplier<? extends SoundEvent> soundSupplier, String tooltipPrefix, String[] variantNames, Properties properties) {
        super(entitySupplier, soundSupplier, tooltipPrefix, variantNames, properties);
    }

    public static boolean isThrowEnchantment(Enchantment enchantment) {
        return enchantment == Enchantments.UNBREAKING || enchantment == Enchantments.THORNS
                || enchantment == Enchantments.PUNCH_ARROWS || enchantment == Enchantments.FLAMING_ARROWS
                || enchantment == Enchantments.MOB_LOOTING || enchantment == Enchantments.LOYALTY;
    }

    private static float getStoredHealth(ItemStack stack) {
        CompoundTag tag = ItemHelper.copyTag(stack);
        return tag.contains("Health", 99) ? tag.getFloat("Health") : MAX_HEALTH;
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
    public int getEnchantmentValue() {
        return 1;
    }

    @Override
    public boolean isEnchantable(@NotNull ItemStack stack) {
        return stack.getMaxStackSize() == 1;
    }

    @Override
    public void checkExtraContent(@Nullable Player player, @NotNull Level level, @NotNull ItemStack containerStack, @NotNull BlockPos pos) {
        if (level instanceof ServerLevel serverLevel) {
            NaturalistEntityTypes.HEDGEHOG.get().spawn(serverLevel, (CompoundTag) null, EntityType.<Hedgehog>createDefaultStackConfig(serverLevel, containerStack, player).andThen(hedgehog -> {
                hedgehog.loadFromHandTag(ItemHelper.copyTag(containerStack));
                hedgehog.setNoGravity(false);
                hedgehog.setFromHand(true);
                hedgehog.setPersistenceRequired();
                hedgehog.setThrowEnchantments(EnchantmentHelper.getEnchantments(containerStack));
            }), pos, MobSpawnType.BUCKET, true, false);
            level.gameEvent(player, GameEvent.ENTITY_PLACE, pos);
        }
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE).getType() != HitResult.Type.MISS) {
            return super.use(level, player, hand);
        }
        if (level instanceof ServerLevel serverLevel) {
            Hedgehog hedgehog = NaturalistEntityTypes.HEDGEHOG.get().create(serverLevel);
            if (hedgehog == null) {
                return InteractionResultHolder.pass(stack);
            }
            Vec3 look = player.getLookAngle();
            hedgehog.moveTo(player.getX() + look.x * 0.6, player.getEyeY() - 0.3 + look.y * 0.6, player.getZ() + look.z * 0.6, player.getYRot(), 0.0F);
            hedgehog.loadFromHandTag(ItemHelper.copyTag(stack));
            if (stack.hasCustomHoverName()) {
                hedgehog.setCustomName(stack.getHoverName());
            }
            hedgehog.setNoGravity(false);
            hedgehog.setFromHand(true);
            hedgehog.setPersistenceRequired();
            hedgehog.setThrowEnchantments(EnchantmentHelper.getEnchantments(stack));
            hedgehog.setThrower(player.getUUID());
            hedgehog.setRolling(true);
            hedgehog.setDeltaMovement(look.scale(1.4).add(player.getDeltaMovement().multiply(1.0, 0.0, 1.0)));
            serverLevel.addFreshEntity(hedgehog);
        }
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.NEUTRAL, 0.5F, 0.9F);
        player.awardStat(Stats.ITEM_USED.get(this));
        player.getCooldowns().addCooldown(this, 20);
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
