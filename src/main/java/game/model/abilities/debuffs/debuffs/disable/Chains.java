package game.model.abilities.debuffs.debuffs.disable;

import game.model.abilities.MagicClasses;
import game.model.abilities.MagicFactory;
import game.model.abilities.debuffs.DebuffMagic;

public class Chains implements DebuffMagic{

    private int damage;
    private int level;
    private int timeOfActions = 5;

    private Chains(int level){
        this.level = level + 1;
        this.damage = getLevel()*2;
    }

    @Override
    public int getDamage() {
        System.out.println("He in ice");
        return damage;
    }

    @Override
    public int getTimeOfAction() {
        return --timeOfActions;
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
        return MagicClasses.COMBAT;
    }

    @Override
    public String toString(){
        return this.getClass().getSimpleName();
    }

    public static MagicFactory magicFactory = Chains::new;
}
