package com.crispytwig.naturalist.common.level.block.entity;

import com.crispytwig.naturalist.common.entity.meerkat.Meerkat;
import com.crispytwig.naturalist.common.level.block.MeerkatWarrenBlock;
import com.crispytwig.naturalist.common.registry.NaturalistBlockEntities;
import com.crispytwig.naturalist.common.registry.NaturalistEntityTypes;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityProcessor;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class MeerkatWarrenBlockEntity extends BlockEntity {
    private static final List<String> IGNORED_TAGS = Arrays.asList(
            "Air", "drop_chances", "equipment", "Brain", "CanPickUpLoot", "DeathTime", "fall_distance", "FallFlying",
            "Fire", "HurtTime", "LeftHanded", "Motion", "NoGravity", "OnGround", "PortalCooldown", "Pos", "Rotation",
            "sleeping_pos", "Passengers", "leash", "UUID", "warren_pos", "CannotEnterWarrenTicks"
    );
    private static final int MIN_OCCUPATION_TICKS = 600;

    private final List<MeerkatData> stored = new ArrayList<>();
    private int releaseCooldown;

    public MeerkatWarrenBlockEntity(BlockPos pos, BlockState state) {
        super(NaturalistBlockEntities.MEERKAT_WARREN.get(), pos, state);
    }

    public boolean isFull() {
        return this.stored.size() >= MeerkatWarrenBlock.MAX_INSIDE;
    }

    public void storeMeerkat(Occupant occupant) {
        this.stored.add(new MeerkatData(occupant));
    }

    public void addOccupant(Meerkat meerkat) {
        if (this.isFull() || !(this.level instanceof ServerLevel serverLevel)) {
            return;
        }
        meerkat.stopRiding();
        meerkat.ejectPassengers();
        meerkat.dropLeash();
        this.storeMeerkat(Occupant.of(meerkat));
        playBurrowEffects(serverLevel, this.worldPosition.above());
        meerkat.discard();
        this.updateOccupantCount();
    }

    private void updateOccupantCount() {
        if (this.level != null) {
            this.level.setBlock(this.worldPosition, this.getBlockState().setValue(MeerkatWarrenBlock.MEERKATS, this.stored.size()), 3);
        }
        this.setChanged();
    }

    public void emptyAllLivingFromWarren(ServerLevel level) {
        List<BlockPos> exits = new ArrayList<>(List.of(this.worldPosition));
        exits.addAll(findExits(level, this.worldPosition));
        Iterator<BlockPos> available = exits.iterator();
        boolean released = false;
        for (MeerkatData data : this.stored) {
            if (!available.hasNext()) {
                break;
            }
            Meerkat meerkat = release(level, this.worldPosition, data.toOccupant(), available.next());
            if (meerkat != null) {
                meerkat.setStayOutOfWarrenCountdown(400);
                released = true;
            }
        }
        this.stored.clear();
        if (released) {
            playBurrowEffects(level, this.worldPosition);
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, MeerkatWarrenBlockEntity warren) {
        warren.stored.forEach(MeerkatData::tick);
        if (--warren.releaseCooldown > 0) {
            return;
        }
        warren.releaseCooldown = Mth.randomBetweenInclusive(level.getRandom(), 60, 140);
        if (!(level instanceof ServerLevel serverLevel) || warren.stored.isEmpty() || !level.isBrightOutside() || level.getRandom().nextBoolean()) {
            return;
        }
        Player player = level.getNearestPlayer(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 64.0D, false);
        if (player == null || player.blockPosition().distSqr(pos) < 9.0D || level.getEntitiesOfClass(Meerkat.class, new AABB(pos).inflate(12.0D)).size() >= 6) {
            return;
        }
        List<BlockPos> exits = findExits(serverLevel, pos);
        Iterator<MeerkatData> iterator = warren.stored.iterator();
        while (iterator.hasNext() && !exits.isEmpty()) {
            MeerkatData data = iterator.next();
            if (data.isReady()) {
                if (release(serverLevel, pos, data.toOccupant(), exits.getFirst()) != null) {
                    iterator.remove();
                    playBurrowEffects(serverLevel, exits.getFirst());
                    warren.updateOccupantCount();
                }
                return;
            }
        }
    }

    private static Meerkat release(ServerLevel level, BlockPos warrenPos, Occupant occupant, BlockPos exit) {
        if (!(occupant.createEntity(level, warrenPos) instanceof Meerkat meerkat)) {
            return null;
        }
        meerkat.snapTo(exit.getX() + 0.5D, exit.getY(), exit.getZ() + 0.5D, level.getRandom().nextFloat() * 360.0F, 0.0F);
        if (occupant.isFresh()) {
            meerkat.finalizeSpawn(level, level.getCurrentDifficultyAt(exit), EntitySpawnReason.TRIGGERED, null);
        }
        return level.addFreshEntity(meerkat) ? meerkat : null;
    }

    private static List<BlockPos> findExits(ServerLevel level, BlockPos pos) {
        List<BlockPos> exits = new ArrayList<>();
        for (BlockPos candidate : BlockPos.betweenClosed(pos.offset(-2, 1, -2), pos.offset(2, 1, 2))) {
            BlockState below = level.getBlockState(candidate.below());
            if (level.getBlockState(candidate).isAir() && level.getBlockState(candidate.above()).isAir() && !below.isAir() && below.getFluidState().isEmpty()) {
                exits.add(candidate.immutable());
            }
        }
        exits.sort(Comparator.comparingDouble(exit -> exit.distSqr(pos)));
        return exits;
    }

    private static void playBurrowEffects(ServerLevel level, BlockPos pos) {
        level.playSound(null, pos, SoundEvents.GRASS_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    @Override
    protected void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);
        this.stored.clear();
        input.read("meerkats", Occupant.LIST_CODEC).orElse(List.of()).forEach(this::storeMeerkat);
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output) {
        super.saveAdditional(output);
        output.store("meerkats", Occupant.LIST_CODEC, this.stored.stream().map(MeerkatData::toOccupant).toList());
    }

    private static class MeerkatData {
        private final Occupant occupant;
        private int ticksInWarren;

        private MeerkatData(Occupant occupant) {
            this.occupant = occupant;
            this.ticksInWarren = occupant.ticksInWarren();
        }

        private void tick() {
            this.ticksInWarren++;
        }

        private boolean isReady() {
            return this.ticksInWarren > this.occupant.minTicksInWarren();
        }

        private Occupant toOccupant() {
            return new Occupant(this.occupant.entityData(), this.ticksInWarren, this.occupant.minTicksInWarren());
        }
    }

    public record Occupant(TypedEntityData<EntityType<?>> entityData, int ticksInWarren, int minTicksInWarren) {
        public static final Codec<List<Occupant>> LIST_CODEC = RecordCodecBuilder.<Occupant>create(instance -> instance.group(
                TypedEntityData.codec(EntityType.CODEC).fieldOf("entity_data").forGetter(Occupant::entityData),
                Codec.INT.fieldOf("ticks_in_warren").forGetter(Occupant::ticksInWarren),
                Codec.INT.fieldOf("min_ticks_in_warren").forGetter(Occupant::minTicksInWarren)
        ).apply(instance, Occupant::new)).listOf();

        public static Occupant of(Entity entity) {
            try (ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(entity.problemPath(), LogUtils.getLogger())) {
                TagValueOutput output = TagValueOutput.createWithContext(reporter, entity.registryAccess());
                entity.save(output);
                IGNORED_TAGS.forEach(output::discard);
                return new Occupant(TypedEntityData.of(entity.getType(), output.buildResult()), 0, MIN_OCCUPATION_TICKS);
            }
        }

        public static Occupant create(int ticksInWarren) {
            return new Occupant(TypedEntityData.of(NaturalistEntityTypes.MEERKAT.get(), new CompoundTag()), ticksInWarren, MIN_OCCUPATION_TICKS);
        }

        private boolean isFresh() {
            return this.entityData.copyTagWithoutId().isEmpty();
        }

        private Entity createEntity(Level level, BlockPos warrenPos) {
            CompoundTag tag = this.entityData.copyTagWithoutId();
            IGNORED_TAGS.forEach(tag::remove);
            Entity entity = EntityType.loadEntityRecursive(this.entityData.type(), tag, level, EntitySpawnReason.LOAD, EntityProcessor.NOP);
            if (entity instanceof Meerkat meerkat) {
                meerkat.setWarrenPos(warrenPos);
                if (!meerkat.isAgeLocked()) {
                    int age = meerkat.getAge();
                    meerkat.setAge(age < 0 ? Math.min(0, age + this.ticksInWarren) : Math.max(0, age - this.ticksInWarren));
                }
                meerkat.setInLoveTime(Math.max(0, meerkat.getInLoveTime() - this.ticksInWarren));
            }
            return entity;
        }
    }
}
