package com.crispytwig.naturalist.client.compat;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.world.entity.animal.firefly.Firefly;
import dev.lambdaurora.lambdynlights.api.entity.luminance.EntityLuminance;
import dev.lambdaurora.lambdynlights.api.item.ItemLightSourceManager;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NonNull;

public final class FireflyLuminance implements EntityLuminance {
    public static final FireflyLuminance INSTANCE = new FireflyLuminance();
    public static final EntityLuminance.Type TYPE = EntityLuminance.Type.registerSimple(
            Naturalist.location("firefly"),
            INSTANCE
    );

    private FireflyLuminance() {}

    @Override
    public @NonNull Type type() {
        return TYPE;
    }

    @Override
    public @Range(from = 0, to = 15) int getLuminance(@NonNull ItemLightSourceManager itemLightSourceManager, @NonNull Entity entity) {
        if (entity instanceof Firefly firefly) {
            return firefly.getGlowLuminance();
        }
        return 0;
    }
}
