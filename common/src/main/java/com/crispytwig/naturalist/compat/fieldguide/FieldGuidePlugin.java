package com.crispytwig.naturalist.compat.fieldguide;

import com.crispytwig.naturalist.world.entity.animal.alligator.Alligator;
import com.crispytwig.naturalist.world.entity.animal.fish.Anglerfish;
import com.crispytwig.naturalist.world.entity.animal.ant.Ant;
import com.crispytwig.naturalist.world.entity.animal.fish.Bass;
import com.crispytwig.naturalist.world.entity.animal.bear.Bear;
import com.crispytwig.naturalist.world.entity.animal.bird.Bird;
import com.crispytwig.naturalist.world.entity.animal.bear.BlackBear;
import com.crispytwig.naturalist.world.entity.animal.fish.Blobfish;
import com.crispytwig.naturalist.world.entity.animal.boar.Boar;
import com.crispytwig.naturalist.world.entity.animal.butterfly.Butterfly;
import com.crispytwig.naturalist.world.entity.animal.capybara.Capybara;
import com.crispytwig.naturalist.world.entity.animal.butterfly.Caterpillar;
import com.crispytwig.naturalist.world.entity.animal.fish.Catfish;
import com.crispytwig.naturalist.world.entity.animal.clam.Clam;
import com.crispytwig.naturalist.world.entity.animal.crab.Crab;
import com.crispytwig.naturalist.world.entity.animal.deer.Deer;
import com.crispytwig.naturalist.world.entity.animal.scorpion.DesertScorpion;
import com.crispytwig.naturalist.world.entity.animal.dragonfly.Dragonfly;
import com.crispytwig.naturalist.world.entity.animal.duck.Duck;
import com.crispytwig.naturalist.world.entity.animal.elephant.Elephant;
import com.crispytwig.naturalist.world.entity.animal.firefly.Firefly;
import com.crispytwig.naturalist.world.entity.animal.giantisopod.GiantIsopod;
import com.crispytwig.naturalist.world.entity.animal.giraffe.Giraffe;
import com.crispytwig.naturalist.world.entity.animal.greatwhiteshark.GreatWhiteShark;
import com.crispytwig.naturalist.world.entity.animal.hedgehog.Hedgehog;
import com.crispytwig.naturalist.world.entity.animal.hippo.Hippo;
import com.crispytwig.naturalist.world.entity.animal.jellyfish.Jellyfish;
import com.crispytwig.naturalist.world.entity.animal.scorpion.JungleScorpion;
import com.crispytwig.naturalist.world.entity.animal.komododragon.KomodoDragon;
import com.crispytwig.naturalist.world.entity.animal.lion.Lion;
import com.crispytwig.naturalist.world.entity.animal.lizard.Lizard;
import com.crispytwig.naturalist.world.entity.animal.elephant.Mammoth;
import com.crispytwig.naturalist.world.entity.animal.mole.Mole;
import com.crispytwig.naturalist.world.entity.animal.ostrich.Ostrich;
import com.crispytwig.naturalist.world.entity.animal.fish.Piranha;
import com.crispytwig.naturalist.world.entity.animal.rat.Rat;
import com.crispytwig.naturalist.world.entity.animal.fish.Ray;
import com.crispytwig.naturalist.world.entity.animal.rhino.Rhino;
import com.crispytwig.naturalist.world.entity.animal.snail.Snail;
import com.crispytwig.naturalist.world.entity.animal.snake.Snake;
import com.crispytwig.naturalist.world.entity.animal.starfish.Starfish;
import com.crispytwig.naturalist.world.entity.animal.tiger.Tiger;
import com.crispytwig.naturalist.world.entity.animal.tortoise.Tortoise;
import com.crispytwig.naturalist.world.entity.animal.turkey.Turkey;
import com.crispytwig.naturalist.world.entity.animal.vulture.Vulture;
import com.crispytwig.naturalist.world.entity.animal.whale.Whale;
import com.crispytwig.naturalist.world.entity.animal.equine.Zebra;
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
            Tortoise.class, Turkey.class, Vulture.class, Whale.class, Zebra.class
    );

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void register() {
        NaturalistVariantProvider provider = new NaturalistVariantProvider();
        for (Class<? extends Mob> entityClass : VARIANT_MOBS) {
            FieldGuideVariantManager.registerProvider((Class) entityClass, provider);
        }
    }
}
