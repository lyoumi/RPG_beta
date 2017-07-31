package game.model.Items.items.heal.healManaPoint;

import game.model.Characters.Human;
import game.model.Items.items.HealingItems;
import game.model.Items.items.heal.HealingItemsFactory;
import game.model.Items.items.heal.HealingItemsList;

public class MiddleFlower implements HealingItems{

    private final int price;

    private MiddleFlower(){
        this.price = 150;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public HealingItemsList getHealingItemClass() {
        return HealingItemsList.MiddleFlower;
    }

    @Override
    public void use(Human human) {
        human.setManaPoint(human.getManaPoint() + human.getMaxManaPoint()/2);
    }

    public static HealingItemsFactory healingItemsFactory = MiddleFlower::new;

    public String toString(){
        return MiddleFlower.class.getSimpleName();
    }
}
