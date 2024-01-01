package src.main.game;

import java.util.HashSet;

public class Game {
    TileBag bag;
    Player[] players;  // note that player doesn't have hashcode or equality. Every player is unique
    int numPlayers;
    WordsSet validWords;

    private Game(int numPlayers, TileBag tiles) {
        this.bag = tiles;
        this.players = new Player[numPlayers];
        this.numPlayers = numPlayers;
    }   

    public Game(int numPlayers, TileBag tiles, WordsSet validWords) {
        this(numPlayers, tiles);
        this.validWords = validWords;
        for (int i=0; i<numPlayers; ++i) {
            players[i] = new Player(this, new Grid(this.validWords), this.bag);
        }
    }

    public Game(int numPlayers, TileBag tiles, HashSet<String> validWords) {
        this(numPlayers, tiles);
        this.validWords = new WordsSet(validWords);
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
        if (!(this.bag.size()<numPlayers)) { // if there aren't enough tiles to peel again
            return false;
        }
        for (Player p: this.players) { // if any player has completed grid
            if (p.canPeel()) {return true;} // note that we already know there are not enough tiles for another peel
        }
        return false;
    }

    /**
     * Begins the game, giving each player the number of tiles that are specified with
     * the number of players
     */
    public void beginGame() {
        // TODO COMPLETE THIS (LOOK at the actual game and how they divvy things up initially)
    }

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
}
