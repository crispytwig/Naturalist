package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.giantisopod.GiantIsopodModel;
import com.crispytwig.naturalist.common.entity.giantisopod.GiantIsopod;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

@SuppressWarnings("unused")
public class GiantIsopodRenderer extends NaturalistMobRenderer<GiantIsopod> {
    public GiantIsopodRenderer(EntityRendererProvider.Context context) {
        super(context, new GiantIsopodModel(context.bakeLayer(GiantIsopodModel.LAYER_LOCATION)), 0.0F, 0.5F, 0.0F);
    }
}
