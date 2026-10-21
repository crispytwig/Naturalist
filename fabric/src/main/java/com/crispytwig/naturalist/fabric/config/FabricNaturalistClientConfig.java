package com.crispytwig.naturalist.fabric.config;

import com.crispytwig.naturalist.NaturalistClientConfig;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class FabricNaturalistClientConfig {
    private static boolean glowGoopTooltip = true;

    private FabricNaturalistClientConfig() {}

    public static void load() {
        Path path = FabricLoader.getInstance().getConfigDir().resolve("naturalist-client.properties");
        Properties properties = new Properties();
        properties.setProperty(NaturalistClientConfig.GLOW_GOOP_TOOLTIP_KEY, "true");

        if (Files.exists(path)) {
            try (Reader reader = Files.newBufferedReader(path)) {
                properties.load(reader);
            } catch (IOException ignored) {}
        }

        glowGoopTooltip = Boolean.parseBoolean(properties.getProperty(NaturalistClientConfig.GLOW_GOOP_TOOLTIP_KEY));

        try (Writer writer = Files.newBufferedWriter(path)) {
            properties.store(writer, "Naturalist Client Config");
        } catch (IOException ignored) {}
    }

    public static boolean isGlowGoopTooltipEnabled() {
        return glowGoopTooltip;
    }
}
