package game.model.Items.items.weapons.weapons.berserk;

import game.model.Characters.Character;
import game.model.Items.EquipmentItems;
import game.model.Items.items.ItemsFactory;
import game.model.Items.items.weapons.Weapons;
import game.model.abilities.Magic;
import game.model.abilities.buffs.buffs.BerserkBuff;

import java.util.Random;

public class BoxGloves implements Weapons {
    private int damage;
    private int itemLevel;
    private Character character;
    private Magic magic;
    private final int price;

    private Random random = new Random();

    private BoxGloves(Character character){
        this.character = character;
        this.itemLevel = random.nextInt(character.getLevel() + 1);
        this.price = 100* getItemLevel();
        this.damage = getItemLevel() * 5 + 5;
        this.magic = BerserkBuff.magicFactory.getMagicFactory(character.getLevel());
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
        return BoxGloves.class.getSimpleName();
    }

    @Override
    public String toString(){
        return getName() + ": " + " ATK +" + getDamage();
    }

    public static ItemsFactory itemsFactory = BoxGloves::new;
}
