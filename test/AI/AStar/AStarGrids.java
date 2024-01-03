package test.AI.AStar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.function.BiFunction;
import java.util.function.Function;

import src.main.AI.AIPlayer;
import src.main.AI.AStarArrayList;
import src.main.AI.AStarHashSets;
import src.main.game.Grid;
import src.main.game.Location;
import src.main.game.Tile;
import src.main.game.TileBag;

class AStarGrids {
    AIPlayer player;
    final Function<AIPlayer, Double> heuristic = (p) -> (double) p.getHand().size();
    final BiFunction<AIPlayer, AIPlayer, Double> cost = (p, q) -> (double) q.getHand().size() - p.getHand().size();
    final Function<AIPlayer, Boolean> isGoal = (p) -> p.getHand().size()==0;
    AStarHashSets<AIPlayer> astarhash;
    AStarArrayList<AIPlayer> astararray;

    @BeforeEach
    void resetPlayer() {
        this.player = null;
    }

    void setupPlayer1() {
        HashSet<String> wordsSet = new HashSet<String>();
        wordsSet.add("act"); // first word to play
        wordsSet.add("tap");
        wordsSet.add("pat"); // can be played

        wordsSet.add("par");
        wordsSet.add("part");
        wordsSet.add("pane");
        wordsSet.add("fact"); 

        char[] letters = "actapat".toCharArray();
        Tile[] tiles = new Tile[letters.length];
        for (int i=0; i<letters.length; ++i) {
            tiles[i] = new Tile(letters[i]);
        }
        TileBag tileBag = new TileBag(tiles, 1);
        
        player = new AIPlayer(null, new Grid(wordsSet), tileBag); // game not needed for this test
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();
    }

    @Test
    void CompletedGrid() {  
        setupPlayer1();
        
        player.placeTile(new Location(0, 0), new Tile('a'));
        player.placeTile(new Location(0, 1), new Tile('c'));
        player.placeTile(new Location(0, 2), new Tile('t'));
        player.placeTile(new Location(1, 2), new Tile('a')); 
        player.placeTile(new Location(2, 2), new Tile('p'));
        // These two placed make empty hand (create word pat)
        player.placeTile(new Location(2, 3), new Tile('a')); 
        player.placeTile(new Location(2, 4), new Tile('t'));
        assertTrue(player.gridValid());
        
        // with valid grid perform A*. We can rid our tiles with just one word
        // TODO make the heuristic methods within their proper class
        astarhash = new AStarHashSets<>(player, cost, heuristic, isGoal);
        System.out.println(astarhash.getFrom());
        ArrayList<AIPlayer> path = astarhash.getPath();
        for (AIPlayer p: path) {
            System.out.println(p.getGrid());
        }
    }

    @Test
    void OneWordAway() {  
        setupPlayer1();
        
        player.placeTile(new Location(0, 0), new Tile('a'));
        player.placeTile(new Location(0, 1), new Tile('c'));
        player.placeTile(new Location(0, 2), new Tile('t'));
        player.placeTile(new Location(1, 2), new Tile('a')); 
        player.placeTile(new Location(2, 2), new Tile('p'));
        // These two placed make empty hand (create word pat or tap again)
        //player.placeTile(new Location(2, 3), new Tile('a')); 
        //player.placeTile(new Location(2, 4), new Tile('t'));
        assertTrue(player.gridValid());
        
        // with valid grid perform A*. We can rid our tiles with just one word
        // TODO make the heuristic methods within their proper class
        astarhash = new AStarHashSets<>(player, cost, heuristic, isGoal);
        System.out.println(astarhash.getFrom());
        ArrayList<AIPlayer> path = astarhash.getPath();
        for (AIPlayer p: path) {
            System.out.println(p.getGrid());
        }
    }

    @Test
    void TwoWordAway() {  
        setupPlayer1();
        
        player.placeTile(new Location(0, 0), new Tile('a'));
        player.placeTile(new Location(0, 1), new Tile('c'));
        player.placeTile(new Location(0, 2), new Tile('t'));
        // below can play tap
        //player.placeTile(new Location(1, 2), new Tile('a'));
        //player.placeTile(new Location(2, 2), new Tile('p'));
        // below can play pat or tap again
        //player.placeTile(new Location(2, 3), new Tile('a')); 
        //player.placeTile(new Location(2, 4), new Tile('t'));
        assertTrue(player.gridValid());
        
        // with valid grid perform A*. We can rid our tiles with just one word
        // TODO make the heuristic methods within their proper class
        astarhash = new AStarHashSets<>(player, cost, heuristic, isGoal);
        System.out.println(astarhash.getFrom());
        ArrayList<AIPlayer> path = astarhash.getPath();
        for (AIPlayer p: path) {
            System.out.println(p.getGrid());
        }
    }

    
    @Test
    void emptyBoardThreeWordsAway() {
        setupPlayer1();
        
        //player.placeTile(new Location(0, 0), new Tile('a'));
        //player.placeTile(new Location(0, 1), new Tile('c'));
        //player.placeTile(new Location(0, 2), new Tile('t'));
        // below can play tap
        //player.placeTile(new Location(1, 2), new Tile('a'));
        //player.placeTile(new Location(2, 2), new Tile('p'));
        // below can play pat or tap again
        //player.placeTile(new Location(2, 3), new Tile('a')); 
        //player.placeTile(new Location(2, 4), new Tile('t'));
        assertTrue(player.gridValid());
        
        // with valid grid perform A*. We can rid our tiles with just one word
        // TODO make the heuristic methods within their proper class
        astarhash = new AStarHashSets<>(player, cost, heuristic, isGoal);
        System.out.println(astarhash.getFrom());
        ArrayList<AIPlayer> path = astarhash.getPath();
        for (AIPlayer p: path) {
            System.out.println(p.getGrid());
        }
    }

    @Test
    void bigExample() {
        // set up all scrabble words
        HashSet<String> wordsSet = new HashSet<>();
        try (Scanner scnr = new Scanner (new File("src/resources/10000words.txt"))) {
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

        //char[] letters = "qqyzjpetramodalmoodlatpedderlfasseetgasmboopbeetrascoolbatgarmanstasbagtryabstrahgloopabcdteraluunnat".toCharArray();
        //char[] letters = "baarteeopg".toCharArray();
        char[] letters = "hello".toCharArray();
        //char[] letters = "abassteerlsmmoo".toCharArray();
        //char[] letters = "date".toCharArray();
        Tile[] tiles = new Tile[letters.length];
        for (int i=0; i<letters.length; ++i) {
            tiles[i] = new Tile(letters[i]);
        }
        TileBag tileBag = new TileBag(tiles, 1);
        player = new AIPlayer(null, new Grid(wordsSet), tileBag); // game not needed for this test
        for (int i=0; i<letters.length; ++i) {player.grabTile();}
        
        /*long startTime = System.currentTimeMillis();
        astarhash = new AStarHashSets<>(player.copy(), cost, heuristic, isGoal);
        long endTime = System.currentTimeMillis();
        System.out.println("Time: " + (endTime-startTime));
        for (AIPlayer p: astarhash.getPath()) {
            System.out.println(p.getGrid());
        }*/

        System.out.println("Beginning");
        long startTimeArray = System.currentTimeMillis();
        astararray = new AStarArrayList<>(player.copy(), cost, heuristic, isGoal); // TODO make A star handle intiial copy with copyable interface
        long endTimeArray = System.currentTimeMillis();
        System.out.println("Time: " + (endTimeArray-startTimeArray));
        for (AIPlayer p: astararray.getPath()) {
            System.out.println(p.getGrid());
        }


    

    }
}

