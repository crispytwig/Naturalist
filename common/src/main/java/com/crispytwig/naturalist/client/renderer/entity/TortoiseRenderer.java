package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.tortoise.TortoiseBabyModel;
import com.crispytwig.naturalist.client.model.animal.tortoise.TortoiseModel;
import com.crispytwig.naturalist.client.renderer.entity.layers.TortoiseMaskLayer;
import com.crispytwig.naturalist.world.entity.animal.tortoise.Tortoise;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class TortoiseRenderer extends NaturalistMobRenderer<Tortoise> {
    public TortoiseRenderer(EntityRendererProvider.Context context) {
        super(context, new TortoiseModel(context.bakeLayer(TortoiseModel.LAYER_LOCATION)), new TortoiseBabyModel(context.bakeLayer(TortoiseBabyModel.LAYER_LOCATION)), 0.8F);
        this.addLayer(new TortoiseMaskLayer<>(this));
    }
}
