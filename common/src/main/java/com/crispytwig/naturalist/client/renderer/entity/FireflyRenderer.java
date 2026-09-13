package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.firefly.FireflyBabyModel;
import com.crispytwig.naturalist.client.model.animal.firefly.FireflyModel;
import com.crispytwig.naturalist.client.renderer.entity.layers.FireflyGlowLayer;
import com.crispytwig.naturalist.world.entity.animal.firefly.Firefly;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class FireflyRenderer extends NaturalistMobRenderer<Firefly> {
    public FireflyRenderer(EntityRendererProvider.@NotNull Context context) {
        super(context, new FireflyModel(context.bakeLayer(FireflyModel.LAYER_LOCATION)), new FireflyBabyModel(context.bakeLayer(FireflyBabyModel.LAYER_LOCATION)), 0.4F);
        this.addLayer(new FireflyGlowLayer(this));
    }
}
