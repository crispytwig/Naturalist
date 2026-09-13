package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.hedgehog.HedgehogModel;
import com.crispytwig.naturalist.client.renderer.entity.layers.DyeLayer;
import com.crispytwig.naturalist.client.renderer.entity.layers.HedgehogGlintLayer;
import com.crispytwig.naturalist.world.entity.animal.hedgehog.Hedgehog;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

@SuppressWarnings("unused")
public class HedgehogRenderer extends NaturalistMobRenderer<Hedgehog> {
    public HedgehogRenderer(EntityRendererProvider.Context context) {
        super(context, new HedgehogModel(context.bakeLayer(HedgehogModel.LAYER_LOCATION)), 0.25F, 0.5F, 0.12F);
        this.addLayer(new DyeLayer<>(this, "hedgehog"));
        this.addLayer(new HedgehogGlintLayer(this));
    }
}
