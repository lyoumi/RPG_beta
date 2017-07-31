package game.model.Items.items.weapons.weapons;

import game.model.Characters.Human;
import game.model.Items.EquipmentItems;
import game.model.Items.items.ItemsFactory;
import game.model.Items.items.weapons.Weapons;
import game.model.abilities.Magic;
import game.model.abilities.buffs.buffs.ArchersBuff;

import java.util.Random;

/**
 * Created by pikachu on 17.07.17.
 */
public class Bow implements Weapons {
    private int damage;
    private int itemLevel;
    private Human human;
    private Magic magic;
    private final int price;

    private Random random = new Random();

    private Bow(Human human){
        this.human = human;
        this.itemLevel = random.nextInt(human.getLevel() + 1);
        this.price = 100* getItemLevel();
        this.damage = getItemLevel() * 7 + 5;
        this.magic = ArchersBuff.magicFactory.getMagicFactory(human.getLevel());
    }

    @Override
    public Magic getBuff() {
        return magic;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public EquipmentItems EQUIPMENT_ITEMS() {
        return EquipmentItems.HANDS;
    }

    @Override
    public int getDamage() {
        return damage;
    }

    @Override
    public int getItemLevel() {
        return itemLevel;
    }

    @Override
    public String getName() {
        return Bow.class.getSimpleName();
    }

    @Override
    public String toString(){
        return Bow.class.getSimpleName() + ": " + " ATK +" + getDamage();
    }

    public static ItemsFactory itemsFactory = Bow::new;
}
