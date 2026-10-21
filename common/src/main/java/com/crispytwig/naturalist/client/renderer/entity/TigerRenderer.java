package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.tiger.TigerBabyModel;
import com.crispytwig.naturalist.client.model.animal.tiger.TigerModel;
import com.crispytwig.naturalist.world.entity.animal.tiger.Tiger;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

@SuppressWarnings("unused")
public class TigerRenderer extends NaturalistMobRenderer<Tiger> {
    public TigerRenderer(EntityRendererProvider.Context context) {
        super(context, new TigerModel(context.bakeLayer(TigerModel.LAYER_LOCATION)), new TigerBabyModel(context.bakeLayer(TigerBabyModel.LAYER_LOCATION)), 1.1F);
    }
}
