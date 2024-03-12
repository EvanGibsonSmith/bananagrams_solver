package src.main.game.players.brokers;

import src.interfaces.Copyable;
import src.interfaces.TileBaggable;
import src.main.game.Tile;
import src.main.game.players.hand.Hand;

// TODO document
public abstract class AbstractBroker implements Copyable<AbstractBroker> {
    Hand hand;
    TileBaggable bag;

    public AbstractBroker(Hand hand, TileBaggable tileBag) {
        this.hand = hand;
        this.bag = tileBag;
    }

    // TODO add can grab tile to make things easier?
    
    abstract public Tile grabTile();
    
    abstract public boolean grabTile(int num);

    // TODO not sure if the higher broker object should have getBag or if it's too revealing.
    // The only issue with removing it, which is easy to resolve with protected fields is BranchingPlayers using it for copy
    abstract public TileBaggable getBag();

    abstract public boolean dump(Tile dropTile); 

    // TODO document. SHOULD NOT COPY HAND, right for the pla
    abstract public AbstractBroker copy(); // TODO maybe copy could specifiy return type better? Probably not though

    public Hand getHand() {return this.hand;}

    public void setHand(Hand hand) {this.hand = hand;}

} 
