package game.model.Items.items.armors.armors;

import game.model.Characters.Human;
import game.model.Items.EquipmentItems;
import game.model.Items.items.ItemsFactory;
import game.model.Items.items.armors.Armor;
import game.model.abilities.Magic;
import game.model.abilities.buffs.buffs.ArchersBuff;
import game.model.abilities.buffs.buffs.ArmorBuff;

import java.util.Random;

/**
 * Created by pikachu on 17.07.17.
 */
public class IronChest implements Armor {

    private int defence;
    private int itemLevel;
    private Human human;
    private Magic magic;
    private final int price;

    private Random random = new Random();

    private IronChest(Human human){
        this.human = human;
        this.itemLevel = random.nextInt(human.getLevel() + 1);
        this.price = getItemLevel()*100;
        this.defence = this.getItemLevel() * 10 + 5;
        this.magic = ArmorBuff.magicFactory.getMagicFactory(getItemLevel());
    }

    @Override
    public EquipmentItems EQUIPMENT_ITEMS() {
        return EquipmentItems.ARMOR;
    }

    @Override
    public int getItemLevel() {
        return itemLevel;
    }

    @Override
    public int getDefence() {
        return defence;
    }

    @Override
    public String getName() {
        return IronChest.class.getSimpleName();
    }

    @Override
    public Magic getBuff() {
        return magic;
    }

    @Override
    public int getPrice() {
        return price;
    }

    public String toString(){
        return IronChest.class.getSimpleName() + ": DEF +" + getDefence();
    }

    public static ItemsFactory itemsFactory = IronChest::new;
}
