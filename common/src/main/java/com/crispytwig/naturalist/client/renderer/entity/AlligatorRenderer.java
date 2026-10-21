package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.model.animal.alligator.AlligatorBabyModel;
import com.crispytwig.naturalist.client.model.animal.alligator.AlligatorModel;
import com.crispytwig.naturalist.client.renderer.entity.layers.GlowLayer;
import com.crispytwig.naturalist.world.entity.animal.alligator.Alligator;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("unused")
public class AlligatorRenderer extends NaturalistMobRenderer<Alligator> {
    private static final ResourceLocation GLOWMASK = Naturalist.location("textures/entity/alligator/alligator_glowmask.png");
    private static final ResourceLocation BABY_GLOWMASK = Naturalist.location("textures/entity/alligator/alligator_baby_glowmask.png");

    public AlligatorRenderer(EntityRendererProvider.Context context) {
        super(context, new AlligatorModel(context.bakeLayer(AlligatorModel.LAYER_LOCATION)), new AlligatorBabyModel(context.bakeLayer(AlligatorBabyModel.LAYER_LOCATION)), 1.0F);
        this.addLayer(new GlowLayer<>(this, entity -> entity.isBaby() ? BABY_GLOWMASK : GLOWMASK));
    }
}
