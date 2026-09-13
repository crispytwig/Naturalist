package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.elephant.ElephantBabyModel;
import com.crispytwig.naturalist.client.model.animal.elephant.ElephantModel;
import com.crispytwig.naturalist.client.renderer.entity.layers.BannerLayer;
import com.crispytwig.naturalist.client.renderer.entity.layers.SeatedRiderLayer;
import com.crispytwig.naturalist.world.entity.animal.elephant.Elephant;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

@SuppressWarnings("unused")
public class ElephantRenderer extends NaturalistMobRenderer<Elephant> {
    private static final float BANNER_X = 15.0F;
    private static final float BANNER_Y = -15.5F;
    private static final float BANNER_Z = -6.0F;
    private static final float BANNER_SCALE = 0.7F;

    public ElephantRenderer(EntityRendererProvider.Context context) {
        super(context, new ElephantModel(context.bakeLayer(ElephantModel.LAYER_LOCATION)), new ElephantBabyModel(context.bakeLayer(ElephantBabyModel.LAYER_LOCATION)), 1.5F);
        this.addLayer(new SeatedRiderLayer<>(this));
        this.addLayer(new BannerLayer<>(this, context, BANNER_X, BANNER_Y, BANNER_Z, BANNER_SCALE));
    }
}
