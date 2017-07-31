package game.model.abilities.buffs.buffs;

import game.model.abilities.MagicClasses;
import game.model.abilities.MagicFactory;
import game.model.abilities.buffs.BuffClasses;
import game.model.abilities.buffs.BuffMagic;

import java.util.HashMap;
import java.util.Map;

public class ArmorBuff implements BuffMagic {

    private final int power;
    private final int defence;
    private int level;

    private ArmorBuff(int level) {
        this.power = 7;
        this.defence = 10;
        this.level = level;
    }

    @Override
    public Map<BuffClasses, Integer> getEffect() {
        Map<BuffClasses, Integer> effect = new HashMap<>();
        effect.put(BuffClasses.defence, defence);
        effect.put(BuffClasses.power, power);
        return effect;
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
        return null;
    }

    public static MagicFactory magicFactory = ArmorBuff::new;
}
