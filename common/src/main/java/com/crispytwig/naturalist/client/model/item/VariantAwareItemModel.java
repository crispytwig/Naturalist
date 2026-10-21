package com.crispytwig.naturalist.client.model.item;

import com.crispytwig.naturalist.world.item.NaturalistBucketItem;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.function.Function;

public class VariantAwareItemModel implements BakedModel {
    private final BakedModel parent;
    private final ItemOverrides overrides;

    public VariantAwareItemModel(BakedModel parent, NaturalistBucketItem item, Function<ResourceLocation, @Nullable BakedModel> modelGetter) {
        this.parent = parent;
        this.overrides = new VariantOverrides(parent, item, modelGetter);
    }

    @Override
    public @NonNull ItemOverrides getOverrides() {
        return this.overrides;
    }

    @Override
    public @NonNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, RandomSource random) {
        return this.parent.getQuads(state, direction, random);
    }

    @Override
    public boolean useAmbientOcclusion() {
        return this.parent.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return this.parent.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return this.parent.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return this.parent.isCustomRenderer();
    }

    @Override
    public @NonNull TextureAtlasSprite getParticleIcon() {
        return this.parent.getParticleIcon();
    }

    @Override
    public @NonNull ItemTransforms getTransforms() {
        return this.parent.getTransforms();
    }

    private static class VariantOverrides extends ItemOverrides {
        private static final ModelBaker DUMMY_BAKER = (ModelBaker) Proxy.newProxyInstance(
                ModelBaker.class.getClassLoader(),
                new Class[]{ModelBaker.class},
                (proxy, method, args) -> "getModelTextureGetter".equals(method.getName())
                        ? (Function<Object, Object>) key -> null
                        : null);

        private final BakedModel parent;
        private final NaturalistBucketItem item;
        private final Function<ResourceLocation, @Nullable BakedModel> modelGetter;

        VariantOverrides(BakedModel parent, NaturalistBucketItem item, Function<ResourceLocation, @Nullable BakedModel> modelGetter) {
            super(DUMMY_BAKER, null, List.of());
            this.parent = parent;
            this.item = item;
            this.modelGetter = modelGetter;
        }

        @Nullable
        @Override
        public BakedModel resolve(BakedModel model, ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
            BakedModel variantModel = VariantItemModels.resolveModelId(this.item, stack, level)
                    .map(this.modelGetter)
                    .orElse(null);
            if (variantModel != null) {
                return variantModel;
            }
            return this.parent.getOverrides().resolve(model, stack, level, entity, seed);
        }
    }
}
