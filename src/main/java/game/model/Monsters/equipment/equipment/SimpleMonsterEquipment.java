package game.model.Monsters.equipment.equipment;

import game.model.Characters.Character;
import game.model.Characters.characters.Archer;
import game.model.Characters.characters.Berserk;
import game.model.Items.EquipmentItems;
import game.model.Items.items.HealingItems;
import game.model.Items.items.Item;
import game.model.Items.items.armors.armors.IronChest;
import game.model.Items.items.armors.boots.IronBoots;
import game.model.Items.items.armors.helmets.IronHelmet;
import game.model.Items.items.heal.healHitPoint.items.BigHPBottle;
import game.model.Items.items.heal.healHitPoint.items.MiddleHPBottle;
import game.model.Items.items.heal.healHitPoint.items.SmallHPBottle;
import game.model.Items.items.heal.healManaPoint.items.BigFlower;
import game.model.Items.items.heal.healManaPoint.items.MiddleFlower;
import game.model.Items.items.heal.healManaPoint.items.SmallFlower;
import game.model.Items.items.weapons.weapons.archer.Bow;
import game.model.Items.items.weapons.weapons.archer.LegendaryBow;
import game.model.Items.items.weapons.weapons.archer.Sword;
import game.model.Items.items.weapons.weapons.berserk.BoxingGloves;
import game.model.Items.items.weapons.weapons.berserk.Gloves;
import game.model.Items.items.weapons.weapons.berserk.LegendaryBoxingGloves;
import game.model.Items.items.weapons.weapons.wizard.LegendaryStaff;
import game.model.Items.items.weapons.weapons.wizard.MagicWand;
import game.model.Items.items.weapons.weapons.wizard.Staff;
import game.model.Monsters.equipment.MonsterEquipment;
import game.model.Monsters.equipment.MonsterEquipmentFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class SimpleMonsterEquipment implements MonsterEquipment {

    private Random random = new Random();

    public HashMap<EquipmentItems, Item> initEquipment(Character character){
        HashMap<EquipmentItems, Item> equipment = new HashMap<>();
        int i = random.nextInt(10);
        if (character instanceof Archer){
            if ((i > 0) && (i < 3))
                equipment.put(Bow.itemsFactory.createNewItem(character).EQUIPMENT_ITEMS(), Bow.itemsFactory.createNewItem(character));
            else
                equipment.put(Sword.itemsFactory.createNewItem(character).EQUIPMENT_ITEMS(), Sword.itemsFactory.createNewItem(character));
        } else if (character instanceof Berserk){
            if ((i > 0) && (i < 3))
                equipment.put(BoxingGloves.itemsFactory.createNewItem(character).EQUIPMENT_ITEMS(), BoxingGloves.itemsFactory.createNewItem(character));
            else
                equipment.put(Gloves.itemsFactory.createNewItem(character).EQUIPMENT_ITEMS(), Gloves.itemsFactory.createNewItem(character));
        } else {
            if ((i > 0) && (i < 3))
                equipment.put(Staff.itemsFactory.createNewItem(character).EQUIPMENT_ITEMS(), Staff.itemsFactory.createNewItem(character));
            else
                equipment.put(MagicWand.itemsFactory.createNewItem(character).EQUIPMENT_ITEMS(), MagicWand.itemsFactory.createNewItem(character));
        }
        equipment.put(IronHelmet.itemsFactory.createNewItem(character).EQUIPMENT_ITEMS(), IronHelmet.itemsFactory.createNewItem(character));
        equipment.put(IronChest.itemsFactory.createNewItem(character).EQUIPMENT_ITEMS(), IronChest.itemsFactory.createNewItem(character));
        equipment.put(IronBoots.itemsFactory.createNewItem(character).EQUIPMENT_ITEMS(), IronBoots.itemsFactory.createNewItem(character));
        return equipment;
    }

    public List<HealingItems> initializeItemList(){
        List<HealingItems> itemsList = new ArrayList<>();
        itemsList.add(BigHPBottle.healingHitPointItemsFactory.getNewHealingHitPointItem());
        itemsList.add(BigFlower.healingHitPointItemsFactory.getNewHealingManaPointItem());
        itemsList.add(MiddleHPBottle.healingHitPointItemsFactory.getNewHealingHitPointItem());
        itemsList.add(MiddleFlower.healingManaPointItemsFactory.getNewHealingManaPointItem());
        itemsList.add(SmallHPBottle.healingHitPointItemsFactory.getNewHealingHitPointItem());
        itemsList.add(SmallFlower.healingManaPointItemsFactory.getNewHealingManaPointItem());

        return  itemsList;
    }




    public static MonsterEquipmentFactory monsterEquipmentFactory = SimpleMonsterEquipment::new;
}
