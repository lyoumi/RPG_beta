package game.model.traders;

import game.model.Characters.Character;

public interface TradersFactory {
    Trader getTrader(Character character);
}
