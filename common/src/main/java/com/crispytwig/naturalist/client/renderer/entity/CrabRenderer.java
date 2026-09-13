package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.client.NaturalistPortraitRenderState;
import com.crispytwig.naturalist.client.model.animal.crab.CrabBabyModel;
import com.crispytwig.naturalist.client.model.animal.crab.CrabModel;
import com.crispytwig.naturalist.client.renderer.entity.layers.CrabItemLayer;
import com.crispytwig.naturalist.world.entity.animal.crab.Crab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import com.crispytwig.naturalist.client.renderer.entity.state.NaturalistRenderState;

@SuppressWarnings("unused")
public class CrabRenderer extends NaturalistMobRenderer<Crab> {
    public CrabRenderer(EntityRendererProvider.Context context) {
        super(context, new CrabModel(context.bakeLayer(CrabModel.LAYER_LOCATION)), new CrabBabyModel(context.bakeLayer(CrabBabyModel.LAYER_LOCATION)), 0.3F, 0.3F);
        this.addLayer(new CrabItemLayer(this));
    }

    @Override
    public NaturalistRenderState<Crab> createRenderState() {
        return new CrabRenderState();
    }

    @Override
    public void extractRenderState(Crab entity, NaturalistRenderState<Crab> state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        this.itemModelResolver.updateForLiving(state.heldItem, entity.getMainHandItem(), ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, entity);

        if (state instanceof CrabRenderState crabState) {
            crabState.portrait = NaturalistPortraitRenderState.ACTIVE;
            if (!crabState.portrait) {
                Vec3 toCamera = Minecraft.getInstance().gameRenderer.mainCamera().position().subtract(entity.position());
                double planeLen = Math.sqrt(toCamera.x * toCamera.x + toCamera.z * toCamera.z);
                float forwardYaw = Mth.rotLerp(partialTick, entity.yBodyRotO, entity.yBodyRot) * Mth.DEG_TO_RAD + Mth.HALF_PI;
                float cameraYaw = planeLen < 1.0E-4D ? forwardYaw : (float) Math.atan2(-toCamera.x, toCamera.z);
                crabState.eyeYaw = Mth.wrapDegrees((forwardYaw - cameraYaw) * Mth.RAD_TO_DEG) * Mth.DEG_TO_RAD;
                crabState.eyePitch = (float) Math.atan2(toCamera.y, planeLen);
            }
        }
    }

    public static class CrabRenderState extends NaturalistRenderState<Crab> {
        public boolean portrait;
        public float eyeYaw;
        public float eyePitch;
    }
}
