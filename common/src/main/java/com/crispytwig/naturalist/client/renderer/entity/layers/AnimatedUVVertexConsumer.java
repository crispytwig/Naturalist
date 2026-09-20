package com.crispytwig.naturalist.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.VertexConsumer;
import org.jspecify.annotations.NonNull;

public class AnimatedUVVertexConsumer implements VertexConsumer {
    private final VertexConsumer delegate;
    private final float vScale;
    private final float vOffset;

    public AnimatedUVVertexConsumer(VertexConsumer delegate, int totalFrames, int currentFrame) {
        this.delegate = delegate;
        this.vScale = 1.0f / totalFrames;
        this.vOffset = (float) currentFrame / totalFrames;
    }

    @Override
    public @NonNull VertexConsumer addVertex(float x, float y, float z) {
        delegate.addVertex(x, y, z);
        return this;
    }

    @Override
    public @NonNull VertexConsumer setColor(int red, int green, int blue, int alpha) {
        delegate.setColor(red, green, blue, alpha);
        return this;
    }

    @Override
    public @NonNull VertexConsumer setColor(int color) {
        delegate.setColor(color);
        return this;
    }

    @Override
    public @NonNull VertexConsumer setUv(float u, float v) {
        delegate.setUv(u, v * vScale + vOffset);
        return this;
    }

    @Override
    public @NonNull VertexConsumer setUv3(float u, float v) {
        delegate.setUv3(u, v);
        return this;
    }

    @Override
    public @NonNull VertexConsumer setUv1(int u, int v) {
        delegate.setUv1(u, v);
        return this;
    }

    @Override
    public @NonNull VertexConsumer setUv2(int u, int v) {
        delegate.setUv2(u, v);
        return this;
    }

    @Override
    public @NonNull VertexConsumer setNormal(float x, float y, float z) {
        delegate.setNormal(x, y, z);
        return this;
    }

    @Override
    public @NonNull VertexConsumer setLineWidth(float width) {
        delegate.setLineWidth(width);
        return this;
    }
}
