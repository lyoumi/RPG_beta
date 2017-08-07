package game.model.abilities.buffs.buffs;

import game.model.abilities.MagicClasses;
import game.model.abilities.MagicFactory;
import game.model.abilities.buffs.BuffClasses;
import game.model.abilities.buffs.BuffMagic;

import java.util.HashMap;
import java.util.Map;

public class WizardBuff  implements BuffMagic{

    private int level;
    private int timeOfActions = 1;
    private final int agility = 2;
    private final int power = 3;
    private final int intelligence = 5;

    private WizardBuff(int level){
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

    public static MagicFactory magicFactory = WizardBuff::new;
}
