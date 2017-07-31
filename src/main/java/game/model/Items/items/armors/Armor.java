package game.model.Items.items.armors;

import game.model.Items.EquipmentItems;
import game.model.Items.items.Item;
import game.model.abilities.Magic;

/**
 * Created by pikachu on 17.07.17.
 */
public interface Armor extends Item {

    /**
     * Метод, возвращающий уровень текущей брони.
     *
     * @return
     *          int level of current armor.
     */
    int getItemLevel();

    /**
     * Метод, возвращающий защиту текущей брони.
     *
     * @return
     *          int damage of current armor.
     */
    int getDefence();

    /**
     * Метод, возвращающий имя текущей брони
     *
     * @return
     *          name of current armor.
     */
    String getName();

    /**
     * Метод, который возвращает слот для экипировки текущего предмета.
     *
     * @return
     *          slot position of current armor.
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
