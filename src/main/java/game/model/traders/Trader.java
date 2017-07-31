package game.model.traders;

import game.model.Items.items.HealingItems;
import game.model.Items.items.Item;

import java.util.List;
import java.util.Map;

/**
 * Базовый интерфейс для реализации классов торговцев.
 */
public interface Trader {

    /**
     * Метод, возвращающий купленный предмет экипировки.
     *
     * @param id
     *          item id
     * @return
     *          equipment item
     */
    Item getEquipmentItem(int id);

    /**
     * Метод, возвращающий предметы для восстановления здоровья/маны в количестве заданным пользователем
     *
     * @param count
     *              count of items purchased
     * @param id
     *              id of items purchased
     * @return
     *              list of items purchased
     */
    List<HealingItems> getHealItems(int count, int id);

    /**
     * Метод, возвращающий список с товаров и их id в {@link Map}
     *
     * @return
     *          priceList of equipment items
     */
    Map<Integer, Item> getPriceListEquipmentObjects();

    /**
     * Метод, возвращающий список с товаров и их id в {@link Map}
     *
     * @return
     *          priceList of equipment items
     */
    Map<Integer, HealingItems> getPriceListHealingObjects();
}
