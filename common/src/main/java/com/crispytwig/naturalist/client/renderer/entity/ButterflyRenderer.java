package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.butterfly.ButterflyModel;
import com.crispytwig.naturalist.world.entity.animal.butterfly.Butterfly;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

@SuppressWarnings("unused")
public class ButterflyRenderer extends NaturalistMobRenderer<Butterfly> {
    public ButterflyRenderer(EntityRendererProvider.Context context) {
        super(context, new ButterflyModel(context.bakeLayer(ButterflyModel.LAYER_LOCATION)), 0.4F);
    }
}
