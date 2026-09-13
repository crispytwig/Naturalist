package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.lizard.LizardModel;
import com.crispytwig.naturalist.client.renderer.entity.layers.DyeLayer;
import com.crispytwig.naturalist.world.entity.animal.lizard.Lizard;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

@SuppressWarnings("unused")
public class LizardRenderer extends NaturalistMobRenderer<Lizard> {
    public LizardRenderer(EntityRendererProvider.Context context) {
        super(context, new LizardModel(context.bakeLayer(LizardModel.LAYER_LOCATION)), 0.4F);
        this.addLayer(new DyeLayer<>(this, "lizard"));
    }
}
