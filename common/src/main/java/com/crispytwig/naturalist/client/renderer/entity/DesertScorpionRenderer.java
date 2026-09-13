package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.scorpion.DesertScorpionModel;
import com.crispytwig.naturalist.world.entity.animal.scorpion.DesertScorpion;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

@SuppressWarnings("unused")
public class DesertScorpionRenderer extends NaturalistMobRenderer<DesertScorpion> {
    public DesertScorpionRenderer(EntityRendererProvider.Context context) {
        super(context, new DesertScorpionModel(context.bakeLayer(DesertScorpionModel.LAYER_LOCATION)), 0.4F);
    }
}
