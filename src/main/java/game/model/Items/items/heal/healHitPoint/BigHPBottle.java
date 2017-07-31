package game.model.Items.items.heal.healHitPoint;

import game.model.Characters.Human;
import game.model.Items.items.HealingItems;
import game.model.Items.items.heal.HealingItemsFactory;
import game.model.Items.items.heal.HealingItemsList;

public class BigHPBottle implements HealingItems {

    private final int price;

    private BigHPBottle(){
        this.price = 200;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public HealingItemsList getHealingItemClass() {
        return HealingItemsList.BigHPBottle;
    }

    @Override
    public void use(Human human) {
        human.setHitPoint(human.getMaxHitPoint());
    }

    public static HealingItemsFactory healingItemsFactory = BigHPBottle::new;

    public String toString(){
        return BigHPBottle.class.getSimpleName();
    }
}
