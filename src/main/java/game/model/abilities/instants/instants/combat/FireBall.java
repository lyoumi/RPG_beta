package game.model.abilities.instants.instants.combat;

import game.model.abilities.MagicClasses;
import game.model.abilities.MagicFactory;
import game.model.abilities.instants.InstantMagic;

/**
 * Боевая магия, наносящая урон противнику
 */
public class FireBall implements InstantMagic {
    private int level;
    private int damage = 50;
    private int manaCost;

    private FireBall(int level){
        this.level = level + 1;
        this.manaCost = getLevel() * 10;
    }

    @Override
    public int getDamage() {
        return damage;
    }

    @Override
    public void setDamage() {
        this.damage += 50;
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
        return MagicClasses.INSTANT;
    }

    public static MagicFactory magicFactory = FireBall::new;
}
