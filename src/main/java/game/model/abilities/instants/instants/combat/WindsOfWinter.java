package game.model.abilities.instants.instants.combat;

import game.model.abilities.MagicClasses;
import game.model.abilities.MagicFactory;
import game.model.abilities.instants.InstantMagic;

public class WindsOfWinter implements InstantMagic{

    private int level;
    private int damage = 60;
    private int manaCost;

    private WindsOfWinter(int level){
        this.level = level;
        this.manaCost = getLevel() * 10;
    }

    @Override
    public int getDamage() {
        return damage;
    }

    @Override
    public void setDamage() {
        damage += 60;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int getManaCost() {
        return manaCost;
    }

    @Override
    public MagicClasses getMagicClass() {
        return MagicClasses.COMBAT;
    }

    public String toString(){
        return this.getClass().getSimpleName();
    }

    public static MagicFactory magicFactory = WindsOfWinter::new;
}
