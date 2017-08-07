package game.model.Items.items.weapons.weapons.wizard;

import game.model.Characters.Character;
import game.model.Items.EquipmentItems;
import game.model.Items.items.ItemsFactory;
import game.model.Items.items.weapons.Weapons;
import game.model.abilities.Magic;
import game.model.abilities.buffs.buffs.WizardBuff;

import java.util.Random;

public class LegendaryStaff implements Weapons{
    private int damage;
    private int itemLevel;
    private Character character;
    private Magic magic;
    private final int price;

    private Random random = new Random();

    private LegendaryStaff(Character character){
        this.character = character;
        this.itemLevel = character.getLevel() + 5;
        this.price = 10000 * getItemLevel();
        this.damage = getItemLevel() * 8 + 5;
        this.magic = WizardBuff.magicFactory.getMagicFactory(character.getLevel());
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
        return LegendaryStaff.class.getSimpleName();
    }

    @Override
    public String toString(){
        return getName() + ": " + " ATK +" + getDamage();
    }

    @Override
    public void finalize() throws Throwable {
        super.finalize();
    }

    public static ItemsFactory itemsFactory = LegendaryStaff::new;
}
