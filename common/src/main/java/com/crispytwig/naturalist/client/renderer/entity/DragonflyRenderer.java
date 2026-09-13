package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.dragonfly.DragonflyModel;
import com.crispytwig.naturalist.world.entity.animal.dragonfly.Dragonfly;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

@SuppressWarnings("unused")
public class DragonflyRenderer extends NaturalistMobRenderer<Dragonfly> {
    public DragonflyRenderer(EntityRendererProvider.Context context) {
        super(context, new DragonflyModel(context.bakeLayer(DragonflyModel.LAYER_LOCATION)), 0.4F);
    }
}
