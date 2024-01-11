package src.main.game.players;

import java.util.Collection;

import src.main.game.Copyable;
import src.main.game.Tile;

// TODO document
public abstract class AbstractBroker<T extends AbstractBroker<T>> implements Copyable<AbstractBroker<T>> {
    Hand hand;

    public AbstractBroker(Hand hand) {
        this.hand = hand;
    }

    abstract public Tile grabTile();
    
    abstract public boolean grabTile(int num);

    // TODO not sure if the higher broker object should have getBag or if it's too revealing.
    // The only issue with removing it, which is easy to resolve with protected fields is BranchingPlayers using it for copy
    abstract public Object getBag();  // TODO fix the return type to something actually reasonable (collection?)

    abstract public boolean dump(Tile dropTile); 

    abstract public AbstractBroker<T> copy();

    public Hand getHand() {return this.hand;}

} 
