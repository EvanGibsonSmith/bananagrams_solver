package src.main;

import java.util.HashSet;

public class Game {
    TileBag bag;
    Player[] players;
    int numPlayers;
    HashSet<String> validWords;

    public Game(int numPlayers, TileBag tiles, HashSet<String> validWords) {
        this.bag = tiles;
        this.validWords = validWords;
        this.players = new Player[numPlayers];
        this.numPlayers = numPlayers;
        for (int i=0; i<numPlayers; ++i) {
            this.players[i] = new Player(new Grid(this.validWords), tiles);
        }
    }

    public Boolean gameComplete() {
        // TODO more elegant way to map every element and check if any are true with built in functions?
        if (!(this.bag.size()<numPlayers)) { // if there aren't enough tiles to peel again
            return false;
        }
        for (Player p: this.players) { // if any player has completed grid TODO could make this peel something a player has to "submit" like in the game
            if (p.gridDone()) {return true;} // note that we already know there are not enough tiles for another peel
        }
        return false;
    }

    // TODO document
    public void peel() {
        // STUB
    }
    
}
