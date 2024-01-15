package test.game.AIplayerwrappers;

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

import src.algorithms.astar.AStarArrayList;
import src.algorithms.astar.AStarHashSets;
import src.main.game.Location;
import src.main.game.Tile;
import src.main.game.grids.Grid;
import src.main.game.players.brokers.HumanBroker;
import src.main.game.players.hand.Hand;
import src.main.game.players.types.branchplayers.AbstractBranchingPlayer;
import src.main.game.players.types.branchplayers.BranchingPlayerSerial;
import src.main.game.players.bags.NormalTileBag;

class AStarGrids {
    AbstractBranchingPlayer player;
    final Function<AbstractBranchingPlayer, Double> heuristic = (p) -> (double) p.getHand().size();
    final BiFunction<AbstractBranchingPlayer, AbstractBranchingPlayer, Double> cost = (p, q) -> (double) q.getHand().size() - p.getHand().size();
    final Function<AbstractBranchingPlayer, Boolean> isGoal = (p) -> p.getHand().size()==0;
    AStarHashSets<? extends AbstractBranchingPlayer> astarhash;
    AStarArrayList<? extends AbstractBranchingPlayer> astararray;

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
        NormalTileBag tileBag = new NormalTileBag(tiles, 1);
        
        // TODO fix this stupid way of setting up player objects
        player = new BranchingPlayerSerial(null, new Grid(wordsSet), new HumanBroker(new Hand(), tileBag)); // game not needed for this test
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();
    }

    // used to set up various letter combinations and characters for peeling
    void playerInputSetup(char[] letters, Queue<Character> nextChars) {
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

        Tile[] tiles = new Tile[letters.length];
        for (int i=0; i<letters.length; ++i) {
            tiles[i] = new Tile(letters[i]);
        }
        NormalTileBag tileBag = new NormalTileBag(tiles, 1);
        
        player = new BranchingPlayerSerial(null, new Grid(wordsSet), new HumanBroker(new Hand(), tileBag)); // game not needed for this test
        for (int i=0; i<letters.length; ++i) {player.grabTile();}
        System.out.println("Player Hand: " + player.getHand());

        System.out.println("Beginning");
        long totalStartTime = System.currentTimeMillis();
        long startTimeArray = System.currentTimeMillis();
        astararray = new AStarArrayList<>(player, cost, heuristic, isGoal);
        astararray.compute();
        long endTimeArray = System.currentTimeMillis();
        System.out.println("Time: " + (endTimeArray-startTimeArray));
        for (AbstractBranchingPlayer p: astararray.getPath()) {
            System.out.println(p.getGrid());
        }

        // update player grid
        // after initial create scanner
        player.playGrid(astararray.getGoal().getGrid());
        while (!nextChars.isEmpty()) {
            char c = nextChars.poll();
            tileBag.addTile(new Tile(c)); // add single tile
            player.grabTile(); // grab that tile 
            startTimeArray = System.currentTimeMillis();
            astararray = new AStarArrayList<>(player, cost, heuristic, isGoal);
            astararray.compute();
            endTimeArray = System.currentTimeMillis();
            System.out.println("Time: " + (endTimeArray-startTimeArray));
            for (AbstractBranchingPlayer p: astararray.getPath()) {
                System.out.println("Hand: " + p.getHand());
                System.out.println(p.getGrid());
            }
            player.playGrid(astararray.getGoal().getGrid());
        }
        System.out.println("Total Time: " + (System.currentTimeMillis() - totalStartTime));

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
        astarhash.compute();
        System.out.println(astarhash.getFrom());
        ArrayList<? extends AbstractBranchingPlayer> path = astarhash.getPath();
        for (AbstractBranchingPlayer p: path) {
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
        astarhash.compute();
        System.out.println(astarhash.getFrom());
        ArrayList<? extends AbstractBranchingPlayer> path = astarhash.getPath();
        for (AbstractBranchingPlayer p: path) {
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
        astarhash.compute();
        System.out.println(astarhash.getFrom());
        ArrayList<? extends AbstractBranchingPlayer> path = astarhash.getPath();
        for (AbstractBranchingPlayer p: path) {
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
        astarhash.compute();
        System.out.println(astarhash.getFrom());
        ArrayList<? extends AbstractBranchingPlayer> path = astarhash.getPath();
        for (AbstractBranchingPlayer p: path) {
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
        NormalTileBag tileBag = new NormalTileBag(tiles, 1);
        player = new BranchingPlayerSerial(null, new Grid(wordsSet), tileBag); // game not needed for this test
        for (int i=0; i<letters.length; ++i) {player.grabTile();}
        System.out.println("Player Hand: " + player.getHand());

        System.out.println("Beginning");
        long startTimeArray = System.currentTimeMillis();
        astararray = new AStarArrayList<>(player, cost, heuristic, isGoal);
        astararray.compute();
        long endTimeArray = System.currentTimeMillis();
        System.out.println("Time: " + (endTimeArray-startTimeArray));
        for (AbstractBranchingPlayer p: astararray.getPath()) {
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

        char[] letters = "abeelopndmopost".toCharArray();
        int leftInBag = 3; // tiles are left in the bag after player draws up
        Tile[] tiles = new Tile[letters.length];
        for (int i=0; i<letters.length; ++i) {
            tiles[i] = new Tile(letters[i]);
        }

        NormalTileBag tileBag = new NormalTileBag(tiles, 1);
        player = new BranchingPlayerSerial(null, new Grid(wordsSet), tileBag); // game not needed for this test
        for (int i=0; i<letters.length-leftInBag; ++i) {player.grabTile();}
        System.out.println(player.getHand());

        System.out.println("Beginning");
        long startTimeArray = System.currentTimeMillis();
        astararray = new AStarArrayList<>(player, cost, heuristic, isGoal);
        astararray.compute();
        long endTimeArray = System.currentTimeMillis();
        System.out.println("Time: " + (endTimeArray-startTimeArray));
        for (AbstractBranchingPlayer p: astararray.getPath()) {
            System.out.println(p.getGrid());
        }

        // with this big grid peel a tile
        player.playGrid(astararray.getGoal().getGrid());
        assertEquals(((NormalTileBag) player.getBag()).size(), leftInBag);
        assertEquals(player.getGrid(), astararray.getGoal().getGrid());
        assertEquals(player.getHand(), astararray.getGoal().getHand());
        assertTrue(player.getGrid().valid());
        assertTrue(player.canPeel());
        // player now has the A* grid placed
        player.grabTile(); // "peel" for this player
        assertEquals(((NormalTileBag) player.getBag()).size(), leftInBag-1);
        assertTrue(player.getGrid().valid());
        assertFalse(player.canPeel());

        // now run A* from this position
        startTimeArray = System.currentTimeMillis();
        astararray = new AStarArrayList(player.copy(), cost, heuristic, isGoal);
        astararray.compute();
        endTimeArray = System.currentTimeMillis();
        System.out.println("Peel Time: " + (endTimeArray-startTimeArray));
        for (AbstractBranchingPlayer p: astararray.getPath()) {
            System.out.println("Hand: " + p.getHand());
            System.out.println(p.getGrid());
        }
    }

    @Test
    void playerInputExampleSmall() {
        char[] letters = "aaeetlop".toCharArray();
        Queue<Character> nextChars = new LinkedList<>();
        nextChars.add('t');
        playerInputSetup(letters, nextChars);
    }

    @Test
    void playerInputExampleMedium() {
        char[] letters = "linedowsteaw".toCharArray();
        Queue<Character> nextChars = new LinkedList<>();
        nextChars.add('a');
        nextChars.add('r');
        nextChars.add('e');
        nextChars.add('i');
        nextChars.add('n');
        playerInputSetup(letters, nextChars);
    }

    @Test
    void playerInputExampleMediumLarge() {
        char[] letters = "ertsyuasfawf".toCharArray();
        Queue<Character> nextChars = new LinkedList<>();

        // note since this is a queue the order is reverse from the other test in AStarGrud
        nextChars.add('g');
        nextChars.add('f');
        nextChars.add('s');
        nextChars.add('s');
        nextChars.add('t');
        playerInputSetup(letters, nextChars);
    }
}

