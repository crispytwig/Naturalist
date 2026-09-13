package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.komododragon.KomodoDragonModel;
import com.crispytwig.naturalist.world.entity.animal.komododragon.KomodoDragon;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

@SuppressWarnings("unused")
public class KomodoDragonRenderer extends NaturalistMobRenderer<KomodoDragon> {
    public KomodoDragonRenderer(EntityRendererProvider.Context context) {
        super(context, new KomodoDragonModel(context.bakeLayer(KomodoDragonModel.LAYER_LOCATION)), 0.65F, 0.45F, 0.3F);
    }
}
