package game.model.abilities.buffs.buffs;

import game.model.abilities.MagicClasses;
import game.model.abilities.MagicFactory;
import game.model.abilities.buffs.BuffClasses;
import game.model.abilities.buffs.BuffMagic;

import java.util.HashMap;
import java.util.Map;

public class ArmorBuff implements BuffMagic {

    private int level;
    private int timeOfActions = 1;
    private final int power;
    private final int defence;

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
    public void setLevel() {
        timeOfActions++;
    }

    @Override
    public int getManaCost() {
        return 0;
    }

    @Override
    public void setDamage() {

    }

    @Override
    public int getTimeOfAction() {
        return timeOfActions;
    }

    @Override
    public MagicClasses getMagicClass() {
        return MagicClasses.BUFF;
    }

    @Override
    public String toString(){
        return this.getClass().getSimpleName();
    }

    public static MagicFactory magicFactory = ArmorBuff::new;
}
