package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.jellyfish.JellyfishModel;
import com.crispytwig.naturalist.world.entity.animal.jellyfish.Jellyfish;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

@SuppressWarnings("unused")
public class JellyfishRenderer extends NaturalistMobRenderer<Jellyfish> {
    public JellyfishRenderer(EntityRendererProvider.Context context) {
        super(context, new JellyfishModel(context.bakeLayer(JellyfishModel.LAYER_LOCATION)), 0.0F);
    }
}
