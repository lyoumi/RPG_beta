package game.model.Monsters.monsters;

import game.model.Characters.Character;
import game.model.Characters.characters.Berserk;
import game.model.Items.EquipmentItems;
import game.model.Items.items.HealingItems;
import game.model.Items.items.Item;
import game.model.Items.items.armors.Armor;
import game.model.Items.items.weapons.Weapons;
import game.model.Monsters.Monster;
import game.model.Monsters.MonsterFactory;
import game.model.Monsters.MonsterNames;
import game.model.Monsters.equipment.equipment.SimpleMonsterEquipment;
import game.model.abilities.Magic;
import game.model.abilities.debuffs.DebuffMagic;
import game.model.abilities.debuffs.debuffs.damage.BurningJoe;
import game.model.abilities.debuffs.debuffs.disable.Chains;
import game.model.abilities.instants.instants.InstantMagic;
import game.model.abilities.instants.instants.combat.FireBall;

import java.util.*;

public class MediumBot implements Monster {
    private static final Random random = new Random();
    private static List<HealingItems> itemsList;
    private static int sizeOfItems;

    private int level;
    private Character character;

    private int damage;
    private int hitPoint;
    private LinkedList<HealingItems> inventory = new LinkedList<>();

    private final int experience;
    private final int gold = 1000;
    private final String name;

    private Map<EquipmentItems, Item> equipmentOfDemon;

    private DebuffMagic debuffMagic;

    private MediumBot(Character character){
        this.character = character;
        if (character instanceof Berserk) level = character.getLevel() + 4;
        else level = character.getLevel() + 1;
        if (character.getLevel() > 5){
            experience = character.getLevel()*10 * 5;
            hitPoint = (level)*150;
            damage = (level)*40;
        }else {
            experience = character.getLevel()*10;
            hitPoint = (level)*70;
            damage = (level)*20;
        }
        setEquipmentOfDemon(character);
        itemsList = SimpleMonsterEquipment.monsterEquipmentFactory.getMonsterEquipment().initializeItemList();
        sizeOfItems = itemsList.size();
        List<MonsterNames> monsterNames = Collections.unmodifiableList(Arrays.asList(MonsterNames.values()));
        this.name = monsterNames.get(random.nextInt(monsterNames.size())).toString();
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
        Weapons weapon = (Weapons)equipmentOfDemon.get(EquipmentItems.HANDS);
        if (isBuffed() && Objects.equals(debuffMagic.getClass().getSimpleName(), "Chains")){
            int turn = debuffMagic.getTimeOfAction();
            System.out.println(turn);
            if (turn > 0){
                System.out.println("He's in ice!");
                return 0;
            }else return damage + weapon.getDamage() + getMagicDamage();
        } else return damage + weapon.getDamage() + getMagicDamage();

    }

    private int getMagicDamage(){
        boolean chance = random.nextBoolean();
        if (chance){
            FireBall fireBall = (FireBall) FireBall.magicFactory.getMagicFactory(character.getLevel());
            return fireBall.getDamage();
        } else return 0;
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
                return applyDamage + debuffMagic.getDamage() - getDefence();
            }else return applyDamage - getDefence();
        }
        else return applyDamage - getDefence();
    }

    private int getDefence() {
        int defence = 0;
        for (Map.Entry<EquipmentItems, Item> entry :
                equipmentOfDemon.entrySet()) {
            if (!entry.getValue().EQUIPMENT_ITEMS().equals(EquipmentItems.HANDS)) {
                defence += ((Armor) entry.getValue()).getDefence();
            }
        }
        return defence;
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
        return MediumBot.class.getSimpleName() + " " + getName() + ": Damage: " + getDamage() + "; HP: " + getHitPoint();
    }

    @Override
    public void finalize() throws Throwable {
        super.finalize();
    }

    public static MonsterFactory monsterFactory = MediumBot::new;
}
