package com.crispytwig.naturalist.mixin;

import net.minecraft.nbt.CompoundTag;
import com.crispytwig.naturalist.registry.NaturalistRegistry;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Fox.class)
public abstract class FoxMixin extends Animal {
    @Unique
    private static final float naturalist$wildOnesChance = 0.02F;

    protected FoxMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "finalizeSpawn", at = @At("RETURN"))
    @SuppressWarnings("unused")
    private void naturalist$carryWildOnes(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag dataTag, CallbackInfoReturnable<SpawnGroupData> cir) {
        if (spawnType != MobSpawnType.NATURAL && spawnType != MobSpawnType.CHUNK_GENERATION && spawnType != MobSpawnType.SPAWN_EGG) {
            return;
        }
        if (this.isBaby() || this.random.nextFloat() >= naturalist$wildOnesChance) {
            return;
        }
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(NaturalistRegistry.MUSIC_DISC_WILD_ONES.get()));
        this.setGuaranteedDrop(EquipmentSlot.MAINHAND);
        this.setCanPickUpLoot(false);
    }
}
