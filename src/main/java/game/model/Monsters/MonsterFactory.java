package game.model.Monsters;

import game.model.Characters.Human;

public interface MonsterFactory {
    Monster createNewMonster(Human human);
}
