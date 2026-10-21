package com.crispytwig.naturalist.world.level.block.entity;

import com.crispytwig.naturalist.registry.NaturalistBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SnailShellBlockEntity extends BlockEntity {
    private ItemStack flower = ItemStack.EMPTY;

    public SnailShellBlockEntity(BlockPos pos, BlockState state) {
        super(NaturalistBlockEntities.SNAIL_SHELL.get(), pos, state);
    }

    public ItemStack getFlower() {
        return this.flower;
    }

    public void setFlower(ItemStack stack) {
        this.flower = stack;
        this.setChanged();
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
        }
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        if (!this.flower.isEmpty()) {
            tag.put("Flower", this.flower.save(new CompoundTag()));
        }
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        this.flower = tag.contains("Flower", Tag.TAG_COMPOUND)
                ? ItemStack.of(tag.getCompound("Flower"))
                : ItemStack.EMPTY;
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        this.saveAdditional(tag);
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
