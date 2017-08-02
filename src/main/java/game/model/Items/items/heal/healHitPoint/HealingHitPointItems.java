package game.model.Items.items.heal.healHitPoint;

import game.model.Characters.Character;
import game.model.Items.items.HealingItems;
import game.model.Items.items.heal.HealingItemsList;

public interface HealingHitPointItems extends HealingItems{
    @Override
    int getPrice();

    @Override
    HealingItemsList getHealingItemClass();

    @Override
    void use(Character character);
}
