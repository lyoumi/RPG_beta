package game.model.Items.items.heal.healManaPoint;

import game.model.Characters.Character;
import game.model.Items.items.HealingItems;
import game.model.Items.items.heal.HealingItemsList;

public interface HealingManaPointItems extends HealingItems{
    @Override
    int getPrice();

    @Override
    HealingItemsList getHealingItemClass();

    @Override
    void use(Character character);
}
