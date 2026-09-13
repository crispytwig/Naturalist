package com.crispytwig.naturalist.world.entity.variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.biome.Biome;

import java.util.Optional;

public final class MobVariant {
    public static final Codec<MobVariant> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.fieldOf("texture").forGetter(variant -> variant.texture),
            Identifier.CODEC.optionalFieldOf("baby_texture").forGetter(variant -> variant.babyTexture),
            RegistryCodecs.homogeneousList(Registries.BIOME).optionalFieldOf("biomes").forGetter(MobVariant::biomes),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("weight", 1).forGetter(MobVariant::weight),
            Codec.INT.optionalFieldOf("priority", 0).forGetter(MobVariant::priority),
            ComponentSerialization.CODEC.optionalFieldOf("tooltip").forGetter(MobVariant::tooltip),
            Identifier.CODEC.optionalFieldOf("item_model").forGetter(MobVariant::itemModel)
    ).apply(instance, MobVariant::new));

    private final Identifier texture;
    private final Optional<Identifier> babyTexture;
    private final Optional<HolderSet<Biome>> biomes;
    private final int weight;
    private final int priority;
    private final Optional<Component> tooltip;
    private final Optional<Identifier> itemModel;
    private final Identifier textureFull;
    private final Identifier babyTextureFull;

    public MobVariant(Identifier texture, Optional<Identifier> babyTexture,
                      Optional<HolderSet<Biome>> biomes, int weight, int priority,
                      Optional<Component> tooltip, Optional<Identifier> itemModel) {
        this.texture = texture;
        this.babyTexture = babyTexture;
        this.biomes = biomes;
        this.weight = weight;
        this.priority = priority;
        this.tooltip = tooltip;
        this.itemModel = itemModel;
        this.textureFull = fullTextureId(texture);
        this.babyTextureFull = fullTextureId(babyTexture.orElse(texture));
    }

    public Identifier texture() {
        return this.textureFull;
    }

    public Identifier babyTexture() {
        return this.babyTextureFull;
    }

    public Optional<HolderSet<Biome>> biomes() {
        return this.biomes;
    }

    public int weight() {
        return this.weight;
    }

    public int priority() {
        return this.priority;
    }

    public Optional<Component> tooltip() {
        return this.tooltip;
    }

    public Optional<Identifier> itemModel() {
        return this.itemModel;
    }

    private static Identifier fullTextureId(Identifier texture) {
        return texture.withPath(path -> "textures/" + path + ".png");
    }
}
