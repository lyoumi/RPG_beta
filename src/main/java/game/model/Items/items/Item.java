package game.model.Items.items;

import game.model.Items.EquipmentItems;
import game.model.abilities.Magic;

/**
 * Created by pikachu on 17.07.17.
 */

/**
 * Базовый интерфейс для создания предметов.
 */
public interface Item {

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
