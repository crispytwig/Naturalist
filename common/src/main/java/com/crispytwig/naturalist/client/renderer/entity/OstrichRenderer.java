package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.ostrich.OstrichBabyModel;
import com.crispytwig.naturalist.client.model.animal.ostrich.OstrichModel;
import com.crispytwig.naturalist.client.renderer.entity.layers.OstrichDyeLayer;
import com.crispytwig.naturalist.client.renderer.entity.layers.SeatedRiderLayer;
import com.crispytwig.naturalist.world.entity.animal.ostrich.Ostrich;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class OstrichRenderer extends NaturalistMobRenderer<Ostrich> {
    public OstrichRenderer(EntityRendererProvider.Context context) {
        super(context, new OstrichModel(context.bakeLayer(OstrichModel.LAYER_LOCATION)), new OstrichBabyModel(context.bakeLayer(OstrichBabyModel.LAYER_LOCATION)), 0.7F);
        this.addLayer(new SeatedRiderLayer<>(this));
        this.addLayer(new OstrichDyeLayer(this));
    }
}
