package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.equine.ZebraBabyModel;
import com.crispytwig.naturalist.client.model.animal.equine.ZebraModel;
import com.crispytwig.naturalist.world.entity.animal.equine.Zebra;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

@SuppressWarnings("unused")
public class ZebraRenderer extends NaturalistMobRenderer<Zebra> {
    public ZebraRenderer(EntityRendererProvider.Context context) {
        super(context, new ZebraModel(context.bakeLayer(ZebraModel.LAYER_LOCATION)), new ZebraBabyModel(context.bakeLayer(ZebraBabyModel.LAYER_LOCATION)), 1.1F);
    }
}
