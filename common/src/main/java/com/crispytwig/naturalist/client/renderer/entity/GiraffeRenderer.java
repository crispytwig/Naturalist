package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.giraffe.GiraffeBabyModel;
import com.crispytwig.naturalist.client.model.giraffe.GiraffeModel;
import com.crispytwig.naturalist.client.renderer.entity.layers.SeatedRiderLayer;
import com.crispytwig.naturalist.common.entity.giraffe.Giraffe;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

@SuppressWarnings("unused")
public class GiraffeRenderer extends NaturalistMobRenderer<Giraffe> {
    public GiraffeRenderer(EntityRendererProvider.Context context) {
        super(context, new GiraffeModel(context.bakeLayer(GiraffeModel.LAYER_LOCATION)), new GiraffeBabyModel(context.bakeLayer(GiraffeBabyModel.LAYER_LOCATION)), 1.1F);
        this.addLayer(new SeatedRiderLayer<>(this));
    }
}
