package game.model.abilities.buffs.buffs;

import game.model.abilities.MagicClasses;
import game.model.abilities.MagicFactory;
import game.model.abilities.buffs.BuffClasses;
import game.model.abilities.buffs.BuffMagic;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pikachu on 18.07.17.
 */
public class ArchersBuff implements BuffMagic {

    private int level;
    private final int agility = 6;
    private final int power = 5;
    private final int intelligence = 4;

    private ArchersBuff(int level){
        this.level = level;
    }

    @Override
    public Map<BuffClasses, Integer> getEffect() {
        Map<BuffClasses, Integer> effects = new HashMap<>();
        effects.put(BuffClasses.agility, agility);
        effects.put(BuffClasses.power, power);
        effects.put(BuffClasses.intelligence, intelligence);
        return effects;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int getManaCost() {
        return 0;
    }

    @Override
    public MagicClasses getMagicClass() {
        return MagicClasses.BUFF;
    }

    public static MagicFactory magicFactory = ArchersBuff::new;
}
