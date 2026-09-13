package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.model.animal.mole.MoleModel;
import com.crispytwig.naturalist.world.entity.animal.mole.Mole;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import com.crispytwig.naturalist.client.renderer.entity.state.NaturalistRenderState;

@SuppressWarnings("unused")
public class MoleRenderer extends NaturalistMobRenderer<Mole> {
    public MoleRenderer(EntityRendererProvider.Context context) {
        super(context, new MoleModel(context.bakeLayer(MoleModel.LAYER_LOCATION)), 0.4F, 0.6F, 0.25F);
    }

    @Override
    protected float getShadowRadius(NaturalistRenderState<Mole> state) {
        if (state.entity.isRolledUp()) {
            return 0.0F;
        }
        return super.getShadowRadius(state);
    }
}
