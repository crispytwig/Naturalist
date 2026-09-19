package com.crispytwig.naturalist.client.renderer.entity.layers;

import com.crispytwig.naturalist.client.model.SeatedModel;
import com.crispytwig.naturalist.client.renderer.entity.state.NaturalistRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

public class SeatedRiderLayer<T extends Mob, M extends EntityModel<? super NaturalistRenderState<T>>> extends RenderLayer<NaturalistRenderState<T>, M> {
    public SeatedRiderLayer(RenderLayerParent<NaturalistRenderState<T>, M> parent) {
        super(parent);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, NaturalistRenderState<T> state, float yRot, float xRot) {
        T entity = state.entity;
        if (!(entity.getFirstPassenger() instanceof Player player) || !(this.getParentModel() instanceof SeatedModel seatedModel)) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        if (player == minecraft.player && minecraft.options.getCameraType().isFirstPerson()) {
            return;
        }
        EntityRenderDispatcher dispatcher = minecraft.getEntityRenderDispatcher();
        EntityRenderState riderState = dispatcher.extractEntity(player, state.partialTick);
        riderState.lightCoords = lightCoords;
        EntityRenderer<?, ? super EntityRenderState> renderer = dispatcher.getRenderer(riderState);
        CameraRenderState camera = minecraft.gameRenderer.getGameRenderState().levelRenderState.cameraRenderState;

        poseStack.pushPose();
        seatedModel.translateToSeat(poseStack);
        poseStack.mulPose(Axis.ZP.rotationDegrees(-seatedModel.seatZRot() * 0.1F * Mth.RAD_TO_DEG));
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.rotLerp(state.partialTick, entity.yBodyRotO, entity.yBodyRot) - 180.0F));
        poseStack.translate(0.0F, -seatedModel.seatHeight(), 0.0F);
        renderer.submit(riderState, poseStack, submitNodeCollector, camera);
        poseStack.popPose();
    }
}
