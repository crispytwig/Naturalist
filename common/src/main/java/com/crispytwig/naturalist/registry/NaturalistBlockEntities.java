package com.crispytwig.naturalist.registry;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.world.level.block.entity.AntHillBlockEntity;
import com.crispytwig.naturalist.world.level.block.entity.SnailShellBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import com.crispytwig.naturalist.platform.registry.DeferredHolder;
import com.crispytwig.naturalist.platform.registry.DeferredRegister;

public class NaturalistBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Naturalist.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AntHillBlockEntity>> ANT_HILL = BLOCK_ENTITY_TYPES.register("ant_hill", () -> BlockEntityType.Builder.of(AntHillBlockEntity::new, NaturalistRegistry.ANT_HILL.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SnailShellBlockEntity>> SNAIL_SHELL = BLOCK_ENTITY_TYPES.register("snail_shell", () -> BlockEntityType.Builder.of(SnailShellBlockEntity::new, NaturalistRegistry.SNAIL_SHELL_BLOCK.get()).build(null));
}
