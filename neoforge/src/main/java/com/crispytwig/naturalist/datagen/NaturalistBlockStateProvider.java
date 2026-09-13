package com.crispytwig.naturalist.datagen;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.registry.NaturalistRegistry;
import com.crispytwig.naturalist.world.level.block.GlowGoopBlock;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.*;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

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

        chrysalisBlock();
        plushBearBlock();
        glowGoopBlock();
        simpleBlock(NaturalistRegistry.SNAIL_EGGS.get(), models().getExistingFile(modLoc("snail_eggs")));
    }

    private void stoneFamily(Block block, StairBlock stairs, SlabBlock slab, WallBlock wall) {
        String baseName = name(block);
        Identifier texture = modLoc("block/" + baseName);

        ModelFile cubeModel = models().cubeAll(baseName, texture);
        simpleBlock(block, cubeModel);
        simpleBlockItem(block, cubeModel);

        ModelFile stairsModel = models().stairs(name(stairs), texture, texture, texture);
        stairsBlock(stairs, stairsModel,
                models().stairsInner(name(stairs) + "_inner", texture, texture, texture),
                models().stairsOuter(name(stairs) + "_outer", texture, texture, texture));
        simpleBlockItem(stairs, stairsModel);

        ModelFile slabModel = models().slab(name(slab), texture, texture, texture);
        slabBlock(slab, slabModel,
                models().slabTop(name(slab) + "_top", texture, texture, texture),
                cubeModel);
        simpleBlockItem(slab, slabModel);

        wallBlock(wall, texture);
        simpleBlockItem(wall, models().wallInventory(name(wall) + "_inventory", texture));
    }

    private void translucentBlock(Block block) {
        String n = name(block);
        ModelFile model = models().cubeAll(n, modLoc("block/" + n)).renderType("translucent");
        simpleBlock(block, model);
        itemModels().getBuilder(n).parent(model).renderType("translucent");
    }

    private void translucentPane(IronBarsBlock block, String glassName) {
        String n = name(block);
        Identifier pane = modLoc("block/" + glassName);
        Identifier edge = modLoc("block/" + n + "_top");

        ModelFile post = models().withExistingParent(n + "_post", mcLoc("block/template_glass_pane_post"))
                .texture("pane", pane).texture("edge", edge).renderType("translucent");
        ModelFile side = models().withExistingParent(n + "_side", mcLoc("block/template_glass_pane_side"))
                .texture("pane", pane).texture("edge", edge).renderType("translucent");
        ModelFile sideAlt = models().withExistingParent(n + "_side_alt", mcLoc("block/template_glass_pane_side_alt"))
                .texture("pane", pane).texture("edge", edge).renderType("translucent");
        ModelFile noSide = models().withExistingParent(n + "_noside", mcLoc("block/template_glass_pane_noside"))
                .texture("pane", pane).renderType("translucent");
        ModelFile noSideAlt = models().withExistingParent(n + "_noside_alt", mcLoc("block/template_glass_pane_noside_alt"))
                .texture("pane", pane).renderType("translucent");

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

        itemModels().getBuilder(n)
                .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
                .texture("layer0", pane)
                .renderType("translucent");
    }

    private void eggBlock(Block block, String baseName) {
        String[] hatchPrefixes = {"", "slightly_cracked_", "very_cracked_"};
        String[] textureSuffixes = {"", "_slightly_cracked", "_very_cracked"};
        String[] templates = {
                "block/template_turtle_egg",
                "block/template_two_turtle_eggs",
                "block/template_three_turtle_eggs",
                "block/template_four_turtle_eggs"
        };

        VariantBlockStateBuilder builder = getVariantBuilder(block);
        for (int eggs = 1; eggs <= 4; eggs++) {
            for (int hatch = 0; hatch <= 2; hatch++) {
                String countPrefix = switch (eggs) {
                    case 2 -> "two_";
                    case 3 -> "three_";
                    case 4 -> "four_";
                    default -> "";
                };
                String modelName = eggs == 1
                        ? hatchPrefixes[hatch] + baseName
                        : countPrefix + hatchPrefixes[hatch] + baseName + "s";

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

    private void chrysalisBlock() {
        ModelFile stage0 = models().getExistingFile(modLoc("chrysalis_stage0"));
        ModelFile stage1 = models().getExistingFile(modLoc("chrysalis_stage1"));
        ModelFile stage2 = models().getExistingFile(modLoc("chrysalis_stage2"));
        ModelFile[] stages = {stage0, stage1, stage2, stage2};

        VariantBlockStateBuilder builder = getVariantBuilder(NaturalistRegistry.CHRYSALIS_BLOCK.get());
        for (int age = 0; age <= 3; age++) {
            for (Direction dir : Direction.Plane.HORIZONTAL) {
                int yRot = switch (dir) {
                    case WEST -> 90;
                    case NORTH -> 180;
                    case EAST -> 270;
                    default -> 0;
                };
                builder.partialState()
                        .with(BlockStateProperties.AGE_3, age)
                        .with(HorizontalDirectionalBlock.FACING, dir)
                        .addModels(new ConfiguredModel(stages[age], 0, yRot, false));
            }
        }
    }

    private void plushBearBlock() {
        ModelFile model = models().getExistingFile(modLoc("plush_bear"));
        VariantBlockStateBuilder builder = getVariantBuilder(NaturalistRegistry.PLUSH_BEAR.get());
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            int yRot = switch (dir) {
                case EAST -> 90;
                case SOUTH -> 180;
                case WEST -> 270;
                default -> 0;
            };
            builder.partialState()
                    .with(HorizontalDirectionalBlock.FACING, dir)
                    .addModels(new ConfiguredModel(model, 0, yRot, false));
        }
    }

    private void glowGoopBlock() {
        ModelFile goop1 = models().getExistingFile(modLoc("goop_1"));
        ModelFile goop2 = models().getExistingFile(modLoc("goop_2"));
        ModelFile goop3 = models().getExistingFile(modLoc("goop_3"));
        ModelFile[] goopModels = {goop1, goop2, goop3};

        VariantBlockStateBuilder builder = getVariantBuilder(NaturalistRegistry.GLOW_GOOP_BLOCK.get());
        for (int goop = 1; goop <= 3; goop++) {
            for (boolean waterlogged : new boolean[]{false, true}) {
                builder.partialState()
                        .with(GlowGoopBlock.GOOP, goop)
                        .with(BlockStateProperties.WATERLOGGED, waterlogged)
                        .addModels(new ConfiguredModel(goopModels[goop - 1]));
            }
        }
    }

    private String name(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).getPath();
    }
}
