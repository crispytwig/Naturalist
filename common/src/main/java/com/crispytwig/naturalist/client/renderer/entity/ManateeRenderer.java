package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.manatee.ManateeBabyModel;
import com.crispytwig.naturalist.client.model.animal.manatee.ManateeModel;
import com.crispytwig.naturalist.world.entity.animal.manatee.Manatee;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

@SuppressWarnings("unused")
public class ManateeRenderer extends NaturalistMobRenderer<Manatee> {
    public ManateeRenderer(EntityRendererProvider.Context context) {
        super(context, new ManateeModel(context.bakeLayer(ManateeModel.LAYER_LOCATION)), new ManateeBabyModel(context.bakeLayer(ManateeBabyModel.LAYER_LOCATION)), 0.0F, 0.0F);
    }
}
