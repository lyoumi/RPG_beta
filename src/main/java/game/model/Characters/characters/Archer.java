package game.model.Characters.characters;

import game.model.Characters.CharacterFactory;
import game.model.Characters.Human;
import game.model.Items.Equipment;
import game.model.Items.EquipmentItems;
import game.model.Items.UsingItems;
import game.model.Items.items.HealingItems;
import game.model.Items.items.Item;
import game.model.Items.items.armors.Armor;
import game.model.Items.items.weapons.Weapons;
import game.model.abilities.Magic;
import game.model.abilities.MagicClasses;
import game.model.abilities.buffs.BuffClasses;
import game.model.abilities.buffs.BuffMagic;
import game.model.abilities.instants.instants.combat.FireBall;
import game.model.abilities.instants.instants.combat.IceChains;
import game.model.abilities.instants.instants.healing.SmallHealing;

import java.util.*;

/**
 * Created by pikachu on 13.07.17.
 */
public class Archer implements Human, UsingItems, Equipment{

    private int agility = 22;
    private int intelligence = 13;
    private int power = 11;
    private double experience = 0;
    private int level = 1;
    private int baseDamage = getAgility()*getMultiplierAgility();
    private int hitPoint = getPower()*getMultiplierPower();
    private int mana = getIntelligence()*getMultiplierIntelligence();
    private ArrayList<HealingItems> inventory = new ArrayList<>();
    private Map<EquipmentItems, Item> equipmentItems = new HashMap<>();
    private Weapons weapon;
    private Armor armor;
    private int defence;
    private Magic magic;
    private int magicPoint = 0;
    private final int multiplierAgility = 2;
    private final int multiplierIntelligence = 11;
    private final int multiplierPower = 6;
    private int gold = 0;

    private int getMultiplierAgility() {
        return multiplierAgility;
    }

    private int getMultiplierPower() {
        return multiplierPower;
    }

    private int getMultiplierIntelligence() {
        return multiplierIntelligence;
    }

    private boolean expToNextLevelReady(){
        return getExperience() >= ((level+1)*1500);
    }

    private double getExperience() {
        return experience;
    }

    private void setExperience(double experience) {
        this.experience += experience;
        changeLevel();
    }

    private double expToNextLevel() {
        return (((getLevel()+1)*1500) - getExperience());
    }

    private boolean changeLevel(){
        if (expToNextLevelReady()) {
            level++;
            System.out.println("Congratulation with level: " + level);
            setMagicPoint(getMagicPoint() + 1);
            System.out.println();
            setAgility(getAgility()+3);
            setIntelligence(getIntelligence()+2);
            setPower(getPower()+2);
            updateStats();
            System.out.println(this);
            return true;
        } else return false;
    }

    private int getAgility() {
        return agility + getSummaryAdditionParam(BuffClasses.intelligence);
    }

    private void setAgility(int agility) {
        this.agility = agility;
    }

    private int getIntelligence() {
        return intelligence + getSummaryAdditionParam(BuffClasses.intelligence);
    }

    private void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    private int notEnoughOfMana(){
        System.out.println("Not enough mana!");
        return 0;
    }

    private int getPower() {
        return power + getSummaryAdditionParam(BuffClasses.power);
    }

    private void setPower(int power) {
        this.power = power;
    }

    private int getDefence() {
        defence = 0;
        for (Map.Entry<EquipmentItems, Item> entry :
                equipmentItems.entrySet()) {
            if (!entry.getValue().EQUIPMENT_ITEMS().equals(EquipmentItems.HANDS)) {
                defence += ((Armor) entry.getValue()).getDefence();
            }
        }
        return defence;
    }

    private int getSummaryAdditionParam(BuffClasses buffClass){
        int summaryAddtitionParam = 0;
        if(!Objects.equals(equipmentItems, null)){
            for (Map.Entry<EquipmentItems, Item> entry : equipmentItems.entrySet()) {
                if (!Objects.equals(entry.getValue().getBuff(), null)){
                    if (entry.getValue().getBuff().getMagicClass().equals(MagicClasses.BUFF)){
                        magic = entry.getValue().getBuff();
                        BuffMagic magic = (BuffMagic) this.magic;
                        if (magic.getEffect().containsKey(buffClass))
                            summaryAddtitionParam += magic.getEffect().get(buffClass);
                    }
                }
            }
        }
        return summaryAddtitionParam;
    }

    private void updateStats(){
        setHitPoint(getPower()*10);
        setDamage(getAgility()*getMultiplierAgility());
        setManaPoint(getAgility()*getMultiplierIntelligence());
    }

    public void setManaPoint(int mana) {
        if (mana > getMaxManaPoint()) this.mana = getMaxManaPoint();
        else this.mana = mana;
    }

    @Override
    public int getGold() {
        return gold;
    }

    @Override
    public void setGold(int gold) {
        this.gold = gold;
    }

    @Override
    public int getMagicPoint(){
        return magicPoint;
    }

    @Override
    public void setMagicPoint(int magicPoint) {
        this.magicPoint = magicPoint;
    }

    @Override
    public void experienceDrop(double experience){
        setExperience(experience);
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int getMagic(Magic magic) {
        if (magic instanceof FireBall) {
            FireBall fireBall = (FireBall) magic;
            if(getManaPoint() >= fireBall.getManaCost()){
                setManaPoint(getManaPoint()-fireBall.getManaCost());
                return fireBall.getDamage();
            } else return notEnoughOfMana();
        }else if (magic instanceof SmallHealing){
            System.out.println(toString());
            SmallHealing healing = (SmallHealing) magic;
            if (getManaPoint() >= healing.getManaCost()){
                setManaPoint(getManaPoint()-healing.getManaCost());
                return healing.getDamage();
            } else return notEnoughOfMana();
        }else if(magic instanceof IceChains){
            IceChains iceChains = (IceChains) magic;
            if(getManaPoint() >= iceChains.getManaCost()){
                setManaPoint(getManaPoint()-iceChains.getManaCost());
                return iceChains.getDamage();
            } else return notEnoughOfMana();
        } else return notEnoughOfMana();
    }

    private int getBaseDamage(){
        return baseDamage;
    }

    @Override
    public int getDamage() {
        if (equipmentItems.containsKey(EquipmentItems.HANDS)) return getBaseDamage() + weapon.getDamage();
        else return getBaseDamage();
    }

    @Override
    public int getManaPoint() {
        return mana;
    }

    @Override
    public void setDamage(int damage) {
        this.baseDamage = damage;
    }

    @Override
    public int applyDamage(int damage)  {
        int applyingDamage = damage - getDefence();
        if (applyingDamage < 0) return 0;
        else return applyingDamage;
    }

    @Override
    public int getHitPoint() {
        if (hitPoint < 0) return 0;
        else return hitPoint;
    }

    @Override
    public void setHitPoint(int hitPoint) {
        if (hitPoint >= getMaxHitPoint()) this.hitPoint = getMaxHitPoint();
        else this.hitPoint = hitPoint;
    }

    @Override
    public int getMaxHitPoint() {
        return getPower()*getMultiplierPower();
    }

    @Override
    public int getMaxManaPoint() {
        return getIntelligence()*getMultiplierIntelligence();
    }

    @Override
    public ArrayList<HealingItems> getInventory() {
        return inventory;
    }

    @Override
    public boolean add(HealingItems item) {
        return inventory.add(item);
    }

    public boolean addAll(List<HealingItems> items) {
        return inventory.addAll(items);
    }

    @Override
    public void use(HealingItems item) {
//        if (item.equals(Items.SmallHPBottle)) {
//            setHitPoint(getHitPoint()+((getPower()*getPower())/2));
//            System.out.println(item + " used");
//            getInventory().remove(item);
//        }
//        if (item.equals(Items.MiddleHPBottle)) {
//            setHitPoint(getHitPoint() + ((getPower()*getPower())*3/4));
//            System.out.println(item + " used");
//            getInventory().remove(item);
//        }
//        if (item.equals(Items.BigHPBottle)) {
//            setHitPoint(getPower()*getMultiplierPower());
//            System.out.println(item + " used");
//            getInventory().remove(item);
//        }
//        if (item.equals(Items.SmallFlower)){
//            setManaPoint(getManaPoint() + (getIntelligence()*getMultiplierIntelligence()));
//            System.out.println(item + " used");
//            getInventory().remove(item);
//        }
//        if (item.equals(Items.MiddleFlower)){
//            setManaPoint(getManaPoint() + (getIntelligence()*getMultiplierIntelligence()*3/4));
//            System.out.println(item + " used");
//            getInventory().remove(item);
//        }
//        if (item.equals(Items.BigFlower)){
//            setManaPoint(getIntelligence()*getMultiplierIntelligence());
//            System.out.println(item + " used");
//            getInventory().remove(item);
//        }
        item.use(this);
    }

    @Override
    public void equip(Item item) {
        if (item.EQUIPMENT_ITEMS().equals(EquipmentItems.HANDS)){
            weapon = (Weapons) item;
            Weapons usingWeapon = (Weapons) equipmentItems.get(EquipmentItems.HANDS);
            if (equipmentItems.containsKey(item.EQUIPMENT_ITEMS())){
                if (weapon.getDamage() > usingWeapon.getDamage()){
                    System.err.println(weapon.getName() + " equipped");
                    equipmentItems.put(weapon.EQUIPMENT_ITEMS(), weapon);
                    updateStats();
                }
            } else {
                System.err.println(weapon.getName() + " equipped");
                equipmentItems.put(weapon.EQUIPMENT_ITEMS(), weapon);
                updateStats();
            }
        } else {
            armor = (Armor) item;
            Armor usingArmor = (Armor)equipmentItems.get(item.EQUIPMENT_ITEMS());
            if (equipmentItems.containsKey(item.EQUIPMENT_ITEMS())){
                if (armor.getDefence() > usingArmor.getDefence()){
                    System.err.println(armor.getName() + " equipped");
                    equipmentItems.put(armor.EQUIPMENT_ITEMS(), armor);
                    updateStats();
                }
            } else {
                System.err.println(armor.getName() + " equipped");
                equipmentItems.put(armor.EQUIPMENT_ITEMS(), armor);
                updateStats();
            }
        }
    }

    @Override
    public void unEquip() {

    }

    @Override
    public Map<EquipmentItems, Item> showEquipment() {
        return equipmentItems;
    }

    public String toString(){
        return "Class: " + Archer.class.getSimpleName() +
                "; HP " + String.valueOf(getHitPoint()) +
                "; MP " + getManaPoint() +
                "; Lvl: " + String.valueOf(getLevel()) +
                "; Exp to next level: " + expToNextLevel() +
                "; DMG: " + getDamage() +
                "; DEF: " + getDefence() +
                "; GOLD: " + getGold();
    }

    public static CharacterFactory characterFactory = Archer::new;
}
