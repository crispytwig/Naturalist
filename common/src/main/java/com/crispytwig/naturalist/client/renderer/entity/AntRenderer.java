package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.ant.AntModel;
import com.crispytwig.naturalist.world.entity.animal.ant.Ant;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

@SuppressWarnings("unused")
public class AntRenderer extends NaturalistMobRenderer<Ant> {
    public AntRenderer(EntityRendererProvider.Context context) {
        super(context, new AntModel(context.bakeLayer(AntModel.LAYER_LOCATION)), 0.25F);
    }
}
