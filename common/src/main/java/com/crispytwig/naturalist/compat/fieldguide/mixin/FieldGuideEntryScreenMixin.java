package com.crispytwig.naturalist.compat.fieldguide.mixin;

import com.crispytwig.naturalist.world.entity.Catchable;
import com.crispytwig.naturalist.world.entity.variant.DataDrivenVariantAnimal;
import com.crispytwig.naturalist.world.entity.variant.MobVariant;
import com.evandev.fieldguide.client.gui.screens.FieldGuideEntryScreen;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Bucketable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Mixin(FieldGuideEntryScreen.class)
public class FieldGuideEntryScreenMixin {

    @Shadow
    private Entity renderedEntity;

    @Shadow
    private List<ItemStack> loadedDrops;

    @Shadow
    @Final
    private List<Identifier> spawnBiomes;

    @Inject(method = "setupDropWidget", at = @At("HEAD"))
    private void naturalist$showCatchItems(boolean unlocked, CallbackInfo ci) {
        if (!unlocked) return;

        Entity entity = this.renderedEntity;
        if (entity == null) return;
        if (!BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).getNamespace().equals("naturalist")) return;

        ItemStack stack = null;
        if (entity instanceof Catchable catchable) {
            stack = catchable.getCaughtItemStack();
            catchable.saveToHandTag(stack);
        } else if (entity instanceof Bucketable bucketable) {
            stack = bucketable.getBucketItemStack();
            bucketable.saveToBucketTag(stack);
        }

        if (stack == null || stack.isEmpty()) return;

        ItemStack catchStack = stack;
        List<ItemStack> drops = new ArrayList<>(this.loadedDrops);
        drops.removeIf(existing -> ItemStack.isSameItemSameComponents(existing, catchStack));
        drops.add(catchStack);
        this.loadedDrops = drops;
    }

    @Inject(method = "setupBiomeWidget", at = @At("HEAD"))
    private void naturalist$filterBiomesByVariant(boolean unlocked, CallbackInfo ci) {
        if (!(this.renderedEntity instanceof DataDrivenVariantAnimal animal)) return;

        var holder = animal.getVariantHolder();
        if (holder.isEmpty()) return;

        var variantBiomes = holder.get().value().biomes();
        if (variantBiomes.isPresent()) {
            List<Identifier> biomes = naturalist$resolveBiomes(variantBiomes.get());
            if (!biomes.isEmpty()) {
                this.spawnBiomes.clear();
                this.spawnBiomes.addAll(biomes);
            }
            return;
        }

        var registry = this.renderedEntity.level().registryAccess().lookup(animal.getVariantRegistryKey());
        if (registry.isEmpty()) return;

        Set<Identifier> claimed = new HashSet<>();
        for (MobVariant variant : registry.get()) {
            variant.biomes().ifPresent(biomes -> claimed.addAll(naturalist$resolveBiomes(biomes)));
        }
        this.spawnBiomes.removeIf(claimed::contains);
    }

    @Unique
    private static List<Identifier> naturalist$resolveBiomes(HolderSet<Biome> biomes) {
        return biomes.stream()
                .map(Holder::unwrapKey)
                .filter(Optional::isPresent)
                .map(key -> key.get().identifier())
                .distinct()
                .toList();
    }
}
