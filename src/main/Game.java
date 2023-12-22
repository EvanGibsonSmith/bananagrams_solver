package src.main;

import java.util.HashSet;

public class Game {
    TileBag bag;
    Player[] players;  // note that player doesn't have hashcode or equality. Every player is unique
    int numPlayers;
    HashSet<String> validWords;

    public Game(int numPlayers, TileBag tiles, HashSet<String> validWords) {
        this.bag = tiles;
        this.validWords = validWords;
        this.players = new Player[numPlayers];
        this.numPlayers = numPlayers;
        for (int i=0; i<numPlayers; ++i) {
            players[i] = new Player(this, new Grid(this.validWords), this.bag);
        }
    }   

    @Override
    public String toString() {
        return ""; // TODO STUB
    }

    public Player[] getPlayers() {
        return this.players;
    }  

    public TileBag getBag() {
        return this.bag;
    }

    public boolean gameComplete() {
        // TODO more elegant way to map every element and check if any are true with built in functions?
        if (!(this.bag.size()<numPlayers)) { // if there aren't enough tiles to peel again
            return false;
        }
        for (Player p: this.players) { // if any player has completed grid TODO could make this peel something a player has to "submit" like in the game
            if (p.canPeel()) {return true;} // note that we already know there are not enough tiles for another peel
        }
        return false;
    }

    /**
     * Begins the game, giving each player the number of tiles that are specified with
     * the number of players
     */
    public void beginGame() {
        // TODO COMPLETE THIS 
    }

    // TODO document
    /**
     * Performs a "peel" from bananagrams in which each player is given one more tile into their hand.
     * Does NOT check if peel is valid or if player requesting it cannot peel. 
     * @returns false if there are not enough tiles to peel, otherwise true
     */
    public boolean peel() {
        if (this.bag.size()<numPlayers) { // if there aren't enough tiles to peel again
            return false;
        }
        for (Player p: this.players) {
            p.grabTile();
        }
        return true;
    }
    
    // TODO this might not be needed if the player handles the dump
    /**
     * Dumps for the specific player given TODO player is private so this needs to be here. Could we instead protect the player class instead of putting this here?
     * Does not check if the player is within the player TODO this seems bad
     * @param p player (that must be in the game) that we want to dump
     * @param t the tile that we wise to dump
     * @return false if the tile was not present in the player's hand, otherwise false
     */
    public boolean dump(Player p, Tile t) {
        // TODO should we check if player in game (Tehcnically an O(N) operation but for only a few players))
        return p.dump(t);
    }
    
    
}
