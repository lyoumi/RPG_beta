package game.model.abilities.instants;

import game.model.abilities.Magic;
import game.model.abilities.MagicClasses;

/**
 * Created by pikachu on 18.07.17.
 */

/**
 * Базовый класс для магии с мгновенным эффектом.
 */
public interface InstantMagic extends Magic{

    /**
     * Метод, который возвращает урон наносимый способностью мгновенного действия.
     *
     * @return
     *          int damage
     */
    int getDamage();

    /**
     * Изменение урона после улучшения магии.
     */
    void setDamage();

    /**
     * Метод, который возвращает уровень способности, соответствующий текущему уровню героя + 1 из {@link game.controller.PlayerController}
     *
     * @return
     *          int level
     */
    @Override
    int getLevel();

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
