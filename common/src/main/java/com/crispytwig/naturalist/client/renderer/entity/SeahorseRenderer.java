package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.seahorse.SeahorseBabyModel;
import com.crispytwig.naturalist.client.model.animal.seahorse.SeahorseModel;
import com.crispytwig.naturalist.world.entity.animal.seahorse.Seahorse;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

@SuppressWarnings("unused")
public class SeahorseRenderer extends NaturalistMobRenderer<Seahorse> {
    public SeahorseRenderer(EntityRendererProvider.Context context) {
        super(context, new SeahorseModel(context.bakeLayer(SeahorseModel.LAYER_LOCATION)), new SeahorseBabyModel(context.bakeLayer(SeahorseBabyModel.LAYER_LOCATION)), 0.0F, 0.0F);
    }
}
