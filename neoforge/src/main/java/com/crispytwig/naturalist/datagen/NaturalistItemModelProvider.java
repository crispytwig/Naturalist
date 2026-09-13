package com.crispytwig.naturalist.datagen;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.registry.NaturalistRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class NaturalistItemModelProvider extends ItemModelProvider {
    public NaturalistItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Naturalist.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(NaturalistRegistry.BUSHMEAT.get());
        basicItem(NaturalistRegistry.COOKED_BUSHMEAT.get());
        basicItem(NaturalistRegistry.DUCK.get());
        basicItem(NaturalistRegistry.COOKED_DUCK.get());
        basicItem(NaturalistRegistry.VENISON.get());
        basicItem(NaturalistRegistry.COOKED_VENISON.get());
        basicItem(NaturalistRegistry.LIZARD_TAIL.get());
        basicItem(NaturalistRegistry.COOKED_LIZARD_TAIL.get());
        basicItem(NaturalistRegistry.CATFISH.get());
        basicItem(NaturalistRegistry.COOKED_CATFISH.get());
        basicItem(NaturalistRegistry.BASS.get());
        basicItem(NaturalistRegistry.COOKED_BASS.get());
        basicItem(NaturalistRegistry.COOKED_EGG.get());

        basicItem(NaturalistRegistry.FUR.get());
        basicItem(NaturalistRegistry.ANTLER.get());
        basicItem(NaturalistRegistry.SNAIL_SHELL.get());

        basicItem(NaturalistRegistry.CATERPILLAR.get());
        basicItem(NaturalistRegistry.DUCK_EGG.get());

        basicItem(NaturalistRegistry.CHRYSALIS.get());
        basicItem(NaturalistRegistry.GLOW_GOOP.get());
        simpleItem("alligator_egg");
        simpleItem("tortoise_egg");
        simpleItem("snail_eggs");
        simpleItem("plush_bear");

        withExistingParent("capture_net", mcLoc("item/handheld"))
                .texture("layer0", modLoc("item/capture_net"));

        withExistingParent("catfish_bucket", mcLoc("item/generated"))
                .texture("layer0", modLoc("item/bucket_catfish"));
        withExistingParent("bass_bucket", mcLoc("item/generated"))
                .texture("layer0", modLoc("item/bucket_bass"));

        spawnEgg("alligator_spawn_egg");
        spawnEgg("bass_spawn_egg");
        spawnEgg("bear_spawn_egg");
        spawnEgg("bluejay_spawn_egg");
        spawnEgg("boar_spawn_egg");
        spawnEgg("butterfly_spawn_egg");
        spawnEgg("canary_spawn_egg");
        spawnEgg("cardinal_spawn_egg");
        spawnEgg("catfish_spawn_egg");
        spawnEgg("caterpillar_spawn_egg");
        spawnEgg("coral_snake_spawn_egg");
        spawnEgg("deer_spawn_egg");
        spawnEgg("dragonfly_spawn_egg");
        spawnEgg("duck_spawn_egg");
        spawnEgg("elephant_spawn_egg");
        spawnEgg("finch_spawn_egg");
        spawnEgg("firefly_spawn_egg");
        spawnEgg("giraffe_spawn_egg");
        spawnEgg("hippo_spawn_egg");
        spawnEgg("lion_spawn_egg");
        spawnEgg("lizard_spawn_egg");
        spawnEgg("rattlesnake_spawn_egg");
        spawnEgg("rhino_spawn_egg");
        spawnEgg("robin_spawn_egg");
        spawnEgg("snake_spawn_egg");
        spawnEgg("snail_spawn_egg");
        spawnEgg("sparrow_spawn_egg");
        spawnEgg("tortoise_spawn_egg");
        spawnEgg("vulture_spawn_egg");
        spawnEgg("zebra_spawn_egg");

        butterflyItem();
        snailItem();
    }

    private void simpleItem(String name) {
        withExistingParent(name, mcLoc("item/generated"))
                .texture("layer0", modLoc("item/" + name));
    }

    private void spawnEgg(String name) {
        withExistingParent(name, mcLoc("item/template_spawn_egg"));
    }

    private void butterflyItem() {
        String[] variants = {"monarch", "clouded_yellow", "swallowtail", "blue_morpho",
                "jade_green_swallowtail", "purple_emperor", "red_admiral"};
        String[] textures = {"butterfly_monarch", "butterfly_clouded_yellow", "butterfly_green_swallowtail",
                "butterfly_blue_morpho", "butterfly_jade_green_swallowtail", "butterfly_purple_emperor",
                "butterfly_red_admiral"};

        ModelFile[] variantModels = new ModelFile[variants.length];
        for (int i = 0; i < variants.length; i++) {
            variantModels[i] = withExistingParent(variants[i], mcLoc("item/generated"))
                    .texture("layer0", modLoc("item/" + textures[i]));
        }

        ItemModelBuilder builder = withExistingParent("butterfly", mcLoc("item/generated"))
                .texture("layer0", modLoc("item/butterfly_monarch"));

        for (int i = 0; i < variants.length; i++) {
            builder.override()
                    .predicate(Identifier.withDefaultNamespace("variant"), i / 7.0f)
                    .model(variantModels[i])
                    .end();
        }
    }

    private void snailItem() {
        String[] colors = {"white", "orange", "magenta", "light_blue", "yellow", "lime",
                "pink", "gray", "light_gray", "cyan", "purple", "blue",
                "brown", "green", "red", "black"};

        ModelFile[] colorModels = new ModelFile[colors.length];
        for (int i = 0; i < colors.length; i++) {
            colorModels[i] = withExistingParent("item/snail/" + colors[i], mcLoc("item/generated"))
                    .texture("layer0", modLoc("item/snail_" + colors[i]));
        }

        ItemModelBuilder builder = withExistingParent("snail", mcLoc("item/generated"))
                .texture("layer0", modLoc("item/snail_brown"));

        for (int i = 0; i < colors.length; i++) {
            builder.override()
                    .predicate(Identifier.withDefaultNamespace("color"), i / 15.0f)
                    .model(colorModels[i])
                    .end();
        }
    }
}
