package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.crab.CrabBabyModel;
import com.crispytwig.naturalist.client.model.animal.crab.CrabModel;
import com.crispytwig.naturalist.client.renderer.entity.layers.CrabHeldItemLayer;
import com.crispytwig.naturalist.world.entity.animal.crab.Crab;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

@SuppressWarnings("unused")
public class CrabRenderer extends NaturalistMobRenderer<Crab> {
    public CrabRenderer(EntityRendererProvider.Context context) {
        super(context, new CrabModel(context.bakeLayer(CrabModel.LAYER_LOCATION)), new CrabBabyModel(context.bakeLayer(CrabBabyModel.LAYER_LOCATION)), 0.3F, 0.3F);
        this.addLayer(new CrabHeldItemLayer(this, context.getItemInHandRenderer()));
    }
}
