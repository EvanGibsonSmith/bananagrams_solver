package src.main.game.players;

import src.main.game.Tile;
import src.main.game.NormalTileBag;
import src.main.game.Game;

// TODO public?!
public class HumanBroker extends AbstractBroker {
    NormalTileBag bag;
    
    // TODO maybe create a dummy game instead of having to create this extra constructor.
    public HumanBroker(Hand hand, int bagSize) {
        super(hand);
        this.bag = new NormalTileBag(bagSize);
    }

    public HumanBroker(Hand hand, Game game) {
        super(hand);
        this.bag = game.getBag();
    }

    public HumanBroker(Hand hand, NormalTileBag bag) {
        super(hand);
        this.bag = bag;
    }

    /**
     * Grabs a tile from the bag and adds it to the players hand.
     * In the context of a game, this is essentially a one player peel.
     * There are no checks to see if the player can actually do this, (i.e) 
     * another player is done or this player is done. That is the responsibility of the 
     * game
     * @return Tile grabbed, null if no tile could be grabbed (bag empty)
     */
    public Tile grabTile() {
        if (bag.isEmpty()) {return null;} // edge case

        Tile grabbedTile = bag.grabTile();
        this.hand.add(grabbedTile); 
        return grabbedTile;
    }

    /**
     * Grabs number of tiles given bag and adds it to the players hand.
     * In the context of a game, used to pass out the initial tiles at the beginning of the game
     * There are no checks to see if the player can actually do this, (i.e) 
     * another player is done or this player is done. That is the responsibility of the 
     * game
     * @param num the number of tiles to grab
     * @return boolean if successful or not, false if not enough tiles to grab num of them
     */
    public boolean grabTile(int num) {
        if (bag.size()>num) {return false;} // edge case

        for (int i=0; i<num; ++i) {
            Tile grabbedTile = bag.grabTile();
            this.hand.add(grabbedTile); 
        }
        return true;
    }
    
    /**
     * Player drops one tile into the bag. This is not done during the game
     * without drawing three tiles. 
     * @param dropTile the tile that is dropped from player
     */
    public void dropTile(Tile dropTile) { // TODO drop tile from the hand, whil
        hand.remove(dropTile);
        //bag.addTile(dropTile); TODO what is the happening
    }
    
    /**
     * The action within the game of dumping one tile to get three in return.
     * This is usually done to remove a diffcult letter, like Q, X, or Z. In a 
     * dump other players in the game do not need to participate like peel
     * @param dropTile the tile chosen to be dumped off
     * @return boolean if the dump was successful (were there three tiles to grab)
     */
    public boolean dump(Tile dropTile) {
        if (!this.hand.contains(dropTile)) {return false;}
        dropTile(dropTile); 

        grabTile(); // grab three tiles (could be easily changed later for alternate implementations)
        grabTile();
        grabTile();
        return true;
    }
    
    public NormalTileBag getBag() {
        return this.bag;
    }

    @Override
    public HumanBroker copy() {
        return new HumanBroker(hand, bag);
    }
}
