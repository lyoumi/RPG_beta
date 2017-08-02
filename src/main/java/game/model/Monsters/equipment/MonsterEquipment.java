package game.model.Monsters.equipment;

import game.model.Characters.Character;
import game.model.Items.EquipmentItems;
import game.model.Items.items.HealingItems;
import game.model.Items.items.Item;

import java.util.HashMap;
import java.util.List;

public interface MonsterEquipment {

    HashMap<EquipmentItems, Item> initEquipment(Character character);

    List<HealingItems> initializeItemList();
}
