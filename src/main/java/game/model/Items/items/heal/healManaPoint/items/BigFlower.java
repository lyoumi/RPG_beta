package game.model.Items.items.heal.healManaPoint.items;

import game.model.Characters.Human;
import game.model.Items.items.heal.HealingItemsList;
import game.model.Items.items.heal.healHitPoint.items.BigHPBottle;
import game.model.Items.items.heal.healManaPoint.HealingManaPointItems;
import game.model.Items.items.heal.healManaPoint.HealingManaPointItemsFactory;

public class BigFlower implements HealingManaPointItems {

    private final int price;

    private BigFlower(){
        this.price = 200;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public HealingItemsList getHealingItemClass() {
        return HealingItemsList.SmallFlower;
    }

    @Override
    public void use(Human human) {
        human.setManaPoint(human.getMaxManaPoint());
    }

    public static HealingManaPointItemsFactory healingHitPointItemsFactory = BigFlower::new;

    public String toString(){
        return BigFlower.class.getSimpleName();
    }
}
