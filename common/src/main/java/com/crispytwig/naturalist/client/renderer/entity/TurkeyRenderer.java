package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.turkey.TurkeyModel;
import com.crispytwig.naturalist.world.entity.animal.turkey.Turkey;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

@SuppressWarnings("unused")
public class TurkeyRenderer extends NaturalistMobRenderer<Turkey> {
    public TurkeyRenderer(EntityRendererProvider.Context context) {
        super(context, new TurkeyModel(context.bakeLayer(TurkeyModel.LAYER_LOCATION)), 0.3F, 0.5F, 0.15F);
    }
}
