package game.model.traders.traders;

import game.model.Characters.Character;
import game.model.Characters.characters.Archer;
import game.model.Characters.characters.Berserk;
import game.model.Items.items.HealingItems;
import game.model.Items.items.Item;
import game.model.Items.items.heal.healHitPoint.items.BigHPBottle;
import game.model.Items.items.heal.healManaPoint.items.BigFlower;
import game.model.Items.items.weapons.weapons.archer.LegendaryBow;
import game.model.Items.items.weapons.weapons.berserk.LegendaryBoxingGloves;
import game.model.Items.items.weapons.weapons.wizard.LegendaryStaff;
import game.model.traders.Trader;
import game.model.traders.TradersFactory;
import ext.RandomUniqueValue;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс-реализация торговца.
 */
public class SimpleTrader implements Trader {

    /**
     * Создаем экземпляр класса для генерации случайного значения типа int.
     */
    private RandomUniqueValue randomUniqueValue = new RandomUniqueValue();

    /**
     * Мар, содержащая id предмета и предметы экипировки.
     */
    private Map<Integer, Item> priceListEquipmentObjects = new LinkedHashMap<>();

    /**
     * Мар, содержащая id предмета и предметы для восстановления здоровья/маны.
     */
    private Map<Integer, HealingItems> priceListHealingObjects = new LinkedHashMap<>();

    /**
     * Объект типа {@link Character} хранящий в себе имплементацию конкретного персонажа.
     */
    private Character character;

    /**
     * Конструктор, инициализирующий map'ы предметов и объект.
     * Также вызывается метод заполняющий map'ы объектами.
     *
     * @param character
     *              character implementation of {@link Character}
     */
    private SimpleTrader(Character character){
        this.character = character;
        generatePriceList();
    }

    /**
     * Метод, заполняющий map'ы предметами и их id.
     */
    private void generatePriceList(){
        if (character instanceof Archer) priceListEquipmentObjects.put(randomUniqueValue.nextUniqueInt(), LegendaryBow.itemsFactory.createNewItem(character));
        else if (character instanceof Berserk) priceListEquipmentObjects.put(randomUniqueValue.nextUniqueInt(), LegendaryBoxingGloves.itemsFactory.createNewItem(character));
        else priceListEquipmentObjects.put(randomUniqueValue.nextUniqueInt(), LegendaryStaff.itemsFactory.createNewItem(character));
        priceListHealingObjects.put(randomUniqueValue.nextUniqueInt(), BigFlower.healingHitPointItemsFactory.getNewHealingManaPointItem());
        priceListHealingObjects.put(randomUniqueValue.nextUniqueInt(), BigHPBottle.healingHitPointItemsFactory.getNewHealingHitPointItem());
    }

    @Override
    public Item getEquipmentItem(int id) {
        Item item = priceListEquipmentObjects.get(id);
        priceListEquipmentObjects.remove(id);
        return item;
    }

    @Override
    public List<HealingItems> getHealItems(int count, int id) {
        List<HealingItems> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(priceListHealingObjects.get(id));
        }
        return list;
    }

    @Override
    public Map<Integer, Item> getPriceListEquipmentObjects() {
        return priceListEquipmentObjects;
    }

    @Override
    public Map<Integer, HealingItems> getPriceListHealingObjects() {
        return priceListHealingObjects;
    }

    public static TradersFactory tradersFactory = SimpleTrader::new;
}
