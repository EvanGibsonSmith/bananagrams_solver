package test.game;

import org.junit.jupiter.api.Test;

import src.main.AI.AStar.AStarArrayList;
import src.main.AI.AStar.AStarHashSets;
import src.main.game.Grid;
import src.main.game.Tile;
import src.main.game.TileBag;
import src.main.game.players.AIPlayers.BranchPlayers.AbstractBranchingPlayer;
import src.main.game.players.AIPlayers.BranchPlayers.BranchingPlayerParallel;
import src.main.game.players.AIPlayers.BranchPlayers.BranchingPlayerSerial;
import src.main.game.players.CheatPlayer;
import src.main.game.players.CheatBroker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;

public class CheatingPlayerTest {
    CheatPlayer player;
    
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
            player = new CheatPlayer(BranchingPlayerParallel.class, 
                                AStarArrayList.class, wordsSet); // game not needed for this test
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

    @Test
    void cheatingPlayerEmptyBoardThreeWordsAway() {
        setupPlayer1();

        // TODO fix the TileBag and Queue need some shared data structure, or the Queue and TileBag need to be implementations of an interface
        player.playSolution(); // should have played entire solution
        ArrayList<? extends AbstractBranchingPlayer> path = player.getAStar().getPath();
        for (AbstractBranchingPlayer p: path) {
            System.out.println(p.getGrid());
        }
    }
    
    @Test
    void cheatingPlayerExampleSmall() {

    }

    @Test
    void cheatingPlayerExampleMedium() {

    }

    @Test
    void cheatingPlayerPlaysNormal() {

    }
    
}
