package com.crispytwig.naturalist.world.level.block.entity;

import com.crispytwig.naturalist.registry.NaturalistBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
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
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        if (!this.flower.isEmpty()) {
            tag.put("Flower", this.flower.save(registries));
        }
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        this.flower = tag.contains("Flower", Tag.TAG_COMPOUND)
                ? ItemStack.parse(registries, tag.getCompound("Flower")).orElse(ItemStack.EMPTY)
                : ItemStack.EMPTY;
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        this.saveAdditional(tag, registries);
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
