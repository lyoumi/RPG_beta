package game.model.Items.items.weapons.weapons;

import game.model.Characters.Human;
import game.model.Items.EquipmentItems;
import game.model.Items.items.ItemsFactory;
import game.model.Items.items.weapons.Weapons;
import game.model.abilities.Magic;
import game.model.abilities.buffs.buffs.ArchersBuff;
import game.model.abilities.debuffs.debuffs.damage.BurningJoe;

import java.util.Random;

public class LegendaryArcherBow implements Weapons {
    private int damage;
    private int itemLevel;
    private Human human;
    private Magic buff;
    private Magic magic;

    private Random random = new Random();

    private LegendaryArcherBow(Human human){
        this.human = human;
        this.itemLevel = random.nextInt(human.getLevel() + 1) + 5;
        this.damage = getItemLevel() * 9;
        this.buff = ArchersBuff.magicFactory.getMagicFactory(human.getLevel());
        this.magic = BurningJoe.magicFactory.getMagicFactory(getItemLevel());
    }

    @Override
    public EquipmentItems EQUIPMENT_ITEMS() {
        return EquipmentItems.HANDS;
    }

    @Override
    public int getDamage() {
        return damage + ((BurningJoe)magic).getDamage();
    }

    @Override
    public int getItemLevel() {
        return itemLevel;
    }

    @Override
    public String getName() {
        return LegendaryArcherBow.class.getSimpleName();
    }

    @Override
    public Magic getBuff() {
        return buff;
    }

    @Override
    public int getPrice() {
        return 0;
    }

    public static ItemsFactory itemsFactory = LegendaryArcherBow::new;
}
