package game.model.Monsters;

import game.model.Characters.Character;

public interface MonsterFactory {
    Monster createNewMonster(Character character);
}
