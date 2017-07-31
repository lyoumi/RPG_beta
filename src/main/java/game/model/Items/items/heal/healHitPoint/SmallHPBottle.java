package game.model.Items.items.heal.healHitPoint;

import game.model.Characters.Human;
import game.model.Items.items.HealingItems;
import game.model.Items.items.heal.HealingItemsFactory;
import game.model.Items.items.heal.HealingItemsList;

public class SmallHPBottle implements HealingItems {

    private final int price;

    private SmallHPBottle(){
        this.price = 100;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public HealingItemsList getHealingItemClass() {
        return HealingItemsList.SmallHPBottle;
    }

    @Override
    public void use(Human human) {
        human.setHitPoint(human.getHitPoint() + human.getMaxHitPoint()/4);
    }

    public static HealingItemsFactory healingItemsFactory = SmallHPBottle::new;

    public String toString(){
        return SmallHPBottle.class.getSimpleName();
    }
}
