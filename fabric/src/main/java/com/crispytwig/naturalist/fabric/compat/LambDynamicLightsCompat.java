package com.crispytwig.naturalist.fabric.compat;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.registry.NaturalistEntityTypes;
import dev.lambdaurora.lambdynlights.api.DynamicLightsContext;
import dev.lambdaurora.lambdynlights.api.DynamicLightsInitializer;
import net.minecraft.resources.ResourceLocation;
import org.jspecify.annotations.NonNull;

public class LambDynamicLightsCompat implements DynamicLightsInitializer {
    @Override
    public void onInitializeDynamicLights(DynamicLightsContext context) {
        context.entityLightSourceManager().onRegisterEvent()
                .register(
                        Naturalist.location("firefly"),
                        registerContext -> registerContext.register(
                                NaturalistEntityTypes.FIREFLY.get(),
                                FireflyLuminance.INSTANCE
                        )
                );
    }

    @Override
    public void onInitializeDynamicLights() {}
}
