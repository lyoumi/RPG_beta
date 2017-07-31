package game.model.Items.items.heal.healHitPoint;

import game.model.Characters.Human;
import game.model.Items.items.HealingItems;
import game.model.Items.items.heal.HealingItemsFactory;
import game.model.Items.items.heal.HealingItemsList;

public class MiddleHPBottle implements HealingItems {

    private final int price;

    private MiddleHPBottle(){
        this.price = 150;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public HealingItemsList getHealingItemClass() {
        return HealingItemsList.MiddleHPBottle;
    }

    @Override
    public void use(Human human) {
        human.setHitPoint(human.getHitPoint() + human.getMaxHitPoint()/2);
    }

    public static HealingItemsFactory healingItemsFactory = MiddleHPBottle::new;

    public String toString(){
        return MiddleHPBottle.class.getSimpleName();
    }
}
