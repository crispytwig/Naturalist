package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.lion.LionBabyModel;
import com.crispytwig.naturalist.client.model.animal.lion.LionModel;
import com.crispytwig.naturalist.world.entity.animal.lion.Lion;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class LionRenderer extends NaturalistMobRenderer<Lion> {
    public LionRenderer(EntityRendererProvider.Context context) {
        super(context, new LionModel(context.bakeLayer(LionModel.LAYER_LOCATION)), new LionBabyModel(context.bakeLayer(LionBabyModel.LAYER_LOCATION)), 1.1F);
    }
}
