package com.crispytwig.naturalist.mixin;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SimpleJsonResourceReloadListener.class)
public abstract class SimpleJsonResourceReloadListenerMixin {
    @Unique
    private static final String naturalist$tacticalFishingFile = "advancement/husbandry/tactical_fishing.json";

    @ModifyExpressionValue(
            method = "scanDirectory",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/StrictJsonParser;parse(Ljava/io/Reader;)Lcom/google/gson/JsonElement;"),
            require = 0
    )
    private static JsonElement naturalist$addFishBucketsFromScanDirectory(JsonElement element, @Local(ordinal = 0) Identifier location) {
        return naturalist$addFishBucketsToTacticalFishing(element, location);
    }

    @ModifyExpressionValue(
            method = "prepare(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)Ljava/util/Map;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/StrictJsonParser;parse(Ljava/io/Reader;)Lcom/google/gson/JsonElement;"),
            require = 0
    )
    private JsonElement naturalist$addFishBucketsFromPrepare(JsonElement element, @Local(ordinal = 0) Identifier location) {
        return naturalist$addFishBucketsToTacticalFishing(element, location);
    }

    @Unique
    private static JsonElement naturalist$addFishBucketsToTacticalFishing(JsonElement element, Identifier location) {
        if (!"minecraft".equals(location.getNamespace()) || !naturalist$tacticalFishingFile.equals(location.getPath())) {
            return element;
        }
        if (element == null || !element.isJsonObject()) {
            return element;
        }
        JsonObject advancement = element.getAsJsonObject();
        JsonObject criteria = GsonHelper.getAsJsonObject(advancement, "criteria", null);
        JsonArray requirements = GsonHelper.getAsJsonArray(advancement, "requirements", null);
        if (criteria == null || requirements == null || requirements.isEmpty() || !requirements.get(0).isJsonArray()) {
            return element;
        }
        JsonArray orGroup = requirements.get(0).getAsJsonArray();
        naturalist$addBucketCriterion(criteria, orGroup, "catfish_bucket");
        naturalist$addBucketCriterion(criteria, orGroup, "bass_bucket");
        return element;
    }

    @Unique
    private static void naturalist$addBucketCriterion(JsonObject criteria, JsonArray orGroup, String name) {
        if (criteria.has(name)) {
            return;
        }
        JsonObject item = new JsonObject();
        item.addProperty("items", "naturalist:" + name);
        JsonObject conditions = new JsonObject();
        conditions.add("item", item);
        JsonObject criterion = new JsonObject();
        criterion.addProperty("trigger", "minecraft:filled_bucket");
        criterion.add("conditions", conditions);
        criteria.add(name, criterion);
        orGroup.add(name);
    }
}
