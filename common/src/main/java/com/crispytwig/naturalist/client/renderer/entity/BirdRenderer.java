package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.bird.BirdBabyModel;
import com.crispytwig.naturalist.client.model.animal.bird.BirdModel;
import com.crispytwig.naturalist.client.renderer.entity.layers.DyeLayer;
import com.crispytwig.naturalist.world.entity.animal.bird.Bird;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

@SuppressWarnings("unused")
public class BirdRenderer extends NaturalistMobRenderer<Bird> {
    public BirdRenderer(EntityRendererProvider.Context context) {
        super(context, new BirdModel(context.bakeLayer(BirdModel.LAYER_LOCATION)), new BirdBabyModel(context.bakeLayer(BirdBabyModel.LAYER_LOCATION)), 0.3F);
        this.addLayer(new DyeLayer<>(this, "bird"));
    }
}
