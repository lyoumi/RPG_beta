package game.model.Items.items;

import game.model.Characters.Human;

public interface ItemsFactory {
    Item createNewItem(Human human);
}
