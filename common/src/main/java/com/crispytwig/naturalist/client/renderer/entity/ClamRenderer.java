package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.clam.ClamModel;
import com.crispytwig.naturalist.client.renderer.entity.layers.ClamItemLayer;
import com.crispytwig.naturalist.world.entity.animal.clam.Clam;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import com.crispytwig.naturalist.client.renderer.entity.state.NaturalistRenderState;

@SuppressWarnings("unused")
public class ClamRenderer extends NaturalistMobRenderer<Clam> {
    public ClamRenderer(EntityRendererProvider.Context context) {
        super(context, new ClamModel(context.bakeLayer(ClamModel.LAYER_LOCATION)), 0.0F);
        this.addLayer(new ClamItemLayer(this));
    }

    @Override
    public void extractRenderState(Clam entity, NaturalistRenderState<Clam> state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        this.itemModelResolver.updateForLiving(state.heldItem, entity.getMainHandItem(), ItemDisplayContext.FIXED, entity);
    }
}
