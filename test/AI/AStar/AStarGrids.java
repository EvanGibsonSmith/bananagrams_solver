package test.AI.AStar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.function.BiFunction;
import java.util.function.Function;

import src.main.AI.AIPlayer;
import src.main.AI.AIPlayerParallel;
import src.main.AI.AIPlayerSerial;
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
    AStarHashSets<? extends AIPlayer> astarhash;
    AStarArrayList<? extends AIPlayer> astararray;

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
        
        player = new AIPlayerSerial(null, new Grid(wordsSet), tileBag); // game not needed for this test
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
        astarhash = new AStarHashSets<>(player, cost, heuristic, isGoal);
        System.out.println(astarhash.getFrom());
        ArrayList<? extends AIPlayer> path = astarhash.getPath();
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
        astarhash = new AStarHashSets<>(player, cost, heuristic, isGoal);
        System.out.println(astarhash.getFrom());
        ArrayList<? extends AIPlayer> path = astarhash.getPath();
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
        astarhash = new AStarHashSets<>(player, cost, heuristic, isGoal);
        System.out.println(astarhash.getFrom());
        ArrayList<? extends AIPlayer> path = astarhash.getPath();
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
        astarhash = new AStarHashSets<>(player, cost, heuristic, isGoal);
        System.out.println(astarhash.getFrom());
        ArrayList<? extends AIPlayer> path = astarhash.getPath();
        for (AIPlayer p: path) {
            System.out.println(p.getGrid());
        }
    }

    @Test
    void bigExample() {
        // set up all scrabble words
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

        char[] letters = "weetoraadosinng".toCharArray();
        Tile[] tiles = new Tile[letters.length];
        for (int i=0; i<letters.length; ++i) {
            tiles[i] = new Tile(letters[i]);
        }
        TileBag tileBag = new TileBag(tiles, 1);
        player = new AIPlayerSerial(null, new Grid(wordsSet), tileBag); // game not needed for this test
        for (int i=0; i<letters.length; ++i) {player.grabTile();}
        System.out.println("Player Hand: " + player.getHand());

        System.out.println("Beginning");
        long startTimeArray = System.currentTimeMillis();
        astararray = new AStarArrayList<>(player, cost, heuristic, isGoal);
        long endTimeArray = System.currentTimeMillis();
        System.out.println("Time: " + (endTimeArray-startTimeArray));
        for (AIPlayer p: astararray.getPath()) {
            System.out.println(p.getGrid());
        }
    }


    @Test
    void bigPeelExample() {
        // set up all scrabble words
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

        //char[] letters = "qqyzjpetramodalmoodlatpedderlfasseetgasmboopbeetrascoolbatgarmanstasbagtryabstrahgloopabcdteraluunnat".toCharArray();
        char[] letters = "abeelopndmopost".toCharArray();
        //char[] letters = "jaqrretiposzz".toCharArray();
        //char[] letters = "abassteerlsmmoo".toCharArray();
        //char[] letters = "date".toCharArray();
        int leftInBag = 3; // tiles are left in the bag after player draws up
        Tile[] tiles = new Tile[letters.length];
        for (int i=0; i<letters.length; ++i) {
            tiles[i] = new Tile(letters[i]);
        }

        TileBag tileBag = new TileBag(tiles, 1);
        player = new AIPlayerSerial(null, new Grid(wordsSet), tileBag); // game not needed for this test
        for (int i=0; i<letters.length-leftInBag; ++i) {player.grabTile();}
        System.out.println(player.getHand());

        System.out.println("Beginning");
        long startTimeArray = System.currentTimeMillis();
        astararray = new AStarArrayList<>(player, cost, heuristic, isGoal);
        long endTimeArray = System.currentTimeMillis();
        System.out.println("Time: " + (endTimeArray-startTimeArray));
        for (AIPlayer p: astararray.getPath()) {
            System.out.println(p.getGrid());
        }

        // with this big grid peel a tile
        player.playGrid(astararray.getGoal().getGrid());
        assertEquals(player.getBag().size(), leftInBag);
        assertEquals(player.getGrid(), astararray.getGoal().getGrid());
        assertEquals(player.getHand(), astararray.getGoal().getHand());
        assertTrue(player.getGrid().valid());
        assertTrue(player.canPeel());
        // player now has the A* grid placed
        player.grabTile(); // "peel" for this player
        assertEquals(player.getBag().size(), leftInBag-1);
        assertTrue(player.getGrid().valid());
        assertFalse(player.canPeel());

        // now run A* from this position
        startTimeArray = System.currentTimeMillis();
        astararray = new AStarArrayList<>(player.copy(), cost, heuristic, isGoal);
        endTimeArray = System.currentTimeMillis();
        System.out.println("Peel Time: " + (endTimeArray-startTimeArray));
        for (AIPlayer p: astararray.getPath()) {
            System.out.println("Hand: " + p.getHand());
            System.out.println(p.getGrid());
        }
    }

    @Test
    void playerInputExample() {
        // set up all scrabble words
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

        char[] letters = "linedtanetoniuw".toCharArray();
        Tile[] tiles = new Tile[letters.length];
        for (int i=0; i<letters.length; ++i) {
            tiles[i] = new Tile(letters[i]);
        }
        TileBag tileBag = new TileBag(tiles, 1);
        player = new AIPlayerSerial(null, new Grid(wordsSet), tileBag); // game not needed for this test
        for (int i=0; i<letters.length; ++i) {player.grabTile();}
        System.out.println("Player Hand: " + player.getHand());

        System.out.println("Beginning");
        long startTimeArray = System.currentTimeMillis();
        astararray = new AStarArrayList<>(player, cost, heuristic, isGoal);
        long endTimeArray = System.currentTimeMillis();
        System.out.println("Time: " + (endTimeArray-startTimeArray));
        for (AIPlayer p: astararray.getPath()) {
            System.out.println(p.getGrid());
        }

        // update player grid
        // after initial create scanner
        player.playGrid(astararray.getGoal().getGrid());
        Queue<Character> nextChars = new LinkedList<>();
        nextChars.add('a');
        nextChars.add('r');
        nextChars.add('e');
        nextChars.add('i');
        nextChars.add('n');
        while (!nextChars.isEmpty()) {
            char c = nextChars.poll();
            tileBag.addTile(new Tile(c)); // add single tile
            player.grabTile(); // grab that tile 
            startTimeArray = System.currentTimeMillis();
            astararray = new AStarArrayList<>(player, cost, heuristic, isGoal);
            endTimeArray = System.currentTimeMillis();
            System.out.println("Time: " + (endTimeArray-startTimeArray));
            for (AIPlayer p: astararray.getPath()) {
                System.out.println("Hand: " + p.getHand());
                System.out.println(p.getGrid());
            }
            player.playGrid(astararray.getGoal().getGrid());
        }
    }
}

