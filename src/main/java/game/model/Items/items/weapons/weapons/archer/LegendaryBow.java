package game.model.Items.items.weapons.weapons.archer;

import game.model.Characters.Character;
import game.model.Items.EquipmentItems;
import game.model.Items.items.ItemsFactory;
import game.model.Items.items.weapons.Weapons;
import game.model.abilities.Magic;
import game.model.abilities.buffs.buffs.ArchersBuff;
import game.model.abilities.debuffs.debuffs.damage.BurningJoe;

import java.util.Random;

public class LegendaryBow implements Weapons {
    private int damage;
    private int itemLevel;
    private Character character;
    private Magic buff;
    private Magic magic;

    private Random random = new Random();

    private LegendaryBow(Character character){
        this.character = character;
        this.itemLevel = random.nextInt(character.getLevel() + 1) + 5;
        this.damage = getItemLevel() * 9;
        this.buff = ArchersBuff.magicFactory.getMagicFactory(character.getLevel());
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
        return LegendaryBow.class.getSimpleName();
    }

    @Override
    public Magic getBuff() {
        return buff;
    }

    @Override
    public int getPrice() {
        return 0;
    }

    @Override
    public String toString(){
        return getName() + ": " + " ATK +" + getDamage();
    }

    public static ItemsFactory itemsFactory = LegendaryBow::new;
}
