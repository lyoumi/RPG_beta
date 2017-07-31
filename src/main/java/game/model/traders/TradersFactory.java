package game.model.traders;

import game.model.Characters.Human;

public interface TradersFactory {
    Trader getTrader(Human human);
}
