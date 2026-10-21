package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.elephant.MammothBabyModel;
import com.crispytwig.naturalist.client.model.animal.elephant.MammothModel;
import com.crispytwig.naturalist.client.renderer.entity.layers.BannerLayer;
import com.crispytwig.naturalist.client.renderer.entity.layers.SeatedRiderLayer;
import com.crispytwig.naturalist.world.entity.animal.elephant.Elephant;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

@SuppressWarnings("unused")
public class MammothRenderer extends NaturalistMobRenderer<Elephant> {
    private static final float BANNER_X = 16.5F;
    private static final float BANNER_Y = -17.5F;
    private static final float BANNER_Z = -4.0F;
    private static final float BANNER_SCALE = 0.8F;

    public MammothRenderer(EntityRendererProvider.Context context) {
        super(context, new MammothModel(context.bakeLayer(MammothModel.LAYER_LOCATION)), new MammothBabyModel(context.bakeLayer(MammothBabyModel.LAYER_LOCATION)), 1.5F);
        this.addLayer(new SeatedRiderLayer<>(this));
        this.addLayer(new BannerLayer(this, context, BANNER_X, BANNER_Y, BANNER_Z, BANNER_SCALE));
    }
}
