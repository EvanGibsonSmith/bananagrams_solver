package src.main.game.players;

import src.main.game.Copyable;
import src.main.game.Tile;

// TODO document
public abstract class AbstractBroker<T extends AbstractBroker<T>> implements Copyable<T> {
    Hand hand;

    public AbstractBroker(Hand hand) {
        this.hand = hand;
    }

    abstract public Tile grabTile();

    abstract public boolean dump(Tile dropTile); 

    public Hand getHand() {return this.hand;}
} 
