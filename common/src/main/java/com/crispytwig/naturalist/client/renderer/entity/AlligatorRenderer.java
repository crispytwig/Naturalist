package com.crispytwig.naturalist.client.renderer.entity;

import net.minecraft.client.renderer.entity.layers.LivingEntityEmissiveLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.model.animal.alligator.AlligatorBabyModel;
import com.crispytwig.naturalist.client.model.animal.alligator.AlligatorModel;
import com.crispytwig.naturalist.world.entity.animal.alligator.Alligator;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

@SuppressWarnings("unused")
public class AlligatorRenderer extends NaturalistMobRenderer<Alligator> {
    private static final Identifier GLOWMASK = Naturalist.location("textures/entity/alligator/alligator_glowmask.png");
    private static final Identifier BABY_GLOWMASK = Naturalist.location("textures/entity/alligator/alligator_baby_glowmask.png");

    public AlligatorRenderer(EntityRendererProvider.Context context) {
        super(context, new AlligatorModel(context.bakeLayer(AlligatorModel.LAYER_LOCATION)), new AlligatorBabyModel(context.bakeLayer(AlligatorBabyModel.LAYER_LOCATION)), 1.0F);
        this.addLayer(new LivingEntityEmissiveLayer<>(this, state -> GLOWMASK, (state, ageInTicks) -> state.isBaby ? 0.0F : 1.0F,
                new AlligatorModel(context.bakeLayer(AlligatorModel.LAYER_LOCATION)), RenderTypes::entityTranslucentEmissive, false));
        this.addLayer(new LivingEntityEmissiveLayer<>(this, state -> BABY_GLOWMASK, (state, ageInTicks) -> state.isBaby ? 1.0F : 0.0F,
                new AlligatorBabyModel(context.bakeLayer(AlligatorBabyModel.LAYER_LOCATION)), RenderTypes::entityTranslucentEmissive, false));
    }
}
