package com.crispytwig.naturalist.client.model;

import com.crispytwig.naturalist.world.entity.TerrainLegSolver;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public abstract class IKEntityModel<E extends LivingEntity> extends NaturalistEntityModel<E> {
    private ModelPart[] legPartsCache;

    protected IKEntityModel(ModelPart root) {
        super(root);
    }

    protected abstract TerrainLegSolver getLegSolver(E entity);

    protected abstract ModelPart bodyPart();

    @Nullable
    protected abstract ModelPart headPart();

    protected abstract ModelPart[] legParts();

    protected float legStrength() {
        return 1.0F;
    }

    protected float pitchStrength() {
        return 1.0F;
    }

    protected float rollStrength() {
        return 0.5F;
    }

    protected float headCounterStrength() {
        return 1.0F;
    }

    protected void articulateLegs(E entity, float partialTick) {
        TerrainLegSolver solver = getLegSolver(entity);
        float hBackLeft = solver.backLeft.getHeight(partialTick);
        float hBackRight = solver.backRight.getHeight(partialTick);
        float hFrontLeft = solver.frontLeft.getHeight(partialTick);
        float hFrontRight = solver.frontRight.getHeight(partialTick);

        float backAvg = (hBackLeft + hBackRight) * 0.5F;
        float frontAvg = (hFrontLeft + hFrontRight) * 0.5F;
        float leftAvg = (hBackLeft + hFrontLeft) * 0.5F;
        float rightAvg = (hBackRight + hFrontRight) * 0.5F;
        float pitch = (float) Math.atan2(frontAvg - backAvg, 2F * solver.frontLeft.forward) * pitchStrength();
        float roll = (float) Math.atan2(leftAvg - rightAvg, 2F * solver.frontLeft.side) * rollStrength();

        solver.renderPitch = pitch;
        solver.renderRoll = roll;

        ModelPart body = this.bodyPart();
        body.y += (backAvg + frontAvg) * 0.5F * 16F * legStrength();
        body.xRot += pitch;
        body.zRot += roll;

        ModelPart head = this.headPart();
        if (head != null) {
            head.xRot -= pitch * headCounterStrength();
            head.zRot -= roll * headCounterStrength();
        }

        if (this.legPartsCache == null) {
            this.legPartsCache = this.legParts();
        }
        ModelPart[] legs = this.legPartsCache;
        offsetFoot(legs[0], hBackLeft);
        offsetFoot(legs[1], hBackRight);
        offsetFoot(legs[2], hFrontLeft);
        offsetFoot(legs[3], hFrontRight);
    }

    private void offsetFoot(ModelPart part, float height) {
        part.y += height * 16F * legStrength();
    }
}
