package game.model.Items.items.heal.healHitPoint.items;

import game.model.Characters.Character;
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
    public void use(Character character) {
        character.setHitPoint(character.getHitPoint() + character.getMaxHitPoint()/2);
    }

    public static HealingHitPointItemsFactory healingHitPointItemsFactory = MiddleHPBottle::new;

    @Override
    public void finalize() throws Throwable {
        super.finalize();
    }

    public String toString(){
        return MiddleHPBottle.class.getSimpleName();
    }
}
