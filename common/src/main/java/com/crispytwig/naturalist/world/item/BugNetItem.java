package com.crispytwig.naturalist.world.item;

import com.crispytwig.naturalist.world.item.crafting.BugNetInteractionRecipe;
import com.crispytwig.naturalist.registry.NaturalistParticleTypes;
import com.crispytwig.naturalist.registry.NaturalistRecipes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class BugNetItem extends Item {
    public BugNetItem(Properties properties) {
        super(properties);
    }

    public static void swing(@NotNull Level level, @NotNull Player player) {
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1.0F, 1.0F);
        if (level instanceof ServerLevel serverLevel) {
            Vec3 look = player.getLookAngle();
            double dist = 1.5;
            double px = player.getX() + look.x * dist;
            double py = player.getEyeY() + look.y * dist;
            double pz = player.getZ() + look.z * dist;
            serverLevel.sendParticles(NaturalistParticleTypes.CAPTURE_NET_SWING.get(), px, py, pz, 1, 0.0, 0.0, 0.0, 0.0);
        }
    }

    public static void playCaughtEffects(@NotNull Level level, @NotNull LivingEntity target) {
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.POOF, target.getX(), target.getY(0.5), target.getZ(), 15,
                    target.getBbWidth() / 2.0, target.getBbHeight() / 2.0, target.getBbWidth() / 2.0, 0.02);
        }
        level.playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.8F, 1.0F);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level level, Player player, @NotNull InteractionHand usedHand) {
        swing(level, player);
        player.swing(usedHand);
        return InteractionResult.SUCCESS;
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack stack, Player player, @NotNull LivingEntity interactionTarget, @NotNull InteractionHand usedHand) {
        if (!(player.level() instanceof ServerLevel serverLevel)) {
            return super.interactLivingEntity(stack, player, interactionTarget, usedHand);
        }
        Optional<BugNetInteractionRecipe> allRecipes = serverLevel.recipeAccess().getRecipes()
                .stream()
                .filter(r -> r.value().getType() == NaturalistRecipes.BUG_NET.get())
                .map(r -> ((RecipeHolder<BugNetInteractionRecipe>) r).value())
                .filter(r -> r.entityType() == interactionTarget.getType())
                .filter(r -> r.hasIngredient(player))
                .findFirst();

        if (allRecipes.isPresent()) {
            BugNetInteractionRecipe recipe = allRecipes.get();
            var dropItem = recipe.dropStack();
            recipe.consumeIngredient(player);
            swing(player.level(), player);
            Containers.dropItemStack(player.level(), interactionTarget.getX(), interactionTarget.getY(), interactionTarget.getZ(), dropItem);
            playCaughtEffects(player.level(), interactionTarget);
            interactionTarget.discard();
            return InteractionResult.SUCCESS;
        }

        return super.interactLivingEntity(stack, player, interactionTarget, usedHand);
    }
}
