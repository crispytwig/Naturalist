package com.crispytwig.naturalist.mixin;

import com.crispytwig.naturalist.world.entity.animal.dragonfly.Dragonfly;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BottleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BottleItem.class)
public class BottleItemMixin extends Item {
    public BottleItemMixin(@NotNull Properties properties) {
        super(properties);
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void onUse(Level level, @NotNull Player player, InteractionHand usedHand, CallbackInfoReturnable<InteractionResult> cir) {
        List<AreaEffectCloud> list = level.getEntitiesOfClass(AreaEffectCloud.class, player.getBoundingBox().inflate(2.0), areaEffectCloud -> areaEffectCloud != null && areaEffectCloud.isAlive() && areaEffectCloud.getOwner() instanceof Dragonfly);
        ItemStack itemStack = player.getItemInHand(usedHand);
        if (!list.isEmpty()) {
            list.getFirst().discard();
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL_DRAGONBREATH, SoundSource.NEUTRAL, 1.0f, 1.0f);
            level.gameEvent(player, GameEvent.FLUID_PICKUP, player.position());
            cir.setReturnValue(InteractionResult.SUCCESS.heldItemTransformedTo(this.naturalist$onTurnBottleIntoItem(itemStack, player, new ItemStack(Items.DRAGON_BREATH))));
        }
    }

    @Unique
    protected ItemStack naturalist$onTurnBottleIntoItem(ItemStack bottleStack, Player player, ItemStack filledBottleStack) {
        player.awardStat(Stats.ITEM_USED.get((BottleItem) (Object) this));
        return ItemUtils.createFilledResult(bottleStack, player, filledBottleStack);
    }
}
