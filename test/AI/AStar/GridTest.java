package test.AI.AStar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.BiFunction;
import java.util.function.Function;

import src.main.AI.AIPlayer;
import src.main.AI.AStarHashSets;
import src.main.game.Grid;
import src.main.game.Location;
import src.main.game.Tile;
import src.main.game.TileBag;

class GridTest {
    AIPlayer player;
    final Function<AIPlayer, Double> heuristic = (p) -> (double) p.getHand().size();
    final BiFunction<AIPlayer, AIPlayer, Double> cost = (p, q) -> (double) q.getHand().size() - p.getHand().size();
    final Function<AIPlayer, Boolean> isGoal = (p) -> p.getHand().size()==0;
    AStarHashSets<AIPlayer> astar;

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
        astar = new AStarHashSets<>(player, cost, heuristic, isGoal);
        System.out.println(astar.getFrom());
        ArrayList<AIPlayer> path = astar.getPath();
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
        astar = new AStarHashSets<>(player, cost, heuristic, isGoal);
        System.out.println(astar.getFrom());
        ArrayList<AIPlayer> path = astar.getPath();
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
        astar = new AStarHashSets<>(player, cost, heuristic, isGoal);
        System.out.println(astar.getFrom());
        ArrayList<AIPlayer> path = astar.getPath();
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
        astar = new AStarHashSets<>(player, cost, heuristic, isGoal);
        System.out.println(astar.getFrom());
        ArrayList<AIPlayer> path = astar.getPath();
        for (AIPlayer p: path) {
            System.out.println(p.getGrid());
        }
    }
}

