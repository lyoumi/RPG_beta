package game.model.Items.items.weapons;

import game.model.Items.EquipmentItems;
import game.model.Items.items.Item;
import game.model.abilities.Magic;

/**
 * Created by pikachu on 17.07.17.
 */

/**
 * Базовый интерфейс для оружия.
 * Наследуется от класса {@link Item}
 */
public interface Weapons extends Item {

    /**
     * Метод, возвращающий урон текущего оружия.
     *
     * @return
     *          int damage of current weapon.
     */
    int getDamage();

    /**
     * Метод, возвращающий уровень текущего оружия.
     *
     * @return
     *          int level of current weapon.
     */
    int getItemLevel();

    /**
     * Метод, возвращающий имя текущего оружия
     *
     * @return
     *          name of current weapon.
     */
    String getName();

    /**
     * Метод, который возвращает слот для экипировки текущего предмета.
     *
     * @return
     *          slot position of current item.
     */
    EquipmentItems EQUIPMENT_ITEMS();

    /**
     * Метод, который возвращает бафф текущего предмета.
     *
     * @return
     *          implementation of {@link game.model.abilities.buffs.BuffMagic}
     */
    Magic getBuff();

    /**
     * Метод, который возвращает стоимость предмета.
     *
     * @return
     *          price of current item.
     */
    int getPrice();
}
