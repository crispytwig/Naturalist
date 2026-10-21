package com.crispytwig.naturalist.datagen;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.registry.NaturalistRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.function.Supplier;

public class NaturalistItemModelProvider extends ItemModelProvider {
    private static final String[] COLORS = {"white", "orange", "magenta", "light_blue", "yellow", "lime",
            "pink", "gray", "light_gray", "cyan", "purple", "blue",
            "brown", "green", "red", "black"};

    public NaturalistItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Naturalist.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        List.of(
                NaturalistRegistry.BUSHMEAT, NaturalistRegistry.COOKED_BUSHMEAT,
                NaturalistRegistry.FUR, NaturalistRegistry.TOOTH, NaturalistRegistry.FAT, NaturalistRegistry.HIDE, NaturalistRegistry.MORSEL,
                NaturalistRegistry.DUCK_EGG, NaturalistRegistry.COOKED_EGG, NaturalistRegistry.ANTLER, NaturalistRegistry.GLOW_GOOP,
                NaturalistRegistry.DUCK, NaturalistRegistry.COOKED_DUCK,
                NaturalistRegistry.VENISON, NaturalistRegistry.COOKED_VENISON,
                NaturalistRegistry.DRUMSTICK, NaturalistRegistry.COOKED_DRUMSTICK,
                NaturalistRegistry.MAMMOTH_MEAT, NaturalistRegistry.COOKED_MAMMOTH_MEAT,
                NaturalistRegistry.LIZARD_TAIL, NaturalistRegistry.COOKED_LIZARD_TAIL,
                NaturalistRegistry.CATFISH, NaturalistRegistry.COOKED_CATFISH,
                NaturalistRegistry.BASS, NaturalistRegistry.COOKED_BASS,
                NaturalistRegistry.ANGLERFISH, NaturalistRegistry.COOKED_ANGLERFISH,
                NaturalistRegistry.BLOBFISH, NaturalistRegistry.COOKED_BLOBFISH,
                NaturalistRegistry.PIRANHA, NaturalistRegistry.COOKED_PIRANHA,
                NaturalistRegistry.CLAM_MEAT, NaturalistRegistry.COOKED_CLAM_MEAT,
                NaturalistRegistry.CRAB_MEAT, NaturalistRegistry.COOKED_CRAB_MEAT,
                NaturalistRegistry.WHISTLE, NaturalistRegistry.MUSIC_DISC_WILD_ONES, NaturalistRegistry.MUSIC_DISC_DEATH_BY_HOGS,
                NaturalistRegistry.CHRYSALIS, NaturalistRegistry.CATERPILLAR, NaturalistRegistry.ANT, NaturalistRegistry.SCORPION,
                NaturalistRegistry.FIREFLY, NaturalistRegistry.BEE, NaturalistRegistry.SILVERFISH, NaturalistRegistry.ENDERMITE,
                NaturalistRegistry.SCORPION_POISON_GLAND, NaturalistRegistry.QUEEN_ANT
        ).forEach(item -> basicItem(item.get()));

        List.of(
                NaturalistRegistry.ALLIGATOR_EGG, NaturalistRegistry.TORTOISE_EGG, NaturalistRegistry.OSTRICH_EGG,
                NaturalistRegistry.SNAIL_EGGS, NaturalistRegistry.PLUSH_BEAR
        ).forEach(block -> basicItem(block.get().asItem()));

        withExistingParent(name(NaturalistRegistry.CAPTURE_NET.get()), mcLoc("item/handheld"))
                .texture("layer0", modLoc("item/capture_net"));

        generated(NaturalistRegistry.CATFISH_BUCKET.get(), "bucket_catfish");
        generated(NaturalistRegistry.BASS_BUCKET.get(), "bucket_bass");
        generated(NaturalistRegistry.DUCK_BUCKET.get(), "bucket_duckling");
        generated(NaturalistRegistry.ANGLERFISH_BUCKET.get(), "bucket_anglerfish");
        generated(NaturalistRegistry.BLOBFISH_BUCKET.get(), "bucket_blobfish");
        generated(NaturalistRegistry.PIRANHA_BUCKET.get(), "bucket_piranha");
        generated(NaturalistRegistry.GIANT_ISOPOD_BUCKET.get(), "bucket_brown_giant_isopod");
        generated(NaturalistRegistry.JELLYFISH_BUCKET.get(), "bucket_white_jellyfish");
        generated(NaturalistRegistry.RAY_BUCKET.get(), "bucket_eagle_ray");
        generated(NaturalistRegistry.STARFISH_BUCKET.get(), "bucket_orange_starfish");

        generated(NaturalistRegistry.BUTTERFLY.get(), "butterfly_monarch");
        generated(NaturalistRegistry.CRAB.get(), "crab_blue");
        generated(NaturalistRegistry.DRAGONFLY.get(), "dragonfly_blue");
        generated(NaturalistRegistry.HEDGEHOG.get(), "hedgehog_brown");
        generated(NaturalistRegistry.RAT.get(), "rat_black");

        knapsack();
        colored(NaturalistRegistry.SNAIL.get(), "snail");
        colored(NaturalistRegistry.SNAIL_SHELL.get(), "snail_shell");

        List.of(
                NaturalistRegistry.RED_STARFISH, NaturalistRegistry.ORANGE_STARFISH,
                NaturalistRegistry.BLUE_STARFISH, NaturalistRegistry.PURPLE_STARFISH
        ).forEach(block -> withExistingParent(name(block.get()), mcLoc("item/generated"))
                .texture("layer0", blockTexture(block.get())));

        froglass(NaturalistRegistry.AZURE_FROGLASS, NaturalistRegistry.AZURE_FROGLASS_PANE);
        froglass(NaturalistRegistry.VERDANT_FROGLASS, NaturalistRegistry.VERDANT_FROGLASS_PANE);
        froglass(NaturalistRegistry.CRIMSON_FROGLASS, NaturalistRegistry.CRIMSON_FROGLASS_PANE);

        List.of(
                NaturalistRegistry.ANT_HILL,
                NaturalistRegistry.SHELLSTONE, NaturalistRegistry.SHELLSTONE_STAIRS, NaturalistRegistry.SHELLSTONE_SLAB,
                NaturalistRegistry.SHELLSTONE_BRICKS, NaturalistRegistry.SHELLSTONE_BRICK_STAIRS, NaturalistRegistry.SHELLSTONE_BRICK_SLAB,
                NaturalistRegistry.CUT_SHELLSTONE, NaturalistRegistry.CUT_SHELLSTONE_STAIRS, NaturalistRegistry.CUT_SHELLSTONE_SLAB,
                NaturalistRegistry.SMOOTH_SHELLSTONE, NaturalistRegistry.SMOOTH_SHELLSTONE_STAIRS, NaturalistRegistry.SMOOTH_SHELLSTONE_SLAB
        ).forEach(block -> blockItem(block, ""));

        List.of(
                NaturalistRegistry.SHELLSTONE_WALL, NaturalistRegistry.SHELLSTONE_BRICK_WALL,
                NaturalistRegistry.CUT_SHELLSTONE_WALL, NaturalistRegistry.SMOOTH_SHELLSTONE_WALL
        ).forEach(block -> blockItem(block, "_inventory"));

        List.of(
                NaturalistRegistry.ALLIGATOR_SPAWN_EGG, NaturalistRegistry.ANGLERFISH_SPAWN_EGG, NaturalistRegistry.RAY_SPAWN_EGG,
                NaturalistRegistry.BLOBFISH_SPAWN_EGG, NaturalistRegistry.PIRANHA_SPAWN_EGG, NaturalistRegistry.BASS_SPAWN_EGG,
                NaturalistRegistry.BEAR_SPAWN_EGG, NaturalistRegistry.BIRD_SPAWN_EGG, NaturalistRegistry.BOAR_SPAWN_EGG,
                NaturalistRegistry.BUTTERFLY_SPAWN_EGG, NaturalistRegistry.CATFISH_SPAWN_EGG, NaturalistRegistry.CATERPILLAR_SPAWN_EGG,
                NaturalistRegistry.CLAM_SPAWN_EGG, NaturalistRegistry.CRAB_SPAWN_EGG, NaturalistRegistry.DEER_SPAWN_EGG,
                NaturalistRegistry.DRAGONFLY_SPAWN_EGG, NaturalistRegistry.DUCK_SPAWN_EGG, NaturalistRegistry.CAPYBARA_SPAWN_EGG,
                NaturalistRegistry.HEDGEHOG_SPAWN_EGG, NaturalistRegistry.ELEPHANT_SPAWN_EGG, NaturalistRegistry.MAMMOTH_SPAWN_EGG,
                NaturalistRegistry.FIREFLY_SPAWN_EGG, NaturalistRegistry.GIANT_ISOPOD_SPAWN_EGG, NaturalistRegistry.JELLYFISH_SPAWN_EGG,
                NaturalistRegistry.GIRAFFE_SPAWN_EGG, NaturalistRegistry.HIPPO_SPAWN_EGG, NaturalistRegistry.LION_SPAWN_EGG,
                NaturalistRegistry.LIZARD_SPAWN_EGG, NaturalistRegistry.RHINO_SPAWN_EGG, NaturalistRegistry.SNAKE_SPAWN_EGG,
                NaturalistRegistry.SNAIL_SPAWN_EGG, NaturalistRegistry.STARFISH_SPAWN_EGG, NaturalistRegistry.TORTOISE_SPAWN_EGG,
                NaturalistRegistry.VULTURE_SPAWN_EGG, NaturalistRegistry.ZEBRA_SPAWN_EGG, NaturalistRegistry.WHALE_SPAWN_EGG,
                NaturalistRegistry.ANT_SPAWN_EGG, NaturalistRegistry.MOLE_SPAWN_EGG, NaturalistRegistry.RAT_SPAWN_EGG,
                NaturalistRegistry.BLACK_BEAR_SPAWN_EGG, NaturalistRegistry.TIGER_SPAWN_EGG, NaturalistRegistry.KOMODO_DRAGON_SPAWN_EGG,
                NaturalistRegistry.OSTRICH_SPAWN_EGG, NaturalistRegistry.DESERT_SCORPION_SPAWN_EGG, NaturalistRegistry.JUNGLE_SCORPION_SPAWN_EGG,
                NaturalistRegistry.GREAT_WHITE_SHARK_SPAWN_EGG, NaturalistRegistry.TURKEY_SPAWN_EGG
        ).forEach(egg -> withExistingParent(name(egg.get()), mcLoc("item/template_spawn_egg")));

        variants();
    }

    private void variants() {
        for (String variant : new String[]{"monarch", "clouded_yellow", "blue_morpho", "jade_green_swallowtail", "purple_emperor", "red_admiral"}) {
            variant("variant/" + variant, "butterfly_" + variant);
        }
        variant("variant/swallowtail", "butterfly_green_swallowtail");

        for (String variant : new String[]{"crab_blue", "crab_brown", "crab_orange", "crab_red", "crab_yellow",
                "dragonfly_blue", "dragonfly_green", "dragonfly_red",
                "hedgehog_brown", "hedgehog_dark", "hedgehog_white",
                "rat_black", "rat_brown", "rat_white"}) {
            variant("variant/" + variant, variant);
        }

        variant("variant/anglerfish_bucket/glow", "bucket_glow_anglerfish");
        variant("variant/anglerfish_bucket/red", "bucket_anglerfish");
        for (String variant : new String[]{"blue", "brown"}) {
            variant("variant/giant_isopod_bucket/" + variant, "bucket_" + variant + "_giant_isopod");
        }
        for (String variant : new String[]{"blue", "green", "orange", "pink", "white"}) {
            variant("variant/jellyfish_bucket/" + variant, "bucket_" + variant + "_jellyfish");
        }
        for (String variant : new String[]{"eagle_ray", "mobula_ray", "stingray"}) {
            variant("variant/ray_bucket/" + variant, "bucket_" + variant);
        }
        for (String variant : new String[]{"blue", "orange", "purple", "red"}) {
            variant("variant/starfish_bucket/" + variant, "bucket_" + variant + "_starfish");
        }
    }

    private void knapsack() {
        ItemModelBuilder filled = withExistingParent("knapsack_filled", mcLoc("item/generated"))
                .texture("layer0", modLoc("item/knapsack"));

        generated(NaturalistRegistry.KNAPSACK.get(), "empty_knapsack")
                .override()
                .predicate(Naturalist.location("filled"), 1.0F)
                .model(filled)
                .end();
    }

    private void colored(Item item, String name) {
        ItemModelBuilder builder = generated(item, name + "_brown");
        for (int i = 0; i < COLORS.length; i++) {
            ItemModelBuilder color = variant(name + "/" + COLORS[i], name + "_" + COLORS[i]);
            builder.override()
                    .predicate(new ResourceLocation("color"), i / 15.0F)
                    .model(color)
                    .end();
        }
    }

    private void froglass(Supplier<? extends Block> glass, Supplier<? extends Block> pane) {
        blockItem(glass, "").renderType("translucent");
        withExistingParent(name(pane.get().asItem()), mcLoc("item/generated"))
                .renderType("translucent")
                .texture("layer0", blockTexture(glass.get()));
    }

    private ItemModelBuilder blockItem(Supplier<? extends Block> block, String suffix) {
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block.get());
        return getBuilder(id.toString())
                .parent(new ModelFile.UncheckedModelFile(id.withPrefix("block/").withSuffix(suffix)));
    }

    private ItemModelBuilder generated(Item item, String texture) {
        return withExistingParent(name(item), mcLoc("item/generated"))
                .texture("layer0", modLoc("item/" + texture));
    }

    private ItemModelBuilder variant(String path, String texture) {
        return withExistingParent("item/" + path, mcLoc("item/generated"))
                .texture("layer0", modLoc("item/" + texture));
    }

    private ResourceLocation blockTexture(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/");
    }

    private String name(Item item) {
        return BuiltInRegistries.ITEM.getKey(item).toString();
    }

    private String name(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).toString();
    }
}
