package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.client.model.animal.snail.SnailModel;
import com.crispytwig.naturalist.world.entity.animal.snail.Snail;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import com.crispytwig.naturalist.client.renderer.entity.state.NaturalistRenderState;

@SuppressWarnings("unused")
public class SnailRenderer extends NaturalistMobRenderer<Snail> {
    private static final Identifier[] TEXTURES_BY_COLOR = buildTextures();

    private final Matrix4f orientation = new Matrix4f();

    public SnailRenderer(EntityRendererProvider.Context context) {
        super(context, new SnailModel(context.bakeLayer(SnailModel.LAYER_LOCATION)), 0.2F);
    }

    private static Identifier[] buildTextures() {
        Identifier[] textures = new Identifier[Snail.Color.BY_ID.length];
        for (Snail.Color color : Snail.Color.BY_ID) {
            textures[color.getId()] = Naturalist.location("textures/entity/snail/" + color.getName() + ".png");
        }
        return textures;
    }

    @Override
    protected Identifier getTexture(Snail entity) {
        if (entity.hasNonDefaultVariant()) {
            return entity.getVariantTexture();
        }
        return TEXTURES_BY_COLOR[entity.getSnailColor().getId()];
    }

    @Override
    protected void setupRotations(NaturalistRenderState<Snail> state, PoseStack poseStack, float bodyRot, float entityScale) {
        Snail entity = state.entity;
        float partialTick = state.partialTick;
        Vec3 normal = entity.getClimbing().getRenderNormal(partialTick);
        Vec3 back = entity.getClimbing().getRenderForwardFlattened(partialTick, normal).scale(-1.0D);
        Vec3 right = normal.cross(back);
        Vec3 anchor = entity.getClimbing().getRenderAnchor(partialTick);

        poseStack.translate(anchor.x, anchor.y, anchor.z);
        poseStack.mulPose(this.orientation.set(
                (float) right.x, (float) right.y, (float) right.z, 0.0F,
                (float) normal.x, (float) normal.y, (float) normal.z, 0.0F,
                (float) back.x, (float) back.y, (float) back.z, 0.0F,
                0.0F, 0.0F, 0.0F, 1.0F));

        if (state.isBaby) {
            poseStack.scale(0.5F, 0.5F, 0.5F);
        }

        super.setupRotations(state, poseStack, 180.0F, entityScale);
    }
}
