package game.model.abilities.debuffs;

import game.model.abilities.Magic;

/**
 * Created by pikachu on 18.07.17.
 */
public interface DebuffMagic extends Magic {

    /**
     * Метод, который возвращает урон наносимый переодической способностью
     *
     * @return
     *          int damage
     */
    int getDamage();

    /**
     * Метод, который возвращает количество ходов, которые будет действовать текщая способность.
     * @return
     *          int count
     */
    int getTimeOfAction();
}
