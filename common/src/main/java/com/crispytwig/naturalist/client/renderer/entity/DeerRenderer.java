package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.deer.DeerBabyModel;
import com.crispytwig.naturalist.client.model.animal.deer.DeerModel;
import com.crispytwig.naturalist.world.entity.animal.deer.Deer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

@SuppressWarnings("unused")
public class DeerRenderer extends NaturalistMobRenderer<Deer> {
    public DeerRenderer(EntityRendererProvider.Context context) {
        super(context, new DeerModel(context.bakeLayer(DeerModel.LAYER_LOCATION)), new DeerBabyModel(context.bakeLayer(DeerBabyModel.LAYER_LOCATION)), 0.8F);
    }
}
