package src.main.game.players.hand;

import java.util.Iterator;

import src.data_structures.MultiSet;
import src.interfaces.Copyable;
import src.main.game.Tile;

public class Hand implements Copyable<Hand>, Iterable<Tile> {
    MultiSet<Tile> hand = new MultiSet<>();

    public Hand() {}

    // TODO should this be here? It is convienent but not necessary 
    public Hand(char[] charHand) {
        for (char c: charHand) {
            hand.add(new Tile(c));
        }
    }

    public Hand(MultiSet<Tile> hand) {
        this.hand = hand;
    }

    @Override
    public String toString() {
        return this.hand.toString();
    }
    
    @Override
    public int hashCode() {
        return this.hand.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass()!=this.getClass()) {return false;}
        return hand.equals(((Hand) obj).getMultiSet());
    }

    @Override 
    public Hand copy() {
        return new Hand(this.hand.copy());
    }

    @Override 
    public Iterator<Tile> iterator() {
        return this.hand.iterator();
    }

    public MultiSet<Tile> getMultiSet() {return hand;}

    /**
     * @param t the tile to check if it is within the hand
     * @return if the tile is within the hand
     */
    public boolean inHand(Tile t) {
        return hand.contains(t);
    }

    /**
     * If player has no tiles left in their hand. This means all the tiles are on the Grid
     * @return boolean of tiles left
     */
    public boolean handEmpty() {
        return hand.isEmpty();
    }

    public void add(Tile t) {hand.add(t);};

    public void remove(Tile t) {hand.remove(t);};
    
    public boolean contains(Tile t) {return hand.contains(t);};

    public int size() {return hand.size();}

    public boolean isEmpty() {return hand.isEmpty();}

}
