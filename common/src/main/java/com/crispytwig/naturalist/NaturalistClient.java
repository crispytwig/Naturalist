package com.crispytwig.naturalist;

import com.crispytwig.naturalist.client.model.animal.alligator.AlligatorBabyModel;
import com.crispytwig.naturalist.client.model.animal.alligator.AlligatorModel;
import com.crispytwig.naturalist.client.model.animal.fish.AnglerfishModel;
import com.crispytwig.naturalist.client.model.animal.ant.AntModel;
import com.crispytwig.naturalist.client.model.animal.fish.BassModel;
import com.crispytwig.naturalist.client.model.animal.bear.BearBabyModel;
import com.crispytwig.naturalist.client.model.animal.bear.BearModel;
import com.crispytwig.naturalist.client.model.animal.bird.BirdBabyModel;
import com.crispytwig.naturalist.client.model.animal.bird.BirdModel;
import com.crispytwig.naturalist.client.model.animal.bear.BlackBearBabyModel;
import com.crispytwig.naturalist.client.model.animal.bear.BlackBearModel;
import com.crispytwig.naturalist.client.model.animal.fish.BlobfishGrayModel;
import com.crispytwig.naturalist.client.model.animal.fish.BlobfishPinkModel;
import com.crispytwig.naturalist.client.model.animal.boar.BoarBabyModel;
import com.crispytwig.naturalist.client.model.animal.boar.BoarModel;
import com.crispytwig.naturalist.client.model.animal.butterfly.ButterflyModel;
import com.crispytwig.naturalist.client.model.animal.capybara.CapybaraBabyModel;
import com.crispytwig.naturalist.client.model.animal.capybara.CapybaraModel;
import com.crispytwig.naturalist.client.model.animal.butterfly.CaterpillarModel;
import com.crispytwig.naturalist.client.model.animal.fish.CatfishModel;
import com.crispytwig.naturalist.client.model.animal.clam.ClamModel;
import com.crispytwig.naturalist.client.model.animal.crab.CrabBabyModel;
import com.crispytwig.naturalist.client.model.animal.crab.CrabModel;
import com.crispytwig.naturalist.client.model.animal.deer.DeerBabyModel;
import com.crispytwig.naturalist.client.model.animal.deer.DeerModel;
import com.crispytwig.naturalist.client.model.animal.scorpion.DesertScorpionModel;
import com.crispytwig.naturalist.client.model.animal.mole.DirtTrailModel;
import com.crispytwig.naturalist.client.model.animal.dragonfly.DragonflyModel;
import com.crispytwig.naturalist.client.model.animal.duck.DuckBabyModel;
import com.crispytwig.naturalist.client.model.animal.duck.DuckModel;
import com.crispytwig.naturalist.client.model.animal.elephant.ElephantBabyModel;
import com.crispytwig.naturalist.client.model.animal.elephant.ElephantModel;
import com.crispytwig.naturalist.client.model.animal.firefly.FireflyBabyModel;
import com.crispytwig.naturalist.client.model.animal.firefly.FireflyModel;
import com.crispytwig.naturalist.client.model.animal.giantisopod.GiantIsopodModel;
import com.crispytwig.naturalist.client.model.animal.giraffe.GiraffeBabyModel;
import com.crispytwig.naturalist.client.model.animal.giraffe.GiraffeModel;
import com.crispytwig.naturalist.client.model.animal.greatwhiteshark.GreatWhiteSharkModel;
import com.crispytwig.naturalist.client.model.animal.hedgehog.HedgehogModel;
import com.crispytwig.naturalist.client.model.animal.hippo.HippoBabyModel;
import com.crispytwig.naturalist.client.model.animal.hippo.HippoModel;
import com.crispytwig.naturalist.client.model.animal.jellyfish.JellyfishModel;
import com.crispytwig.naturalist.client.model.animal.scorpion.JungleScorpionModel;
import com.crispytwig.naturalist.client.model.animal.komododragon.KomodoDragonModel;
import com.crispytwig.naturalist.client.model.animal.fish.LargeBassModel;
import com.crispytwig.naturalist.client.model.animal.lion.LionBabyModel;
import com.crispytwig.naturalist.client.model.animal.lion.LionModel;
import com.crispytwig.naturalist.client.model.animal.lizard.LizardModel;
import com.crispytwig.naturalist.client.model.animal.lizard.LizardTailModel;
import com.crispytwig.naturalist.client.model.animal.elephant.MammothBabyModel;
import com.crispytwig.naturalist.client.model.animal.elephant.MammothModel;
import com.crispytwig.naturalist.client.model.animal.fish.MediumBassModel;
import com.crispytwig.naturalist.client.model.animal.mole.MoleModel;
import com.crispytwig.naturalist.client.model.animal.ostrich.OstrichBabyModel;
import com.crispytwig.naturalist.client.model.animal.ostrich.OstrichModel;
import com.crispytwig.naturalist.client.model.animal.fish.PiranhaModel;
import com.crispytwig.naturalist.client.model.animal.rat.RatModel;
import com.crispytwig.naturalist.client.model.animal.fish.RayModel;
import com.crispytwig.naturalist.client.model.animal.rhino.RhinoModel;
import com.crispytwig.naturalist.client.model.animal.snail.SnailModel;
import com.crispytwig.naturalist.client.model.animal.snake.SnakeModel;
import com.crispytwig.naturalist.client.model.animal.starfish.StarfishModel;
import com.crispytwig.naturalist.client.model.animal.tiger.TigerBabyModel;
import com.crispytwig.naturalist.client.model.animal.tiger.TigerModel;
import com.crispytwig.naturalist.client.model.animal.tortoise.TortoiseBabyModel;
import com.crispytwig.naturalist.client.model.animal.tortoise.TortoiseModel;
import com.crispytwig.naturalist.client.model.animal.turkey.TurkeyModel;
import com.crispytwig.naturalist.client.model.animal.vulture.VultureBabyModel;
import com.crispytwig.naturalist.client.model.animal.vulture.VultureModel;
import com.crispytwig.naturalist.client.model.animal.whale.WhaleBabyModel;
import com.crispytwig.naturalist.client.model.animal.whale.WhaleModel;
import com.crispytwig.naturalist.client.model.animal.equine.ZebraBabyModel;
import com.crispytwig.naturalist.client.model.animal.equine.ZebraModel;
import com.crispytwig.naturalist.client.gui.screens.inventory.ElephantInventoryScreen;
import com.crispytwig.naturalist.client.renderer.entity.AlligatorRenderer;
import com.crispytwig.naturalist.client.renderer.entity.AnglerfishRenderer;
import com.crispytwig.naturalist.client.renderer.entity.AntRenderer;
import com.crispytwig.naturalist.client.renderer.entity.BassRenderer;
import com.crispytwig.naturalist.client.renderer.entity.BearRenderer;
import com.crispytwig.naturalist.client.renderer.entity.BirdRenderer;
import com.crispytwig.naturalist.client.renderer.entity.BlackBearRenderer;
import com.crispytwig.naturalist.client.renderer.entity.BlobfishRenderer;
import com.crispytwig.naturalist.client.renderer.entity.BoarRenderer;
import com.crispytwig.naturalist.client.renderer.entity.ButterflyRenderer;
import com.crispytwig.naturalist.client.renderer.entity.CapybaraRenderer;
import com.crispytwig.naturalist.client.renderer.entity.CarriedFoodRenderer;
import com.crispytwig.naturalist.client.renderer.entity.CaterpillarRenderer;
import com.crispytwig.naturalist.client.renderer.entity.CatfishRenderer;
import com.crispytwig.naturalist.client.renderer.entity.ClamRenderer;
import com.crispytwig.naturalist.client.renderer.entity.CrabRenderer;
import com.crispytwig.naturalist.client.renderer.entity.DeerRenderer;
import com.crispytwig.naturalist.client.renderer.entity.DesertScorpionRenderer;
import com.crispytwig.naturalist.client.renderer.entity.DirtTrailRenderer;
import com.crispytwig.naturalist.client.renderer.entity.DragonflyRenderer;
import com.crispytwig.naturalist.client.renderer.entity.DuckRenderer;
import com.crispytwig.naturalist.client.renderer.entity.ElephantRenderer;
import com.crispytwig.naturalist.client.renderer.entity.FireflyRenderer;
import com.crispytwig.naturalist.client.renderer.entity.GiantIsopodRenderer;
import com.crispytwig.naturalist.client.renderer.entity.GiraffeRenderer;
import com.crispytwig.naturalist.client.renderer.entity.GreatWhiteSharkRenderer;
import com.crispytwig.naturalist.client.renderer.entity.HedgehogRenderer;
import com.crispytwig.naturalist.client.renderer.entity.HippoRenderer;
import com.crispytwig.naturalist.client.renderer.entity.JellyfishRenderer;
import com.crispytwig.naturalist.client.renderer.entity.JungleScorpionRenderer;
import com.crispytwig.naturalist.client.renderer.entity.KomodoDragonRenderer;
import com.crispytwig.naturalist.client.renderer.entity.LionRenderer;
import com.crispytwig.naturalist.client.renderer.entity.LizardRenderer;
import com.crispytwig.naturalist.client.renderer.entity.LizardTailRenderer;
import com.crispytwig.naturalist.client.renderer.entity.MammothRenderer;
import com.crispytwig.naturalist.client.renderer.entity.MoleRenderer;
import com.crispytwig.naturalist.client.renderer.entity.OstrichRenderer;
import com.crispytwig.naturalist.client.renderer.entity.PiranhaRenderer;
import com.crispytwig.naturalist.client.renderer.entity.RatRenderer;
import com.crispytwig.naturalist.client.renderer.entity.RayRenderer;
import com.crispytwig.naturalist.client.renderer.entity.RhinoRenderer;
import com.crispytwig.naturalist.client.renderer.entity.SnailRenderer;
import com.crispytwig.naturalist.client.renderer.blockentity.SnailShellRenderer;
import com.crispytwig.naturalist.client.renderer.entity.SnakeRenderer;
import com.crispytwig.naturalist.client.renderer.entity.StarfishRenderer;
import com.crispytwig.naturalist.client.renderer.entity.TigerRenderer;
import com.crispytwig.naturalist.client.renderer.entity.TortoiseRenderer;
import com.crispytwig.naturalist.client.renderer.entity.TurkeyRenderer;
import com.crispytwig.naturalist.client.renderer.entity.VultureRenderer;
import com.crispytwig.naturalist.client.renderer.entity.WhaleRenderer;
import com.crispytwig.naturalist.client.renderer.entity.ZebraRenderer;
import com.crispytwig.naturalist.registry.NaturalistBlockEntities;
import com.crispytwig.naturalist.registry.NaturalistEntityTypes;
import com.crispytwig.naturalist.registry.NaturalistMenus;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import com.crispytwig.naturalist.client.renderer.item.properties.conditional.KnapsackFilled;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.resources.Identifier;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public final class NaturalistClient {
    private NaturalistClient() {
    }

    @FunctionalInterface
    public interface LayerRegistrar {
        void register(ModelLayerLocation location, Supplier<LayerDefinition> definition);
    }

    public static void registerLayerDefinitions(LayerRegistrar r) {
        r.register(AlligatorBabyModel.LAYER_LOCATION, AlligatorBabyModel::createBodyLayer);
        r.register(AlligatorModel.LAYER_LOCATION, AlligatorModel::createBodyLayer);
        r.register(AnglerfishModel.LAYER_LOCATION, AnglerfishModel::createBodyLayer);
        r.register(AntModel.LAYER_LOCATION, AntModel::createBodyLayer);
        r.register(BassModel.LAYER_LOCATION, BassModel::createBodyLayer);
        r.register(MediumBassModel.LAYER_LOCATION, MediumBassModel::createBodyLayer);
        r.register(LargeBassModel.LAYER_LOCATION, LargeBassModel::createBodyLayer);
        r.register(BearBabyModel.LAYER_LOCATION, BearBabyModel::createBodyLayer);
        r.register(BearModel.LAYER_LOCATION, BearModel::createBodyLayer);
        r.register(BirdBabyModel.LAYER_LOCATION, BirdBabyModel::createBodyLayer);
        r.register(BirdModel.LAYER_LOCATION, BirdModel::createBodyLayer);
        r.register(BlackBearBabyModel.LAYER_LOCATION, BlackBearBabyModel::createBodyLayer);
        r.register(BlackBearModel.LAYER_LOCATION, BlackBearModel::createBodyLayer);
        r.register(BlobfishGrayModel.LAYER_LOCATION, BlobfishGrayModel::createBodyLayer);
        r.register(BlobfishPinkModel.LAYER_LOCATION, BlobfishPinkModel::createBodyLayer);
        r.register(BoarBabyModel.LAYER_LOCATION, BoarBabyModel::createBodyLayer);
        r.register(BoarModel.LAYER_LOCATION, BoarModel::createBodyLayer);
        r.register(ButterflyModel.LAYER_LOCATION, ButterflyModel::createBodyLayer);
        r.register(CapybaraBabyModel.LAYER_LOCATION, CapybaraBabyModel::createBodyLayer);
        r.register(CapybaraModel.LAYER_LOCATION, CapybaraModel::createBodyLayer);
        r.register(CaterpillarModel.LAYER_LOCATION, CaterpillarModel::createBodyLayer);
        r.register(CatfishModel.LAYER_LOCATION, CatfishModel::createBodyLayer);
        r.register(ClamModel.LAYER_LOCATION, ClamModel::createBodyLayer);
        r.register(CrabBabyModel.LAYER_LOCATION, CrabBabyModel::createBodyLayer);
        r.register(HedgehogModel.LAYER_LOCATION, HedgehogModel::createBodyLayer);
        r.register(CrabModel.LAYER_LOCATION, CrabModel::createBodyLayer);
        r.register(DeerBabyModel.LAYER_LOCATION, DeerBabyModel::createBodyLayer);
        r.register(DeerModel.LAYER_LOCATION, DeerModel::createBodyLayer);
        r.register(DesertScorpionModel.LAYER_LOCATION, DesertScorpionModel::createBodyLayer);
        r.register(DirtTrailModel.LAYER_LOCATION, DirtTrailModel::createBodyLayer);
        r.register(DragonflyModel.LAYER_LOCATION, DragonflyModel::createBodyLayer);
        r.register(DuckBabyModel.LAYER_LOCATION, DuckBabyModel::createBodyLayer);
        r.register(DuckModel.LAYER_LOCATION, DuckModel::createBodyLayer);
        r.register(ElephantBabyModel.LAYER_LOCATION, ElephantBabyModel::createBodyLayer);
        r.register(ElephantModel.LAYER_LOCATION, ElephantModel::createBodyLayer);
        r.register(FireflyBabyModel.LAYER_LOCATION, FireflyBabyModel::createBodyLayer);
        r.register(FireflyModel.LAYER_LOCATION, FireflyModel::createBodyLayer);
        r.register(GiantIsopodModel.LAYER_LOCATION, GiantIsopodModel::createBodyLayer);
        r.register(GiraffeBabyModel.LAYER_LOCATION, GiraffeBabyModel::createBodyLayer);
        r.register(GiraffeModel.LAYER_LOCATION, GiraffeModel::createBodyLayer);
        r.register(GreatWhiteSharkModel.LAYER_LOCATION, GreatWhiteSharkModel::createBodyLayer);
        r.register(HippoBabyModel.LAYER_LOCATION, HippoBabyModel::createBodyLayer);
        r.register(HippoModel.LAYER_LOCATION, HippoModel::createBodyLayer);
        r.register(JellyfishModel.LAYER_LOCATION, JellyfishModel::createBodyLayer);
        r.register(JungleScorpionModel.LAYER_LOCATION, JungleScorpionModel::createBodyLayer);
        r.register(KomodoDragonModel.LAYER_LOCATION, KomodoDragonModel::createBodyLayer);
        r.register(LionBabyModel.LAYER_LOCATION, LionBabyModel::createBodyLayer);
        r.register(LionModel.LAYER_LOCATION, LionModel::createBodyLayer);
        r.register(LizardModel.LAYER_LOCATION, LizardModel::createBodyLayer);
        r.register(LizardTailModel.LAYER_LOCATION, LizardTailModel::createBodyLayer);
        r.register(MammothBabyModel.LAYER_LOCATION, MammothBabyModel::createBodyLayer);
        r.register(MammothModel.LAYER_LOCATION, MammothModel::createBodyLayer);
        r.register(MoleModel.LAYER_LOCATION, MoleModel::createBodyLayer);
        r.register(OstrichBabyModel.LAYER_LOCATION, OstrichBabyModel::createBodyLayer);
        r.register(OstrichModel.LAYER_LOCATION, OstrichModel::createBodyLayer);
        r.register(PiranhaModel.LAYER_LOCATION, PiranhaModel::createBodyLayer);
        r.register(RatModel.LAYER_LOCATION, RatModel::createBodyLayer);
        r.register(RayModel.LAYER_LOCATION, RayModel::createBodyLayer);
        r.register(RhinoModel.LAYER_LOCATION, RhinoModel::createBodyLayer);
        r.register(SnailModel.LAYER_LOCATION, SnailModel::createBodyLayer);
        r.register(SnakeModel.LAYER_LOCATION, SnakeModel::createBodyLayer);
        r.register(StarfishModel.LAYER_LOCATION, StarfishModel::createBodyLayer);
        r.register(TigerBabyModel.LAYER_LOCATION, TigerBabyModel::createBodyLayer);
        r.register(TigerModel.LAYER_LOCATION, TigerModel::createBodyLayer);
        r.register(TortoiseBabyModel.LAYER_LOCATION, TortoiseBabyModel::createBodyLayer);
        r.register(TortoiseModel.LAYER_LOCATION, TortoiseModel::createBodyLayer);
        r.register(TurkeyModel.LAYER_LOCATION, TurkeyModel::createBodyLayer);
        r.register(VultureBabyModel.LAYER_LOCATION, VultureBabyModel::createBodyLayer);
        r.register(VultureModel.LAYER_LOCATION, VultureModel::createBodyLayer);
        r.register(WhaleBabyModel.LAYER_LOCATION, WhaleBabyModel::createBodyLayer);
        r.register(WhaleModel.LAYER_LOCATION, WhaleModel::createBodyLayer);
        r.register(ZebraBabyModel.LAYER_LOCATION, ZebraBabyModel::createBodyLayer);
        r.register(ZebraModel.LAYER_LOCATION, ZebraModel::createBodyLayer);
    }

    @FunctionalInterface
    public interface RendererRegistrar {
        <T extends Entity> void register(EntityType<? extends T> type, EntityRendererProvider<T> provider);
    }

    public static void registerRenderers(RendererRegistrar r) {
        r.register(NaturalistEntityTypes.SNAIL.get(), SnailRenderer::new);
        r.register(NaturalistEntityTypes.BEAR.get(), BearRenderer::new);
        r.register(NaturalistEntityTypes.BUTTERFLY.get(), ButterflyRenderer::new);
        r.register(NaturalistEntityTypes.FIREFLY.get(), FireflyRenderer::new);
        r.register(NaturalistEntityTypes.SNAKE.get(), SnakeRenderer::new);
        r.register(NaturalistEntityTypes.CRAB.get(), CrabRenderer::new);
        r.register(NaturalistEntityTypes.DEER.get(), DeerRenderer::new);
        r.register(NaturalistEntityTypes.BIRD.get(), BirdRenderer::new);
        r.register(NaturalistEntityTypes.CATERPILLAR.get(), CaterpillarRenderer::new);
        r.register(NaturalistEntityTypes.RHINO.get(), RhinoRenderer::new);
        r.register(NaturalistEntityTypes.LION.get(), LionRenderer::new);
        r.register(NaturalistEntityTypes.ELEPHANT.get(), ElephantRenderer::new);
        r.register(NaturalistEntityTypes.MAMMOTH.get(), MammothRenderer::new);
        r.register(NaturalistEntityTypes.ZEBRA.get(), ZebraRenderer::new);
        r.register(NaturalistEntityTypes.GIRAFFE.get(), GiraffeRenderer::new);
        r.register(NaturalistEntityTypes.HIPPO.get(), HippoRenderer::new);
        r.register(NaturalistEntityTypes.VULTURE.get(), VultureRenderer::new);
        r.register(NaturalistEntityTypes.BOAR.get(), BoarRenderer::new);
        r.register(NaturalistEntityTypes.DRAGONFLY.get(), DragonflyRenderer::new);
        r.register(NaturalistEntityTypes.CATFISH.get(), CatfishRenderer::new);
        r.register(NaturalistEntityTypes.ALLIGATOR.get(), AlligatorRenderer::new);
        r.register(NaturalistEntityTypes.BASS.get(), BassRenderer::new);
        r.register(NaturalistEntityTypes.LIZARD.get(), LizardRenderer::new);
        r.register(NaturalistEntityTypes.LIZARD_TAIL.get(), LizardTailRenderer::new);
        r.register(NaturalistEntityTypes.TORTOISE.get(), TortoiseRenderer::new);
        r.register(NaturalistEntityTypes.DUCK.get(), DuckRenderer::new);
        r.register(NaturalistEntityTypes.DUCK_EGG.get(), ThrownItemRenderer::new);
        r.register(NaturalistEntityTypes.STARFISH.get(), StarfishRenderer::new);
        r.register(NaturalistEntityTypes.CLAM.get(), ClamRenderer::new);
        r.register(NaturalistEntityTypes.GIANT_ISOPOD.get(), GiantIsopodRenderer::new);
        r.register(NaturalistEntityTypes.JELLYFISH.get(), JellyfishRenderer::new);
        r.register(NaturalistEntityTypes.ANGLERFISH.get(), AnglerfishRenderer::new);
        r.register(NaturalistEntityTypes.RAY.get(), RayRenderer::new);
        r.register(NaturalistEntityTypes.BLOBFISH.get(), BlobfishRenderer::new);
        r.register(NaturalistEntityTypes.PIRANHA.get(), PiranhaRenderer::new);
        r.register(NaturalistEntityTypes.WHALE.get(), WhaleRenderer::new);
        r.register(NaturalistEntityTypes.ANT.get(), AntRenderer::new);
        r.register(NaturalistEntityTypes.CARRIED_FOOD.get(), CarriedFoodRenderer::new);
        r.register(NaturalistEntityTypes.MOLE.get(), MoleRenderer::new);
        r.register(NaturalistEntityTypes.DIRT_TRAIL.get(), DirtTrailRenderer::new);
        r.register(NaturalistEntityTypes.RAT.get(), RatRenderer::new);
        r.register(NaturalistEntityTypes.BLACK_BEAR.get(), BlackBearRenderer::new);
        r.register(NaturalistEntityTypes.TIGER.get(), TigerRenderer::new);
        r.register(NaturalistEntityTypes.KOMODO_DRAGON.get(), KomodoDragonRenderer::new);
        r.register(NaturalistEntityTypes.OSTRICH.get(), OstrichRenderer::new);
        r.register(NaturalistEntityTypes.DESERT_SCORPION.get(), DesertScorpionRenderer::new);
        r.register(NaturalistEntityTypes.JUNGLE_SCORPION.get(), JungleScorpionRenderer::new);
        r.register(NaturalistEntityTypes.GREAT_WHITE_SHARK.get(), GreatWhiteSharkRenderer::new);
        r.register(NaturalistEntityTypes.TURKEY.get(), TurkeyRenderer::new);
        r.register(NaturalistEntityTypes.CAPYBARA.get(), CapybaraRenderer::new);
        r.register(NaturalistEntityTypes.HEDGEHOG.get(), HedgehogRenderer::new);
    }

    @FunctionalInterface
    public interface MenuScreenRegistrar {
        <M extends AbstractContainerMenu, U extends Screen & MenuAccess<M>> void register(MenuType<? extends M> type, MenuScreens.ScreenConstructor<M, U> factory);
    }

    public static void registerMenuScreens(MenuScreenRegistrar r) {
        r.register(NaturalistMenus.ELEPHANT.get(), ElephantInventoryScreen::new);
    }

    @FunctionalInterface
    public interface BlockEntityRendererRegistrar {
        <T extends BlockEntity, S extends BlockEntityRenderState> void register(BlockEntityType<? extends T> type, BlockEntityRendererProvider<T, S> provider);
    }

    public static void registerBlockEntityRenderers(BlockEntityRendererRegistrar r) {
        r.register(NaturalistBlockEntities.SNAIL_SHELL.get(), SnailShellRenderer::new);
    }

    @FunctionalInterface
    public interface ConditionalItemModelPropertyRegistrar {
        void register(Identifier id, MapCodec<? extends ConditionalItemModelProperty> codec);
    }

    public static void registerConditionalItemModelProperties(ConditionalItemModelPropertyRegistrar r) {
        r.register(KnapsackFilled.ID, KnapsackFilled.MAP_CODEC);
    }
}
