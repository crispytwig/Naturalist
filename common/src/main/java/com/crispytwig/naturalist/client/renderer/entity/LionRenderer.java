package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.lion.LionBabyModel;
import com.crispytwig.naturalist.client.model.lion.LionModel;
import com.crispytwig.naturalist.common.entity.lion.Lion;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

@SuppressWarnings("unused")
public class LionRenderer extends NaturalistMobRenderer<Lion> {
    public LionRenderer(EntityRendererProvider.Context context) {
        super(context, new LionModel(context.bakeLayer(LionModel.LAYER_LOCATION)), new LionBabyModel(context.bakeLayer(LionBabyModel.LAYER_LOCATION)), 1.1F);
    }
}
