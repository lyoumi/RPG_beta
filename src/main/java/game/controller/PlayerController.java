package game.controller;

import game.model.Characters.Character;
import game.model.Characters.characters.Archer;
import game.model.Characters.characters.Berserk;
import game.model.Characters.characters.Wizard;
import game.model.Items.Equipment;
import game.model.Items.EquipmentItems;
import game.model.Items.UsingItems;
import game.model.Items.items.HealingItems;
import game.model.Items.items.Item;
import game.model.Monsters.Monster;
import game.model.Monsters.equipment.equipment.SimpleMonsterEquipment;
import game.model.Monsters.monsters.EasyBot;
import game.model.Monsters.monsters.Boss;
import game.model.Monsters.monsters.MediumBot;
import game.model.abilities.Magic;
import game.model.abilities.MagicClasses;
import game.model.abilities.instants.InstantMagic;
import game.model.abilities.instants.instants.healing.SmallHealing;
import game.model.abilities.magicStyle.MagicStyle;
import game.model.traders.Trader;
import game.model.traders.traders.SimpleTrader;

import java.io.IOException;
import java.util.*;

/**
 * Created by pikachu on 13.07.17.
 */
public class PlayerController {

    private static Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();
    private static final List<HealingItems> itemsList = SimpleMonsterEquipment.monsterEquipmentFactory.getMonsterEquipment().initializeItemList();
    private static final int sizeOfItems = itemsList.size();

    /**
     * Основно метод контроля персонажа
     * В этом методе проверяется количество здоровья персонажа и, в случае его смерти, останавливает игру
     * В конце каждого хода пользователю предлагается на выбор использовать имеющиеся предметы,
     * отправить героя добывать ресурсы и опыт, продолжить приключение или же остановить игру.
     *
     * @param character
     *          Character implementation of {@link Character}
     */
    private synchronized void beginGame(Character character){

        System.out.println(character);

        while (true) {

            Monster monster = spawn(character);
            System.out.println("\nBattle began with " + monster);

            String resultOfBattle = manualBattle(character, monster);
            System.out.println(resultOfBattle);
            endEvent(character, monster, false);

            nextChoice(character);
        }
    }

    /**
     * Метод, описывающий возможный выбор по окончании ручного или автоматического боя, или же поиска ресурсов.
     *
     * @param character
     *              Character implementation of {@link Character}
     * @return
     *              boolean result
     */
    private boolean nextChoice(Character character){
        System.out.println("What's next: use item for healHitPoint, walking for find new items, auto-battle for check your fortune, market for go to shop stop for break adventures or continue....");
        choice:
        while (true) {
            String s = scanner.nextLine();
            switch (s) {
                case "use item":
                    useItem(character);
                    break choice;
                case "walking":
                    String endOfWalk = walking(character);
                    System.out.println(endOfWalk);
                    break choice;
                case "auto-battle":
                    autoBattle(character);
                    break choice;
                case "continue":
                    break choice;
                case "market":
                    trader(character);
                    break choice;
                case "stop":
                    exit();
                    break;
                default:
                    System.out.println("Pls, make the correct choice....");
                    break;
            }
        }
        return true;
    }

    /**
     * Метод симулирующий бой между героем и монстром
     * В ходе боя игрок может покинуть бой для дальнейшего приключения, или же использовать имеющиеся у него веши
     *
     * @param character
     *              Character implementation of {@link Character}
     * @param monster
     *              Monster implementation of {@link Monster}
     */
    private synchronized String manualBattle(Character character, Monster monster){
        battle:
        do {
            punch(character, monster);
            if (monster.isDead()) break;
            System.out.println(character);
            System.out.println("Choose next turn: use item for healHitPoint, use magic for addition damage, leave battle for alive or continue....");
            choice:
            while (true){
                String s = scanner.nextLine();
                switch(s){
                    case "use item":
                        useItem(character);
                        break choice;
                    case "use magic":
                        useMagic(character, monster);
                        break choice;
                    case "continue":
                        break choice;
                    case "leave":
                        break battle;
                    default:
                        System.out.println("Pls, make the correct choice....");
                        break;
                }
            }
        }while ((character.getHitPoint() > 0) && (monster.getHitPoint() > 0));
        checkNewMagicPoint(character);
        return "The manualBattle is over. Your stats: " + character;
    }

    /**
     * Режим автоматического ведения боя. В случае с малым количеством здоровья персонаж будет способен
     * самостоятельно восстановить здоровье, а в случае отсутствия предметов для восстановления
     * отправится в путешествие для их поиска (walking())
     *
     * @param character
     *          Character implementation of {@link Character}
     */
    private void autoBattle(Character character){
        try{
            while (System.in.available()==0){
                if (character.getInventory().size() < 5) {
                    System.out.println("I need go walk.... Pls, wait some time, I will be back\n" + character);
                    String walkingResults = walking(character);
                    System.out.println(walkingResults);
                } else{
                    if (random.nextInt(10000000) == 9999999){
                        System.out.println("\n");
                        Monster monster = spawn(character);
                        if (monster instanceof Boss)
                            System.out.println(monster);
                        do {
                            if (character.getHitPoint() <= character.getMaxHitPoint()/2)
                                if (!autoHeal(character)) break;
                            punch(character, monster);
                            if (monster.isDead()) break;
                            if (monster instanceof Boss) System.out.println(character);
                        } while ((character.getHitPoint() > 0) && (monster.getHitPoint() > 0));
                        endEvent(character, monster, true);
                        checkNewMagicPoint(character);
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Метод, реализующий поведение торговца.
     *
     * @param character
     *              character implementation of {@link Character}
     */
    private void trader(Character character){
        Trader trader = SimpleTrader.tradersFactory.getTrader(character);
        market:
        while(true){
            System.out.println("\nHello my friend! Look at my priceList: enter equipment, healHitPoint or exit for exit from market....");
            String s = scanner.nextLine();
            switch (s){
                case "equipment":{
                    for (Map.Entry<Integer, Item> entry :
                            trader.getPriceListEquipmentObjects().entrySet()) {
                        System.out.println("Price: " + entry.getValue().getPrice() + "G - " + "id: " + entry.getKey() + "; " + entry.getValue());
                    }
                    System.out.println("Pls, make your choice....");
                    while(true){
                        System.out.println("Pls, enter id....");
                        int id = scanner.nextInt();
                        if (trader.getPriceListEquipmentObjects().containsKey(id)){
                            if (character.getGold() >= trader.getPriceListEquipmentObjects().get(id).getPrice()){
                                character.setGold(character.getGold()-trader.getPriceListEquipmentObjects().get(id).getPrice());
                                ((Equipment) character).equip(trader.getEquipmentItem(id));
                            } else System.out.println("Not enough of money!");
                            break;
                        } else System.out.println("Pls, enter a correct id");
                    }
                    break;
                }
                case "healHitPoint":{
                    for (Map.Entry<Integer, HealingItems> entry :
                            trader.getPriceListHealingObjects().entrySet()){
                        System.out.println("Price: " + entry.getValue().getPrice() + "G - " + "id: " + entry.getKey() + "; " + entry.getValue());
                    }
                    while(true){
                        System.out.println("Pls, enter id....");
                        int id = scanner.nextInt();
                        if (trader.getPriceListHealingObjects().containsKey(id)){
                            System.out.println("Enter count....");
                            int count = scanner.nextInt();
                            if (character.getGold() >= trader.getPriceListHealingObjects().get(id).getPrice()*count){
                                character.setGold(character.getGold()-trader.getPriceListEquipmentObjects().get(id).getPrice());
                                ((UsingItems) character).addAll(trader.getHealItems(count, (id)));
                            } else System.out.println("Not enough of money!");
                            break;
                        } else System.out.println("Pls, enter a correct id");
                    }
                    break;
                }
                case "exit": break market;
                default: System.out.println("Pls, make a correct choice....");
            }
        }
    }

    /**
     * Метод проверяющий наличие неиспользованных очков навыков и реализующий их распределение.
     *
     * @param character
     *              Character implementation of {@link Character}
     */
    private void checkNewMagicPoint(Character character){
        while (character.getMagicPoint() != 0){
            System.out.println(character.getMagicPoint());
            System.out.println("You can upgrade your skills " + (MagicStyle.getMagicStyle(character)) + " by index...");
            String choice = scanner.nextLine();
            if (choice.equals("1")||choice.equals("2")||choice.equals("3")) {
                Integer c = Integer.valueOf(choice);
                c = --c;
                if (MagicStyle.getMagicStyle(character).get(c).getMagicClass().equals(MagicClasses.COMBAT)){
                    ((InstantMagic)MagicStyle.getMagicStyle(character).get(c)).setDamage();
                    character.setMagicPoint(character.getMagicPoint()-1);
                    System.out.println(MagicStyle.getMagicStyle(character).get(c) + " was upgraded");
                    break;
                } else System.out.println("This magic not upgradable, make another choice...");
            } else System.out.println("Wrong value...");
        }
    }

    /**
     * Использование магии
     *
     * @param character
     *          Character implementation of {@link Character}
     * @param monster
     *          Monster implementation of {@link Monster}
     */
    private void useMagic(Character character, Monster monster){
        ArrayList<Magic> magics = MagicStyle.getMagicStyle(character);
        System.out.println("Select magic: " + magics);
        while (true) {
            String magicChoice = scanner.nextLine();
            if (magicChoice.equals("1")||magicChoice.equals("2")||magicChoice.equals("3")){
                int mc = Integer.valueOf(magicChoice);
                mc = --mc;
                if ((mc < magics.size()) && (mc >= 0)) {
                    Magic magic = magics.get(mc);
                    if (magic.getMagicClass().equals(MagicClasses.COMBAT)){
                        monster.setDebuff(magic);
                        monster.setHitPoint(monster.getHitPoint() - monster.applyDamage(character.getMagic(magic)));
                        break;
                    } else if (magic.getMagicClass().equals(MagicClasses.HEALING)){
                        character.setHitPoint(character.getHitPoint() + character.getMagic(magic));
                        break;
                    } else {
                        character.getMagic(magic);
                    }
                }
            } else System.out.println("Pls, enter correct index");
        }
    }

    /**
     * Данный метод описывает автоматизированное поведение героя
     * В данном методе герой в цикле while() получает 0.0000001 опыта и случайно выпадающие предметы
     * Остановка цикла происходит при вводе с клавиатуры 0
     *
     * @param character
     *          Character implementation of {@link Character}
     * @return
     *          String result of walking
     */
    private String walking(Character character){
        try{
            while (System.in.available()==0) {
                character.experienceDrop(0.0000001);
                if (random.nextInt(10000000) == 999999) {
                    HealingItems item = itemsList.get(random.nextInt(sizeOfItems));
                    System.out.println("I found " + item);
                    character.getInventory().add(item);
                }
                if (character.getInventory().size() > ((character.getLevel()+1)*10)) break;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return "The walk is over. Your stats: " + character;
    }

    /**
     * Метод, реализующий удар монстра и героя. Возвращает true после удара
     *
     * @param character
     *          Character implementation of {@link Character}
     * @param monster
     *          Monster implementation of {@link Monster}
     */
    private void punch(Character character, Monster monster){
        System.out.println(monster);
        monster.setHitPoint((monster.getHitPoint() - monster.applyDamage(character.getDamage())));
        character.setHitPoint((character.getHitPoint() - character.applyDamage(monster.getDamageForBattle())));
        System.out.println(monster);
    }

    /**
     * Метод предназначенный для автоматического восполнения здоровья
     * Возвращает true в случае успешного восполнения здоровья и false в случае если этого не произошло
     *
     * @param character
     *          Character implementation of {@link Character}
     * @return
     *          boolean result of healHitPoint
     */

    private boolean autoHeal(Character character) {

        if (character.checkHitPointBottle()){
            return character.healHitPoint();
        } else if (!character.checkHitPointBottle() && (character.checkManaPointBottle())){
            if (character.getManaPoint() >= SmallHealing.magicFactory.getMagicFactory(character.getLevel()).getManaCost()){
                Magic magic = SmallHealing.magicFactory.getMagicFactory(character.getLevel());
                character.getMagic(magic);
                return true;
            } else {
                if (character.healManaPoint()){
                    Magic magic = SmallHealing.magicFactory.getMagicFactory(character.getLevel());
                    character.getMagic(magic);
                    return true;
                } else return false;
            }
        } else return false;
    }


    /**
     * Пользователю предлагается использовать один из имеющихся у него предметов,
     * предвартельно ознакомив его с содержимым инвентаря. Доступ к предмету осществляется по индексу.
     * <p>
     * После ввода индекса осуществляется проверка на наличие этого предмета в инвентаре, после чего вызывается
     * метод use() из класса персонажа.
     *
     * @param character
     *              Character implementation of {@link Character}
     * @return
     *          boolean result of using item
     */
    private boolean useItem(Character character) {
        System.out.println("Use your items? " + character.getInventory() + "\nPls, select by index....");
        int position = scanner.nextInt();
        if (character.getInventory().contains(character.getInventory().get(--position))){
            ((UsingItems) character).use(character.getInventory().get(position));
            return true;
        } else {
            System.out.println("Item not found");
            return false;
        }
    }

    /**
     * Метод, вызывающийся по окончанию боя с монстром.
     * В качестве входных параметров метод принимает объекты героя и монстра
     * для установления факта смерти одного из них и логический тип mode
     * для определения режима последующего дропа снаряжения.
     *
     * В случае смерти героя вызывается метод exit() и игра завершается.
     * В случае смерти монстра вызывается метод drop(), в котором герой может поднять
     * снаряжение оставшееся после убитого монстра.
     *
     * @param character
     *              Character implementation of {@link Character}
     * @param monster
     *              Monster implementation of {@link Monster}
     * @param mode
     *              boolean mode for drop items
     */
    private void endEvent(Character character, Monster monster, boolean mode){
        if (character.getHitPoint() <= 0) {
            System.err.println("YOU ARE DEAD");
            exit();
        } else if (monster.getHitPoint() <= 0) {
            drop(character, monster, mode);
        }
    }

    /**
     * После смерти монстра выпадает случайный премет из списка Item.
     * У игрока есть возможность поднять этот предмет и переместить в свой инвентарь
     * <p>
     * Входные параметры:
     *
     * @param character
     *              Character implementation of {@link Character}
     * @param monster
     *              Monster implementation of {@link Monster}
     *
     * @return
     *              boolean result
     */
    private boolean drop(Character character, Monster monster, boolean autoDrop) {

        if (autoDrop){
            character.experienceDrop(monster.getExperience());
            ((UsingItems) character).add(monster.getInventory().pollLast());
            character.setGold(character.getGold() + monster.getDroppedGold());
            Map<EquipmentItems, Item> droppedEquipment = monster.getDroppedItems();
            for (Map.Entry<EquipmentItems, Item> entry : droppedEquipment.entrySet()) {
                ((Equipment) character).equip(entry.getValue());
            }
            return true;
        }

        else{
            if (!Objects.equals(monster.getDroppedItems(), null)){
                Map<EquipmentItems, Item> droppedEquipment = monster.getDroppedItems();
                System.out.println("You have found " + monster.getDroppedGold());
                character.setGold(character.getGold() + monster.getDroppedGold());
                System.out.println("Your equipment " + ((Equipment) character).showEquipment());
                System.out.println("Pls, choose equipment or equip all....");
                System.out.println(droppedEquipment);
                String equipAll = scanner.nextLine();
                if (Objects.equals(equipAll, "equip all"))
                    for (Map.Entry<EquipmentItems, Item> entry : droppedEquipment.entrySet()) {
                        ((Equipment) character).equip(entry.getValue());
                    }
                else
                while (true){
                    System.out.println("Your equipment " + ((Equipment) character).showEquipment());
                    System.out.println("Pls, choose equipment....");
                    System.out.println(droppedEquipment);
                    String key;
                    List <String> list = Arrays.asList("HEAD", "HANDS", "LEGS", "ARMOR");
                    while (true){
                        key = scanner.nextLine();
                        if (list.contains(key)) break;
                        else System.out.println("Pls, enter another key....");
                    }
                    ((Equipment) character).equip(droppedEquipment.get(EquipmentItems.valueOf(key)));
                    droppedEquipment.remove((EquipmentItems.valueOf(key)));
                    System.out.println("Equip more?");
                    if (Objects.equals(scanner.nextLine(), "No") || droppedEquipment.isEmpty()) break;
                }
            }
            character.experienceDrop(monster.getExperience());
            System.out.println("You can add to your inventory " + monster.getInventory());
            while (true) {
                String s = scanner.nextLine();
                if (Objects.equals(s, "add")) {
                    ((UsingItems) character).add(monster.getInventory().pollLast());
                    break;
                } else System.out.println("Pls, make the correct choice....");
            }
            return true;
        }
    }

    /**
     * Метод, отвечающий за генерацию монстра
     *
     * @param character
     *          Character implementation of {@link Character}
     * @return
     *          New implementation of {@link Monster} with incremented character level
     *
     */
    private Monster spawn(Character character) {
        int chance = random.nextInt(100);
        if (character.getLevel()%25 == 0) return Boss.monsterFactory.createNewMonster(character);
        else if ((chance > 0)&&(chance < 25)) return MediumBot.monsterFactory.createNewMonster(character);
        else return EasyBot.monsterFactory.createNewMonster(character);
    }


    /**
     * End of game
     */
    private void exit(){
        System.out.println("\nGAME OVER\n");
        System.exit(0);
    }

    public static void main(String[] args){
        PlayerController playerController = new PlayerController();
        System.out.println("Hello in Middle-Earth....");
        System.out.println("Choose your class: archer, berserk, wizard....");
        choice:
        while(true){
            String s = scanner.nextLine();
            switch (s) {
                case "archer":
                    playerController.beginGame(Archer.characterFactory.createNewCharacter());
                    break choice;
                case "berserk":
                    playerController.beginGame(Berserk.characterFactory.createNewCharacter());
                    break choice;
                case "wizard":
                    playerController.beginGame(Wizard.characterFactory.createNewCharacter());
                    break choice;
                default:
                    System.out.println("Pls, make the correct choice....");
                    break;
            }
        }
    }
}