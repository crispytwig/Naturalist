package com.crispytwig.naturalist.world.entity.variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.biome.Biome;

import java.util.List;
import java.util.Optional;

public final class MobVariant {
    public static final Codec<MobVariant> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("texture").forGetter(variant -> variant.texture),
            ResourceLocation.CODEC.optionalFieldOf("baby_texture").forGetter(variant -> variant.babyTexture),
            RegistryCodecs.homogeneousList(Registries.BIOME).optionalFieldOf("biomes").forGetter(MobVariant::biomes),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("weight", 1).forGetter(MobVariant::weight),
            Codec.INT.optionalFieldOf("priority", 0).forGetter(MobVariant::priority),
            ExtraCodecs.COMPONENT.optionalFieldOf("tooltip").forGetter(MobVariant::tooltip),
            ResourceLocation.CODEC.optionalFieldOf("item_model").forGetter(MobVariant::itemModel)
    ).apply(instance, MobVariant::new));

    public static final Codec<MobVariant> NETWORK_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("texture").forGetter(variant -> variant.texture),
            ResourceLocation.CODEC.optionalFieldOf("baby_texture").forGetter(variant -> variant.babyTexture),
            ResourceLocation.CODEC.listOf().optionalFieldOf("biomes").forGetter(MobVariant::biomeIds),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("weight", 1).forGetter(MobVariant::weight),
            Codec.INT.optionalFieldOf("priority", 0).forGetter(MobVariant::priority),
            ExtraCodecs.COMPONENT.optionalFieldOf("tooltip").forGetter(MobVariant::tooltip),
            ResourceLocation.CODEC.optionalFieldOf("item_model").forGetter(MobVariant::itemModel)
    ).apply(instance, MobVariant::fromNetwork));

    private final ResourceLocation texture;
    private final Optional<ResourceLocation> babyTexture;
    private final Optional<HolderSet<Biome>> biomes;
    private final Optional<List<ResourceLocation>> networkBiomeIds;
    private final int weight;
    private final int priority;
    private final Optional<Component> tooltip;
    private final Optional<ResourceLocation> itemModel;
    private final ResourceLocation textureFull;
    private final ResourceLocation babyTextureFull;

    public MobVariant(ResourceLocation texture, Optional<ResourceLocation> babyTexture,
                      Optional<HolderSet<Biome>> biomes, int weight, int priority,
                      Optional<Component> tooltip, Optional<ResourceLocation> itemModel) {
        this(texture, babyTexture, biomes, Optional.empty(), weight, priority, tooltip, itemModel);
    }

    private MobVariant(ResourceLocation texture, Optional<ResourceLocation> babyTexture,
                       Optional<HolderSet<Biome>> biomes, Optional<List<ResourceLocation>> networkBiomeIds,
                       int weight, int priority, Optional<Component> tooltip, Optional<ResourceLocation> itemModel) {
        this.texture = texture;
        this.babyTexture = babyTexture;
        this.biomes = biomes;
        this.networkBiomeIds = networkBiomeIds;
        this.weight = weight;
        this.priority = priority;
        this.tooltip = tooltip;
        this.itemModel = itemModel;
        this.textureFull = fullTextureId(texture);
        this.babyTextureFull = fullTextureId(babyTexture.orElse(texture));
    }

    private static MobVariant fromNetwork(ResourceLocation texture, Optional<ResourceLocation> babyTexture,
                                          Optional<List<ResourceLocation>> biomeIds, int weight, int priority,
                                          Optional<Component> tooltip, Optional<ResourceLocation> itemModel) {
        return new MobVariant(texture, babyTexture, Optional.empty(), biomeIds, weight, priority, tooltip, itemModel);
    }

    public ResourceLocation texture() {
        return this.textureFull;
    }

    public ResourceLocation babyTexture() {
        return this.babyTextureFull;
    }

    public Optional<HolderSet<Biome>> biomes() {
        return this.biomes;
    }

    public Optional<List<ResourceLocation>> biomeIds() {
        if (this.biomes.isPresent()) {
            return this.biomes.map(set -> set.stream()
                    .map(Holder::unwrapKey)
                    .filter(Optional::isPresent)
                    .map(key -> key.get().location())
                    .distinct()
                    .toList());
        }
        return this.networkBiomeIds;
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

    public Optional<ResourceLocation> itemModel() {
        return this.itemModel;
    }

    private static ResourceLocation fullTextureId(ResourceLocation texture) {
        return texture.withPath(path -> "textures/" + path + ".png");
    }
}
