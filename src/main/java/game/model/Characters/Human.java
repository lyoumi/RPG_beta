package game.model.Characters;

import game.model.Items.items.HealingItems;
import game.model.abilities.Magic;

import java.util.ArrayList;

/**
 * Created by pikachu on 13.07.17.
 */

/**
 * Базовый интерфейс для реализации классов персонажей.
 */
public interface Human {

    String getName();

    /**
     * Метод, возвращающий текущее количество золота у персонажа.
     *
     * @return
     *          int amount of gold.
     */
    int getGold();

    /**
     * Метод для изменения текущего количества золота.
     *
     * @param gold
     *              new amount of gold.
     */
    void setGold(int gold);

    /**
     * Метод для изменения текущего количества маны
     *
     * @return
     *          new amount of mana
     */
    int getManaPoint();

    /**
     * Метод для изменения текущего количества маны.
     *
     * @param mana
     *          new amount of mana.
     */
    void setManaPoint(int mana);

    /**
     * Метод, возвращаюший текущее количество маны
     *
     * @return
     *          int amount of skillPoint.
     */
    int getMagicPoint();

    /**
     * Метод для изменения текущего количества очков умений для прокачки персонажа.
     *
     * @param magicPoint
     *          new amount of magicPoint
     */
    void setMagicPoint(int magicPoint);

    /**
     * Метод, возвращающий текущее значение урона персонада.
     *
     * @return
     *          int damage
     */
    int getDamage();

    /**
     * Метод, возвращаюший значение полученного урона.
     *
     * @param damage
     *          int applied damage
     * @return
     *          int applied damage
     */
    int applyDamage(int damage);

    /**
     * Метод, возвращающий текущее количество здоровья.
     *
     * @return
     *          int hitPoint.
     */
    int getHitPoint();

    /**
     * Метод для изменения текущего количества здоровья.
     *
     * @param hitPoint
     *          new hitPoint value.
     */
    void setHitPoint(int hitPoint);

    /**
     * Метод, возвращающий максимальное количество здоровья с текущими зарактеристиками.
     *
     * @return
     *          maxHitPoint.
     */
    int getMaxHitPoint();

    /**
     * Метод, возвращающий максимальное количество маны с текущими зарактеристиками.
     *
     * @return
     *          maxManaPoint.
     */
    int getMaxManaPoint();

    /**
     * Метод, возвращающий инвентарь игрока в виде {@link ArrayList}
     *
     * @return
     *          list of {@link HealingItems}
     */
    ArrayList<HealingItems> getInventory();

    /**
     * Метод, вызываюшийся после убийства монстра и добавлении нового опыта.
     *
     * @param experience
     *          new amount of experience.
     */
    void experienceDrop(double experience);

    /**
     * Метод, возвращающий текущий уровень героя.
     *
     * @return
     *          int current level.
     */
    int getLevel();

    /**
     * Метод, изменяющий текущее количество урона.
     *
     * @param damage
     *          new damage.
     */
    void setDamage(int damage);

    /**
     * Метод реализующий использование магии у героя.
     *
     * @param magic
     *          implementation of {@link Magic}
     * @return
     *          magic damage.
     */
    int getMagic(Magic magic);

    boolean healHitPoint();

    boolean healManaPoint();

    boolean checkHitPointBottle();

    boolean checkManaPointBottle();
}
