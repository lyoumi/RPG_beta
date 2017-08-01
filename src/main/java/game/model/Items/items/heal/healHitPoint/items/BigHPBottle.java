package game.model.Items.items.heal.healHitPoint.items;

import game.model.Characters.Human;
import game.model.Items.items.HealingItems;
import game.model.Items.items.heal.HealingItemsFactory;
import game.model.Items.items.heal.HealingItemsList;
import game.model.Items.items.heal.healHitPoint.HealingHitPointItems;
import game.model.Items.items.heal.healHitPoint.HealingHitPointItemsFactory;

public class BigHPBottle implements HealingHitPointItems {

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

//    public static HealingItemsFactory healingItemsFactory = new HealingItemsFactory() {
//        @Override
//        public HealingHitPointItems getHealingItem() {
//            return new BigHPBottle();
//        }
//    };

    public static HealingHitPointItemsFactory healingHitPointItemsFactory = BigHPBottle::new;

    public String toString(){
        return BigHPBottle.class.getSimpleName();
    }
}
