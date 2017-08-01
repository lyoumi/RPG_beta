package game.model.Items.items.heal.healHitPoint.items;

import game.model.Characters.Human;
import game.model.Items.items.HealingItems;
import game.model.Items.items.heal.HealingItemsFactory;
import game.model.Items.items.heal.HealingItemsList;
import game.model.Items.items.heal.healHitPoint.HealingHitPointItems;
import game.model.Items.items.heal.healHitPoint.HealingHitPointItemsFactory;

public class MiddleHPBottle implements HealingHitPointItems {

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

    public static HealingHitPointItemsFactory healingHitPointItemsFactory = MiddleHPBottle::new;


    public String toString(){
        return MiddleHPBottle.class.getSimpleName();
    }
}
