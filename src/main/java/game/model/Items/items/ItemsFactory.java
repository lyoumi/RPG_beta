package game.model.Items.items;

import game.model.Characters.Character;

public interface ItemsFactory {
    Item createNewItem(Character character);
}
