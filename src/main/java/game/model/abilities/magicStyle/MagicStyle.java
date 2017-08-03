package game.model.abilities.magicStyle;

import game.model.Characters.Character;
import game.model.Characters.characters.Archer;
import game.model.Characters.characters.Berserk;
import game.model.abilities.Magic;
import game.model.abilities.buffs.buffs.DragonForm;
import game.model.abilities.buffs.buffs.ForceOfJedi;
import game.model.abilities.instants.instants.combat.DragonBall;
import game.model.abilities.instants.instants.combat.FireBall;
import game.model.abilities.instants.instants.combat.IceChains;
import game.model.abilities.instants.instants.combat.WindsOfWinter;
import game.model.abilities.instants.instants.healing.SmallHealing;

import java.util.ArrayList;

/**
 * Класс, дающий персонажу соответствующий его классу набор магий.
 */
public class MagicStyle {
    public static ArrayList<Magic> getMagicStyle(Character character){
        ArrayList<Magic> listOfMagic = new ArrayList<>();
        if (character instanceof Archer){
            listOfMagic.add(FireBall.magicFactory.getMagicFactory(character.getLevel()));
            listOfMagic.add(IceChains.magicFactory.getMagicFactory(character.getLevel()));
            listOfMagic.add(SmallHealing.magicFactory.getMagicFactory(character.getLevel()));
            return listOfMagic;
        } else if (character instanceof Berserk){
            listOfMagic.add(DragonBall.magicFactory.getMagicFactory(character.getLevel()));
            listOfMagic.add(DragonForm.magicFactory.getMagicFactory(character.getLevel()));
            listOfMagic.add(SmallHealing.magicFactory.getMagicFactory(character.getLevel()));
            return listOfMagic;
        } else {
            listOfMagic.add(WindsOfWinter.magicFactory.getMagicFactory(character.getLevel()));
            listOfMagic.add(SmallHealing.magicFactory.getMagicFactory(character.getLevel()));
            listOfMagic.add(ForceOfJedi.magicFactory.getMagicFactory(character.getLevel()));
            return listOfMagic;
        }
    }
}
