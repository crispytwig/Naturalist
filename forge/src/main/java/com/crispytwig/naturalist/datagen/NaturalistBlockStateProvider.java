package com.crispytwig.naturalist.datagen;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.registry.NaturalistRegistry;
import com.crispytwig.naturalist.world.level.block.AntHillBlock;
import com.crispytwig.naturalist.world.level.block.ChrysalisBlock;
import com.crispytwig.naturalist.world.level.block.GlowGoopBlock;
import com.crispytwig.naturalist.world.level.block.SnailShellBlock;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.*;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;

public class NaturalistBlockStateProvider extends BlockStateProvider {
    public NaturalistBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Naturalist.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        stoneFamily(NaturalistRegistry.SHELLSTONE.get(), NaturalistRegistry.SHELLSTONE_STAIRS.get(),
                NaturalistRegistry.SHELLSTONE_SLAB.get(), NaturalistRegistry.SHELLSTONE_WALL.get());
        stoneFamily(NaturalistRegistry.SHELLSTONE_BRICKS.get(), NaturalistRegistry.SHELLSTONE_BRICK_STAIRS.get(),
                NaturalistRegistry.SHELLSTONE_BRICK_SLAB.get(), NaturalistRegistry.SHELLSTONE_BRICK_WALL.get());
        stoneFamily(NaturalistRegistry.CUT_SHELLSTONE.get(), NaturalistRegistry.CUT_SHELLSTONE_STAIRS.get(),
                NaturalistRegistry.CUT_SHELLSTONE_SLAB.get(), NaturalistRegistry.CUT_SHELLSTONE_WALL.get());
        stoneFamily(NaturalistRegistry.SMOOTH_SHELLSTONE.get(), NaturalistRegistry.SMOOTH_SHELLSTONE_STAIRS.get(),
                NaturalistRegistry.SMOOTH_SHELLSTONE_SLAB.get(), NaturalistRegistry.SMOOTH_SHELLSTONE_WALL.get());

        translucentBlock(NaturalistRegistry.AZURE_FROGLASS.get());
        translucentBlock(NaturalistRegistry.VERDANT_FROGLASS.get());
        translucentBlock(NaturalistRegistry.CRIMSON_FROGLASS.get());
        translucentPane(NaturalistRegistry.AZURE_FROGLASS_PANE.get(), "azure_froglass");
        translucentPane(NaturalistRegistry.VERDANT_FROGLASS_PANE.get(), "verdant_froglass");
        translucentPane(NaturalistRegistry.CRIMSON_FROGLASS_PANE.get(), "crimson_froglass");

        eggBlock(NaturalistRegistry.ALLIGATOR_EGG.get(), "alligator_egg");
        eggBlock(NaturalistRegistry.TORTOISE_EGG.get(), "tortoise_egg");
        ostrichEggBlock();

        starfishBlock(NaturalistRegistry.RED_STARFISH.get());
        starfishBlock(NaturalistRegistry.ORANGE_STARFISH.get());
        starfishBlock(NaturalistRegistry.BLUE_STARFISH.get());
        starfishBlock(NaturalistRegistry.PURPLE_STARFISH.get());

        antHillBlock();
        snailShellBlock();
        chrysalisBlock();
        plushBearBlock();
        glowGoopBlock();
        simpleBlock(NaturalistRegistry.SNAIL_EGGS.get(), models().getExistingFile(modLoc("snail_eggs")));
    }

    private void stoneFamily(Block block, StairBlock stairs, SlabBlock slab, WallBlock wall) {
        String baseName = name(block);
        ResourceLocation texture = modLoc("block/" + baseName);

        ModelFile cubeModel = models().cubeAll(baseName, texture);
        simpleBlock(block, cubeModel);

        stairsBlock(stairs, models().stairs(name(stairs), texture, texture, texture),
                models().stairsInner(name(stairs) + "_inner", texture, texture, texture),
                models().stairsOuter(name(stairs) + "_outer", texture, texture, texture));

        slabBlock(slab, models().slab(name(slab), texture, texture, texture),
                models().slabTop(name(slab) + "_top", texture, texture, texture),
                cubeModel);

        wallBlock(wall, texture);
        models().wallInventory(name(wall) + "_inventory", texture);
    }

    private void translucentBlock(Block block) {
        String name = name(block);
        simpleBlock(block, models().cubeAll(name, modLoc("block/" + name)).renderType("translucent"));
    }

    private void translucentPane(IronBarsBlock block, String glassName) {
        String name = name(block);
        ResourceLocation pane = modLoc("block/" + glassName);
        ResourceLocation edge = modLoc("block/" + name + "_top");

        ModelFile post = models().panePost(name + "_post", pane, edge).renderType("translucent");
        ModelFile side = models().paneSide(name + "_side", pane, edge).renderType("translucent");
        ModelFile sideAlt = models().paneSideAlt(name + "_side_alt", pane, edge).renderType("translucent");
        ModelFile noSide = models().paneNoSide(name + "_noside", pane).renderType("translucent");
        ModelFile noSideAlt = models().paneNoSideAlt(name + "_noside_alt", pane).renderType("translucent");

        getMultipartBuilder(block)
                .part().modelFile(post).addModel().end()
                .part().modelFile(side).addModel()
                    .condition(PipeBlock.NORTH, true).end()
                .part().modelFile(side).rotationY(90).addModel()
                    .condition(PipeBlock.EAST, true).end()
                .part().modelFile(sideAlt).addModel()
                    .condition(PipeBlock.SOUTH, true).end()
                .part().modelFile(sideAlt).rotationY(90).addModel()
                    .condition(PipeBlock.WEST, true).end()
                .part().modelFile(noSide).addModel()
                    .condition(PipeBlock.NORTH, false).end()
                .part().modelFile(noSideAlt).addModel()
                    .condition(PipeBlock.EAST, false).end()
                .part().modelFile(noSideAlt).rotationY(90).addModel()
                    .condition(PipeBlock.SOUTH, false).end()
                .part().modelFile(noSide).rotationY(270).addModel()
                    .condition(PipeBlock.WEST, false).end();
    }

    private void eggBlock(Block block, String baseName) {
        String[] hatchPrefixes = {"", "slightly_cracked_", "very_cracked_"};
        String[] textureSuffixes = {"", "_slightly_cracked", "_very_cracked"};
        String[] countPrefixes = {"", "two_", "three_", "four_"};
        String[] templates = {
                "block/template_turtle_egg",
                "block/template_two_turtle_eggs",
                "block/template_three_turtle_eggs",
                "block/template_four_turtle_eggs"
        };

        VariantBlockStateBuilder builder = getVariantBuilder(block);
        for (int eggs = 1; eggs <= 4; eggs++) {
            for (int hatch = 0; hatch <= 2; hatch++) {
                String modelName = eggs == 1
                        ? hatchPrefixes[hatch] + baseName
                        : countPrefixes[eggs - 1] + hatchPrefixes[hatch] + baseName + "s";

                ModelFile model = models().withExistingParent(modelName, mcLoc(templates[eggs - 1]))
                        .texture("all", modLoc("block/" + baseName + textureSuffixes[hatch]));

                builder.partialState()
                        .with(TurtleEggBlock.EGGS, eggs)
                        .with(TurtleEggBlock.HATCH, hatch)
                        .addModels(
                                new ConfiguredModel(model, 0, 0, false),
                                new ConfiguredModel(model, 0, 90, false),
                                new ConfiguredModel(model, 0, 180, false),
                                new ConfiguredModel(model, 0, 270, false));
            }
        }
    }

    private void ostrichEggBlock() {
        ResourceLocation parent = modLoc("block/ostrich_egg");
        ModelFile[] eggModels = {
                models().getExistingFile(parent),
                models().withExistingParent("ostrich_egg_slightly_cracked", parent)
                        .texture("all", modLoc("block/ostrich_egg_slightly_cracked")),
                models().withExistingParent("ostrich_egg_very_cracked", parent)
                        .texture("all", modLoc("block/ostrich_egg_very_cracked"))
        };

        VariantBlockStateBuilder builder = getVariantBuilder(NaturalistRegistry.OSTRICH_EGG.get());
        for (int hatch = 0; hatch <= 2; hatch++) {
            builder.partialState()
                    .with(TurtleEggBlock.HATCH, hatch)
                    .addModels(new ConfiguredModel(eggModels[hatch]));
        }
    }

    private void starfishBlock(Block block) {
        ModelFile model = models().getExistingFile(modLoc(name(block)));
        getMultipartBuilder(block)
                .part().modelFile(model).addModel()
                    .condition(MultifaceBlock.getFaceProperty(Direction.NORTH), true).end()
                .part().modelFile(model).rotationY(90).addModel()
                    .condition(MultifaceBlock.getFaceProperty(Direction.EAST), true).end()
                .part().modelFile(model).rotationY(180).addModel()
                    .condition(MultifaceBlock.getFaceProperty(Direction.SOUTH), true).end()
                .part().modelFile(model).rotationY(270).addModel()
                    .condition(MultifaceBlock.getFaceProperty(Direction.WEST), true).end()
                .part().modelFile(model).rotationX(270).uvLock(true).addModel()
                    .condition(MultifaceBlock.getFaceProperty(Direction.UP), true).end()
                .part().modelFile(model).rotationX(90).uvLock(true).addModel()
                    .condition(MultifaceBlock.getFaceProperty(Direction.DOWN), true).end();
    }

    private void antHillBlock() {
        getVariantBuilder(NaturalistRegistry.ANT_HILL.get())
                .partialState().with(AntHillBlock.OPEN, false)
                    .addModels(new ConfiguredModel(models().cubeAll("ant_hill", modLoc("block/ant_hill"))))
                .partialState().with(AntHillBlock.OPEN, true)
                    .addModels(new ConfiguredModel(models().cubeAll("ant_hill_open", modLoc("block/ant_hill_open"))));
    }

    private void snailShellBlock() {
        VariantBlockStateBuilder builder = getVariantBuilder(NaturalistRegistry.SNAIL_SHELL_BLOCK.get());
        for (DyeColor color : DyeColor.values()) {
            builder.partialState()
                    .with(SnailShellBlock.COLOR, color)
                    .addModels(new ConfiguredModel(models().getExistingFile(modLoc("block/snail_shell/" + color.getName()))));
        }
    }

    private void chrysalisBlock() {
        ModelFile stage2 = models().getExistingFile(modLoc("chrysalis_stage2"));
        ModelFile[] stages = {
                models().getExistingFile(modLoc("chrysalis_stage0")),
                models().getExistingFile(modLoc("chrysalis_stage1")),
                stage2,
                stage2
        };
        horizontalBlock(NaturalistRegistry.CHRYSALIS_BLOCK.get(), state -> stages[state.getValue(ChrysalisBlock.AGE)], 0);
    }

    private void plushBearBlock() {
        horizontalBlock(NaturalistRegistry.PLUSH_BEAR.get(), models().getExistingFile(modLoc("plush_bear")));
    }

    private void glowGoopBlock() {
        ModelFile[] goopModels = {
                models().getExistingFile(modLoc("goop_1")),
                models().getExistingFile(modLoc("goop_2")),
                models().getExistingFile(modLoc("goop_3"))
        };
        getVariantBuilder(NaturalistRegistry.GLOW_GOOP_BLOCK.get())
                .forAllStates(state -> ConfiguredModel.builder()
                        .modelFile(goopModels[state.getValue(GlowGoopBlock.GOOP) - 1])
                        .build());
    }

    private String name(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).getPath();
    }
}
