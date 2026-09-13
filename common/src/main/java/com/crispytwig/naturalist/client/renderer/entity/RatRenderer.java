package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.rat.RatModel;
import com.crispytwig.naturalist.world.entity.animal.rat.Rat;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

@SuppressWarnings("unused")
public class RatRenderer extends NaturalistMobRenderer<Rat> {
    public RatRenderer(EntityRendererProvider.Context context) {
        super(context, new RatModel(context.bakeLayer(RatModel.LAYER_LOCATION)), 0.3F, 0.75F, 0.2F);
    }
}
