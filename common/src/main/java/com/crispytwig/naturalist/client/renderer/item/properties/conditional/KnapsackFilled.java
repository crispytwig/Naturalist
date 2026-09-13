package com.crispytwig.naturalist.client.renderer.item.properties.conditional;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.world.item.KnapsackItem;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record KnapsackFilled() implements ConditionalItemModelProperty {
    public static final Identifier ID = Naturalist.location("knapsack_filled");
    public static final MapCodec<KnapsackFilled> MAP_CODEC = MapCodec.unit(new KnapsackFilled());

    @Override
    public boolean get(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity owner, int seed, ItemDisplayContext displayContext) {
        return KnapsackItem.isFilled(itemStack);
    }

    @Override
    public MapCodec<KnapsackFilled> type() {
        return MAP_CODEC;
    }
}
