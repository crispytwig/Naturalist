package com.crispytwig.naturalist.datagen;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.registry.NaturalistSoundEvents;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;

public class NaturalistSoundDefinitionsProvider extends SoundDefinitionsProvider {
    public NaturalistSoundDefinitionsProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Naturalist.MOD_ID, existingFileHelper);
    }

    @Override
    public void registerSounds() {
        add(NaturalistSoundEvents.GIANT_ISOPOD_ROLL, definition()
                .subtitle("naturalist.subtitles.entity.giant_isopod.roll")
                .with(
                        sound("naturalist:mob/giant_isopod/hide1"),
                        sound("naturalist:mob/giant_isopod/hide2"),
                        sound("naturalist:mob/giant_isopod/hide3")));

        add(NaturalistSoundEvents.JELLYFISH_IDLE, definition()
                .subtitle("naturalist.subtitles.entity.jellyfish.idle")
                .with(
                        sound("naturalist:mob/jellyfish/idle1"),
                        sound("naturalist:mob/jellyfish/idle2")));

        add(NaturalistSoundEvents.JELLYFISH_SWIM, definition()
                .subtitle("naturalist.subtitles.entity.jellyfish.swim")
                .with(
                        sound("naturalist:mob/jellyfish/swim1"),
                        sound("naturalist:mob/jellyfish/swim2")));

        add(NaturalistSoundEvents.JELLYFISH_HURT, definition()
                .subtitle("naturalist.subtitles.entity.jellyfish.hurt")
                .with(
                        sound("naturalist:mob/jellyfish/hurt1"),
                        sound("naturalist:mob/jellyfish/hurt2"),
                        sound("naturalist:mob/jellyfish/hurt3")));

        add(NaturalistSoundEvents.JELLYFISH_DEATH, definition()
                .subtitle("naturalist.subtitles.entity.jellyfish.death")
                .with(
                        sound("naturalist:mob/jellyfish/death1"),
                        sound("naturalist:mob/jellyfish/death2")));

        add(NaturalistSoundEvents.ANGLERFISH_IDLE, definition()
                .subtitle("naturalist.subtitles.entity.anglerfish.idle")
                .with(
                        sound("naturalist:mob/anglerfish/idle1"),
                        sound("naturalist:mob/anglerfish/idle2"),
                        sound("naturalist:mob/anglerfish/idle3"),
                        sound("naturalist:mob/anglerfish/idle4"),
                        sound("naturalist:mob/anglerfish/idle5")));

        add(NaturalistSoundEvents.ANGLERFISH_SWIM, definition()
                .subtitle("naturalist.subtitles.entity.anglerfish.swim")
                .with(
                        sound("naturalist:mob/anglerfish/swim1"),
                        sound("naturalist:mob/anglerfish/swim2"),
                        sound("naturalist:mob/anglerfish/swim3"),
                        sound("naturalist:mob/anglerfish/swim4")));

        add(NaturalistSoundEvents.ANGLERFISH_HURT, definition()
                .subtitle("naturalist.subtitles.entity.anglerfish.hurt")
                .with(
                        sound("naturalist:mob/anglerfish/hurt1"),
                        sound("naturalist:mob/anglerfish/hurt2")));

        add(NaturalistSoundEvents.ANGLERFISH_DEATH, definition()
                .subtitle("naturalist.subtitles.entity.anglerfish.death")
                .with(
                        sound("naturalist:mob/anglerfish/death1"),
                        sound("naturalist:mob/anglerfish/death2")));

        add(NaturalistSoundEvents.WHALE_AMBIENT, definition()
                .subtitle("naturalist.subtitles.entity.whale.ambient")
                .with(
                        sound("naturalist:mob/whale/idle2"),
                        sound("naturalist:mob/whale/idle3"),
                        sound("naturalist:mob/whale/idle6")));

        add(NaturalistSoundEvents.WHALE_HURT, definition()
                .subtitle("naturalist.subtitles.entity.whale.hurt")
                .with(
                        sound("naturalist:mob/whale/hurt1"),
                        sound("naturalist:mob/whale/hurt2"),
                        sound("naturalist:mob/whale/hurt3"),
                        sound("naturalist:mob/whale/hurt4")));

        add(NaturalistSoundEvents.WHALE_DEATH, definition()
                .subtitle("naturalist.subtitles.entity.whale.death")
                .with(
                        sound("naturalist:mob/whale/death1"),
                        sound("naturalist:mob/whale/death2")));

        add(NaturalistSoundEvents.WHALE_BLOWHOLE, definition()
                .subtitle("naturalist.subtitles.entity.whale.blowhole")
                .with(
                        sound("naturalist:mob/whale/blowhole1"),
                        sound("naturalist:mob/whale/blowhole2"),
                        sound("naturalist:mob/whale/blowhole3"),
                        sound("naturalist:mob/whale/blowhole4")));

        add(NaturalistSoundEvents.ANT_AMBIENT, definition()
                .subtitle("naturalist.subtitles.entity.ant.ambient")
                .with(
                        sound("naturalist:mob/ant/idle1"),
                        sound("naturalist:mob/ant/idle2"),
                        sound("naturalist:mob/ant/idle3")));

        add(NaturalistSoundEvents.ANT_HURT, definition()
                .subtitle("naturalist.subtitles.entity.ant.hurt")
                .with(
                        sound("naturalist:mob/ant/hurt1"),
                        sound("naturalist:mob/ant/hurt2"),
                        sound("naturalist:mob/ant/hurt3")));

        add(NaturalistSoundEvents.ANT_DEATH, definition()
                .subtitle("naturalist.subtitles.entity.ant.death")
                .with(
                        sound("naturalist:mob/ant/death1"),
                        sound("naturalist:mob/ant/death2"),
                        sound("naturalist:mob/ant/death3")));

        add(NaturalistSoundEvents.ANT_STEP, definition()
                .subtitle("subtitles.block.generic.footsteps")
                .with(
                        sound("naturalist:mob/ant/step1"),
                        sound("naturalist:mob/ant/step2"),
                        sound("naturalist:mob/ant/step3"),
                        sound("naturalist:mob/ant/step4")));

        add(NaturalistSoundEvents.MOLE_AMBIENT, definition()
                .subtitle("naturalist.subtitles.entity.mole.ambient")
                .with(
                        sound("naturalist:mob/mole/idle1"),
                        sound("naturalist:mob/mole/idle2"),
                        sound("naturalist:mob/mole/idle3")));

        add(NaturalistSoundEvents.MOLE_HURT, definition()
                .subtitle("naturalist.subtitles.entity.mole.hurt")
                .with(
                        sound("naturalist:mob/mole/hurt1"),
                        sound("naturalist:mob/mole/hurt2"),
                        sound("naturalist:mob/mole/hurt3")));

        add(NaturalistSoundEvents.MOLE_DEATH, definition()
                .subtitle("naturalist.subtitles.entity.mole.death")
                .with(
                        sound("naturalist:mob/mole/death1"),
                        sound("naturalist:mob/mole/death2"),
                        sound("naturalist:mob/mole/death3")));

        add(NaturalistSoundEvents.MOLE_DIG_DOWN, definition()
                .subtitle("naturalist.subtitles.entity.mole.dig_down")
                .with(
                        sound("naturalist:mob/mole/dig_down1"),
                        sound("naturalist:mob/mole/dig_down2"),
                        sound("naturalist:mob/mole/dig_down3")));

        add(NaturalistSoundEvents.MOLE_DIG_UP, definition()
                .subtitle("naturalist.subtitles.entity.mole.dig_up")
                .with(
                        sound("naturalist:mob/mole/dig_up1"),
                        sound("naturalist:mob/mole/dig_up2"),
                        sound("naturalist:mob/mole/dig_up3")));

        add(NaturalistSoundEvents.MOLE_PEEK, definition()
                .subtitle("naturalist.subtitles.entity.mole.peek")
                .with(
                        sound("naturalist:mob/mole/peek1"),
                        sound("naturalist:mob/mole/peek2"),
                        sound("naturalist:mob/mole/peek3")));

        add(NaturalistSoundEvents.MOLE_STEP, definition()
                .subtitle("subtitles.block.generic.footsteps")
                .with(
                        sound("naturalist:mob/mole/step1"),
                        sound("naturalist:mob/mole/step2"),
                        sound("naturalist:mob/mole/step3"),
                        sound("naturalist:mob/mole/step4"),
                        sound("naturalist:mob/mole/step5"),
                        sound("naturalist:mob/mole/step6")));

        add(NaturalistSoundEvents.RAT_AMBIENT, definition()
                .subtitle("naturalist.subtitles.entity.rat.ambient")
                .with(
                        sound("naturalist:mob/rat/idle1"),
                        sound("naturalist:mob/rat/idle2"),
                        sound("naturalist:mob/rat/idle3")));

        add(NaturalistSoundEvents.RAT_HURT, definition()
                .subtitle("naturalist.subtitles.entity.rat.hurt")
                .with(
                        sound("naturalist:mob/rat/hurt1"),
                        sound("naturalist:mob/rat/hurt2"),
                        sound("naturalist:mob/rat/hurt3")));

        add(NaturalistSoundEvents.RAT_DEATH, definition()
                .subtitle("naturalist.subtitles.entity.rat.death")
                .with(
                        sound("naturalist:mob/rat/death1"),
                        sound("naturalist:mob/rat/death2"),
                        sound("naturalist:mob/rat/death3")));

        add(NaturalistSoundEvents.RAT_SWIM, definition()
                .subtitle("naturalist.subtitles.entity.rat.swim")
                .with(sound("minecraft:entity.generic.swim", SoundDefinition.SoundType.EVENT)));

        add(NaturalistSoundEvents.RAT_STEP, definition()
                .subtitle("subtitles.block.generic.footsteps")
                .with(
                        sound("naturalist:mob/rat/step1"),
                        sound("naturalist:mob/rat/step2"),
                        sound("naturalist:mob/rat/step3"),
                        sound("naturalist:mob/rat/step4"),
                        sound("naturalist:mob/rat/step5"),
                        sound("naturalist:mob/rat/step6")));

        add(NaturalistSoundEvents.BLACK_BEAR_AMBIENT, definition()
                .subtitle("naturalist.subtitles.entity.black_bear.ambient")
                .with(
                        sound("naturalist:mob/black_bear/idle1"),
                        sound("naturalist:mob/black_bear/idle2"),
                        sound("naturalist:mob/black_bear/idle3")));

        add(NaturalistSoundEvents.BLACK_BEAR_HURT, definition()
                .subtitle("naturalist.subtitles.entity.black_bear.hurt")
                .with(
                        sound("naturalist:mob/black_bear/hurt1"),
                        sound("naturalist:mob/black_bear/hurt2"),
                        sound("naturalist:mob/black_bear/hurt3")));

        add(NaturalistSoundEvents.BLACK_BEAR_DEATH, definition()
                .subtitle("naturalist.subtitles.entity.black_bear.death")
                .with(
                        sound("naturalist:mob/black_bear/death1"),
                        sound("naturalist:mob/black_bear/death2"),
                        sound("naturalist:mob/black_bear/death3")));

        add(NaturalistSoundEvents.BLACK_BEAR_SLEEP, definition()
                .subtitle("naturalist.subtitles.entity.black_bear.sleep")
                .with(
                        sound("naturalist:mob/black_bear/sleep1"),
                        sound("naturalist:mob/black_bear/sleep2")));

        add(NaturalistSoundEvents.BLACK_BEAR_EAT, definition()
                .subtitle("naturalist.subtitles.entity.black_bear.eat")
                .with(
                        sound("naturalist:mob/black_bear/eat1"),
                        sound("naturalist:mob/black_bear/eat2")));

        add(NaturalistSoundEvents.BLACK_BEAR_SNIFF, definition()
                .subtitle("naturalist.subtitles.entity.black_bear.sniff")
                .with(
                        sound("naturalist:mob/black_bear/sniff1"),
                        sound("naturalist:mob/black_bear/sniff2")));

        add(NaturalistSoundEvents.BLACK_BEAR_ATTACK, definition()
                .subtitle("naturalist.subtitles.entity.black_bear.attack")
                .with(
                        sound("naturalist:mob/black_bear/attack1"),
                        sound("naturalist:mob/black_bear/attack2"),
                        sound("naturalist:mob/black_bear/attack3")));

        add(NaturalistSoundEvents.TIGER_AMBIENT, definition()
                .subtitle("naturalist.subtitles.entity.tiger.ambient")
                .with(
                        sound("naturalist:mob/tiger/idle1"),
                        sound("naturalist:mob/tiger/idle2"),
                        sound("naturalist:mob/tiger/idle3")));

        add(NaturalistSoundEvents.TIGER_HURT, definition()
                .subtitle("naturalist.subtitles.entity.tiger.hurt")
                .with(
                        sound("naturalist:mob/tiger/hurt1"),
                        sound("naturalist:mob/tiger/hurt2"),
                        sound("naturalist:mob/tiger/hurt3")));

        add(NaturalistSoundEvents.TIGER_DEATH, definition()
                .subtitle("naturalist.subtitles.entity.tiger.death")
                .with(
                        sound("naturalist:mob/tiger/death1"),
                        sound("naturalist:mob/tiger/death2")));

        add(NaturalistSoundEvents.TIGER_ATTACK, definition()
                .subtitle("naturalist.subtitles.entity.tiger.attack")
                .with(
                        sound("naturalist:mob/tiger/attack1"),
                        sound("naturalist:mob/tiger/attack2")));

        add(NaturalistSoundEvents.TIGER_SLEEP, definition()
                .subtitle("naturalist.subtitles.entity.tiger.sleep")
                .with(
                        sound("naturalist:mob/tiger/sleep1"),
                        sound("naturalist:mob/tiger/sleep2")));

        add(NaturalistSoundEvents.TIGER_PREY, definition()
                .subtitle("naturalist.subtitles.entity.tiger.prey")
                .with(
                        sound("naturalist:mob/tiger/prey1"),
                        sound("naturalist:mob/tiger/prey2")));

        add(NaturalistSoundEvents.TIGER_STEP, definition()
                .subtitle("subtitles.block.generic.footsteps")
                .with(
                        sound("naturalist:mob/tiger/step1"),
                        sound("naturalist:mob/tiger/step2"),
                        sound("naturalist:mob/tiger/step3"),
                        sound("naturalist:mob/tiger/step4")));

        add(NaturalistSoundEvents.KOMODO_DRAGON_AMBIENT, definition()
                .subtitle("naturalist.subtitles.entity.komodo_dragon.ambient")
                .with(
                        sound("naturalist:mob/komodo_dragon/idle1"),
                        sound("naturalist:mob/komodo_dragon/idle2"),
                        sound("naturalist:mob/komodo_dragon/idle3")));

        add(NaturalistSoundEvents.KOMODO_DRAGON_HURT, definition()
                .subtitle("naturalist.subtitles.entity.komodo_dragon.hurt")
                .with(
                        sound("naturalist:mob/komodo_dragon/hurt1"),
                        sound("naturalist:mob/komodo_dragon/hurt2"),
                        sound("naturalist:mob/komodo_dragon/hurt3")));

        add(NaturalistSoundEvents.KOMODO_DRAGON_DEATH, definition()
                .subtitle("naturalist.subtitles.entity.komodo_dragon.death")
                .with(
                        sound("naturalist:mob/komodo_dragon/death1"),
                        sound("naturalist:mob/komodo_dragon/death2"),
                        sound("naturalist:mob/komodo_dragon/death3")));

        add(NaturalistSoundEvents.KOMODO_DRAGON_ATTACK, definition()
                .subtitle("naturalist.subtitles.entity.komodo_dragon.attack")
                .with(
                        sound("naturalist:mob/komodo_dragon/attack1"),
                        sound("naturalist:mob/komodo_dragon/attack2"),
                        sound("naturalist:mob/komodo_dragon/attack3")));

        add(NaturalistSoundEvents.KOMODO_DRAGON_STEP, definition()
                .subtitle("subtitles.block.generic.footsteps")
                .with(
                        sound("naturalist:mob/komodo_dragon/step1"),
                        sound("naturalist:mob/komodo_dragon/step2"),
                        sound("naturalist:mob/komodo_dragon/step3"),
                        sound("naturalist:mob/komodo_dragon/step4")));

        add(NaturalistSoundEvents.OSTRICH_AMBIENT, definition()
                .subtitle("naturalist.subtitles.entity.ostrich.ambient")
                .with(
                        sound("naturalist:mob/ostrich/idle1"),
                        sound("naturalist:mob/ostrich/idle2"),
                        sound("naturalist:mob/ostrich/idle3")));

        add(NaturalistSoundEvents.OSTRICH_HURT, definition()
                .subtitle("naturalist.subtitles.entity.ostrich.hurt")
                .with(
                        sound("naturalist:mob/ostrich/hurt1"),
                        sound("naturalist:mob/ostrich/hurt2"),
                        sound("naturalist:mob/ostrich/hurt3")));

        add(NaturalistSoundEvents.OSTRICH_DEATH, definition()
                .subtitle("naturalist.subtitles.entity.ostrich.death")
                .with(
                        sound("naturalist:mob/ostrich/death1"),
                        sound("naturalist:mob/ostrich/death2")));

        add(NaturalistSoundEvents.OSTRICH_ATTACK, definition()
                .subtitle("naturalist.subtitles.entity.ostrich.attack")
                .with(
                        sound("naturalist:mob/ostrich/attack1"),
                        sound("naturalist:mob/ostrich/attack2")));

        add(NaturalistSoundEvents.OSTRICH_BURY, definition()
                .subtitle("naturalist.subtitles.entity.ostrich.bury")
                .with(
                        sound("naturalist:mob/ostrich/bury1"),
                        sound("naturalist:mob/ostrich/bury2"),
                        sound("naturalist:mob/ostrich/bury3")));

        add(NaturalistSoundEvents.OSTRICH_AMBIENT_BABY, definition()
                .subtitle("naturalist.subtitles.entity.ostrich.ambient")
                .with(
                        sound("naturalist:mob/ostrich/baby_idle1"),
                        sound("naturalist:mob/ostrich/baby_idle2"),
                        sound("naturalist:mob/ostrich/baby_idle3")));

        add(NaturalistSoundEvents.OSTRICH_HURT_BABY, definition()
                .subtitle("naturalist.subtitles.entity.ostrich.hurt")
                .with(
                        sound("naturalist:mob/ostrich/baby_hurt1"),
                        sound("naturalist:mob/ostrich/baby_hurt2"),
                        sound("naturalist:mob/ostrich/baby_hurt3")));

        add(NaturalistSoundEvents.OSTRICH_DEATH_BABY, definition()
                .subtitle("naturalist.subtitles.entity.ostrich.death")
                .with(
                        sound("naturalist:mob/ostrich/baby_death1"),
                        sound("naturalist:mob/ostrich/baby_death2")));

        add(NaturalistSoundEvents.OSTRICH_STEP, definition()
                .subtitle("subtitles.block.generic.footsteps")
                .with(
                        sound("naturalist:mob/ostrich/step1"),
                        sound("naturalist:mob/ostrich/step2"),
                        sound("naturalist:mob/ostrich/step3"),
                        sound("naturalist:mob/ostrich/step4"),
                        sound("naturalist:mob/ostrich/step5"),
                        sound("naturalist:mob/ostrich/step6")));

        add(NaturalistSoundEvents.OSTRICH_EGG_BREAK, definition()
                .subtitle("naturalist.subtitles.entity.ostrich.egg_break")
                .with(
                        sound("mob/turtle/egg/egg_break1"),
                        sound("mob/turtle/egg/egg_break2")));

        add(NaturalistSoundEvents.OSTRICH_EGG_CRACK, definition()
                .subtitle("naturalist.subtitles.entity.ostrich.egg_crack")
                .with(
                        sound("naturalist:mob/ostrich/egg_crack1"),
                        sound("naturalist:mob/ostrich/egg_crack2"),
                        sound("naturalist:mob/ostrich/egg_crack3")));

        add(NaturalistSoundEvents.OSTRICH_EGG_HATCH, definition()
                .subtitle("naturalist.subtitles.entity.ostrich.egg_hatch")
                .with(
                        sound("mob/turtle/baby/egg_hatched1"),
                        sound("mob/turtle/baby/egg_hatched2"),
                        sound("mob/turtle/baby/egg_hatched3")));

        add(NaturalistSoundEvents.SCORPION_AMBIENT, definition()
                .subtitle("naturalist.subtitles.entity.scorpion.ambient")
                .with(
                        sound("naturalist:mob/scorpion/idle1"),
                        sound("naturalist:mob/scorpion/idle2"),
                        sound("naturalist:mob/scorpion/idle3"),
                        sound("naturalist:mob/scorpion/idle4")));

        add(NaturalistSoundEvents.SCORPION_HURT, definition()
                .subtitle("naturalist.subtitles.entity.scorpion.hurt")
                .with(
                        sound("naturalist:mob/scorpion/hurt1"),
                        sound("naturalist:mob/scorpion/hurt2"),
                        sound("naturalist:mob/scorpion/hurt3")));

        add(NaturalistSoundEvents.SCORPION_DEATH, definition()
                .subtitle("naturalist.subtitles.entity.scorpion.death")
                .with(
                        sound("naturalist:mob/scorpion/death1"),
                        sound("naturalist:mob/scorpion/death2"),
                        sound("naturalist:mob/scorpion/death3")));

        add(NaturalistSoundEvents.SCORPION_ATTACK, definition()
                .subtitle("naturalist.subtitles.entity.scorpion.attack")
                .with(
                        sound("naturalist:mob/scorpion/attack1"),
                        sound("naturalist:mob/scorpion/attack2"),
                        sound("naturalist:mob/scorpion/attack3")));

        add(NaturalistSoundEvents.SCORPION_STEP, definition()
                .subtitle("subtitles.block.generic.footsteps")
                .with(
                        sound("naturalist:mob/scorpion/step1"),
                        sound("naturalist:mob/scorpion/step2"),
                        sound("naturalist:mob/scorpion/step3"),
                        sound("naturalist:mob/scorpion/step4"),
                        sound("naturalist:mob/scorpion/step5"),
                        sound("naturalist:mob/scorpion/step6")));

        add(NaturalistSoundEvents.GREAT_WHITE_SHARK_AMBIENT, definition()
                .subtitle("naturalist.subtitles.entity.great_white_shark.ambient")
                .with(
                        sound("naturalist:mob/great_white_shark/idle1"),
                        sound("naturalist:mob/great_white_shark/idle3"),
                        sound("naturalist:mob/great_white_shark/idle4")));

        add(NaturalistSoundEvents.GREAT_WHITE_SHARK_HURT, definition()
                .subtitle("naturalist.subtitles.entity.great_white_shark.hurt")
                .with(
                        sound("naturalist:mob/great_white_shark/hurt1"),
                        sound("naturalist:mob/great_white_shark/hurt2"),
                        sound("naturalist:mob/great_white_shark/hurt3")));

        add(NaturalistSoundEvents.GREAT_WHITE_SHARK_DEATH, definition()
                .subtitle("naturalist.subtitles.entity.great_white_shark.death")
                .with(
                        sound("naturalist:mob/great_white_shark/death1"),
                        sound("naturalist:mob/great_white_shark/death2")));

        add(NaturalistSoundEvents.GREAT_WHITE_SHARK_ATTACK, definition()
                .subtitle("naturalist.subtitles.entity.great_white_shark.attack")
                .with(
                        sound("naturalist:mob/great_white_shark/attack1"),
                        sound("naturalist:mob/great_white_shark/attack2")));

        add(NaturalistSoundEvents.GREAT_WHITE_SHARK_FLOP, definition()
                .subtitle("naturalist.subtitles.entity.great_white_shark.flop")
                .with(
                        sound("naturalist:mob/great_white_shark/flop1"),
                        sound("naturalist:mob/great_white_shark/flop3"),
                        sound("naturalist:mob/great_white_shark/flop4"),
                        sound("naturalist:mob/great_white_shark/flop5")));

        add(NaturalistSoundEvents.GREAT_WHITE_SHARK_SWIM, definition()
                .subtitle("naturalist.subtitles.entity.great_white_shark.swim")
                .with(
                        sound("naturalist:mob/great_white_shark/swim1"),
                        sound("naturalist:mob/great_white_shark/swim2"),
                        sound("naturalist:mob/great_white_shark/swim3"),
                        sound("naturalist:mob/great_white_shark/swim4"),
                        sound("naturalist:mob/great_white_shark/swim5")));

        add(NaturalistSoundEvents.GREAT_WHITE_SHARK_SWIM_FAST, definition()
                .subtitle("naturalist.subtitles.entity.great_white_shark.swim")
                .with(
                        sound("naturalist:mob/great_white_shark/swim_fast1"),
                        sound("naturalist:mob/great_white_shark/swim_fast2"),
                        sound("naturalist:mob/great_white_shark/swim_fast3"),
                        sound("naturalist:mob/great_white_shark/swim_fast4"),
                        sound("naturalist:mob/great_white_shark/swim_fast5")));

        add(NaturalistSoundEvents.TURKEY_AMBIENT, definition()
                .subtitle("naturalist.subtitles.entity.turkey.ambient")
                .with(
                        sound("naturalist:mob/turkey/idle1"),
                        sound("naturalist:mob/turkey/idle2"),
                        sound("naturalist:mob/turkey/idle3")));

        add(NaturalistSoundEvents.TURKEY_HURT, definition()
                .subtitle("naturalist.subtitles.entity.turkey.hurt")
                .with(
                        sound("naturalist:mob/turkey/hurt1"),
                        sound("naturalist:mob/turkey/hurt2"),
                        sound("naturalist:mob/turkey/hurt3")));

        add(NaturalistSoundEvents.TURKEY_DEATH, definition()
                .subtitle("naturalist.subtitles.entity.turkey.death")
                .with(
                        sound("naturalist:mob/turkey/death1"),
                        sound("naturalist:mob/turkey/death2"),
                        sound("naturalist:mob/turkey/death3")));

        add(NaturalistSoundEvents.TURKEY_STEP, definition()
                .with(
                        sound("naturalist:mob/turkey/step1"),
                        sound("naturalist:mob/turkey/step2"),
                        sound("naturalist:mob/turkey/step3"),
                        sound("naturalist:mob/turkey/step4"),
                        sound("naturalist:mob/turkey/step5"),
                        sound("naturalist:mob/turkey/step6")));

        add(NaturalistSoundEvents.TURKEY_PECK, definition()
                .subtitle("naturalist.subtitles.entity.turkey.peck")
                .with(
                        sound("naturalist:mob/turkey/peck1"),
                        sound("naturalist:mob/turkey/peck2"),
                        sound("naturalist:mob/turkey/peck3")));

        add(NaturalistSoundEvents.RAY_IDLE, definition()
                .subtitle("naturalist.subtitles.entity.ray.idle")
                .with(
                        sound("naturalist:mob/ray/idle1"),
                        sound("naturalist:mob/ray/idle2"),
                        sound("naturalist:mob/ray/idle3")));

        add(NaturalistSoundEvents.RAY_SWIM, definition()
                .subtitle("naturalist.subtitles.entity.ray.swim")
                .with(
                        sound("naturalist:mob/ray/swim1"),
                        sound("naturalist:mob/ray/swim2"),
                        sound("naturalist:mob/ray/swim3"),
                        sound("naturalist:mob/ray/swim4")));

        add(NaturalistSoundEvents.RAY_HURT, definition()
                .subtitle("naturalist.subtitles.entity.ray.hurt")
                .with(
                        sound("naturalist:mob/ray/hurt1"),
                        sound("naturalist:mob/ray/hurt2")));

        add(NaturalistSoundEvents.RAY_DEATH, definition()
                .subtitle("naturalist.subtitles.entity.ray.death")
                .with(
                        sound("naturalist:mob/ray/death1"),
                        sound("naturalist:mob/ray/death2")));

        add(NaturalistSoundEvents.BLOBFISH_FLOP, definition()
                .subtitle("naturalist.subtitles.entity.blobfish.flop")
                .with(
                        sound("minecraft:entity/pufferfish/flop1").volume(0.3),
                        sound("minecraft:entity/pufferfish/flop2").volume(0.3),
                        sound("minecraft:entity/pufferfish/flop3").volume(0.3),
                        sound("minecraft:entity/pufferfish/flop4").volume(0.3)));

        add(NaturalistSoundEvents.BLOBFISH_SWIM, definition()
                .subtitle("naturalist.subtitles.entity.blobfish.swim")
                .with(
                        sound("naturalist:mob/blobfish/swim1"),
                        sound("naturalist:mob/blobfish/swim2"),
                        sound("naturalist:mob/blobfish/swim3"),
                        sound("naturalist:mob/blobfish/swim4")));

        add(NaturalistSoundEvents.BLOBFISH_HURT, definition()
                .subtitle("naturalist.subtitles.entity.blobfish.hurt")
                .with(
                        sound("naturalist:mob/blobfish/hurt1"),
                        sound("naturalist:mob/blobfish/hurt2")));

        add(NaturalistSoundEvents.BLOBFISH_DEATH, definition()
                .subtitle("naturalist.subtitles.entity.blobfish.death")
                .with(
                        sound("naturalist:mob/blobfish/death1"),
                        sound("naturalist:mob/blobfish/death2")));

        add(NaturalistSoundEvents.PIRANHA_FLOP, definition()
                .subtitle("naturalist.subtitles.entity.piranha.flop")
                .with(
                        sound("minecraft:entity/pufferfish/flop1").volume(0.3),
                        sound("minecraft:entity/pufferfish/flop2").volume(0.3)));

        add(NaturalistSoundEvents.PIRANHA_IDLE, definition()
                .subtitle("naturalist.subtitles.entity.piranha.idle")
                .with(
                        sound("naturalist:mob/piranha/idle1"),
                        sound("naturalist:mob/piranha/idle2"),
                        sound("naturalist:mob/piranha/idle3")));

        add(NaturalistSoundEvents.PIRANHA_HURT, definition()
                .subtitle("naturalist.subtitles.entity.piranha.hurt")
                .with(
                        sound("naturalist:mob/piranha/hurt1"),
                        sound("naturalist:mob/piranha/hurt2")));

        add(NaturalistSoundEvents.PIRANHA_DEATH, definition()
                .subtitle("naturalist.subtitles.entity.piranha.death")
                .with(
                        sound("naturalist:mob/piranha/death1"),
                        sound("naturalist:mob/piranha/death2")));

        add(NaturalistSoundEvents.CLAM_AMBIENT, definition()
                .subtitle("naturalist.subtitles.entity.clam.ambient")
                .with(
                        sound("naturalist:mob/clam/idle1"),
                        sound("naturalist:mob/clam/idle2"),
                        sound("naturalist:mob/clam/idle3")));

        add(NaturalistSoundEvents.CLAM_HURT, definition()
                .subtitle("naturalist.subtitles.entity.clam.hurt")
                .with(
                        sound("naturalist:mob/clam/hurt1"),
                        sound("naturalist:mob/clam/hurt2")));

        add(NaturalistSoundEvents.CLAM_DEATH, definition()
                .subtitle("naturalist.subtitles.entity.clam.death")
                .with(sound("naturalist:mob/clam/death1")));

        add(NaturalistSoundEvents.CLAM_LAUNCH, definition()
                .subtitle("naturalist.subtitles.entity.clam.launch")
                .with(sound("naturalist:mob/clam/launch1")));

        add(NaturalistSoundEvents.CRAB_AMBIENT, definition()
                .subtitle("naturalist.subtitles.entity.crab.ambient")
                .with(
                        sound("naturalist:mob/crab/idle1"),
                        sound("naturalist:mob/crab/idle2"),
                        sound("naturalist:mob/crab/idle3")));

        add(NaturalistSoundEvents.CRAB_HURT, definition()
                .subtitle("naturalist.subtitles.entity.crab.hurt")
                .with(
                        sound("naturalist:mob/crab/hurt1"),
                        sound("naturalist:mob/crab/hurt2"),
                        sound("naturalist:mob/crab/hurt3")));

        add(NaturalistSoundEvents.CRAB_DEATH, definition()
                .subtitle("naturalist.subtitles.entity.crab.death")
                .with(
                        sound("naturalist:mob/crab/death1"),
                        sound("naturalist:mob/crab/death2")));

        add(NaturalistSoundEvents.CRAB_STEP, definition()
                .subtitle("subtitles.block.generic.footsteps")
                .with(
                        sound("naturalist:mob/crab/run1"),
                        sound("naturalist:mob/crab/run2"),
                        sound("naturalist:mob/crab/run3")));

        add(NaturalistSoundEvents.CRAB_PINCER, definition()
                .subtitle("naturalist.subtitles.entity.crab.pincer")
                .with(
                        sound("naturalist:mob/crab/pincer1"),
                        sound("naturalist:mob/crab/pincer2")));

        add(NaturalistSoundEvents.TORTOISE_HIDE, definition()
                .subtitle("naturalist.subtitles.entity.tortoise.hide")
                .with(
                        sound("naturalist:mob/tortoise/hide1"),
                        sound("naturalist:mob/tortoise/hide2")));

        add(NaturalistSoundEvents.TORTOISE_THUD, definition()
                .subtitle("naturalist.subtitles.entity.tortoise.thud")
                .with(sound("damage/fallsmall")));

        add("entity.tortoise.shell_block", definition()
                .subtitle("naturalist.subtitles.entity.tortoise.shell_block")
                .with(
                        sound("item/shield/block1"),
                        sound("item/shield/block2"),
                        sound("item/shield/block3"),
                        sound("item/shield/block4"),
                        sound("item/shield/block5")));

        add(NaturalistSoundEvents.TORTOISE_EGG_BREAK, definition()
                .subtitle("naturalist.subtitles.entity.tortoise.egg_break")
                .with(
                        sound("mob/turtle/egg/egg_break1"),
                        sound("mob/turtle/egg/egg_break2")));

        add(NaturalistSoundEvents.TORTOISE_EGG_CRACK, definition()
                .subtitle("naturalist.subtitles.entity.tortoise.egg_crack")
                .with(
                        sound("mob/turtle/egg/egg_crack1"),
                        sound("mob/turtle/egg/egg_crack2"),
                        sound("mob/turtle/egg/egg_crack3"),
                        sound("mob/turtle/egg/egg_crack4"),
                        sound("mob/turtle/egg/egg_crack5")));

        add(NaturalistSoundEvents.TORTOISE_EGG_HATCH, definition()
                .subtitle("naturalist.subtitles.entity.tortoise.egg_hatch")
                .with(
                        sound("mob/turtle/baby/egg_hatched1"),
                        sound("mob/turtle/baby/egg_hatched2"),
                        sound("mob/turtle/baby/egg_hatched3")));

        add(NaturalistSoundEvents.TORTOISE_HURT, definition()
                .subtitle("naturalist.subtitles.entity.tortoise.hurt")
                .with(
                        sound("naturalist:mob/tortoise/hurt1"),
                        sound("naturalist:mob/tortoise/hurt2"),
                        sound("naturalist:mob/tortoise/hurt3")));

        add(NaturalistSoundEvents.TORTOISE_DEATH, definition()
                .subtitle("naturalist.subtitles.entity.tortoise.death")
                .with(
                        sound("naturalist:mob/tortoise/death1"),
                        sound("naturalist:mob/tortoise/death2")));

        add(NaturalistSoundEvents.TORTOISE_AMBIENT, definition()
                .subtitle("naturalist.subtitles.entity.tortoise.ambient")
                .with(
                        sound("naturalist:mob/tortoise/idle1"),
                        sound("naturalist:mob/tortoise/idle2"),
                        sound("naturalist:mob/tortoise/idle3")));

        add(NaturalistSoundEvents.TORTOISE_AMBIENT_BABY, definition()
                .subtitle("naturalist.subtitles.entity.tortoise.ambient")
                .with(
                        sound("naturalist:mob/tortoise/baby_idle1"),
                        sound("naturalist:mob/tortoise/baby_idle2"),
                        sound("naturalist:mob/tortoise/baby_idle3")));

        add(NaturalistSoundEvents.TORTOISE_HURT_BABY, definition()
                .subtitle("naturalist.subtitles.entity.tortoise.hurt")
                .with(
                        sound("naturalist:mob/tortoise/baby_hurt1"),
                        sound("naturalist:mob/tortoise/baby_hurt2"),
                        sound("naturalist:mob/tortoise/baby_hurt3")));

        add(NaturalistSoundEvents.TORTOISE_DEATH_BABY, definition()
                .subtitle("naturalist.subtitles.entity.tortoise.death")
                .with(
                        sound("naturalist:mob/tortoise/baby_death1"),
                        sound("naturalist:mob/tortoise/baby_death2"),
                        sound("naturalist:mob/tortoise/baby_death3")));

        add(NaturalistSoundEvents.ALLIGATOR_EGG_BREAK, definition()
                .subtitle("naturalist.subtitles.entity.alligator.egg_break")
                .with(
                        sound("mob/turtle/egg/egg_break1"),
                        sound("mob/turtle/egg/egg_break2")));

        add(NaturalistSoundEvents.ALLIGATOR_EGG_CRACK, definition()
                .subtitle("naturalist.subtitles.entity.alligator.egg_crack")
                .with(
                        sound("mob/turtle/egg/egg_crack1"),
                        sound("mob/turtle/egg/egg_crack2"),
                        sound("mob/turtle/egg/egg_crack3"),
                        sound("mob/turtle/egg/egg_crack4"),
                        sound("mob/turtle/egg/egg_crack5")));

        add(NaturalistSoundEvents.ALLIGATOR_EGG_HATCH, definition()
                .subtitle("naturalist.subtitles.entity.alligator.egg_hatch")
                .with(
                        sound("mob/turtle/baby/egg_hatched1"),
                        sound("mob/turtle/baby/egg_hatched2"),
                        sound("mob/turtle/baby/egg_hatched3")));

        add(NaturalistSoundEvents.SNAKE_HISS, definition()
                .subtitle("naturalist.subtitles.entity.snake.hiss")
                .with(sound("naturalist:mob/snake/hiss")));

        add(NaturalistSoundEvents.SNAKE_HURT, definition()
                .subtitle("naturalist.subtitles.entity.snake.hurt")
                .with(
                        sound("naturalist:mob/snake/hurt"),
                        sound("naturalist:mob/snake/hurt2"),
                        sound("naturalist:mob/snake/hurt3")));

        add(NaturalistSoundEvents.SNAKE_DEATH, definition()
                .subtitle("naturalist.subtitles.entity.snake.death")
                .with(
                        sound("naturalist:mob/snake/death1"),
                        sound("naturalist:mob/snake/death2")));

        add(NaturalistSoundEvents.SNAKE_AMBIENT, definition()
                .subtitle("naturalist.subtitles.entity.snake.ambient")
                .with(
                        sound("naturalist:mob/snake/idle1"),
                        sound("naturalist:mob/snake/idle2"),
                        sound("naturalist:mob/snake/idle3")));

        add(NaturalistSoundEvents.SNAKE_RATTLE, definition()
                .subtitle("naturalist.subtitles.entity.snake.rattle")
                .with(sound("naturalist:mob/snake/rattle")));

        add(NaturalistSoundEvents.SNAIL_AMBIENT, definition()
                .subtitle("naturalist.subtitles.entity.snail.ambient")
                .with(
                        sound("naturalist:mob/snail/idle1"),
                        sound("naturalist:mob/snail/idle2"),
                        sound("naturalist:mob/snail/idle3"),
                        sound("naturalist:mob/snail/idle4")));

        add(NaturalistSoundEvents.SNAIL_HURT, definition()
                .subtitle("naturalist.subtitles.entity.snail.hurt")
                .with(
                        sound("naturalist:mob/snail/hurt1"),
                        sound("naturalist:mob/snail/hurt2"),
                        sound("naturalist:mob/snail/hurt3")));

        add(NaturalistSoundEvents.SNAIL_CRUSH, definition()
                .subtitle("naturalist.subtitles.entity.snail.crush")
                .with(
                        sound("minecraft:mob/turtle/egg/egg_crack1"),
                        sound("minecraft:mob/turtle/egg/egg_crack2"),
                        sound("minecraft:mob/turtle/egg/egg_crack3"),
                        sound("minecraft:mob/turtle/egg/egg_crack4"),
                        sound("minecraft:mob/turtle/egg/egg_crack5")));

        add(NaturalistSoundEvents.SNAIL_FORWARD, definition()
                .subtitle("naturalist.subtitles.entity.snail.move")
                .with(sound("naturalist:mob/snail/forward")));

        add(NaturalistSoundEvents.SNAIL_BACK, definition()
                .subtitle("naturalist.subtitles.entity.snail.move")
                .with(sound("naturalist:mob/snail/back")));

        add(NaturalistSoundEvents.BEAR_HURT, definition()
                .subtitle("naturalist.subtitles.entity.bear.hurt")
                .with(
                        sound("naturalist:mob/bear/hurt1"),
                        sound("naturalist:mob/bear/hurt2"),
                        sound("naturalist:mob/bear/hurt3")));

        add(NaturalistSoundEvents.BEAR_DEATH, definition()
                .subtitle("naturalist.subtitles.entity.bear.death")
                .with(
                        sound("naturalist:mob/bear/death1"),
                        sound("naturalist:mob/bear/death2"),
                        sound("naturalist:mob/bear/death3")));

        add(NaturalistSoundEvents.BEAR_AMBIENT, definition()
                .subtitle("naturalist.subtitles.entity.bear.ambient")
                .with(
                        sound("naturalist:mob/bear/idle1"),
                        sound("naturalist:mob/bear/idle2"),
                        sound("naturalist:mob/bear/idle3")));

        add(NaturalistSoundEvents.BEAR_AMBIENT_BABY, definition()
                .subtitle("naturalist.subtitles.entity.bear.ambient")
                .with(
                        sound("naturalist:mob/bear/baby_idle1"),
                        sound("naturalist:mob/bear/baby_idle2"),
                        sound("naturalist:mob/bear/baby_idle3"),
                        sound("naturalist:mob/bear/baby_idle4")));

        add(NaturalistSoundEvents.BEAR_HURT_BABY, definition()
                .subtitle("naturalist.subtitles.entity.bear.hurt")
                .with(
                        sound("naturalist:mob/bear/baby_hurt1"),
                        sound("naturalist:mob/bear/baby_hurt2"),
                        sound("naturalist:mob/bear/baby_hurt3")));

        add(NaturalistSoundEvents.BEAR_DEATH_BABY, definition()
                .subtitle("naturalist.subtitles.entity.bear.death")
                .with(
                        sound("naturalist:mob/bear/baby_death1"),
                        sound("naturalist:mob/bear/baby_death2")));

        add(NaturalistSoundEvents.BEAR_SLEEP, definition()
                .subtitle("naturalist.subtitles.entity.bear.sleep")
                .with(
                        sound("minecraft:mob/fox/sleep1"),
                        sound("minecraft:mob/fox/sleep2"),
                        sound("minecraft:mob/fox/sleep3"),
                        sound("minecraft:mob/fox/sleep4"),
                        sound("minecraft:mob/fox/sleep5")));

        add(NaturalistSoundEvents.BEAR_SNIFF, definition()
                .subtitle("naturalist.subtitles.entity.bear.sniff")
                .with(
                        sound("minecraft:mob/fox/sniff1"),
                        sound("minecraft:mob/fox/sniff2"),
                        sound("minecraft:mob/fox/sniff3"),
                        sound("minecraft:mob/fox/sniff4")));

        add(NaturalistSoundEvents.WHISTLE, definition()
                .subtitle("naturalist.subtitles.item.whistle")
                .with(sound("naturalist:item/whistle/whistle")));

        add(NaturalistSoundEvents.KNAPSACK_PICKUP, definition()
                .subtitle("naturalist.subtitles.item.knapsack.pickup")
                .with(sound("naturalist:item/knapsack/pickup")));

        add(NaturalistSoundEvents.KNAPSACK_PLACE, definition()
                .subtitle("naturalist.subtitles.item.knapsack.place")
                .with(sound("naturalist:item/knapsack/place")));

        add(NaturalistSoundEvents.BEAR_SPIT, definition()
                .subtitle("naturalist.subtitles.entity.bear.spit")
                .with(
                        sound("minecraft:mob/fox/spit1"),
                        sound("minecraft:mob/fox/spit2"),
                        sound("minecraft:mob/fox/spit3")));

        add(NaturalistSoundEvents.BEAR_EAT, definition()
                .subtitle("naturalist.subtitles.entity.bear.eat")
                .with(
                        sound("minecraft:mob/panda/eat1"),
                        sound("minecraft:mob/panda/eat2"),
                        sound("minecraft:mob/panda/eat3"),
                        sound("minecraft:mob/panda/eat4"),
                        sound("minecraft:mob/panda/eat5"),
                        sound("minecraft:mob/panda/eat6"),
                        sound("minecraft:mob/panda/eat7"),
                        sound("minecraft:mob/panda/eat8"),
                        sound("minecraft:mob/panda/eat9"),
                        sound("minecraft:mob/panda/eat10"),
                        sound("minecraft:mob/panda/eat11"),
                        sound("minecraft:mob/panda/eat12")));

        add(NaturalistSoundEvents.BIRD_HURT, definition()
                .subtitle("naturalist.subtitles.entity.bird.hurt")
                .with(
                        sound("naturalist:mob/bird/generic/hurt1"),
                        sound("naturalist:mob/bird/generic/hurt2"),
                        sound("naturalist:mob/bird/generic/hurt3")));

        add(NaturalistSoundEvents.BIRD_DEATH, definition()
                .subtitle("naturalist.subtitles.entity.bird.death")
                .with(
                        sound("naturalist:mob/bird/generic/death1"),
                        sound("naturalist:mob/bird/generic/death2"),
                        sound("naturalist:mob/bird/generic/death3")));

        add(NaturalistSoundEvents.BIRD_EAT, definition()
                .subtitle("naturalist.subtitles.entity.bird.eat")
                .with(
                        sound("minecraft:mob/parrot/eat1"),
                        sound("minecraft:mob/parrot/eat2"),
                        sound("minecraft:mob/parrot/eat3")));

        add(NaturalistSoundEvents.BIRD_FLY, definition()
                .subtitle("naturalist.subtitles.entity.bird.fly")
                .with(
                        sound("minecraft:mob/parrot/fly1"),
                        sound("minecraft:mob/parrot/fly2"),
                        sound("minecraft:mob/parrot/fly3"),
                        sound("minecraft:mob/parrot/fly4"),
                        sound("minecraft:mob/parrot/fly5"),
                        sound("minecraft:mob/parrot/fly6"),
                        sound("minecraft:mob/parrot/fly7")));

        add(NaturalistSoundEvents.BIRD_AMBIENT_BLUEJAY, definition()
                .subtitle("naturalist.subtitles.entity.bird.ambient_bluejay")
                .with(
                        sound("naturalist:mob/bird/bluejay/idle1"),
                        sound("naturalist:mob/bird/bluejay/idle2")));

        add(NaturalistSoundEvents.BIRD_AMBIENT_CANARY, definition()
                .subtitle("naturalist.subtitles.entity.bird.ambient_canary")
                .with(
                        sound("naturalist:mob/bird/canary/idle"),
                        sound("naturalist:mob/bird/canary/song")));

        add(NaturalistSoundEvents.BIRD_AMBIENT_ROBIN, definition()
                .subtitle("naturalist.subtitles.entity.bird.ambient_robin")
                .with(
                        sound("naturalist:mob/bird/robin/idle"),
                        sound("naturalist:mob/bird/robin/song")));

        add(NaturalistSoundEvents.BIRD_AMBIENT_FINCH, definition()
                .subtitle("naturalist.subtitles.entity.bird.ambient_finch")
                .with(
                        sound("naturalist:mob/bird/finch/idle1"),
                        sound("naturalist:mob/bird/finch/idle2"),
                        sound("naturalist:mob/bird/finch/idle3"),
                        sound("naturalist:mob/bird/finch/song1"),
                        sound("naturalist:mob/bird/finch/song2"),
                        sound("naturalist:mob/bird/finch/song3")));

        add(NaturalistSoundEvents.BIRD_AMBIENT_SPARROW, definition()
                .subtitle("naturalist.subtitles.entity.bird.ambient_sparrow")
                .with(
                        sound("naturalist:mob/bird/sparrow/idle1"),
                        sound("naturalist:mob/bird/sparrow/idle2"),
                        sound("naturalist:mob/bird/sparrow/idle3"),
                        sound("naturalist:mob/bird/sparrow/idle4")));

        add(NaturalistSoundEvents.BIRD_AMBIENT_CARDINAL, definition()
                .subtitle("naturalist.subtitles.entity.bird.ambient_cardinal")
                .with(
                        sound("naturalist:mob/bird/cardinal/idle"),
                        sound("naturalist:mob/bird/cardinal/song")));

        add(NaturalistSoundEvents.FIREFLY_AMBIENT, definition()
                .subtitle("naturalist.subtitles.entity.firefly.ambient")
                .with(
                        sound("naturalist:mob/firefly/idle1"),
                        sound("naturalist:mob/firefly/idle2"),
                        sound("naturalist:mob/firefly/idle3")));

        add(NaturalistSoundEvents.FIREFLY_HURT, definition()
                .subtitle("naturalist.subtitles.entity.firefly.hurt")
                .with(sound("naturalist:mob/firefly/hurt")));

        add(NaturalistSoundEvents.FIREFLY_DEATH, definition()
                .subtitle("naturalist.subtitles.entity.firefly.death")
                .with(sound("naturalist:mob/firefly/death")));

        add(NaturalistSoundEvents.FIREFLY_HIDE, definition()
                .subtitle("naturalist.subtitles.entity.firefly.hide")
                .with(sound("naturalist:mob/firefly/hide")));

        add(NaturalistSoundEvents.RHINO_SCRAPE, definition()
                .subtitle("naturalist.subtitles.entity.rhino.scrape")
                .with(sound("naturalist:mob/rhino/scrape")));

        add(NaturalistSoundEvents.RHINO_AMBIENT, definition()
                .subtitle("naturalist.subtitles.entity.rhino.ambient")
                .with(
                        sound("naturalist:mob/rhino/idle1"),
                        sound("naturalist:mob/rhino/idle2"),
                        sound("naturalist:mob/rhino/idle3")));

        add(NaturalistSoundEvents.RHINO_AMBIENT_BABY, definition()
                .subtitle("naturalist.subtitles.entity.rhino.ambient")
                .with(sound("naturalist:mob/rhino/idle_baby")));

        add(NaturalistSoundEvents.RHINO_HURT, definition()
                .subtitle("naturalist.subtitles.entity.rhino.hurt")
                .with(
                        sound("naturalist:mob/rhino/hurt1"),
                        sound("naturalist:mob/rhino/hurt2"),
                        sound("naturalist:mob/rhino/hurt3")));

        add(NaturalistSoundEvents.RHINO_DEATH, definition()
                .subtitle("naturalist.subtitles.entity.rhino.death")
                .with(
                        sound("naturalist:mob/rhino/death1"),
                        sound("naturalist:mob/rhino/death2")));

        add(NaturalistSoundEvents.LION_HURT, definition()
                .subtitle("naturalist.subtitles.entity.lion.hurt")
                .with(
                        sound("naturalist:mob/lion/hurt1"),
                        sound("naturalist:mob/lion/hurt2")));

        add(NaturalistSoundEvents.LION_AMBIENT, definition()
                .subtitle("naturalist.subtitles.entity.lion.ambient")
                .with(
                        sound("naturalist:mob/lion/idle1"),
                        sound("naturalist:mob/lion/idle2"),
                        sound("naturalist:mob/lion/idle3")));

        add(NaturalistSoundEvents.LION_ROAR, definition()
                .subtitle("naturalist.subtitles.entity.lion.roar")
                .with(
                        sound("naturalist:mob/lion/roar1"),
                        sound("naturalist:mob/lion/roar2")));

        add(NaturalistSoundEvents.LION_AMBIENT_BABY, definition()
                .subtitle("naturalist.subtitles.entity.lion.ambient")
                .with(
                        sound("naturalist:mob/lion/baby_idle1"),
                        sound("naturalist:mob/lion/baby_idle2"),
                        sound("naturalist:mob/lion/baby_idle3")));

        add(NaturalistSoundEvents.LION_HURT_BABY, definition()
                .subtitle("naturalist.subtitles.entity.lion.hurt")
                .with(
                        sound("naturalist:mob/lion/baby_hurt1"),
                        sound("naturalist:mob/lion/baby_hurt2"),
                        sound("naturalist:mob/lion/baby_hurt3")));

        add(NaturalistSoundEvents.LION_DEATH_BABY, definition()
                .subtitle("naturalist.subtitles.entity.lion.death")
                .with(
                        sound("naturalist:mob/lion/baby_death1"),
                        sound("naturalist:mob/lion/baby_death2")));

        add(NaturalistSoundEvents.LION_DEATH, definition()
                .subtitle("naturalist.subtitles.entity.lion.death")
                .with(
                        sound("naturalist:mob/lion/death1"),
                        sound("naturalist:mob/lion/death2"),
                        sound("naturalist:mob/lion/death3")));

        add(NaturalistSoundEvents.ELEPHANT_AMBIENT, definition()
                .subtitle("naturalist.subtitles.entity.elephant.ambient")
                .with(
                        sound("naturalist:mob/elephant/idle1"),
                        sound("naturalist:mob/elephant/idle2"),
                        sound("naturalist:mob/elephant/idle3")));

        add(NaturalistSoundEvents.ELEPHANT_AMBIENT_BABY, definition()
                .subtitle("naturalist.subtitles.entity.elephant.ambient")
                .with(
                        sound("naturalist:mob/elephant/baby_idle1"),
                        sound("naturalist:mob/elephant/baby_idle2")));

        add(NaturalistSoundEvents.ELEPHANT_HURT_BABY, definition()
                .subtitle("naturalist.subtitles.entity.elephant.hurt")
                .with(
                        sound("naturalist:mob/elephant/baby_hurt1"),
                        sound("naturalist:mob/elephant/baby_hurt2")));

        add(NaturalistSoundEvents.ELEPHANT_DEATH_BABY, definition()
                .subtitle("naturalist.subtitles.entity.elephant.death")
                .with(sound("naturalist:mob/elephant/baby_death1")));

        add(NaturalistSoundEvents.ELEPHANT_HURT, definition()
                .subtitle("naturalist.subtitles.entity.elephant.hurt")
                .with(sound("naturalist:mob/elephant/hurt")));

        add(NaturalistSoundEvents.ELEPHANT_DEATH, definition()
                .subtitle("naturalist.subtitles.entity.elephant.death")
                .with(
                        sound("naturalist:mob/elephant/death1"),
                        sound("naturalist:mob/elephant/death2")));

        add(NaturalistSoundEvents.MAMMOTH_AMBIENT, definition()
                .subtitle("naturalist.subtitles.entity.mammoth.ambient")
                .with(
                        sound("naturalist:mob/elephant/idle1"),
                        sound("naturalist:mob/elephant/idle2"),
                        sound("naturalist:mob/elephant/idle3")));

        add(NaturalistSoundEvents.MAMMOTH_AMBIENT_BABY, definition()
                .subtitle("naturalist.subtitles.entity.mammoth.ambient")
                .with(
                        sound("naturalist:mob/elephant/baby_idle1").pitch(0.75),
                        sound("naturalist:mob/elephant/baby_idle2").pitch(0.75)));

        add(NaturalistSoundEvents.MAMMOTH_HURT_BABY, definition()
                .subtitle("naturalist.subtitles.entity.mammoth.hurt")
                .with(
                        sound("naturalist:mob/elephant/baby_hurt1").pitch(0.75),
                        sound("naturalist:mob/elephant/baby_hurt2").pitch(0.75)));

        add(NaturalistSoundEvents.MAMMOTH_DEATH_BABY, definition()
                .subtitle("naturalist.subtitles.entity.mammoth.death")
                .with(sound("naturalist:mob/elephant/baby_death1").pitch(0.75)));

        add(NaturalistSoundEvents.MAMMOTH_HURT, definition()
                .subtitle("naturalist.subtitles.entity.mammoth.hurt")
                .with(sound("naturalist:mob/elephant/hurt")));

        add(NaturalistSoundEvents.MAMMOTH_DEATH, definition()
                .subtitle("naturalist.subtitles.entity.mammoth.death")
                .with(
                        sound("naturalist:mob/elephant/death1"),
                        sound("naturalist:mob/elephant/death2")));

        add(NaturalistSoundEvents.DEER_AMBIENT, definition()
                .subtitle("naturalist.subtitles.entity.deer.ambient")
                .with(
                        sound("naturalist:mob/deer/idle1"),
                        sound("naturalist:mob/deer/idle2"),
                        sound("naturalist:mob/deer/idle3")));

        add(NaturalistSoundEvents.DEER_HURT, definition()
                .subtitle("naturalist.subtitles.entity.deer.hurt")
                .with(
                        sound("naturalist:mob/deer/hurt"),
                        sound("naturalist:mob/deer/hurt2")));

        add(NaturalistSoundEvents.DEER_AMBIENT_BABY, definition()
                .subtitle("naturalist.subtitles.entity.deer.ambient")
                .with(
                        sound("naturalist:mob/deer/fawn_idle1"),
                        sound("naturalist:mob/deer/fawn_idle2")));

        add(NaturalistSoundEvents.DEER_HURT_BABY, definition()
                .subtitle("naturalist.subtitles.entity.deer.hurt")
                .with(
                        sound("naturalist:mob/deer/fawn_hurt1"),
                        sound("naturalist:mob/deer/fawn_hurt2")));

        add(NaturalistSoundEvents.DEER_DEATH_BABY, definition()
                .subtitle("naturalist.subtitles.entity.deer.death")
                .with(
                        sound("naturalist:mob/deer/fawn_death1"),
                        sound("naturalist:mob/deer/fawn_death2")));

        add(NaturalistSoundEvents.DEER_DEATH, definition()
                .subtitle("naturalist.subtitles.entity.deer.death")
                .with(
                        sound("naturalist:mob/deer/death1"),
                        sound("naturalist:mob/deer/death2")));

        add(NaturalistSoundEvents.ZEBRA_AMBIENT, definition()
                .subtitle("naturalist.subtitles.entity.zebra.ambient")
                .with(
                        sound("naturalist:mob/zebra/idle1"),
                        sound("naturalist:mob/zebra/idle2")));

        add(NaturalistSoundEvents.ZEBRA_HURT, definition()
                .subtitle("naturalist.subtitles.entity.zebra.hurt")
                .with(sound("naturalist:mob/zebra/hurt")));

        add(NaturalistSoundEvents.ZEBRA_DEATH, definition()
                .subtitle("naturalist.subtitles.entity.zebra.death")
                .with(
                        sound("naturalist:mob/zebra/death1"),
                        sound("naturalist:mob/zebra/death2")));

        add(NaturalistSoundEvents.ZEBRA_EAT, definition()
                .subtitle("naturalist.subtitles.entity.zebra.eat")
                .with(
                        sound("minecraft:entity/horse/eat1"),
                        sound("minecraft:entity/horse/eat2"),
                        sound("minecraft:entity/horse/eat3"),
                        sound("minecraft:entity/horse/eat4"),
                        sound("minecraft:entity/horse/eat5")));

        add(NaturalistSoundEvents.ZEBRA_BREATHE, definition()
                .subtitle("naturalist.subtitles.entity.zebra.breathe")
                .with(
                        sound("minecraft:mob/horse/breathe1"),
                        sound("minecraft:mob/horse/breathe2"),
                        sound("minecraft:mob/horse/breathe3")));

        add(NaturalistSoundEvents.ZEBRA_ANGRY, definition()
                .subtitle("naturalist.subtitles.entity.zebra.angry")
                .with(sound("minecraft:mob/horse/angry1")));

        add(NaturalistSoundEvents.ZEBRA_JUMP, definition()
                .subtitle("naturalist.subtitles.entity.zebra.jump")
                .with(sound("minecraft:mob/horse/jump")));

        add(NaturalistSoundEvents.VULTURE_AMBIENT, definition()
                .subtitle("naturalist.subtitles.entity.vulture.ambient")
                .with(
                        sound("naturalist:mob/vulture/idle1"),
                        sound("naturalist:mob/vulture/idle2"),
                        sound("naturalist:mob/vulture/idle3")));

        add(NaturalistSoundEvents.VULTURE_HURT, definition()
                .subtitle("naturalist.subtitles.entity.vulture.hurt")
                .with(
                        sound("naturalist:mob/vulture/hurt1"),
                        sound("naturalist:mob/vulture/hurt2"),
                        sound("naturalist:mob/vulture/hurt3")));

        add(NaturalistSoundEvents.VULTURE_DEATH, definition()
                .subtitle("naturalist.subtitles.entity.vulture.death")
                .with(
                        sound("naturalist:mob/vulture/death1"),
                        sound("naturalist:mob/vulture/death2"),
                        sound("naturalist:mob/vulture/death3")));

        add(NaturalistSoundEvents.GIRAFFE_AMBIENT, definition()
                .subtitle("naturalist.subtitles.entity.giraffe.ambient")
                .with(sound("naturalist:mob/giraffe_idle")));

        add(NaturalistSoundEvents.GIRAFFE_AMBIENT_BABY, definition()
                .subtitle("naturalist.subtitles.entity.giraffe.ambient")
                .with(
                        sound("naturalist:mob/giraffe/baby_idle1"),
                        sound("naturalist:mob/giraffe/baby_idle2"),
                        sound("naturalist:mob/giraffe/baby_idle3")));

        add(NaturalistSoundEvents.GIRAFFE_HURT_BABY, definition()
                .subtitle("naturalist.subtitles.entity.giraffe.hurt")
                .with(
                        sound("naturalist:mob/giraffe/baby_hurt1"),
                        sound("naturalist:mob/giraffe/baby_hurt2")));

        add(NaturalistSoundEvents.GIRAFFE_DEATH_BABY, definition()
                .subtitle("naturalist.subtitles.entity.giraffe.death")
                .with(
                        sound("naturalist:mob/giraffe/baby_death1"),
                        sound("naturalist:mob/giraffe/baby_death2")));

        add(NaturalistSoundEvents.GIRAFFE_HURT, definition()
                .subtitle("naturalist.subtitles.entity.giraffe.hurt")
                .with(
                        sound("naturalist:mob/giraffe/hurt1"),
                        sound("naturalist:mob/giraffe/hurt2")));

        add(NaturalistSoundEvents.GIRAFFE_DEATH, definition()
                .subtitle("naturalist.subtitles.entity.giraffe.death")
                .with(
                        sound("naturalist:mob/giraffe/death1"),
                        sound("naturalist:mob/giraffe/death2")));

        add(NaturalistSoundEvents.HIPPO_AMBIENT, definition()
                .subtitle("naturalist.subtitles.entity.hippo.ambient")
                .with(
                        sound("naturalist:mob/hippo/idle1"),
                        sound("naturalist:mob/hippo/idle2"),
                        sound("naturalist:mob/hippo/idle3")));

        add(NaturalistSoundEvents.HIPPO_HURT, definition()
                .subtitle("naturalist.subtitles.entity.hippo.hurt")
                .with(
                        sound("naturalist:mob/hippo/hurt1"),
                        sound("naturalist:mob/hippo/hurt2"),
                        sound("naturalist:mob/hippo/hurt3")));

        add(NaturalistSoundEvents.HIPPO_DEATH, definition()
                .subtitle("naturalist.subtitles.entity.hippo.death")
                .with(
                        sound("naturalist:mob/hippo/death1"),
                        sound("naturalist:mob/hippo/death2")));

        add(NaturalistSoundEvents.ALLIGATOR_HURT, definition()
                .subtitle("naturalist.subtitles.entity.alligator.hurt")
                .with(
                        sound("naturalist:mob/alligator/hurt1"),
                        sound("naturalist:mob/alligator/hurt2"),
                        sound("naturalist:mob/alligator/hurt3")));

        add(NaturalistSoundEvents.ALLIGATOR_DEATH, definition()
                .subtitle("naturalist.subtitles.entity.alligator.death")
                .with(sound("naturalist:mob/alligator/death")));

        add(NaturalistSoundEvents.ALLIGATOR_AMBIENT, definition()
                .subtitle("naturalist.subtitles.entity.alligator.ambient")
                .with(
                        sound("naturalist:mob/alligator/idle1"),
                        sound("naturalist:mob/alligator/idle2")));

        add(NaturalistSoundEvents.ALLIGATOR_AMBIENT_BABY, definition()
                .subtitle("naturalist.subtitles.entity.alligator.ambient")
                .with(
                        sound("naturalist:mob/alligator/baby_idle1"),
                        sound("naturalist:mob/alligator/baby_idle2"),
                        sound("naturalist:mob/alligator/baby_idle3"),
                        sound("naturalist:mob/alligator/baby_idle4")));

        add(NaturalistSoundEvents.ALLIGATOR_HURT_BABY, definition()
                .subtitle("naturalist.subtitles.entity.alligator.hurt")
                .with(
                        sound("naturalist:mob/alligator/baby_hurt1"),
                        sound("naturalist:mob/alligator/baby_hurt2"),
                        sound("naturalist:mob/alligator/baby_hurt3")));

        add(NaturalistSoundEvents.ALLIGATOR_DEATH_BABY, definition()
                .subtitle("naturalist.subtitles.entity.alligator.death")
                .with(
                        sound("naturalist:mob/alligator/baby_death1"),
                        sound("naturalist:mob/alligator/baby_death2"),
                        sound("naturalist:mob/alligator/baby_death3")));

        add(NaturalistSoundEvents.BOAR_AMBIENT, definition()
                .subtitle("naturalist.subtitles.entity.boar.ambient")
                .with(
                        sound("naturalist:mob/boar/idle1"),
                        sound("naturalist:mob/boar/idle2"),
                        sound("naturalist:mob/boar/idle3")));

        add(NaturalistSoundEvents.BOAR_HURT, definition()
                .subtitle("naturalist.subtitles.entity.boar.hurt")
                .with(
                        sound("naturalist:mob/boar/hurt1"),
                        sound("naturalist:mob/boar/hurt2"),
                        sound("naturalist:mob/boar/hurt3")));

        add(NaturalistSoundEvents.BOAR_AMBIENT_BABY, definition()
                .subtitle("naturalist.subtitles.entity.boar.ambient")
                .with(
                        sound("naturalist:mob/boar/piglet_idle1"),
                        sound("naturalist:mob/boar/piglet_idle2"),
                        sound("naturalist:mob/boar/piglet_idle3")));

        add(NaturalistSoundEvents.BOAR_HURT_BABY, definition()
                .subtitle("naturalist.subtitles.entity.boar.hurt")
                .with(
                        sound("naturalist:mob/boar/piglet_hurt1"),
                        sound("naturalist:mob/boar/piglet_hurt2")));

        add(NaturalistSoundEvents.BOAR_DEATH_BABY, definition()
                .subtitle("naturalist.subtitles.entity.boar.death")
                .with(
                        sound("naturalist:mob/boar/piglet_death1"),
                        sound("naturalist:mob/boar/piglet_death2")));

        add(NaturalistSoundEvents.BOAR_DEATH, definition()
                .subtitle("naturalist.subtitles.entity.boar.death")
                .with(
                        sound("naturalist:mob/boar/death1"),
                        sound("naturalist:mob/boar/death2")));

        add("entity.boar.step", definition()
                .subtitle("subtitles.block.generic.footsteps")
                .with(
                        sound("minecraft:mob/pig/step1"),
                        sound("minecraft:mob/pig/step2"),
                        sound("minecraft:mob/pig/step3"),
                        sound("minecraft:mob/pig/step4")));

        add(NaturalistSoundEvents.CATFISH_FLOP, definition()
                .subtitle("naturalist.subtitles.entity.catfish.flop")
                .with(
                        sound("minecraft:entity/pufferfish/flop1").volume(0.3),
                        sound("minecraft:entity/pufferfish/flop2").volume(0.3),
                        sound("minecraft:entity/pufferfish/flop3").volume(0.3),
                        sound("minecraft:entity/pufferfish/flop4").volume(0.3)));

        add(NaturalistSoundEvents.BASS_FLOP, definition()
                .subtitle("naturalist.subtitles.entity.bass.flop")
                .with(
                        sound("minecraft:entity/fish/flop1").volume(0.3).pitch(0.8),
                        sound("minecraft:entity/fish/flop2").volume(0.3).pitch(0.8),
                        sound("minecraft:entity/fish/flop3").volume(0.3).pitch(0.8),
                        sound("minecraft:entity/fish/flop4").volume(0.3).pitch(0.8)));

        add(NaturalistSoundEvents.DRAGONFLY_LOOP, definition()
                .subtitle("naturalist.subtitles.entity.dragonfly.loop")
                .with(
                        sound("minecraft:mob/bee/loop1"),
                        sound("minecraft:mob/bee/loop2"),
                        sound("minecraft:mob/bee/loop3"),
                        sound("minecraft:mob/bee/loop4"),
                        sound("minecraft:mob/bee/loop5")));

        add(NaturalistSoundEvents.DUCK_AMBIENT, definition()
                .subtitle("naturalist.subtitles.entity.duck.ambient")
                .with(
                        sound("naturalist:mob/duck/idle1"),
                        sound("naturalist:mob/duck/idle2")));

        add(NaturalistSoundEvents.DUCK_HURT, definition()
                .subtitle("naturalist.subtitles.entity.duck.hurt")
                .with(
                        sound("naturalist:mob/duck/hurt1"),
                        sound("naturalist:mob/duck/hurt2")));

        add(NaturalistSoundEvents.DUCK_DEATH, definition()
                .subtitle("naturalist.subtitles.entity.duck.death")
                .with(
                        sound("naturalist:mob/duck/death1"),
                        sound("naturalist:mob/duck/death2")));

        add(NaturalistSoundEvents.DUCK_AMBIENT_BABY, definition()
                .subtitle("naturalist.subtitles.entity.duck.ambient")
                .with(
                        sound("naturalist:mob/duck/duckling_idle1"),
                        sound("naturalist:mob/duck/duckling_idle2")));

        add(NaturalistSoundEvents.DUCK_HURT_BABY, definition()
                .subtitle("naturalist.subtitles.entity.duck.hurt")
                .with(
                        sound("naturalist:mob/duck/duckling_hurt1"),
                        sound("naturalist:mob/duck/duckling_hurt2")));

        add(NaturalistSoundEvents.DUCK_DEATH_BABY, definition()
                .subtitle("naturalist.subtitles.entity.duck.death")
                .with(
                        sound("naturalist:mob/duck/duckling_death1"),
                        sound("naturalist:mob/duck/duckling_death2")));

        add(NaturalistSoundEvents.DUCK_STEP, definition()
                .subtitle("subtitles.block.generic.footsteps")
                .with(
                        sound("naturalist:mob/duck/step1"),
                        sound("naturalist:mob/duck/step2"),
                        sound("naturalist:mob/duck/step3"),
                        sound("naturalist:mob/duck/step4"),
                        sound("naturalist:mob/duck/step5")));

        add(NaturalistSoundEvents.RUBBER_DUCKY_AMBIENT, definition()
                .subtitle("naturalist.subtitles.entity.duck.ambient")
                .with(
                        sound("naturalist:mob/duck/rubber_ducky/idle1"),
                        sound("naturalist:mob/duck/rubber_ducky/idle2")));

        add(NaturalistSoundEvents.RUBBER_DUCKY_HURT, definition()
                .subtitle("naturalist.subtitles.entity.duck.hurt")
                .with(
                        sound("naturalist:mob/duck/rubber_ducky/hurt1"),
                        sound("naturalist:mob/duck/rubber_ducky/hurt2")));

        add(NaturalistSoundEvents.RUBBER_DUCKY_DEATH, definition()
                .subtitle("naturalist.subtitles.entity.duck.death")
                .with(
                        sound("naturalist:mob/duck/rubber_ducky/hurt1"),
                        sound("naturalist:mob/duck/rubber_ducky/hurt2")));

        add(NaturalistSoundEvents.LIZARD_AMBIENT, definition()
                .subtitle("naturalist.subtitles.entity.lizard.ambient")
                .with(
                        sound("naturalist:mob/lizard/idle1"),
                        sound("naturalist:mob/lizard/idle2"),
                        sound("naturalist:mob/lizard/idle3")));

        add(NaturalistSoundEvents.LIZARD_HURT, definition()
                .subtitle("naturalist.subtitles.entity.lizard.hurt")
                .with(
                        sound("naturalist:mob/lizard/hurt1"),
                        sound("naturalist:mob/lizard/hurt2"),
                        sound("naturalist:mob/lizard/hurt3")));

        add(NaturalistSoundEvents.LIZARD_DEATH, definition()
                .subtitle("naturalist.subtitles.entity.lizard.death")
                .with(
                        sound("naturalist:mob/lizard/death1"),
                        sound("naturalist:mob/lizard/death2")));

        add(NaturalistSoundEvents.BUTTERFLY_AMBIENT, definition()
                .subtitle("naturalist.subtitles.entity.butterfly.ambient")
                .with(
                        sound("naturalist:mob/butterfly/idle1"),
                        sound("naturalist:mob/butterfly/idle2")));

        add(NaturalistSoundEvents.BUTTERFLY_HURT, definition()
                .subtitle("naturalist.subtitles.entity.butterfly.hurt")
                .with(
                        sound("naturalist:mob/butterfly/hurt1"),
                        sound("naturalist:mob/butterfly/hurt2")));

        add(NaturalistSoundEvents.DRAGONFLY_HURT, definition()
                .subtitle("naturalist.subtitles.entity.dragonfly.hurt")
                .with(sound("naturalist:mob/dragonfly/hurt1")));

        add(NaturalistSoundEvents.DRAGONFLY_DEATH, definition()
                .subtitle("naturalist.subtitles.entity.dragonfly.death")
                .with(sound("naturalist:mob/dragonfly/death1")));

        add(NaturalistSoundEvents.CAPYBARA_AMBIENT, definition()
                .subtitle("naturalist.subtitles.entity.capybara.ambient")
                .with(
                        sound("naturalist:mob/capybara/idle1"),
                        sound("naturalist:mob/capybara/idle2"),
                        sound("naturalist:mob/capybara/idle3"),
                        sound("naturalist:mob/capybara/idle4")));

        add(NaturalistSoundEvents.CAPYBARA_HURT, definition()
                .subtitle("naturalist.subtitles.entity.capybara.hurt")
                .with(
                        sound("naturalist:mob/capybara/hurt1"),
                        sound("naturalist:mob/capybara/hurt2")));

        add(NaturalistSoundEvents.CAPYBARA_DEATH, definition()
                .subtitle("naturalist.subtitles.entity.capybara.death")
                .with(
                        sound("naturalist:mob/capybara/death1"),
                        sound("naturalist:mob/capybara/death2")));

        add(NaturalistSoundEvents.CAPYBARA_SLEEP, definition()
                .subtitle("naturalist.subtitles.entity.capybara.sleep")
                .with(sound("naturalist:mob/capybara/sleep1")));

        add(NaturalistSoundEvents.HEDGEHOG_AMBIENT, definition()
                .subtitle("naturalist.subtitles.entity.hedgehog.ambient")
                .with(
                        sound("naturalist:mob/hedgehog/idle1"),
                        sound("naturalist:mob/hedgehog/idle2"),
                        sound("naturalist:mob/hedgehog/idle3")));

        add(NaturalistSoundEvents.HEDGEHOG_HURT, definition()
                .subtitle("naturalist.subtitles.entity.hedgehog.hurt")
                .with(
                        sound("naturalist:mob/hedgehog/hurt1"),
                        sound("naturalist:mob/hedgehog/hurt2"),
                        sound("naturalist:mob/hedgehog/hurt3")));

        add(NaturalistSoundEvents.HEDGEHOG_DEATH, definition()
                .subtitle("naturalist.subtitles.entity.hedgehog.death")
                .with(
                        sound("naturalist:mob/hedgehog/death1"),
                        sound("naturalist:mob/hedgehog/death2")));

        add(NaturalistSoundEvents.HEDGEHOG_HIDE, definition()
                .subtitle("naturalist.subtitles.entity.hedgehog.hide")
                .with(
                        sound("naturalist:mob/hedgehog/hide1"),
                        sound("naturalist:mob/hedgehog/hide2"),
                        sound("naturalist:mob/hedgehog/hide3")));

        add(NaturalistSoundEvents.HEDGEHOG_UNHIDE, definition()
                .subtitle("naturalist.subtitles.entity.hedgehog.unhide")
                .with(
                        sound("naturalist:mob/hedgehog/unhide1"),
                        sound("naturalist:mob/hedgehog/unhide2")));

        add(NaturalistSoundEvents.MUSIC_DISC_DEATH_BY_HOGS, definition()
                .with(sound("naturalist:records/death_by_hogs").stream()));

        add(NaturalistSoundEvents.MUSIC_DISC_WILD_ONES, definition()
                .with(sound("naturalist:records/wild_ones").stream()));
    }
}
