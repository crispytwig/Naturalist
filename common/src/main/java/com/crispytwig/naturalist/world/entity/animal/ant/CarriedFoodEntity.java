package com.crispytwig.naturalist.world.entity.animal.ant;

import com.crispytwig.naturalist.registry.NaturalistEntityTypes;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class CarriedFoodEntity extends ItemEntity {
    private static final EntityDataAccessor<Integer> DATA_ANT_ID = SynchedEntityData.defineId(CarriedFoodEntity.class, EntityDataSerializers.INT);
    public static final double BACK_GAP = 0.05D;
    private static final int GRACE_TICKS = 60;

    @Nullable
    private UUID antUUID;
    private int unresolvedTicks;
    private int bobAge;

    public CarriedFoodEntity(EntityType<? extends ItemEntity> type, Level level) {
        super(type, level);
    }

    public CarriedFoodEntity(Level level, Ant ant, ItemStack stack) {
        super(NaturalistEntityTypes.CARRIED_FOOD.get(), level);
        this.setItem(stack);
        this.setNoPickUpDelay();
        this.setNoGravity(true);
        this.setAnt(ant);
        this.setPos(ant.getX(), ant.getY() + ant.getBbHeight() + BACK_GAP, ant.getZ());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ANT_ID, -1);
    }

    public void setAnt(Ant ant) {
        this.antUUID = ant.getUUID();
        this.entityData.set(DATA_ANT_ID, ant.getId());
    }

    @Nullable
    public Ant resolveAnt() {
        if (this.level().isClientSide()) {
            int id = this.entityData.get(DATA_ANT_ID);
            return id != -1 && this.level().getEntity(id) instanceof Ant ant ? ant : null;
        }
        if (this.antUUID != null && this.level() instanceof ServerLevel server && server.getEntity(this.antUUID) instanceof Ant ant) {
            this.entityData.set(DATA_ANT_ID, ant.getId());
            return ant;
        }
        return null;
    }

    @Override
    public void tick() {
        this.baseTick();
        this.bobAge++;
        if (!this.level().isClientSide() && this.getItem().isEmpty()) {
            this.discard();
            return;
        }
        Ant ant = this.resolveAnt();
        if (this.level().isClientSide()) {
            if (ant != null) {
                this.follow(ant);
            }
            return;
        }
        if (ant != null && ant.isAlive() && ant.isCarriedFood(this)) {
            this.unresolvedTicks = 0;
            this.follow(ant);
        } else if (ant != null && ant.isAlive()) {
            this.releaseToWorld();
        } else if (++this.unresolvedTicks > GRACE_TICKS) {
            this.releaseToWorld();
        }
    }

    private void follow(Ant ant) {
        this.setDeltaMovement(Vec3.ZERO);
        this.setPos(ant.getX(), ant.getY() + ant.getBbHeight() + BACK_GAP, ant.getZ());
    }

    public void releaseToWorld() {
        if (!this.level().isClientSide()) {
            ItemEntity dropped = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), this.getItem().copy());
            dropped.setDefaultPickUpDelay();
            this.level().addFreshEntity(dropped);
        }
        this.discard();
    }

    @Override
    public void playerTouch(@NotNull Player player) {
        if (this.level().isClientSide()) {
            super.playerTouch(player);
            return;
        }
        int before = this.getItem().getCount();
        super.playerTouch(player);
        if (this.isRemoved() || this.getItem().getCount() < before) {
            Ant ant = this.resolveAnt();
            if (ant != null) {
                ant.onCarriedFoodTaken(player);
            }
        }
    }

    @Override
    public int getAge() {
        return this.bobAge;
    }

    @Override
    public void addAdditionalSaveData(@NotNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.storeNullable("Ant", UUIDUtil.CODEC, this.antUUID);
    }

    @Override
    public void readAdditionalSaveData(@NotNull ValueInput input) {
        super.readAdditionalSaveData(input);
        this.antUUID = input.read("Ant", UUIDUtil.CODEC).orElse(null);
    }
}
