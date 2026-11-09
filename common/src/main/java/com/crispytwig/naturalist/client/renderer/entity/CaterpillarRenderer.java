package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.butterfly.CaterpillarModel;
import com.crispytwig.naturalist.common.entity.butterfly.Caterpillar;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

@SuppressWarnings("unused")
public class CaterpillarRenderer extends NaturalistMobRenderer<Caterpillar> {
    public CaterpillarRenderer(EntityRendererProvider.Context context) {
        super(context, new CaterpillarModel(context.bakeLayer(CaterpillarModel.LAYER_LOCATION)), 0.3F);
    }
}
