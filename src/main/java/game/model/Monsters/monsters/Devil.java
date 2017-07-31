package game.model.Monsters.monsters;

import game.model.Characters.Human;
import game.model.Items.EquipmentItems;
import game.model.Items.items.HealingItems;
import game.model.Items.items.Item;
import game.model.Items.items.armors.Armor;
import game.model.Items.items.weapons.Weapons;
import game.model.Monsters.Monster;
import game.model.Monsters.MonsterFactory;
import game.model.Monsters.equipment.equipment.SimpleMonsterEquipment;
import game.model.abilities.Magic;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Devil implements Monster {

    private static final Random random = new Random();
    private static int sizeOfItems;
    private static List<HealingItems> itemsList;

    private int level;
    private Human human;

    private int damage;
    private int hitPoint;
    private LinkedList<HealingItems> inventory = new LinkedList<>();

    private Map<EquipmentItems, Item> equipmentOfDevil;

    private final int experience = 100000;
    private final int gold = 100000;

    private Devil(Human human){
        this.human = human;
        level = human.getLevel() + 1;
        hitPoint = (level)*500;
        damage = (level)*100;
        setEquipmentOfDevil(human);
        itemsList = SimpleMonsterEquipment.monsterEquipmentFactory.getMonsterEquipment().initializeItemList();
        sizeOfItems = itemsList.size();
    }

    private int getDamage(){
        return damage;
    }

    private void setEquipmentOfDevil(Human human){
        equipmentOfDevil = SimpleMonsterEquipment.monsterEquipmentFactory.getMonsterEquipment().initEquipment(human);
    }

    @Override
    public int getExperience() {
        return experience;
    }

    private int getDefence() {
        int defence = 0;
        for (Map.Entry<EquipmentItems, Item> entry :
                equipmentOfDevil.entrySet()) {
            if (!entry.getValue().EQUIPMENT_ITEMS().equals(EquipmentItems.HANDS)) {
                defence += ((Armor) entry.getValue()).getDefence();
            }
        }
        return defence;
    }

    @Override
    public int getDamageForBattle() {
        Weapons weapons = (Weapons)equipmentOfDevil.get(EquipmentItems.HANDS);
        return getDamage() + weapons.getDamage();
    }

    @Override
    public int applyDamage(int applyDamage) {
        return applyDamage-getDefence();
    }

    @Override
    public int getHitPoint() {
        if (hitPoint < 0) return 0;
        else return hitPoint;
    }

    @Override
    public void setHitPoint(int hitPoint) {
        this.hitPoint = hitPoint;
    }

    @Override
    public LinkedList<HealingItems> getInventory() {
        inventory.add(itemsList.get(random.nextInt(sizeOfItems)));
        return inventory;
    }

    @Override
    public Map<EquipmentItems, Item> getDroppedItems() {
        return equipmentOfDevil;
    }

    @Override
    public boolean setDebuff(Magic magic) {
        return false;
    }

    @Override
    public boolean isDead() {
        return getHitPoint() == 0;
    }

    @Override
    public int getDroppedGold() {
        return gold;
    }

    public String toString(){
        return Devil.class.getSimpleName() +": HP " + getHitPoint() + "; ATK +" + getDamageForBattle();
    }

    public static MonsterFactory monsterFactory = Devil::new;
}
