package com.crispytwig.naturalist.world.item;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.world.entity.Catchable;
import com.crispytwig.naturalist.world.entity.ContainerBoundWorker;
import com.crispytwig.naturalist.world.entity.variant.DataDrivenVariantAnimal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntitySpawnRequest;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PostSpawnProcessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class CaughtEntityItem extends NaturalistBucketItem {
    private final Supplier<? extends EntityType<?>> typeSup;

    public CaughtEntityItem(Supplier<? extends EntityType<?>> entitySupplier, Supplier<? extends SoundEvent> soundSupplier, Properties properties) {
        this(entitySupplier, soundSupplier, null, null, properties);
    }

    public CaughtEntityItem(Supplier<? extends EntityType<?>> entitySupplier, Supplier<? extends SoundEvent> soundSupplier, @Nullable String tooltipPrefix, @Nullable String[] variantNames, Properties properties) {
        super(entitySupplier.get(), Fluids.EMPTY, soundSupplier.get(), properties, true, tooltipPrefix, variantNames);
        this.typeSup = entitySupplier;
    }

    public EntityType<?> type() {
        return this.typeSup.get();
    }

    public ItemStack capture(@NotNull Mob mob) {
        CompoundTag entityTag;
        try (ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(mob.problemPath(), Naturalist.LOGGER)) {
            TagValueOutput output = TagValueOutput.createWithContext(reporter, mob.registryAccess());
            mob.save(output);
            entityTag = output.buildResult();
        }
        ItemStack stack = new ItemStack(this);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(entityTag));
        return stack;
    }

    public static InteractionResult giveCaught(@NotNull Player player, @NotNull InteractionHand hand, @NotNull ItemStack net, @NotNull Mob mob, @NotNull CaughtEntityItem item) {
        ItemStack caught = item.capture(mob);
        BugNetItem.swing(player.level(), player);
        BugNetItem.playCaughtEffects(player.level(), mob);
        net.hurtAndBreak(1, player, hand);
        if (!player.getInventory().add(caught)) {
            ItemHelper.spawnItemOnEntity(player, caught);
        }
        mob.discard();
        return InteractionResult.SUCCESS;
    }

    @SuppressWarnings("unchecked")
    private void spawn(ServerLevel serverLevel, ItemStack itemStack, @Nullable LivingEntity user, BlockPos pos) {
        CompoundTag tag = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        Entity entity;
        if (tag.contains("id")) {
            VoxelShape shape = serverLevel.getBlockState(pos).getCollisionShape(serverLevel, pos);
            double y = pos.getY() + (shape.isEmpty() ? 0.0D : shape.max(Direction.Axis.Y));
            entity = EntityType.loadEntityRecursive(tag, serverLevel, new EntitySpawnRequest(EntitySpawnReason.BUCKET, false), e -> {
                e.snapTo(pos.getX() + 0.5D, y, pos.getZ() + 0.5D, e.getYRot(), e.getXRot());
                if (e instanceof Mob mob) {
                    mob.setPersistenceRequired();
                }
                return e;
            });
            if (entity != null) {
                serverLevel.addFreshEntity(entity);
            }
        } else {
            PostSpawnProcessor<Entity> config = EntityType.<Entity>createDefaultStackConfig(serverLevel, itemStack, user).andThen(e -> {
                if (e instanceof Catchable catchable) {
                    catchable.loadFromHandTag(tag);
                    catchable.setFromHand(true);
                } else if (e instanceof DataDrivenVariantAnimal variantAnimal && tag.contains(DataDrivenVariantAnimal.VARIANT_TAG)) {
                    variantAnimal.loadVariant(tag);
                }
                if (e instanceof Mob mob) {
                    mob.setPersistenceRequired();
                }
            });
            entity = ((EntityType<Entity>) this.type()).spawn(serverLevel, config, pos, EntitySpawnReason.BUCKET, true, false);
        }

        if (entity instanceof ContainerBoundWorker worker) {
            worker.tryAssignWorkstation(pos);
        }
    }

    @Override
    public void checkExtraContent(@Nullable LivingEntity user, @NotNull Level level, @NotNull ItemStack containerStack, @NotNull BlockPos pos) {
        if (level instanceof ServerLevel serverLevel) {
            this.spawn(serverLevel, containerStack, user, pos);
            level.gameEvent(user, GameEvent.ENTITY_PLACE, pos);
        }
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        if (blockhitresult.getType() == HitResult.Type.MISS) {
            return InteractionResult.PASS;
        } else if (blockhitresult.getType() != HitResult.Type.BLOCK) {
            return InteractionResult.PASS;
        } else {
            BlockPos pos = blockhitresult.getBlockPos();
            Direction direction = blockhitresult.getDirection();
            if (level.mayInteract(player, pos) && player.mayUseItemAt(pos.relative(direction), direction, itemstack)) {
                this.checkExtraContent(player, level, itemstack, pos);
                this.playEmptySound(player, level, pos);
                player.awardStat(Stats.ITEM_USED.get(this));
                return InteractionResult.SUCCESS.heldItemTransformedTo(getEmptySuccessItem(itemstack, player));
            } else {
                return InteractionResult.FAIL;
            }
        }
    }

    public static @NotNull ItemStack getEmptySuccessItem(@NotNull ItemStack bucketStack, Player player) {
        return !player.getAbilities().instabuild ? new ItemStack(Items.AIR) : bucketStack;
    }
}
