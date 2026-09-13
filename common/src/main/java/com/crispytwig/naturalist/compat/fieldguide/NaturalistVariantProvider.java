package com.crispytwig.naturalist.compat.fieldguide;

import com.crispytwig.naturalist.world.entity.animal.greatwhiteshark.GreatWhiteShark;
import com.crispytwig.naturalist.world.entity.animal.whale.Whale;
import com.crispytwig.naturalist.world.entity.variant.DataDrivenVariantAnimal;
import com.crispytwig.naturalist.world.entity.variant.MobVariant;
import com.evandev.fieldguide.api.variant.VariantDef;
import com.evandev.fieldguide.api.variant.VariantProvider;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.fish.WaterAnimal;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NaturalistVariantProvider<T extends Mob & DataDrivenVariantAnimal> implements VariantProvider<T> {

    @Override
    public List<VariantDef> getVariants(T entity) {
        List<VariantDef> variants = new ArrayList<>();
        Optional<Registry<MobVariant>> registry = entity.level().registryAccess().lookup(entity.getVariantRegistryKey());
        if (registry.isEmpty() || registry.get().size() <= 1) return variants;

        Identifier defaultVariant = entity.getDefaultVariant().identifier();
        if (registry.get().containsKey(defaultVariant)) {
            variants.add(new VariantDef(defaultVariant.getPath(), defaultVariant));
        }

        for (Identifier id : registry.get().keySet()) {
            if (!id.equals(defaultVariant)) {
                variants.add(new VariantDef(id.getPath(), id));
            }
        }
        return variants;
    }

    @Override
    public void apply(T entity, VariantDef def) {
        if (entity instanceof WaterAnimal || entity instanceof Whale || entity instanceof GreatWhiteShark) {
            entity.wasTouchingWater = true;
        }

        if (def.value() instanceof Identifier id) {
            entity.setVariantString(id.toString());
        }
    }

    @Override
    public VariantDef getCurrent(T entity) {
        Identifier current = entity.getVariantLocation();
        return new VariantDef(current.getPath(), current);
    }
}
