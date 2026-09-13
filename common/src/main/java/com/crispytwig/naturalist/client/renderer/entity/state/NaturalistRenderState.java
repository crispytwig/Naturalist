package com.crispytwig.naturalist.client.renderer.entity.state;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import org.jspecify.annotations.Nullable;

public class NaturalistRenderState<E extends Entity> extends LivingEntityRenderState {
    public E entity;
    public float partialTick;
    public @Nullable Identifier texture;
    public final ItemStackRenderState heldItem = new ItemStackRenderState();
}
