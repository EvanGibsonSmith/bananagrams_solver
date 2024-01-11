package src.main.game.players;

import java.util.LinkedList;
import java.util.Queue;

import src.main.game.Tile;

class CheatBroker extends AbstractBroker<CheatBroker> {
    Queue<Tile> queue = new LinkedList<>();

    public CheatBroker(Hand hand) {
        super(hand);
    }

    public CheatBroker(Hand hand, Queue<Tile> queue) {
        super(hand);
        this.queue = queue;
    }

    /**
     * TODO DOCUMENT
     */
    public void addToQueue(Tile t) {queue.add(t);}

    public void clearQueue() {queue.clear();}

    public void setQueue(Queue<Tile> newQueue) {queue = newQueue;}

    /**
     * Grabs a tile from the bag and adds it to the players hand.
     * In the context of a game, this is essentially a one player peel.
     * There are no checks to see if the player can actually do this, (i.e) 
     * another player is done or this player is done. That is the responsibility of the 
     * game
     * @return Tile grabbed, null if no tile could be grabbed (bag empty)
     */
    public Tile grabTile() {
        if (queue.isEmpty()) {return null;} // edge case

        Tile t = queue.poll();
        hand.add(t);
        return t;
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
        if (queue.size()<num) {return false;}
        for (int i=0; i<num; ++i) {
            grabTile();
        }
        return true;
    }
    
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
    public CheatBroker copy() {
        return new CheatBroker(hand);
    }
}
