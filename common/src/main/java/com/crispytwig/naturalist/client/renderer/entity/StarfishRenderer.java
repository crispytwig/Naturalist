package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.starfish.StarfishModel;
import com.crispytwig.naturalist.common.entity.starfish.Starfish;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

@SuppressWarnings("unused")
public class StarfishRenderer extends NaturalistMobRenderer<Starfish> {
    public StarfishRenderer(EntityRendererProvider.Context context) {
        super(context, new StarfishModel(context.bakeLayer(StarfishModel.LAYER_LOCATION)), 0.0F);
    }
}
