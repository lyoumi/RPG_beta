package game.model.Monsters;

import game.model.Items.EquipmentItems;
import game.model.Items.items.HealingItems;
import game.model.Items.items.Item;
import game.model.abilities.Magic;

import java.util.LinkedList;
import java.util.Map;

/**
 * Created by pikachu on 13.07.17.
 */

/**
 * Базовый интерфейс для создания монстров.
 */
public interface Monster {

    /**
     * Метод, возвращающий опыт после смерти монстра.
     *
     * @return
     *          int experience.
     */
    int getExperience();

    /**
     * Метод, возвращающий урон монстра используемый в бою.
     *
     * @return
     *          int damage.
     */
    int getDamageForBattle();

    /**
     * Метод, возвращаюший значение полученного урона.
     *
     * @param applyDamage
     *          int applied damage
     * @return
     *          int applied damage
     */
    int applyDamage(int applyDamage);

    /**
     * Метод, возвращающий текущее количество здоровья.
     *
     * @return
     *          int hitPoint.
     */
    int getHitPoint();

    /**
     * Метод для изменения текущего количества здоровья.
     *
     * @param hitPoint
     *          new hitPoint value.
     */
    void setHitPoint(int hitPoint);

    /**
     * Метод, возвращающий выпавшие предметы из монстра {@link HealingItems}
     * @return
     *          dropped {@link HealingItems}
     */
    LinkedList<HealingItems> getInventory();

    /**
     * Метод, возвращающий выпавшие предметы из монстра {@link Item}
     *
     * @return
     *          dropped {@link Map} of {@link Item}
     */
    Map<EquipmentItems, Item> getDroppedItems();

    /**
     * Устанавливает дебаф на монстра (постепенный урон, запрещение атаки).
     *
     * @param magic
     *           {@link game.model.abilities.debuffs.DebuffMagic} implementation.
     * @return
     */
    boolean setDebuff(Magic magic);

    /**
     * Метод, возвращающий true в случае если монстр мертв и false если жив.
     *
     * @return
     *          boolean isDead.
     */
    boolean isDead();

    /**
     * Метод, возвращающий золото, выпавшее после смерти монстра.
     *
     * @return
     *          int dropped gold
     */
    int getDroppedGold();

    void finalize() throws Throwable;
}
