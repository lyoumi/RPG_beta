package game.model.abilities.debuffs.debuffs.damage;

import game.model.abilities.MagicClasses;
import game.model.abilities.MagicFactory;
import game.model.abilities.debuffs.DebuffMagic;

public class BurningJoe implements DebuffMagic {

    private int level;
    private int damage;
    private int timeOfAction = 5;

    private BurningJoe(int level){
        this.level = level + 1;
        this.damage = getLevel()*3;
    }

    @Override
    public int getTimeOfAction() {
        timeOfAction -= 1;
        return timeOfAction;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int getManaCost() {
        return 30;
    }

    @Override
    public int getDamage() {
        return damage;
    }

    @Override
    public MagicClasses getMagicClass() {
        return MagicClasses.DEBUFF;
    }

    public static MagicFactory magicFactory = BurningJoe::new;
}
