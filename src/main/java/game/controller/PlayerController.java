package game.controller;

import com.sun.jndi.ldap.Ber;
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
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.*;

/**
 * Created by pikachu on 13.07.17.
 */
public class PlayerController{

    public boolean canTakeMessage = false;

    private boolean firstTime = true;
    private String choice;

    public Text viewName;
    public Text viewHitPoint;
    public Text viewManaPoint;
    public Text viewGold;
    public TextArea inputMessageArea;
    public VBox messageBox;
    public Button buttonSendMessage;

    public class InnerPlayerControllerClass implements Runnable{

        private Scanner scanner = new Scanner(System.in);
        private final Random random = new Random();
        private final List<HealingItems> itemsList = SimpleMonsterEquipment.monsterEquipmentFactory.getMonsterEquipment().initializeItemList();
        private final int sizeOfItems = itemsList.size();
        private Character character;

        public void setCharacter(Character character) {
            this.character = character;
        }

        @Override
        public void run() {
            updateScreen();
            beginGame(character);
        }

        /**
         * Основно метод контроля персонажа
         * В этом методе проверяется количество здоровья персонажа и, в случае его смерти, останавливает игру
         * В конце каждого хода пользователю предлагается на выбор использовать имеющиеся предметы,
         * отправить героя добывать ресурсы и опыт, продолжить приключение или же остановить игру.
         *
         * @param character
         *          Character implementation of {@link Character}
         */
        synchronized void beginGame(Character character){

            System.out.println(character);

            while (true) {
                Monster monster = spawn(character);
                String monsterInfo = "\nBattle began with " + monster;
                System.out.println(monsterInfo);
                Text viewMonsterInformation = new Text(monsterInfo);
                Platform.runLater(() -> messageBox.getChildren().add(viewMonsterInformation));
                String resultOfBattle = manualBattle(character, monster);
                endEvent(character, monster, false);
                Text resultOfBattleView = new Text(resultOfBattle);
                Platform.runLater(() -> messageBox.getChildren().add(resultOfBattleView));
                System.out.println(resultOfBattle);
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
        boolean nextChoice(Character character){
            Text choice = new Text("What's next: use item for healHitPoint, walking for find new items, \nauto-battle for check your fortune, market for go to shop, \nstop for break adventures or continue....");
            Platform.runLater(() -> messageBox.getChildren().add(choice));
            System.out.println("What's next: use item for healHitPoint, walking for find new items, auto-battle for check your fortune, market for go to shop stop for break adventures or continue....");
            choice:
            while (true) {
                while (!canTakeMessage){
                    System.out.print("");
                }
                String s = getChoice();
                switch (s) {
                    case "use item":
                        useItem(character);
                        break;
                    case "walking":
                        String endOfWalk = walking(character);
                        Text resultOfWalking = new Text(endOfWalk);
                        Platform.runLater(() -> messageBox.getChildren().add(resultOfWalking));
                        System.out.println(endOfWalk);
                        break;
                    case "auto-battle":
                        autoBattle(character);
                        break;
                    case "continue":
                        break choice;
                    case "market":
                        trader(character);
                        break;
                    case "stop":
                        exit();
                        break;
                    default:
                        Text wrongInput = new Text("Pls, make correct choice....");
                        Platform.runLater(() -> messageBox.getChildren().add(wrongInput));
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
        synchronized String manualBattle(Character character, Monster monster){
            battle:
            do {
                punch(character, monster);
                updateScreen();
                if (monster.isDead()) return "He is dead";
//                System.out.println(character);
                Platform.runLater(() -> messageBox.getChildren().add(new Text(character.toString())));
                Platform.runLater(() -> messageBox.getChildren().add(new Text("Choose next turn: use item for healHitPoint, use magic for addition damage, leave battle for alive or continue....")));
//                System.out.println("Choose next turn: use item for healHitPoint, use magic for addition damage, leave battle for alive or continue....");
                choice:
                while (true){

                    while (!canTakeMessage){
                        System.out.print("");
                    }
                    String s = getChoice();
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
                            Platform.runLater(() -> messageBox.getChildren().add(new Text("Pls, make correct choice....")));
                            break;
                    }
                }
            }while ((character.getHitPoint() > 0) && (monster.getHitPoint() > 0));
            updateScreen();
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
        void autoBattle(Character character){
            while (!Objects.equals(getChoice(), "break auto-battle")){
                if (character.getInventory().size() < 5) {
                    Platform.runLater(() -> messageBox.getChildren().add(new Text("I need go walk.... Pls, wait some time, I will be back\n" + character.toString())));
//                        System.out.println("I need go walk.... Pls, wait some time, I will be back\n" + character);
                    String walkingResults = walking(character);
                    Platform.runLater(() -> messageBox.getChildren().add(new Text(walkingResults)));
//                        System.out.println(walkingResults);
                } else{
                    if (random.nextInt(10000000) == 9999999){
                        System.out.println("\n");
                        Monster monster = spawn(character);
                        if (monster instanceof Boss) {
                            Text boss = new Text(monster.toString());
                            Platform.runLater(() -> messageBox.getChildren().add(boss));
//                                System.out.println(monster);
                        }
                        do {
                            updateScreen();
                            if (character.getHitPoint() <= character.getMaxHitPoint()/2)
                                if (!autoHeal(character)) break;
                            punch(character, monster);
                            if (monster.isDead()) break;
                            if (monster instanceof Boss) {
                                Platform.runLater(() -> messageBox.getChildren().add(new Text(character.toString())));
                                System.out.println(character);
                            }
                        } while ((character.getHitPoint() > 0) && (monster.getHitPoint() > 0));
                        endEvent(character, monster, true);
                        checkNewMagicPoint(character);
                    }
                }
            }
        }

        /**
         * Метод, реализующий поведение торговца.
         *
         * @param character
         *              character implementation of {@link Character}
         */
        void trader(Character character){
            Trader trader = SimpleTrader.tradersFactory.getTrader(character);
            market:
            while(true){
                Platform.runLater(() -> messageBox.getChildren().add(new Text("\nHello my friend! Look at my priceList: enter equipment, healHitPoint or exit for exit from market....")));
//                System.out.println("\nHello my friend! Look at my priceList: enter equipment, healHitPoint or exit for exit from market....");
                while (!canTakeMessage){
                    System.out.print("");
                }
                String s = getChoice();
                switch (s){
                    case "equipment":{
                        for (Map.Entry<Integer, Item> entry :
                                trader.getPriceListEquipmentObjects().entrySet()) {
                            Platform.runLater(() -> messageBox.getChildren().add(new Text("Price: " + entry.getValue().getPrice() + "G - " + "id: " + entry.getKey() + "; " + entry.getValue())));
//                            System.out.println("Price: " + entry.getValue().getPrice() + "G - " + "id: " + entry.getKey() + "; " + entry.getValue());
                        }
                        Platform.runLater(() -> messageBox.getChildren().add(new Text("Pls, make your choice or enter 0 for exit....")));
//                        System.out.println("Pls, make your choice or enter 0 for exit....");
                        while(true){
                            Platform.runLater(() -> messageBox.getChildren().add(new Text("Pls, enter id....")));
//                            System.out.println("Pls, enter id....");
                            while (!canTakeMessage){
                                System.out.print("");
                            }
                            int id = Integer.valueOf(getChoice());
                            if (trader.getPriceListEquipmentObjects().containsKey(id)){
                                if (character.getGold() >= trader.getPriceListEquipmentObjects().get(id).getPrice()){
                                    character.setGold(character.getGold()-trader.getPriceListEquipmentObjects().get(id).getPrice());
                                    ((Equipment) character).equip(trader.getEquipmentItem(id));
                                } else Platform.runLater(() -> messageBox.getChildren().add(new Text("Not enough of money!")));
//                                    System.out.println("Not enough of money!");
                                break;
                            } else if (id == 0) break;
                            else Platform.runLater(() -> messageBox.getChildren().add(new Text("Pls, enter correct id....")));
//                                System.out.println("Pls, enter a correct id");
                        }
                        break;
                    }
                    case "healHitPoint":{
                        for (Map.Entry<Integer, HealingItems> entry :
                                trader.getPriceListHealingObjects().entrySet()){
                            System.out.println("Price: " + entry.getValue().getPrice() + "G - " + "id: " + entry.getKey() + "; " + entry.getValue());
                        }
                        while(true){
                            Platform.runLater(() -> messageBox.getChildren().add(new Text("Pls, enter id or enter 0 for exit....")));
//                            System.out.println("Pls, enter id or enter 0 for exit....");
                            while (!canTakeMessage){
                                System.out.print("");
                            }
                            int id = Integer.valueOf(getChoice());
                            if (trader.getPriceListHealingObjects().containsKey(id)){
                                System.out.println("Enter count....");
                                int count = scanner.nextInt();
                                if (character.getGold() >= trader.getPriceListHealingObjects().get(id).getPrice()*count){
                                    character.setGold(character.getGold()-trader.getPriceListEquipmentObjects().get(id).getPrice());
                                    ((UsingItems) character).addAll(trader.getHealItems(count, (id)));
                                } else Platform.runLater(() -> messageBox.getChildren().add(new Text("Not enough of money!")));
//                                    System.out.println("Not enough of money!");
                                break;
                            } else if (id == 0) break;
                            else Platform.runLater(() -> messageBox.getChildren().add(new Text("Pls, enter correct id....")));
//                                System.out.println("Pls, enter a correct id");
                        }
                        break;
                    }
                    case "exit": break market;
                    default: Platform.runLater(() -> messageBox.getChildren().add(new Text("Pls, make the correct choice....")));
//                        System.out.println("Pls, make a correct choice....");
                }
            }
        }

        /**
         * Метод проверяющий наличие неиспользованных очков навыков и реализующий их распределение.
         *
         * @param character
         *              Character implementation of {@link Character}
         */
        void checkNewMagicPoint(Character character){
            while (character.getMagicPoint() != 0){
                Platform.runLater(() -> messageBox.getChildren().add(new Text("You have " + character.getMagicPoint())));
//                System.out.println(character.getMagicPoint());
                Platform.runLater(() -> messageBox.getChildren().add(new Text("You can upgrade your skills " + (MagicStyle.getMagicStyle(character)) + " by index...")));
//                System.out.println("You can upgrade your skills " + (MagicStyle.getMagicStyle(character)) + " by index...");
                while (!canTakeMessage){
                    System.out.print("");
                }
                String choice = getChoice();
                if (choice.equals("1")||choice.equals("2")||choice.equals("3")) {
                    Integer c = Integer.valueOf(choice);
                    c = --c;
                    if (MagicStyle.getMagicStyle(character).get(c).getMagicClass().equals(MagicClasses.COMBAT)){
                        ((InstantMagic)MagicStyle.getMagicStyle(character).get(c)).setDamage();
                        character.setMagicPoint(character.getMagicPoint()-1);
                        Integer finalC = c;
                        Platform.runLater(() -> messageBox.getChildren().add(new Text(MagicStyle.getMagicStyle(character).get(finalC) + " was upgraded")));
//                        System.out.println(MagicStyle.getMagicStyle(character).get(c) + " was upgraded");
                        break;
                    } else Platform.runLater(() -> messageBox.getChildren().add(new Text("This magic not upgradable, make another choice...")));
//                        System.out.println("This magic not upgradable, make another choice...");
                } else Platform.runLater(() -> messageBox.getChildren().add(new Text("Wrong value....")));
//                    System.out.println("Wrong value...");
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
        void useMagic(Character character, Monster monster){
            ArrayList<Magic> magics = MagicStyle.getMagicStyle(character);
            Platform.runLater(() -> messageBox.getChildren().add(new Text("Select magic: " + magics)));
//            System.out.println("Select magic: " + magics);
            while (true) {
                while (!canTakeMessage){
                    System.out.print("");
                }
                String magicChoice = getChoice();
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
                        updateScreen();
                    }
                } else Platform.runLater(() -> messageBox.getChildren().add(new Text("Pls, make the correct choice....")));
//                    System.out.println("Pls, enter correct index");
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
        String walking(Character character){
            while (character.getInventory().size() < ((character.getLevel()+1)*10)) {
                character.experienceDrop(0.0000001);
                if (random.nextInt(10000000) == 999999) {
                    HealingItems item = itemsList.get(random.nextInt(sizeOfItems));
                    Platform.runLater(() -> messageBox.getChildren().add(new Text("I found " + item)));
//                        System.out.println("I found " + item);
                    character.getInventory().add(item);
                }
//                if (character.getInventory().size() > ((character.getLevel()+1)*10)) break;
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
        void punch(Character character, Monster monster){
            Platform.runLater(() -> messageBox.getChildren().add(new Text(monster.toString())));
//            System.out.println(monster);
            monster.setHitPoint((monster.getHitPoint() - monster.applyDamage(character.getDamage())));
            character.setHitPoint((character.getHitPoint() - character.applyDamage(monster.getDamageForBattle())));
            Platform.runLater(() -> messageBox.getChildren().add(new Text(monster.toString())));
//            System.out.println(monster);
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
        boolean autoHeal(Character character) {

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
        boolean useItem(Character character) {
            Platform.runLater(() -> messageBox.getChildren().add(new Text("Use your items? " + character.getInventory() + "\nPls, select by index....")));
//            System.out.println("Use your items? " + character.getInventory() + "\nPls, select by index....");
            while (!canTakeMessage){
                System.out.print("");
            }
            int position = Integer.valueOf(getChoice());
            position = --position;
            if (character.getInventory().contains(character.getInventory().get(position))){
                int finalPosition = position;
                Platform.runLater(() -> messageBox.getChildren().add(new Text(character.getInventory().get(finalPosition).toString())));
                ((UsingItems) character).use(character.getInventory().get(position));
                return true;
            } else {
                Platform.runLater(() -> messageBox.getChildren().add(new Text("Item not found")));
//                System.out.println("Item not found");
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
        void endEvent(Character character, Monster monster, boolean mode){
            if (character.getHitPoint() <= 0) {
                Platform.runLater(() -> messageBox.getChildren().add(new Text("YOU ARE DEAD")));
//                System.err.println("YOU ARE DEAD");
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
        boolean drop(Character character, Monster monster, boolean autoDrop) {

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
                    Platform.runLater(() -> messageBox.getChildren().add(new Text("You have found " + monster.getDroppedGold() + "G")));
//                    System.out.println("You have found " + monster.getDroppedGold() + "G");
                    character.setGold(character.getGold() + monster.getDroppedGold());
                    Platform.runLater(() -> messageBox.getChildren().add(new Text("Your equipment " + ((Equipment) character).showEquipment())));
//                    System.out.println("Your equipment " + ((Equipment) character).showEquipment());
                    Platform.runLater(() -> messageBox.getChildren().add(new Text("Pls, choose equipment or equip all....")));
//                    System.out.println("Pls, choose equipment or equip all....");
                    Platform.runLater(() -> messageBox.getChildren().add(new Text(droppedEquipment.toString())));
//                    System.out.println(droppedEquipment);
                    Platform.runLater(() -> messageBox.getChildren().add(new Text("Equip all, or manual?")));
//                    System.out.println("Equip all, or manual?");
                    String equipAll;
                    while (true){
                        while (!canTakeMessage){
                            System.out.print("");
                        }
                        equipAll = getChoice();
                        if (Objects.equals(equipAll, "equip all")){
                            for (Map.Entry<EquipmentItems, Item> entry : droppedEquipment.entrySet()) {
                                Platform.runLater(() -> messageBox.getChildren().add(new Text(entry + " equipped")));
                                ((Equipment) character).equip(entry.getValue());
                            }
                            break;
                        } else if (Objects.equals(equipAll, "manual")){
                            break;
                        } else Platform.runLater(() -> messageBox.getChildren().add(new Text("Pls, make the correct choice....")));
//                            System.out.println("Pls, make correct choice....");
                    }

                    if (Objects.equals(equipAll, "manual"))
                        while (true){
                            Platform.runLater(() -> messageBox.getChildren().add(new Text("Your equipment " + ((Equipment) character).showEquipment())));
//                            System.out.println("Your equipment " + ((Equipment) character).showEquipment());
                            Platform.runLater(() -> messageBox.getChildren().add(new Text("Pls, choose equipment....")));
//                            System.out.println("Pls, choose equipment....");
                            Platform.runLater(() -> messageBox.getChildren().add(new Text(droppedEquipment.toString())));
                            System.out.println(droppedEquipment);
                            String key;
                            List <String> list = Arrays.asList("HEAD", "HANDS", "LEGS", "ARMOR");
                            while (true){
                                while (!canTakeMessage){
                                    System.out.print("");
                                }
                                key = getChoice();
                                if (list.contains(key)) break;
                                else Platform.runLater(() -> messageBox.getChildren().add(new Text("Pls, enter another key....")));
//                                    System.out.println("Pls, enter another key....");
                            }
                            ((Equipment) character).equip(droppedEquipment.get(EquipmentItems.valueOf(key)));
                            droppedEquipment.remove((EquipmentItems.valueOf(key)));
                            Platform.runLater(() -> messageBox.getChildren().add(new Text("Equip more?")));
//                            System.out.println("Equip more?");
                            while (!canTakeMessage){
                                System.out.print("");
                            }
                            String exit = getChoice();
                            if (Objects.equals(exit, "No") || droppedEquipment.isEmpty()) break;
                        }
                }
                character.experienceDrop(monster.getExperience());
                Platform.runLater(() -> messageBox.getChildren().add(new Text("You can add to your inventory " + monster.getInventory())));
//                System.out.println("You can add to your inventory " + monster.getInventory());
                while (true) {
                    while (!canTakeMessage){
                        System.out.print("");
                    }
                    String s = getChoice();
                    if (Objects.equals(s, "add")) {
                        ((UsingItems) character).add(monster.getInventory().pollLast());
                        break;
                    } else Platform.runLater(() -> messageBox.getChildren().add(new Text("Pls, make the correct choice....")));
//                        System.out.println("Pls, make the correct choice....");
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
        Monster spawn(Character character) {
            int chance = random.nextInt(100);
            if (character.getLevel()%25 == 0) return Boss.monsterFactory.createNewMonster(character);
            else if ((chance > 0)&&(chance < 25)) return MediumBot.monsterFactory.createNewMonster(character);
            else return EasyBot.monsterFactory.createNewMonster(character);
        }

        /**
         * End of game
         */
        void exit(){
            System.out.println("\nGAME OVER\n");
            System.exit(0);
        }

        private void updateScreen(){
            Platform.runLater(() -> {
                viewName.setText("NAME: " + character.getName());
                viewHitPoint.setText("HP: " + character.getHitPoint());
                viewManaPoint.setText("MP: " + character.getManaPoint());
                viewGold.setText("GOLD: " + character.getGold());
            });
        }

    }

    @FXML
    public void initialize(){
        Text text = new Text("Hello from new world implementation!" + "\nPls, choice your hero: archer, berserk, wizard....");
        messageBox.getChildren().add(text);
    }

    private String getChoice(){
        canTakeMessage = false;
        return choice;
    }

    public void listening(){
        String message = inputMessageArea.getText();
        inputMessageArea.clear();
        Text sendingText = new Text(message);
        messageBox.getChildren().add(sendingText);
        if (firstTime){
            if (Objects.equals(message, "archer")) {
                Character character = Archer.characterFactory.createNewCharacter();
                launch(character);
            } else if (Objects.equals(message, "berserk")){
                Character character = Berserk.characterFactory.createNewCharacter();
                launch(character);
            } else if (Objects.equals(message, "wizard")){
                Character character = Wizard.characterFactory.createNewCharacter();
                launch(character);
            } else {
                Text sendingMessage = new Text("Wrong choice.... Pls, enter archer, berserk or wizard...");
                messageBox.getChildren().add(sendingMessage);
            }
            firstTime = false;
        } else {
            canTakeMessage = true;
            choice = message;
        }
    }

    private void launch(Character character){
        InnerPlayerControllerClass pc = new InnerPlayerControllerClass();
        pc.setCharacter(character);
        Thread thread = new Thread(pc);
        thread.start();
    }

    public void exit(){
        Text text = new Text("\nGAME OVER\n");
        messageBox.getChildren().add(text);
        System.exit(0);
    }
}