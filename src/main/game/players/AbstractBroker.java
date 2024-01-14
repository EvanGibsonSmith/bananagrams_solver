package src.main.game.players;

import src.main.game.Copyable;
import src.main.game.Tile;
import src.main.game.TileBagable;

// TODO document
public abstract class AbstractBroker implements Copyable<AbstractBroker> {
    Hand hand;
    TileBagable bag;

    public AbstractBroker(Hand hand, TileBagable tileBag) {
        this.hand = hand;
        this.bag = tileBag;
    }

    abstract public Tile grabTile();
    
    abstract public boolean grabTile(int num);

    // TODO not sure if the higher broker object should have getBag or if it's too revealing.
    // The only issue with removing it, which is easy to resolve with protected fields is BranchingPlayers using it for copy
    abstract public TileBagable getBag();

    abstract public boolean dump(Tile dropTile); 

    abstract public AbstractBroker copy(); // TODO maybe copy could specifiy return type better? Probably not though

    public Hand getHand() {return this.hand;}

} 
