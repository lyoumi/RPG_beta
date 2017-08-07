package game.model.abilities.buffs.buffs;

import game.model.abilities.MagicClasses;
import game.model.abilities.MagicFactory;
import game.model.abilities.buffs.BuffClasses;
import game.model.abilities.buffs.BuffMagic;

import java.util.HashMap;
import java.util.Map;

public class ForceOfJedi implements BuffMagic{

    private int level;
    private int timeOfActions = 1;
    private int manaCost;
    private int power = 5;
    private int intelligence = 7;

    private ForceOfJedi(int level) {
        this.level = level;
        this.manaCost = getLevel()*7;
    }

    @Override
    public Map<BuffClasses, Integer> getEffect() {
        Map<BuffClasses, Integer> effects = new HashMap<>();
        effects.put(BuffClasses.power, power);
        effects.put(BuffClasses.intelligence, intelligence);
        return effects;
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
        return manaCost;
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

    public static MagicFactory magicFactory = ForceOfJedi::new;
}
