package com.crispytwig.naturalist.world.entity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;

public final class PetTargeting {
    private PetTargeting() {
    }

    public static boolean protectsOwnedPet(TamableAnimal self, LivingEntity target) {
        return !self.isTame() || self.getOwnerUUID() == null || !(target instanceof OwnableEntity ownable)
                || !self.getOwnerUUID().equals(ownable.getOwnerUUID());
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
