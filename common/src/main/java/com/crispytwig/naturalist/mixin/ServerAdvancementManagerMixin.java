package com.crispytwig.naturalist.mixin;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(ServerAdvancementManager.class)
public class ServerAdvancementManagerMixin {
    @Unique
    private static final ResourceLocation naturalist$tacticalFishing = new ResourceLocation("husbandry/tactical_fishing");

    @SuppressWarnings("unused")
    @Inject(method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V", at = @At("HEAD"))
    private void naturalist$addFishBucketsToTacticalFishing(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler, CallbackInfo ci) {
        JsonElement element = map.get(naturalist$tacticalFishing);
        if (element == null || !element.isJsonObject()) {
            return;
        }
        JsonObject advancement = element.getAsJsonObject();
        JsonObject criteria = GsonHelper.getAsJsonObject(advancement, "criteria", null);
        JsonArray requirements = GsonHelper.getAsJsonArray(advancement, "requirements", null);
        if (criteria == null || requirements == null || requirements.isEmpty() || !requirements.get(0).isJsonArray()) {
            return;
        }
        JsonArray orGroup = requirements.get(0).getAsJsonArray();
        naturalist$addBucketCriterion(criteria, orGroup, "catfish_bucket");
        naturalist$addBucketCriterion(criteria, orGroup, "bass_bucket");
    }

    @Unique
    private static void naturalist$addBucketCriterion(JsonObject criteria, JsonArray orGroup, String name) {
        if (criteria.has(name)) {
            return;
        }
        JsonObject item = new JsonObject();
        JsonArray items = new JsonArray();
        items.add("naturalist:" + name);
        item.add("items", items);
        JsonObject conditions = new JsonObject();
        conditions.add("item", item);
        JsonObject criterion = new JsonObject();
        criterion.addProperty("trigger", "minecraft:filled_bucket");
        criterion.add("conditions", conditions);
        criteria.add(name, criterion);
        orGroup.add(name);
    }
}
