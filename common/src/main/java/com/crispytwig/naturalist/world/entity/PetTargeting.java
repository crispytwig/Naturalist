package com.crispytwig.naturalist.world.entity;

import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;

public final class PetTargeting {
    private PetTargeting() {
    }

    public static boolean protectsOwnedPet(TamableAnimal self, LivingEntity target) {
        EntityReference<LivingEntity> selfOwner = self.getOwnerReference();
        if (!self.isTame() || selfOwner == null || !(target instanceof OwnableEntity ownable)) {
            return true;
        }
        EntityReference<LivingEntity> targetOwner = ownable.getOwnerReference();
        return targetOwner == null || !selfOwner.getUUID().equals(targetOwner.getUUID());
    }

    public static boolean wantsToAttack(LivingEntity target, LivingEntity owner) {
        if (target instanceof Creeper || target instanceof Ghast || target instanceof ArmorStand) {
            return false;
        }
        if (target instanceof Player targetPlayer && owner instanceof Player ownerPlayer && !ownerPlayer.canHarmPlayer(targetPlayer)) {
            return false;
        }
        if (target instanceof AbstractHorse horse && horse.isTamed()) {
            return false;
        }
        return !(target instanceof TamableAnimal tamable) || !tamable.isTame();
    }
}
