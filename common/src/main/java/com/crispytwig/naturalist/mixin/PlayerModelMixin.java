package com.crispytwig.naturalist.mixin;

import com.crispytwig.naturalist.world.entity.IKMount;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerModel.class)
public abstract class PlayerModelMixin<T extends LivingEntity> extends HumanoidModel<T> {
    @Unique
    private static final float naturalist$waistY = 12.0F;
    @Unique
    private static final float naturalist$pitchSign = -1.0F;
    @Unique
    private static final float naturalist$rollSign = -1.0F;

    @Shadow @Final public ModelPart jacket;
    @Shadow @Final public ModelPart leftSleeve;
    @Shadow @Final public ModelPart rightSleeve;

    @Unique
    private boolean naturalist$leanApplied;

    public PlayerModelMixin() {
        super(null);
    }

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("HEAD"))
    private void naturalist$resetLean(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        if (naturalist$leanApplied || naturalist$leanMount(entity) != null) {
            this.head.resetPose();
            this.body.resetPose();
            this.leftArm.resetPose();
            this.rightArm.resetPose();
            this.naturalist$leanApplied = false;
        }
    }

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("RETURN"))
    private void naturalist$counterLean(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        IKMount mount = naturalist$leanMount(entity);
        if (mount == null) {
            return;
        }
        float pitch = mount.getRenderPitch() * naturalist$pitchSign;
        float roll = mount.getRenderRoll() * naturalist$rollSign;
        if (pitch == 0.0F && roll == 0.0F) {
            return;
        }
        float cosP = (float) Math.cos(pitch), sinP = (float) Math.sin(pitch);
        float cosR = (float) Math.cos(roll), sinR = (float) Math.sin(roll);
        naturalist$rotateWaist(this.head, pitch, roll, cosP, sinP, cosR, sinR);
        naturalist$rotateWaist(this.body, pitch, roll, cosP, sinP, cosR, sinR);
        naturalist$rotateWaist(this.leftArm, pitch, roll, cosP, sinP, cosR, sinR);
        naturalist$rotateWaist(this.rightArm, pitch, roll, cosP, sinP, cosR, sinR);
        this.hat.copyFrom(this.head);
        this.jacket.copyFrom(this.body);
        this.leftSleeve.copyFrom(this.leftArm);
        this.rightSleeve.copyFrom(this.rightArm);
        this.naturalist$leanApplied = true;
    }

    @Unique
    private static IKMount naturalist$leanMount(LivingEntity entity) {
        if (entity instanceof Player && entity.getVehicle() instanceof IKMount mount) {
            return mount;
        }
        return null;
    }

    @Unique
    private static void naturalist$rotateWaist(ModelPart part, float pitch, float roll, float cosP, float sinP, float cosR, float sinR) {
        float dx = part.x;
        float dy = part.y - naturalist$waistY;
        float dz = part.z;

        float y1 = dy * cosP - dz * sinP;

        part.x = dx * cosR - y1 * sinR;
        part.y = naturalist$waistY + dx * sinR + y1 * cosR;
        part.z = dy * sinP + dz * cosP;
        part.xRot += pitch;
        part.zRot += roll;
    }
}
