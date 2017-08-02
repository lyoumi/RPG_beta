package game.model.abilities.instants.instants.healing;

import game.model.abilities.MagicClasses;
import game.model.abilities.MagicFactory;
import game.model.abilities.instants.InstantMagic;

public class SmallHealing implements InstantMagic {

    private int maxHitPoint;

    private SmallHealing(int maxHitPoint){
        this.maxHitPoint = maxHitPoint;
    }

    @Override
    public int getDamage() {
        return maxHitPoint/4;
    }

    @Override
    public void setDamage() {

    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getManaCost() {
        return 40;
    }

    @Override
    public MagicClasses getMagicClass() {
        return MagicClasses.HEALING;
    }

    @Override
    public String toString(){
        return this.getClass().getSimpleName();
    }

    public static MagicFactory magicFactory = SmallHealing::new;
}
