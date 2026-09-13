package com.crispytwig.naturalist.client.renderer.entity;

import com.crispytwig.naturalist.world.entity.animal.ant.CarriedFoodEntity;
import com.crispytwig.naturalist.world.entity.animal.ant.Ant;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.state.ItemEntityRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.Vec3;

@SuppressWarnings("unused")
public class CarriedFoodRenderer extends ItemEntityRenderer {
    public CarriedFoodRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.0F;
    }

    @Override
    public ItemEntityRenderState createRenderState() {
        return new CarriedFoodRenderState();
    }

    @Override
    public void extractRenderState(ItemEntity entity, ItemEntityRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        CarriedFoodRenderState foodState = (CarriedFoodRenderState) state;
        foodState.antOffset = Vec3.ZERO;
        if (entity instanceof CarriedFoodEntity food) {
            Ant ant = food.resolveAnt();
            if (ant != null) {
                double dx = Mth.lerp(partialTick, ant.xOld, ant.getX()) - Mth.lerp(partialTick, entity.xOld, entity.getX());
                double dy = Mth.lerp(partialTick, ant.yOld, ant.getY()) + ant.getBbHeight() + CarriedFoodEntity.BACK_GAP - Mth.lerp(partialTick, entity.yOld, entity.getY());
                double dz = Mth.lerp(partialTick, ant.zOld, ant.getZ()) - Mth.lerp(partialTick, entity.zOld, entity.getZ());
                foodState.antOffset = new Vec3(dx, dy, dz);
            }
        }
    }

    @Override
    public Vec3 getRenderOffset(ItemEntityRenderState state) {
        Vec3 base = super.getRenderOffset(state);
        return base.add(((CarriedFoodRenderState) state).antOffset);
    }

    public static class CarriedFoodRenderState extends ItemEntityRenderState {
        public Vec3 antOffset = Vec3.ZERO;
    }
}
