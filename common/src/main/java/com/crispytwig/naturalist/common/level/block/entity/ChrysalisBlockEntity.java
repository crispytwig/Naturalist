package com.crispytwig.naturalist.common.level.block.entity;

import com.crispytwig.naturalist.common.registry.NaturalistBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChrysalisBlockEntity extends BlockEntity {
    private static final String CUSTOM_NAME_TAG = "CustomName";

    @Nullable
    private Component customName;

    public ChrysalisBlockEntity(BlockPos pos, BlockState state) {
        super(NaturalistBlockEntities.CHRYSALIS.get(), pos, state);
    }

    @Nullable
    public Component getCustomName() {
        return this.customName;
    }

    public void setCustomName(@Nullable Component customName) {
        this.customName = customName;
        this.setChanged();
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output) {
        super.saveAdditional(output);
        output.storeNullable(CUSTOM_NAME_TAG, ComponentSerialization.CODEC, this.customName);
    }

    @Override
    protected void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);
        this.customName = parseCustomNameSafe(input, CUSTOM_NAME_TAG);
    }

    @Override
    protected void applyImplicitComponents(@NotNull DataComponentGetter components) {
        super.applyImplicitComponents(components);
        this.customName = components.get(DataComponents.CUSTOM_NAME);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.@NotNull Builder components) {
        super.collectImplicitComponents(components);
        components.set(DataComponents.CUSTOM_NAME, this.customName);
    }

    @Override
    public void removeComponentsFromTag(@NotNull ValueOutput output) {
        output.discard(CUSTOM_NAME_TAG);
    }
}
