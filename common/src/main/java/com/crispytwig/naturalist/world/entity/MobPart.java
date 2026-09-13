package com.crispytwig.naturalist.world.entity;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import com.crispytwig.naturalist.world.level.MultipartLevel;

@SuppressWarnings("unused")
public class MobPart extends Entity {
    private final Mob parent;
    private final EntityDimensions baseSize;
    private float scale = 1.0F;

    public MobPart(Mob parent, float width, float height) {
        super(parent.getType(), parent.level());
        this.parent = parent;
        this.baseSize = EntityDimensions.scalable(width, height);
        this.refreshDimensions();
    }

    public Mob getParent() {
        return this.parent;
    }

    public void updateScale(float newScale) {
        if (newScale != this.scale) {
            this.scale = newScale;
            this.refreshDimensions();
        }
    }

    public static void assignIds(MobPart[] parts, int parentId) {
        for (int i = 0; i < parts.length; i++) {
            parts[i].setId(parentId + 1 + i);
        }
    }

    public static void registerAll(MultipartLevel level, MobPart[] parts) {
        for (MobPart part : parts) {
            level.naturalist$addMobPart(part);
        }
    }

    public static void unregisterAll(MultipartLevel level, MobPart[] parts) {
        for (MobPart part : parts) {
            level.naturalist$removeMobPart(part);
        }
    }

    public static void pushEntities(Mob parent, MobPart[] parts) {
        for (MobPart part : parts) {
            for (Entity entity : parent.level().getEntities(part, part.getBoundingBox(),
                    e -> !e.is(parent) && !(e instanceof MobPart) && e.isPushable())) {
                part.push(entity);
            }
        }
    }

    public static boolean resolveBodyCollisions(Mob parent, MobPart[] parts) {
        Vec3 push = Vec3.ZERO;
        Vec3 center = parent.position().add(0.0D, parent.getBbHeight() * 0.5D, 0.0D);
        for (MobPart part : parts) {
            if (!parent.level().getBlockCollisions(part, part.getBoundingBox()).iterator().hasNext()) {
                continue;
            }
            Vec3 away = center.subtract(part.position().add(0.0D, part.getBbHeight() * 0.5D, 0.0D));
            if (away.lengthSqr() < 1.0E-4D) {
                continue;
            }
            push = push.add(away.normalize().scale(0.04D));
        }
        if (push.lengthSqr() == 0.0D) {
            return false;
        }
        if (push.length() > 0.12D) {
            push = push.normalize().scale(0.12D);
        }
        parent.move(MoverType.SELF, push);
        parent.setDeltaMovement(parent.getDeltaMovement().add(push.scale(0.2D)));
        return true;
    }

    @Override
    public @NotNull EntityDimensions getDimensions(@NotNull Pose pose) {
        return this.baseSize.scale(this.scale);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
    }

    @Override
    protected void readAdditionalSaveData(@NotNull ValueInput input) {
    }

    @Override
    protected void addAdditionalSaveData(@NotNull ValueOutput output) {
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    @Override
    public boolean is(@NotNull Entity entity) {
        return this == entity || this.parent == entity;
    }

    @Override
    public boolean hurtServer(@NotNull ServerLevel level, @NotNull DamageSource source, float amount) {
        return !this.isInvulnerableToBase(source) && this.parent.hurtServer(level, source, amount);
    }

    @Override
    public boolean hurtClient(@NotNull DamageSource source) {
        return this.parent.hurtClient(source);
    }

    @Override
    public @NotNull InteractionResult interact(@NotNull Player player, @NotNull InteractionHand hand, @NotNull Vec3 location) {
        return this.parent.interact(player, hand, location);
    }

    @Override
    public @Nullable ItemStack getPickResult() {
        return this.parent.getPickResult();
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket(@NotNull ServerEntity serverEntity) {
        throw new UnsupportedOperationException();
    }
}
