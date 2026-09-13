package com.crispytwig.naturalist.world.entity.variant;

import com.crispytwig.naturalist.Naturalist;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import com.crispytwig.naturalist.registry.NaturalistMobVariants;

import java.util.Optional;

public interface DataDrivenVariantAnimal {
    String VARIANT_TAG = "Variant";

    default ResourceKey<Registry<MobVariant>> getVariantRegistryKey() {
        return NaturalistMobVariants.registryFor(((Mob) this).getType());
    }

    default ResourceKey<MobVariant> getDefaultVariant() {
        return NaturalistMobVariants.defaultFor(((Mob) this).getType());
    }

    default String[] getLegacyVariantNames() {
        return new String[]{this.getDefaultVariant().identifier().getPath()};
    }

    Identifier getFallbackVariantTexture();

    String getVariantString();

    void setVariantString(String location);

    default Identifier getVariantLocation() {
        Identifier location = Identifier.tryParse(this.getVariantString());
        return location != null ? location : this.getDefaultVariant().identifier();
    }

    default void setVariant(Holder<MobVariant> variant) {
        variant.unwrapKey().ifPresent(key -> this.setVariantString(key.identifier().toString()));
    }

    default Optional<Holder.Reference<MobVariant>> getVariantHolder() {
        return MobVariantUtil.byId(((Mob) this).level().registryAccess(), this.getVariantRegistryKey(), this.getVariantLocation());
    }

    default boolean hasNonDefaultVariant() {
        return !this.getVariantLocation().equals(this.getDefaultVariant().identifier());
    }

    default Identifier getVariantTexture() {
        return this.getVariantHolder().map(holder -> holder.value().texture()).orElseGet(this::getFallbackVariantTexture);
    }

    default Identifier getVariantBabyTexture() {
        return this.getVariantHolder().map(holder -> holder.value().babyTexture()).orElseGet(this::getFallbackVariantTexture);
    }

    default void saveVariant(CompoundTag tag) {
        tag.putString(VARIANT_TAG, this.getVariantLocation().toString());
    }

    default void loadVariant(CompoundTag tag) {
        MobVariantUtil.readVariantId(tag, this.getLegacyVariantNames())
                .ifPresent(location -> this.setVariantString(location.toString()));
    }

    default void saveVariant(ValueOutput output) {
        output.putString(VARIANT_TAG, this.getVariantLocation().toString());
    }

    default void loadVariant(ValueInput input) {
        MobVariantUtil.readVariantId(input, this.getLegacyVariantNames())
                .ifPresent(location -> this.setVariantString(location.toString()));
    }

    default int getLegacyVariantIndex() {
        String path = this.getVariantLocation().getPath();
        String[] legacy = this.getLegacyVariantNames();
        for (int i = 0; i < legacy.length; i++) {
            if (legacy[i].equals(path)) {
                return i;
            }
        }
        return 0;
    }

    default void setVariantByLegacyIndex(int index) {
        String[] legacy = this.getLegacyVariantNames();
        this.setVariantString(Naturalist.location(legacy[Math.floorMod(index, legacy.length)]).toString());
    }

    default void selectVariantForSpawn(ServerLevelAccessor level) {
        MobVariantUtil.selectVariantForSpawn(level, ((Mob) this).blockPosition(), this.getVariantRegistryKey())
                .ifPresent(this::setVariant);
    }

    default String getOffspringVariantId(AgeableMob otherParent, RandomSource random) {
        return otherParent instanceof DataDrivenVariantAnimal other && random.nextBoolean()
                ? other.getVariantString()
                : this.getVariantString();
    }
}
