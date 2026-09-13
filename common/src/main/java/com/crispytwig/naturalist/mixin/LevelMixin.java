package com.crispytwig.naturalist.mixin;

import com.crispytwig.naturalist.world.entity.MobPart;
import com.crispytwig.naturalist.world.level.MultipartLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

@Mixin(Level.class)
public abstract class LevelMixin implements MultipartLevel {
    @Unique
    private final Set<MobPart> naturalist$mobParts = new HashSet<>();

    @Override
    public void naturalist$addMobPart(MobPart part) {
        this.naturalist$mobParts.add(part);
    }

    @Override
    public void naturalist$removeMobPart(MobPart part) {
        this.naturalist$mobParts.remove(part);
    }

    @Override
    public MobPart naturalist$getMobPart(int id) {
        for (MobPart part : this.naturalist$mobParts) {
            if (part.getId() == id) {
                return part;
            }
        }
        return null;
    }

    @Inject(method = "getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;",
            at = @At("RETURN"))
    private void naturalist$includeMobParts(Entity except, AABB box, Predicate<? super Entity> predicate, CallbackInfoReturnable<List<Entity>> cir) {
        if (this.naturalist$mobParts.isEmpty()) {
            return;
        }
        List<Entity> list = cir.getReturnValue();
        for (MobPart part : this.naturalist$mobParts) {
            if (part != except && part.getParent() != except && part.getBoundingBox().intersects(box) && predicate.test(part)) {
                list.add(part);
            }
        }
    }
}
