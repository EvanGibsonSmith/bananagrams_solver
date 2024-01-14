package src.main.game.players;

import java.util.Queue;

import src.main.game.Tile;

// TODO probably shouldn't be public, the methods here should do into extending player classes?
public class CheatBroker extends AbstractBroker {

    public CheatBroker(Hand hand) {
        super(hand, new CheatTileBag());
    }

    public CheatBroker(Hand hand, Queue<Tile> queue) {
        super(hand, new CheatTileBag(queue));
    }

    /**
     * TODO DOCUMENT
     */
    public void addToQueue(Tile t) {bag.addTile(t);}

    public void clearQueue() {((CheatTileBag) bag).clear();} // we know type since CheatBroker only accepts CheatTileBag
 
    public void setQueue(Queue<Tile> newQueue) {((CheatTileBag) bag).setBag(newQueue);}

    // TODO add grabTile() for tile that allows a tile to just be force placed into hand
    
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
    public boolean grabTile(int num) {return ((CheatTileBag) bag).grabTile(num);}
    
    /**
     * TODO DOCUMENT
     */
    public void dropTile(Tile dropTile) {
        hand.remove(dropTile);
    }
    
    /**
     * TODO DOCUMENT
     */
    public boolean dump(Tile dropTile) {
        if (!this.hand.contains(dropTile)) {return false;}
        dropTile(dropTile); 

        grabTile(); // grab three tiles (could be easily changed later for alternate implementations)
        grabTile();
        grabTile();
        return true;
    }

    @Override
    // TODO DOCUMENT NOTE THIS DOESN'T COPY HAND SO PLAYER CAN DO IT
    public CheatBroker copy() {
        return new CheatBroker(hand);
    }

    @Override
    public CheatTileBag getBag() {
        return (CheatTileBag) this.bag; // we know this is normal tile bag since that is all HumanBroker accepts
    }
}
