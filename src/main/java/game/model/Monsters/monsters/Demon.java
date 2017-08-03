package game.model.Monsters.monsters;

import game.model.Characters.Character;
import game.model.Items.EquipmentItems;
import game.model.Items.items.HealingItems;
import game.model.Items.items.Item;
import game.model.Monsters.Monster;
import game.model.Monsters.MonsterFactory;
import game.model.Monsters.MonsterNames;
import game.model.Monsters.equipment.equipment.SimpleMonsterEquipment;
import game.model.abilities.Magic;
import game.model.abilities.debuffs.DebuffMagic;
import game.model.abilities.debuffs.debuffs.damage.BurningJoe;
import game.model.abilities.debuffs.debuffs.disable.Chains;
import game.model.abilities.instants.instants.InstantMagic;

import java.util.*;

/**
 * Created by pikachu on 13.07.17.
 */
public class Demon implements Monster {

    private static Random random = new Random();
    private static int sizeOfItems;
    private static List<HealingItems> itemsList;

    private int level;
    private Character character;

    private int damage;
    private int hitPoint;
    private LinkedList<HealingItems> inventory = new LinkedList<>();
    private Map<EquipmentItems, Item> equipmentOfDemon;

    private final int experience = 100;
    private final int gold = 50;
    private final String name;

    private DebuffMagic debuffMagic;


    private Demon(Character character){
        this.character = character;
        level = character.getLevel() + 1;
        hitPoint = level *35;
        damage = level *10;
        setEquipmentOfDemon(character);
        itemsList = SimpleMonsterEquipment.monsterEquipmentFactory.getMonsterEquipment().initializeItemList();
        sizeOfItems = itemsList.size();
        List<MonsterNames> names = Collections.unmodifiableList(Arrays.asList(MonsterNames.values()));
        this.name = names.get(random.nextInt(names.size())).toString();
    }


    private void setEquipmentOfDemon(Character character) {
        this.equipmentOfDemon = SimpleMonsterEquipment.monsterEquipmentFactory.getMonsterEquipment().initEquipment(character);
    }

    private boolean isBuffed() {
        return !Objects.equals(debuffMagic, null);
    }

    public int getExperience(){
        return experience;
    }

    @Override
    public int getDamageForBattle() {
        if (isBuffed() && Objects.equals(debuffMagic.getClass().getSimpleName(), "Chains")){
            int turn = debuffMagic.getTimeOfAction();
            System.out.println(turn);
            if (turn > 0){
                System.out.println("He's in ice!");
                return 0;
            }else return damage;
        }
        else return damage;

    }

    private int getDamage(){
        return damage;
    }

    @Override
    public int applyDamage(int applyDamage) {
        if (isBuffed() && debuffMagic.getClass().getSimpleName().contentEquals("BurningJoe")){
                int turn = debuffMagic.getTimeOfAction();
                System.out.println(turn);
                if (turn > 0){
                    System.out.println("He's in flame!");
                    return applyDamage + debuffMagic.getDamage();
                }else return applyDamage;
        }
        else return applyDamage;
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

    public Map<EquipmentItems, Item> getDroppedItems(){
        return equipmentOfDemon;
    }

    @Override
    public boolean setDebuff(Magic magic) {
        if (Objects.equals(magic.getClass().getSimpleName(), InstantMagic.FireBall.toString()))
            debuffMagic = (DebuffMagic) BurningJoe.magicFactory.getMagicFactory(level);
        else if (Objects.equals(magic.getClass().getSimpleName(), InstantMagic.IceChains.toString()))
            debuffMagic = (DebuffMagic) Chains.magicFactory.getMagicFactory(level);
        return true;
    }

    @Override
    public boolean isDead() {
        return getHitPoint() == 0;
    }

    @Override
    public int getDroppedGold() {
        return gold;
    }

    private String getName(){return name;}

    public String toString(){
        return Demon.class.getSimpleName() + " " +getName() + ": " +  "; Damage: " + getDamage() + "; HitPoint: " + getHitPoint();
    }

    public static MonsterFactory monsterFactory = Demon::new;
}
