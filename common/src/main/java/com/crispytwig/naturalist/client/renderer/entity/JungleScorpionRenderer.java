package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.scorpion.JungleScorpionModel;
import com.crispytwig.naturalist.world.entity.animal.scorpion.JungleScorpion;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

@SuppressWarnings("unused")
public class JungleScorpionRenderer extends NaturalistMobRenderer<JungleScorpion> {
    public JungleScorpionRenderer(EntityRendererProvider.Context context) {
        super(context, new JungleScorpionModel(context.bakeLayer(JungleScorpionModel.LAYER_LOCATION)), 0.7F);
    }
}
