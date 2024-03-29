package src.main.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

import src.main.game.grids.Grid;
import src.main.game.players.bags.NormalTileBag;
import src.main.game.players.brokers.HumanBroker;
import src.main.game.players.gridarrangers.GridArranger;
import src.main.game.players.hand.Hand;
import src.main.game.players.types.Player;
import src.main.game.wordssets.WordsSet;

public class Game {
    NormalTileBag bag = defaultTileBag();
    int[] initPlayerTiles = new int[] {-1, -1, 21, 21, 21, 15, 15, 11, 11};
    WordsSet validWords = defaultWordsSet(); 
    Player[] players;  // note that player doesn't have hashcode or equality. Every player is unique
    int numPlayers;

    private WordsSet defaultWordsSet() {
        HashSet<String> wordsSet = new HashSet<>();
        try (Scanner scnr = new Scanner (new File("src/resources/scrabbleWords.txt"))) {
            scnr.useDelimiter("\n");
            while (scnr.hasNext()) {
                String next = scnr.next();
                if (next.length()-1>2) {
                    wordsSet.add(next.substring(0, next.length()-1));
                }
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new WordsSet(wordsSet);
    }

    private NormalTileBag defaultTileBag() {
        int[] alphabetAmounts = new int[] {13, 3, 3, 6, 18, 3, 4, 3, 12, 2, 2, 5, 3, 8, 11, 3, 2, 9, 6, 9, 6, 3, 3, 2, 3, 2};
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        NormalTileBag bag = new NormalTileBag(Arrays.stream(alphabetAmounts).sum()); // size is just number of tiles total from sum
        for (int a=0; a<alphabet.length; ++a) {
            for (int b=0; b<alphabetAmounts[a]; ++b) {
                bag.addTile(new Tile(alphabet[a]));
            }
        }
        return bag;
    }

    private Game(int numPlayers, NormalTileBag tiles) {
        this.bag = tiles;
        this.players = new Player[numPlayers];
        this.numPlayers = numPlayers;
    }   

    // TODO this is a bad constructor since a broker is a specific type?
    public Game(int numPlayers, NormalTileBag tiles, WordsSet validWords) {
        this(numPlayers, tiles);
        this.validWords = validWords;
        for (int i=0; i<numPlayers; ++i) {
            players[i] = new Player(this, new GridArranger(new Grid(this.validWords)), new HumanBroker(new Hand(), this));
        }
    }

    public Game(int numPlayers, NormalTileBag tiles, WordsSet validWords, int[] initPlayerTiles) {
        this(numPlayers, tiles, validWords);
        this.initPlayerTiles = initPlayerTiles;
    }

    // TODO these constructors needed?
    public Game(int numPlayers, NormalTileBag tiles, HashSet<String> validWords) {
        this(numPlayers, tiles);
        this.validWords = new WordsSet(validWords);
        for (int i=0; i<numPlayers; ++i) {
            Hand newHand = new Hand();
            players[i] = new Player(this, new GridArranger(new Grid(this.validWords), newHand), new HumanBroker(newHand, tiles));
        }
    }

    public Game(int numPlayers, NormalTileBag tiles, HashSet<String> validWords, int[] initPlayerTiles) {
        this(numPlayers, tiles, validWords);
        this.initPlayerTiles = initPlayerTiles;
    }

    public Game(Player[] players, NormalTileBag tiles, WordsSet validWords, int[] initPlayerTiles) {
        this(players);
        this.bag = tiles;
        this.numPlayers = players.length;
        this.validWords = validWords;
        this.initPlayerTiles = initPlayerTiles;
    }

    public Game(Player[] players, NormalTileBag tiles, HashSet<String> validWords, int[] initPlayerTiles) {
        this(players);
        this.bag = tiles;
        this.numPlayers = players.length;
        this.validWords = new WordsSet(validWords);
        this.initPlayerTiles = initPlayerTiles;
    }

    public Game(Player[] players) {
        this.players = players;
        for (Player p: players) { // set player's game to this one
            p.setGame(this);
        }
    }

    @Override
    public String toString() {
        return "Bag: \n" + bag.toString() + "Player: \n" + players.toString();
    }

    public Player[] getPlayers() {
        return this.players;
    }  

    public NormalTileBag getBag() {
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
        int numInitialTiles = this.initPlayerTiles[this.numPlayers];
        for (Player p: this.players) {
            p.grabTile(numInitialTiles);
        }
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
