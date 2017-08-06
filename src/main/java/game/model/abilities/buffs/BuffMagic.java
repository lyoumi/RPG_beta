package game.model.abilities.buffs;

import game.model.abilities.Magic;
import game.model.abilities.MagicClasses;

import java.util.Map;

/**
 * Created by pikachu on 18.07.17.
 */
public interface BuffMagic extends Magic{

    /**
     * Map, которая возвращает название эффекта с его параметром
     *
     * @return
     *          map
     */
    Map<BuffClasses, Integer> getEffect();

    /**
     * Метод, который возвращает уровень способности, соответствующий текущему уровню героя + 1 из {@link game.controller.PlayerController}
     *
     * @return
     *          int level
     */
    @Override
    int getLevel();

    void setLevel();

    /**
     * Метод, который возвращает ману, требуемую на выполнение заклинания
     *
     * @return
     *          int manaCost
     */
    @Override
    int getManaCost();

    /**
     * Метод, который возвращает тип заклинания из {@link MagicClasses}
     *
     * @return
     *          magicClass
     */
    @Override
    MagicClasses getMagicClass();
}
