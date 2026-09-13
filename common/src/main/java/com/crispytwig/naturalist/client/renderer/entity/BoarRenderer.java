package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.boar.BoarBabyModel;
import com.crispytwig.naturalist.client.model.animal.boar.BoarModel;
import com.crispytwig.naturalist.world.entity.animal.boar.Boar;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

@SuppressWarnings("unused")
public class BoarRenderer extends NaturalistMobRenderer<Boar> {
    public BoarRenderer(EntityRendererProvider.Context context) {
        super(context, new BoarModel(context.bakeLayer(BoarModel.LAYER_LOCATION)), new BoarBabyModel(context.bakeLayer(BoarBabyModel.LAYER_LOCATION)), 0.7F);
    }
}
