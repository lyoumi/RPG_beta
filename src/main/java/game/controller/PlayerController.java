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
import game.model.Items.items.heal.HealingItemsList;
import game.model.Items.items.heal.healHitPoint.items.BigHPBottle;
import game.model.Items.items.heal.healHitPoint.items.MiddleHPBottle;
import game.model.Items.items.heal.healHitPoint.items.SmallHPBottle;
import game.model.Items.items.heal.healManaPoint.items.BigFlower;
import game.model.Items.items.heal.healManaPoint.items.MiddleFlower;
import game.model.Items.items.heal.healManaPoint.items.SmallFlower;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.*;

/**
 * Created by pikachu on 13.07.17.
 */
public class PlayerController{

    public boolean canTakeMessage;
    public Text viewAttack;

    private boolean firstTime = true;
    private String choice;

    public Text viewName;
    public Text viewLevel;
    public Text viewExp;
    public Text viewHitPoint;
    public Text viewManaPoint;
    public Text viewGold;
    public TextArea inputMessageArea;
    public Button buttonSendMessage;
    public VBox messageBox;
    public VBox currentChoiceBox;
    public VBox itemBox;
    public VBox equipmentBox;
    public ScrollPane messageBoxScrollPane;

    public class InnerPlayerControllerClass implements Runnable{

        private final Random random = new Random();
        private final List<HealingItems> itemsList = SimpleMonsterEquipment.monsterEquipmentFactory.getMonsterEquipment().initializeItemList();
        private final int sizeOfItems = itemsList.size();
        private Character character;

        public void setCharacter(Character character) {
            this.character = character;
        }

        @Override
        public void run() {
            updateScreen(character);
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
        private synchronized void beginGame(Character character){

            while (true) {
                Monster monster = spawn(character);
                String monsterInfo = "\n   info: Battle began with " + monster;
                Text viewMonsterInformation = new Text(monsterInfo);
                Platform.runLater(() -> messageBox.getChildren().add(viewMonsterInformation));
                String resultOfBattle = manualBattle(character, monster);
                endEvent(character, monster, false);
                Text resultOfBattleView = new Text("   info: " + resultOfBattle);
                Platform.runLater(() -> messageBox.getChildren().add(resultOfBattleView));
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
            Text choice = new Text("\n   info: What's next: use item for healHitPoint, walking for find new items, \n   auto-battle for check your fortune, market for go to shop, \n   stop for break adventures or continue....\n");
            choice:
            while (true) {
                updateChoiceBox(" use item", " walking", " auto-battle", " market", " stop", " continue");
                Platform.runLater(() -> messageBox.getChildren().add(choice));
                while (!canTakeMessage){
                    System.out.print("");
                }
                String currentChoice = getChoice();
                switch (currentChoice) {
                    case "use item":
                        useItem(character);
                        break;
                    case "walking":
                        String endOfWalk = walking(character);
                        Text resultOfWalking = new Text(endOfWalk);
                        Platform.runLater(() -> messageBox.getChildren().add(resultOfWalking));
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
        private synchronized String manualBattle(Character character, Monster monster){
            battle:
            do {
                updateScreen(character);
                punch(character, monster);
                updateScreen(character);
                if (monster.isDead()) return "   The manualBattle is over";
                if (character.getHitPoint() <= 0) exit();
                Platform.runLater(() -> messageBox.getChildren().add(new Text("   info: Choose next turn: use item for healHitPoint, use magic for addition damage, leave battle for alive or continue....")));
                choice:
                while (true){
                    updateChoiceBox(" use item", " use magic", " leave", " continue");
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
                            Platform.runLater(() -> {
                                Text notCorrectChoice = new Text("   info: Pls, make the correct choice....");
                                notCorrectChoice.setFill(Color.RED);
                                messageBox.getChildren().add(notCorrectChoice);
                            });
                            break;
                    }
                }
            }while ((character.getHitPoint() > 0) && (monster.getHitPoint() > 0));
            updateScreen(character);
            try {
                finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            return "";
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
            updateChoiceBox(" break");
            while (!Objects.equals(getChoice(), "break")){
                if (character.getInventory().size() < 5) {
                    Platform.runLater(() -> {
                        Text walkingBeginning = new Text("   info: I need go walk.... Pls, wait some time, I will be back\n" + character.toString());
                        walkingBeginning.setFill(Color.DARKBLUE);
                        messageBox.getChildren().add(walkingBeginning);
                    });
                    String walkingResults = walking(character);
                    Text viewWalkingResult = new Text(walkingResults);
                    viewWalkingResult.setFill(Color.DARKBLUE);
                    Platform.runLater(() -> {
                        messageBox.getChildren().add(viewWalkingResult);
                    });
                } else{
                    if (random.nextInt(10000000) == 9999999){
                        Monster monster = spawn(character);
                        Text boss = new Text("   info: battle began with " + monster.toString());
                        boss.setFill(Color.ORANGERED);
                        Platform.runLater(() -> {
                            messageBox.getChildren().add(boss);
                        });
                        do {
                            updateScreen(character);
                            if (character.getHitPoint() <= character.getMaxHitPoint()/2) {
                                if (!autoHeal(character)) break;
                            }
                            updateScreen(character);
                            punch(character, monster);
                            if (monster.isDead()) break;
                            else {
                                Text monsterInfo = new Text("   info: " + monster.toString());
                                monsterInfo.setFill(Color.ORANGERED);
                                Platform.runLater(() -> {
                                    messageBox.getChildren().add(monsterInfo);
                                });
                            }
                            if (character.getHitPoint() == 0) exit();
                        } while (true);
                        updateScreen(character);
                        endEvent(character, monster, true);
                        checkNewMagicPoint(character);
                        try {
                            monster.finalize();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
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
        private void trader(Character character){
            Trader trader = SimpleTrader.tradersFactory.getTrader(character);
            market:
            while(true){
                updateChoiceBox(" equipment", " items", " exit");
                Text viewWelcomeSpeech = new Text("\n   info: Hello my friend! Look at my priceList: enter equipment, items or exit for exit from market....");
                viewWelcomeSpeech.setFill(Color.BLUEVIOLET);
                Platform.runLater(() -> {
                    messageBox.getChildren().add(viewWelcomeSpeech);
                });
                while (!canTakeMessage){
                    System.out.print("");
                }
                String s = getChoice();
                switch (s){
                    case "equipment":{
                        for (Map.Entry<Integer, Item> entry :
                                trader.getPriceListEquipmentObjects().entrySet()) {
                            Platform.runLater(() -> messageBox.getChildren().add(new Text("   info: Price: " + entry.getValue().getPrice() + "G - " + "id: " + entry.getKey() + "; " + entry.getValue())));
                        }
                        Platform.runLater(() -> messageBox.getChildren().add(new Text("   info: Pls, make your choice or enter 0 for exit....")));
                        while(true){
                            updateChoiceBox(" id", " 0");
                            Platform.runLater(() -> messageBox.getChildren().add(new Text("   info: Pls, enter id....")));
                            while (!canTakeMessage){
                                System.out.print("");
                            }
                            String stringId = getChoice();
                            int id;
                            if (isDigit(stringId)) {
                                id = Integer.valueOf(stringId);
                                if (trader.getPriceListEquipmentObjects().containsKey(id)){
                                    if (character.getGold() >= trader.getPriceListEquipmentObjects().get(id).getPrice()){
                                        character.setGold(character.getGold()-trader.getPriceListEquipmentObjects().get(id).getPrice());
                                        ((Equipment) character).equip(trader.getEquipmentItem(id));
                                        updateScreen(character);
                                    } else Platform.runLater(() -> {
                                        Text viewNotEnoughOfMoney = new Text("   info: Not enough of money!");
                                        viewNotEnoughOfMoney.setFill(Color.RED);
                                        messageBox.getChildren().add(viewNotEnoughOfMoney);
                                    });
                                    break;
                                } else if (id == 0) break;
                            }
                            else Platform.runLater(() -> {
                                    Text notCorrectId = new Text("   info: Pls, enter correct id....");
                                    notCorrectId.setFill(Color.RED);
                                    messageBox.getChildren().add(notCorrectId);
                                });
                        }
                        break;
                    }
                    case "items":{
                        for (Map.Entry<Integer, HealingItems> entry :
                                trader.getPriceListHealingObjects().entrySet()){
                            Platform.runLater(() -> messageBox.getChildren().add(new Text("   info: Price: " + entry.getValue().getPrice() + "G - " + "id: " + entry.getKey() + "; " + entry.getValue())));
                        }
                        while(true){
                            updateChoiceBox(" id", " 0");
                            Platform.runLater(() -> messageBox.getChildren().add(new Text("   info: Pls, enter id or enter 0 for exit....")));
                            while (!canTakeMessage){
                                System.out.print("");
                            }
                            String stringId = getChoice();
                            Integer id;
                            if (isDigit(stringId)){
                                id = Integer.valueOf(getChoice());
                                if (trader.getPriceListHealingObjects().containsKey(id)){
                                    updateChoiceBox("count");
                                    Platform.runLater(() -> messageBox.getChildren().add(new Text("   info: Enter count....")));
                                    while (true){
                                        while (!canTakeMessage){
                                            System.out.print("");
                                        }
                                        String stringValue = getChoice();
                                        int count;
                                        if (isDigit(stringValue)){
                                            count = Integer.valueOf(getChoice());
                                            if (character.getGold() >= trader.getPriceListHealingObjects().get(id).getPrice()*count){
                                                character.setGold(character.getGold()-(trader.getPriceListHealingObjects().get(id).getPrice()*count));
                                                ((UsingItems) character).addAll(trader.getHealItems(count, (id)));
                                                updateScreen(character);
                                                break;
                                            } else {
                                                Platform.runLater(() -> {
                                                    Text viewNotEnoughOfMoney = new Text("   info: Not enough of money!");
                                                    viewNotEnoughOfMoney.setFill(Color.RED);
                                                    messageBox.getChildren().add(viewNotEnoughOfMoney);
                                                });
                                                break;
                                            }
                                        } else Platform.runLater(() -> {
                                            Text notCorrectChoice = new Text("   info: Pls, make the correct choice....");
                                            notCorrectChoice.setFill(Color.RED);
                                            messageBox.getChildren().add(notCorrectChoice);
                                        });

                                    }
                                    break;
                                } else if (id == 0) break;
                            }
                            else Platform.runLater(() -> {
                                    Text notCorrectId = new Text("   info: Pls, enter correct id....");
                                    notCorrectId.setFill(Color.RED);
                                    messageBox.getChildren().add(notCorrectId);
                                });
                        }
                        break;
                    }
                    case "exit": break market;
                    default: Platform.runLater(() -> {
                        Text notCorrectChoice = new Text("   info: Pls, make the correct choice....");
                        notCorrectChoice.setFill(Color.RED);
                        messageBox.getChildren().add(notCorrectChoice);
                    });
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
                Platform.runLater(() -> messageBox.getChildren().add(new Text("   info: You have " + character.getMagicPoint())));
                Platform.runLater(() -> messageBox.getChildren().add(new Text("   info: You can upgrade your skills " + (MagicStyle.getMagicStyle(character)) + " by index...")));
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
                        try {
                            finalize();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                        break;
                    } else Platform.runLater(() -> messageBox.getChildren().add(new Text("   info: This magic not upgradable, make another choice...")));
                } else Platform.runLater(() -> {
                    Text notCorrectChoice = new Text("   info: Pls, make the correct choice....");
                    notCorrectChoice.setFill(Color.RED);
                    messageBox.getChildren().add(notCorrectChoice);
                });
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
            Text viewSelectMagic = new Text("   info: Select magic: " + magics);
            updateChoiceBox("1", "2", "3");
            viewSelectMagic.setFill(Color.BLUE);
            Platform.runLater(() -> {
                messageBox.getChildren().add(viewSelectMagic);
            });
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
                            updateScreen(character);
                            break;
                        } else if (magic.getMagicClass().equals(MagicClasses.HEALING)){
                            character.setHitPoint(character.getHitPoint() + character.getMagic(magic));
                            updateScreen(character);
                            break;
                        } else {
                            character.getMagic(magic);
                            updateScreen(character);
                            break;
                        }
                    }
                } else Platform.runLater(() -> {
                    Text notCorrectChoice = new Text("   info: Pls, make the correct choice....");
                    notCorrectChoice.setFill(Color.RED);
                    messageBox.getChildren().add(notCorrectChoice);
                });
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
            while (character.getInventory().size() < ((character.getLevel()+1)*10)) {
                character.experienceDrop(0.0000001);
                if (random.nextInt(10000000) == 999999) {
                    HealingItems item = itemsList.get(random.nextInt(sizeOfItems));
                    Platform.runLater(() -> {
                        Text viewFoundedInfo = new Text("   info: I found " + item);
                        viewFoundedInfo.setFill(Color.GREEN);
                        messageBox.getChildren().add(viewFoundedInfo);
                    });
                    character.getInventory().add(item);
                    updateScreen(character);
                }
            }
            return "The walk is over";
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
            monster.setHitPoint((monster.getHitPoint() - monster.applyDamage(character.getDamage())));
            character.setHitPoint((character.getHitPoint() - character.applyDamage(monster.getDamageForBattle())));
            updateScreen(character);
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
            Platform.runLater(() -> {
                Text viewUsingItems = new Text("   info: Use your items? " + character.getInventory() + "\nPls, select by index....");
                viewUsingItems.setFill(Color.GREEN);
                messageBox.getChildren().add(viewUsingItems);
            });
            while (!canTakeMessage){
                System.out.print("");
            }
            int position = Integer.valueOf(getChoice());
            position = --position;
            if (character.getInventory().contains(character.getInventory().get(position))){
                int finalPosition = position;
                Platform.runLater(() -> messageBox.getChildren().add(new Text(character.getInventory().get(finalPosition).toString())));
                ((UsingItems) character).use(character.getInventory().get(position));
                updateScreen(character);
                return true;
            } else {
                Platform.runLater(() -> {
                    Text itemNotFound = new Text("   info: Item not found");
                    itemNotFound.setFill(Color.DARKRED);
                    messageBox.getChildren().add(itemNotFound);
                });
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
                Platform.runLater(() -> messageBox.getChildren().add(new Text("   info: YOU ARE DEAD")));
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
                updateScreen(character);
                try {
                    finalize();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                return true;
            }

            else{
                if (!Objects.equals(monster.getDroppedItems(), null)){
                    Map<EquipmentItems, Item> droppedEquipment = monster.getDroppedItems();
                    character.setGold(character.getGold() + monster.getDroppedGold());
                    Platform.runLater(() -> {
                        Text droppedGold = new Text("   info: You have found " + monster.getDroppedGold() + "G");
                        droppedGold.setFill(Color.BROWN);
                        messageBox.getChildren().add(droppedGold);
                        Text viewDroppedEquipment = new Text("   info: You have found: " + droppedEquipment.toString());
                        viewDroppedEquipment.setFill(Color.BLUE);
                        messageBox.getChildren().add(viewDroppedEquipment);
                        messageBox.getChildren().add(new Text("   info: Equip all, or manual?"));
                    });
                    String equipAll;
                    while (true){
                        updateChoiceBox(" equip all", " manual");
                        while (!canTakeMessage){
                            System.out.print("");
                        }
                        equipAll = getChoice();
                        if (Objects.equals(equipAll, "equip all")){
                            for (Map.Entry<EquipmentItems, Item> entry : droppedEquipment.entrySet()) {
                                ((Equipment) character).equip(entry.getValue());
                            }
                            updateScreen(character);
                            break;
                        } else if (Objects.equals(equipAll, "manual")){
                            break;
                        } else Platform.runLater(() -> {
                            Text notCorrectChoice = new Text("   info: Pls, make the correct choice....");
                            notCorrectChoice.setFill(Color.RED);
                            messageBox.getChildren().add(notCorrectChoice);
                        });
                    }

                    if (Objects.equals(equipAll, "manual"))
                        while (true){
                            Platform.runLater(() -> messageBox.getChildren().add(new Text("   info: Pls, choose equipment....")));
                            Platform.runLater(() -> messageBox.getChildren().add(new Text("   info:" + droppedEquipment.toString())));
                            String key;
                            List <String> list = Arrays.asList("HEAD", "HANDS", "LEGS", "ARMOR");
                            updateChoiceBox(" HEAD", " HANDS", " LEGS", " ARMOR");
                            while (true){
                                while (!canTakeMessage){
                                    System.out.print("");
                                }
                                key = getChoice();
                                if (list.contains(key)) break;
                                else Platform.runLater(() -> messageBox.getChildren().add(new Text("   info: Pls, enter another key....")));
                            }
                            ((Equipment) character).equip(droppedEquipment.get(EquipmentItems.valueOf(key)));
                            updateScreen(character);
                            droppedEquipment.remove((EquipmentItems.valueOf(key)));
                            Platform.runLater(() -> messageBox.getChildren().add(new Text("   info: Equip more?")));
                            while (!canTakeMessage){
                                System.out.print("");
                            }
                            String exit = getChoice();
                            if (Objects.equals(exit, "No") || droppedEquipment.isEmpty()) break;
                        }
                }
                character.experienceDrop(monster.getExperience());
                Platform.runLater(() -> {
                    Text viewFoundedHealingItems = new Text("   info: You can add to your inventory " + monster.getInventory());
                    viewFoundedHealingItems.setFill(Color.GREEN);
                    messageBox.getChildren().add(viewFoundedHealingItems);
                });
                while (true) {
                    updateChoiceBox(" add", " skip");
                    while (!canTakeMessage){
                        System.out.print("");
                    }
                    String choice = getChoice();
                    if (Objects.equals(choice, "add")) {
                        ((UsingItems) character).add(monster.getInventory().pollLast());
                        updateScreen(character);
                        break;
                    } else if(Objects. equals(choice, "skip")) break;
                    else Platform.runLater(() -> {
                        Text notCorrectChoice = new Text("   info: Pls, make the correct choice....");
                        notCorrectChoice.setFill(Color.RED);
                        messageBox.getChildren().add(notCorrectChoice);
                    });
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
            if (character.getLevel()%3 == 0) return Boss.monsterFactory.createNewMonster(character);
            else if ((chance > 0)&&(chance < 25)) return MediumBot.monsterFactory.createNewMonster(character);
            else return EasyBot.monsterFactory.createNewMonster(character);
        }

        /**
         * End of game
         */
        private void exit(){
            Platform.runLater(() -> {
                Text game_over = new Text("   info: GAME OVER");
                game_over.setFill(Color.RED);
                messageBox.getChildren().add(game_over);
                inputMessageArea.setDisable(true);
            });
            while (true){
                System.out.print("");
            }
        }

        private int getBigHitPointBottles(Character character){
            int count = 0;
            for (HealingItems item :
                character.getInventory() ) {
                if (item instanceof BigHPBottle) count++;
            }
            return count;
        }

        private int getMiddleHitPointBottles(Character character){
            int count = 0;
            for (HealingItems item :
                    character.getInventory() ) {
                if (item instanceof MiddleHPBottle) count++;
            }
            return count;
        }

        private int getSmallHitPointBottles(Character character){
            int count = 0;
            for (HealingItems item :
                    character.getInventory() ) {
                if (item instanceof SmallHPBottle) count++;
            }
            return count;
        }

        private int getBigFlowers(Character character){
            int count = 0;
            for (HealingItems item :
                    character.getInventory() ) {
                if (item instanceof BigFlower) count++;
            }
            return count;
        }

        private int getMiddleFlowers(Character character){
            int count = 0;
            for (HealingItems item :
                    character.getInventory() ) {
                if (item instanceof MiddleFlower) count++;
            }
            return count;
        }

        private int getSmallFlowers(Character character){
            int count = 0;
            for (HealingItems item :
                    character.getInventory() ) {
                if (item instanceof SmallFlower) count++;
            }
            return count;
        }

        private synchronized void updateScreen(Character character){
            Platform.runLater(() -> {
                viewLevel.setText("LVL: " + character.getLevel());
                viewName.setText("NAME: " + character.getName());
                viewExp.setText("Experience to next level:\n" + String.valueOf((int)character.expToNextLevel()));
                viewHitPoint.setText("HP: " + character.getHitPoint());
                viewManaPoint.setText("MP: " + character.getManaPoint());
                viewAttack.setText("ATK: " + character.getDamage());
                viewGold.setText("GOLD: " + character.getGold());

            });

            if (!character.getInventory().isEmpty()){
                Platform.runLater(() -> {
                    itemBox.getChildren().clear();
                    Text viewHealingBigHitPointBottles = new Text("BigHPBottles: " + getBigHitPointBottles(character));
                    viewHealingBigHitPointBottles.setFill(Color.INDIGO);
                    Text viewHealingMiddleHitPointBottles = new Text("MiddleHPBottles: " + getMiddleHitPointBottles(character));
                    viewHealingMiddleHitPointBottles.setFill(Color.INDIGO);
                    Text viewHealingSmallHitPointBottles = new Text("SmallHPBottles: " + getSmallHitPointBottles(character));
                    viewHealingSmallHitPointBottles.setFill(Color.INDIGO);
                    Text viewHealingBigFlowers = new Text("BigFlowers: " + getBigFlowers(character));
                    viewHealingBigFlowers.setFill(Color.BLUE);
                    Text viewHealingMiddleFlowers = new Text("MiddleFlowers: " + getMiddleFlowers(character));
                    viewHealingMiddleFlowers.setFill(Color.BLUE);
                    Text viewHealingSmallFlowers = new Text("SmallFlowers: " + getSmallFlowers(character));
                    viewHealingSmallFlowers.setFill(Color.BLUE);

                    itemBox.getChildren().add(viewHealingBigHitPointBottles);
                    itemBox.getChildren().add(viewHealingMiddleHitPointBottles);
                    itemBox.getChildren().add(viewHealingSmallHitPointBottles);
                    itemBox.getChildren().add(viewHealingBigFlowers);
                    itemBox.getChildren().add(viewHealingMiddleFlowers);
                    itemBox.getChildren().add(viewHealingSmallFlowers);
                });
            }

            if (!((Equipment)character).showEquipment().isEmpty()){
                Platform.runLater(() -> equipmentBox.getChildren().clear());
                for (Map.Entry<EquipmentItems, Item> entry :
                        ((Equipment) character).showEquipment().entrySet()) {
                    Platform.runLater(() -> equipmentBox.getChildren().add(new Text(entry.getKey() + ": " + entry.getValue())));
                }
            }
        }

        private synchronized void updateChoiceBox(String... choice){
            Platform.runLater(() -> {
                currentChoiceBox.getChildren().clear();
                Text title = new Text("Current:");
                title.setFill(Color.BLACK);
                title.setFont(Font.font("Ubuntu", 18));
                currentChoiceBox.getChildren().add(title);
            });
            for (String s :
                    choice) {
                Platform.runLater(() -> {
                    Text current = new Text(s);
                    current.setFont(Font.font("Ubuntu",14));
                    currentChoiceBox.getChildren().add(current);
                });
            }
        }

        private boolean isDigit(String s) throws NumberFormatException {
            try {
                Integer.parseInt(s);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    }

    @FXML
    public void initialize(){
        Text text = new Text("Hello from new world implementation!" + "\nPls, choice your hero: archer, berserk, wizard....");
        messageBox.getChildren().add(text);
        messageBoxScrollPane.vvalueProperty().bind(messageBox.heightProperty());
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
                firstTime = false;
            } else if (Objects.equals(message, "berserk")){
                Character character = Berserk.characterFactory.createNewCharacter();
                launch(character);
                firstTime = false;
            } else if (Objects.equals(message, "wizard")){
                Character character = Wizard.characterFactory.createNewCharacter();
                launch(character);
                firstTime = false;
            } else {
                Text sendingMessage = new Text("Wrong choice.... Pls, enter archer, berserk or wizard...");
                messageBox.getChildren().add(sendingMessage);
            }
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