package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.meerkat.MeerkatBabyModel;
import com.crispytwig.naturalist.client.model.meerkat.MeerkatModel;
import com.crispytwig.naturalist.common.entity.meerkat.Meerkat;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

@SuppressWarnings("unused")
public class MeerkatRenderer extends NaturalistMobRenderer<Meerkat> {
    public MeerkatRenderer(EntityRendererProvider.Context context) {
        super(context, new MeerkatModel(context.bakeLayer(MeerkatModel.LAYER_LOCATION)), new MeerkatBabyModel(context.bakeLayer(MeerkatBabyModel.LAYER_LOCATION)), 0.3F, 0.18F);
    }
}
