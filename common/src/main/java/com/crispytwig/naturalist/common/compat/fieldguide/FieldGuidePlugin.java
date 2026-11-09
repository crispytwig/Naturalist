package com.crispytwig.naturalist.common.compat.fieldguide;

import com.crispytwig.naturalist.common.entity.alligator.Alligator;
import com.crispytwig.naturalist.common.entity.fish.Anglerfish;
import com.crispytwig.naturalist.common.entity.ant.Ant;
import com.crispytwig.naturalist.common.entity.fish.Bass;
import com.crispytwig.naturalist.common.entity.bear.Bear;
import com.crispytwig.naturalist.common.entity.bird.Bird;
import com.crispytwig.naturalist.common.entity.bear.BlackBear;
import com.crispytwig.naturalist.common.entity.fish.Blobfish;
import com.crispytwig.naturalist.common.entity.boar.Boar;
import com.crispytwig.naturalist.common.entity.butterfly.Butterfly;
import com.crispytwig.naturalist.common.entity.capybara.Capybara;
import com.crispytwig.naturalist.common.entity.butterfly.Caterpillar;
import com.crispytwig.naturalist.common.entity.fish.Catfish;
import com.crispytwig.naturalist.common.entity.clam.Clam;
import com.crispytwig.naturalist.common.entity.crab.Crab;
import com.crispytwig.naturalist.common.entity.deer.Deer;
import com.crispytwig.naturalist.common.entity.scorpion.DesertScorpion;
import com.crispytwig.naturalist.common.entity.dragonfly.Dragonfly;
import com.crispytwig.naturalist.common.entity.duck.Duck;
import com.crispytwig.naturalist.common.entity.elephant.Elephant;
import com.crispytwig.naturalist.common.entity.firefly.Firefly;
import com.crispytwig.naturalist.common.entity.giantisopod.GiantIsopod;
import com.crispytwig.naturalist.common.entity.giraffe.Giraffe;
import com.crispytwig.naturalist.common.entity.greatwhiteshark.GreatWhiteShark;
import com.crispytwig.naturalist.common.entity.hedgehog.Hedgehog;
import com.crispytwig.naturalist.common.entity.hippo.Hippo;
import com.crispytwig.naturalist.common.entity.jellyfish.Jellyfish;
import com.crispytwig.naturalist.common.entity.scorpion.JungleScorpion;
import com.crispytwig.naturalist.common.entity.komododragon.KomodoDragon;
import com.crispytwig.naturalist.common.entity.lion.Lion;
import com.crispytwig.naturalist.common.entity.lizard.Lizard;
import com.crispytwig.naturalist.common.entity.elephant.Mammoth;
import com.crispytwig.naturalist.common.entity.mole.Mole;
import com.crispytwig.naturalist.common.entity.ostrich.Ostrich;
import com.crispytwig.naturalist.common.entity.fish.Piranha;
import com.crispytwig.naturalist.common.entity.rat.Rat;
import com.crispytwig.naturalist.common.entity.fish.Ray;
import com.crispytwig.naturalist.common.entity.rhino.Rhino;
import com.crispytwig.naturalist.common.entity.snail.Snail;
import com.crispytwig.naturalist.common.entity.snake.Snake;
import com.crispytwig.naturalist.common.entity.starfish.Starfish;
import com.crispytwig.naturalist.common.entity.tiger.Tiger;
import com.crispytwig.naturalist.common.entity.tortoise.Tortoise;
import com.crispytwig.naturalist.common.entity.turkey.Turkey;
import com.crispytwig.naturalist.common.entity.vulture.Vulture;
import com.crispytwig.naturalist.common.entity.whale.Whale;
import com.crispytwig.naturalist.common.entity.manatee.Manatee;
import com.crispytwig.naturalist.common.entity.seahorse.Seahorse;
import com.crispytwig.naturalist.common.entity.equine.Zebra;
import com.evandev.fieldguide.variant.FieldGuideVariantManager;
import net.minecraft.world.entity.Mob;

import java.util.List;

public class FieldGuidePlugin {

    private static final List<Class<? extends Mob>> VARIANT_MOBS = List.of(
            Alligator.class, Anglerfish.class, Ant.class, Bass.class, Bear.class,
            Bird.class, BlackBear.class, Blobfish.class, Boar.class, Butterfly.class,
            Capybara.class, Caterpillar.class, Catfish.class, Clam.class, Crab.class, Deer.class,
            DesertScorpion.class, Dragonfly.class, Duck.class, Elephant.class, Firefly.class,
            GiantIsopod.class, Giraffe.class, GreatWhiteShark.class, Hedgehog.class, Hippo.class, Jellyfish.class,
            JungleScorpion.class, KomodoDragon.class, Lion.class, Lizard.class, Mammoth.class,
            Mole.class, Ostrich.class, Piranha.class, Rat.class, Ray.class,
            Rhino.class, Snail.class, Snake.class, Starfish.class, Tiger.class,
            Tortoise.class, Turkey.class, Vulture.class, Whale.class, Zebra.class, Manatee.class, Seahorse.class
    );

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void register() {
        NaturalistVariantProvider provider = new NaturalistVariantProvider();
        for (Class<? extends Mob> entityClass : VARIANT_MOBS) {
            FieldGuideVariantManager.registerProvider((Class) entityClass, provider);
        }
    }
}
