package com.crispytwig.naturalist.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.VertexConsumer;
import org.jetbrains.annotations.NotNull;

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
    public @NotNull VertexConsumer vertex(double x, double y, double z) {
        this.delegate.vertex(x, y, z);
        return this;
    }

    @Override
    public @NotNull VertexConsumer color(int red, int green, int blue, int alpha) {
        this.delegate.color(red, green, blue, alpha);
        return this;
    }

    @Override
    public @NotNull VertexConsumer uv(float u, float v) {
        this.delegate.uv(u, v * this.vScale + this.vOffset);
        return this;
    }

    @Override
    public @NotNull VertexConsumer overlayCoords(int u, int v) {
        this.delegate.overlayCoords(u, v);
        return this;
    }

    @Override
    public @NotNull VertexConsumer uv2(int u, int v) {
        this.delegate.uv2(u, v);
        return this;
    }

    @Override
    public @NotNull VertexConsumer normal(float x, float y, float z) {
        this.delegate.normal(x, y, z);
        return this;
    }

    @Override
    public void endVertex() {
        this.delegate.endVertex();
    }

    @Override
    public void defaultColor(int red, int green, int blue, int alpha) {
        this.delegate.defaultColor(red, green, blue, alpha);
    }

    @Override
    public void unsetDefaultColor() {
        this.delegate.unsetDefaultColor();
    }
}
