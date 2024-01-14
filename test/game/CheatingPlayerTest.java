package test.game;

import org.junit.jupiter.api.Test;

import src.main.AI.AStar.AStarArrayList;
import src.main.AI.AStar.AStarHashSets;
import src.main.AI.AStar.AbstractAStar;
import src.main.game.Grid;
import src.main.game.Tile;
import src.main.game.NormalTileBag;
import src.main.game.players.AIPlayers.BranchPlayers.AbstractBranchingPlayer;
import src.main.game.players.AIPlayers.BranchPlayers.BranchingPlayerParallel;
import src.main.game.players.AIPlayers.BranchPlayers.BranchingPlayerSerial;
import src.main.game.players.CheatPlayer;
import src.main.game.players.CheatTileBag;
import src.main.game.players.GridArranger;
import src.main.game.players.Hand;
import src.main.game.players.HumanBroker;
import src.main.game.players.AbstractBroker;
import src.main.game.players.CheatBroker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class CheatingPlayerTest {
    CheatPlayer player;

    HashSet<String> getScrabbleWords() {
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
        return wordsSet;
    }

    void setupPlayer1()  {
        HashSet<String> wordsSet = new HashSet<String>();
        wordsSet.add("act"); // first word to play
        wordsSet.add("tap");
        wordsSet.add("pat"); // can be played

        wordsSet.add("par");
        wordsSet.add("part");
        wordsSet.add("pane");
        wordsSet.add("fact"); 
        
        try {
            AbstractBroker broker = new CheatBroker(new Hand());
            AbstractBranchingPlayer playerPart = new BranchingPlayerParallel(null, new Grid(wordsSet), broker);
            player = new CheatPlayer(AStarArrayList.class, playerPart); // game not needed for this test
        }
        catch (Exception e) {
            e.printStackTrace();
        }

       // CheatBroker broker = player.getBroker(); // TODO below should probably be player.addToQueue()
       // TODO should do with CheatBroker eventually, but there are TileBag casting issues 
        CheatBroker broker = player.getBroker();
        broker.addToQueue(new Tile('a')); // TODO this should probably be able to to take in characters as well
        broker.addToQueue(new Tile('c'));
        broker.addToQueue(new Tile('t'));
        broker.addToQueue(new Tile('a'));
        broker.addToQueue(new Tile('p'));
        broker.addToQueue(new Tile('a'));
        broker.addToQueue(new Tile('t'));
        
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();
    }

    void runTestPrintOut() {
        long totalTimeStart = System.currentTimeMillis();
        // first time before peeling begins
        long startTime= System.currentTimeMillis();
        player.playSolution(); // should have played entire solution
        long endTime = System.currentTimeMillis();
        ArrayList<? extends AbstractBranchingPlayer> firstPath = player.getAStar().getPath();
        System.out.println("Time: " + (endTime - startTime));
        for (AbstractBranchingPlayer p: firstPath) {
            System.out.println("Hand: " + p.getHand()); 
            System.out.println(p.getGrid());
        }

        while (!player.getBroker().queueEmpty()) {
            System.out.println("Peel!");
            player.getBroker().grabTile();
            startTime = System.currentTimeMillis();
            player.playSolution();
            endTime = System.currentTimeMillis();
            ArrayList<? extends AbstractBranchingPlayer> path = player.getAStar().getPath();
            System.out.println("Time: " + (endTime - startTime));
            for (AbstractBranchingPlayer p: path) {
                System.out.println("Hand: " + p.getHand()); 
                System.out.println(p.getGrid());
            }
        }
        System.out.println("Total Time " + (System.currentTimeMillis()-totalTimeStart));
    }
    @Test
    void cheatingPlayerEmptyBoardThreeWordsAway() {
        setupPlayer1();

        player.solveGrid(); // solves grid but does not play the solution
        ArrayList<? extends AbstractBranchingPlayer> path1 = player.getAStar().getPath();
        for (AbstractBranchingPlayer p: path1) {
            System.out.println(p.getGrid());
        }
        // so player should still have blank grid and full hand
        assertEquals(player.getBroker().getHand().size(), 7);

        // TODO fix the TileBag and Queue need some shared data structure, or the Queue and TileBag need to be implementations of an interface
        player.playSolution(); // should have played entire solution
        ArrayList<? extends AbstractBranchingPlayer> path2 = player.getAStar().getPath();
        for (AbstractBranchingPlayer p: path2) {
            System.out.println(p.getGrid());
        }
        // now hand has actually been playerd so it's empty
        assertTrue(player.getBroker().getHand().isEmpty());
    }
    
    @Test
    void cheatingPlayerExampleSmall() throws Exception {
        char[] letters = "aaeetlop".toCharArray();
        Queue<Tile> nextChars = new LinkedList<>();
        // TODO some static helper method within Tile class to convert from charactesr to tiles for interable?
        nextChars.add(new Tile('t'));
        
        HashSet<String> wordsSet = getScrabbleWords();
        // setup player
        AbstractBroker broker = new CheatBroker(new Hand(letters), nextChars);
        AbstractBranchingPlayer playerPart = new BranchingPlayerSerial(null, new Grid(wordsSet), broker);
        player = new CheatPlayer(AStarArrayList.class, playerPart); // game not needed for this test
        player.getBroker().setQueue(nextChars);

        runTestPrintOut();
    }

    @Test
    void cheatingPlayerExampleMedium() throws Exception {
        char[] letters = "linedowsteaw".toCharArray();
        Queue<Tile> nextChars = new LinkedList<>();

        // note since this is a queue the order is reverse from the other test in AStarGrud
        nextChars.add(new Tile('a'));
        nextChars.add(new Tile('r'));
        nextChars.add(new Tile('e'));
        nextChars.add(new Tile('i'));
        nextChars.add(new Tile('n'));
        
        HashSet<String> wordsSet = getScrabbleWords();
        // setup player
        AbstractBroker broker = new CheatBroker(new Hand(letters), nextChars);
        AbstractBranchingPlayer playerPart = new BranchingPlayerSerial(null, new Grid(wordsSet), broker);
        player = new CheatPlayer(AStarArrayList.class, playerPart); // game not needed for this test

        runTestPrintOut();
    }

    @Test
    void cheatingPlayerMediumLargeExample() throws Exception {
        char[] letters = "ertsyuasfawf".toCharArray();
        Queue<Tile> nextChars = new LinkedList<>();

        // note since this is a queue the order is reverse from the other test in AStarGrud
        nextChars.add(new Tile('g'));
        nextChars.add(new Tile('f'));
        nextChars.add(new Tile('s'));
        nextChars.add(new Tile('s'));
        nextChars.add(new Tile('t'));
        
        HashSet<String> wordsSet = getScrabbleWords();
        // setup player
        AbstractBroker broker = new CheatBroker(new Hand(letters), nextChars);
        AbstractBranchingPlayer playerPart = new BranchingPlayerSerial(null, new Grid(wordsSet), broker);
        player = new CheatPlayer(AStarArrayList.class, playerPart); // game not needed for this test

        runTestPrintOut();
    }

    @Test
    void cheatingPlayerLargeExample() throws Exception {
        char[] letters = "apoelrettonsopa".toCharArray();
        Queue<Tile> nextChars = new LinkedList<>();

        // note since this is a queue the order is reverse from the other test in AStarGrud
        nextChars.add(new Tile('e'));
        nextChars.add(new Tile('r'));
        nextChars.add(new Tile('r'));
        nextChars.add(new Tile('f'));
        nextChars.add(new Tile('s'));
        nextChars.add(new Tile('y'));
        nextChars.add(new Tile('m'));
        nextChars.add(new Tile('n'));
        nextChars.add(new Tile('r'));
        nextChars.add(new Tile('r'));
        
        HashSet<String> wordsSet = getScrabbleWords();
        // setup player
        AbstractBroker broker = new CheatBroker(new Hand(letters), nextChars);
        AbstractBranchingPlayer playerPart = new BranchingPlayerSerial(null, new Grid(wordsSet), broker);
        player = new CheatPlayer(AStarArrayList.class, playerPart); // game not needed for this test

        runTestPrintOut();
    }


    @Test
    void cheatingPlayerPlaysNormal() {
        // TODO make test in which cheating player doesn't use cheating capabilities and makes manual moves
    }
    
}
