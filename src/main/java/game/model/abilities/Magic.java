package game.model.abilities;

/**
 * Created by pikachu on 18.07.17.
 */

/**
 * Базовый интерфейс для магий.
 */
public interface Magic {

    /**
     * Метод, который возвращает уровень способности, соответствующий текущему уровню героя + 1 из {@link game.controller.PlayerController}
     *
     * @return
     *          int level
     */
    int getLevel();

    /**
     * Метод, который возвращает ману, требуемую на выполнение заклинания
     *
     * @return
     *          int manaCost
     */
    int getManaCost();

    /**
     * Метод, который возвращает тип заклинания из {@link MagicClasses}
     *
     * @return
     *          magicClass
     */
    MagicClasses getMagicClass();

}
