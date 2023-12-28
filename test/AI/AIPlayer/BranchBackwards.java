package test.AI.AIPlayer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.util.HashSet;
import java.util.Set;

import src.main.game.Grid;
import src.main.game.Location;
import src.main.game.Tile;
import src.main.game.TileBag;
import src.main.AI.AIPlayer;
import src.data_structures.MultiSet;

public class BranchBackwards {

    MultiSet<MultiSet<String>> getAllWordsPlayed(Set<AIPlayer> players) {
        MultiSet<MultiSet<String>> out = new MultiSet<MultiSet<String>>();
        for (AIPlayer player: players) {
            out.add(player.getGrid().getWordsPlayed());
        }
        return out;
    }
    
    @Test 
    void singleBranchTilesBeforeTest() {
        HashSet<String> wordsSet = new HashSet<String>();
        wordsSet.add("act"); // first word to play
        wordsSet.add("tap");
        wordsSet.add("pat");
        wordsSet.add("eat");

        wordsSet.add("par");
        wordsSet.add("part");
        wordsSet.add("pane");

        wordsSet.add("fact"); // ONLY word that can be added from act

        char[] letters = "actapat".toCharArray();
        Tile[] tiles = new Tile[letters.length];
        for (int i=0; i<letters.length; ++i) {
            tiles[i] = new Tile(letters[i]);
        }
        TileBag tileBag = new TileBag(tiles, 1);
        
        AIPlayer player = new AIPlayer(null, new Grid(wordsSet), tileBag); // game not needed for this test
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();

        System.out.println(player.getHand()); // player has act letters
        // player places act across
        player.placeTile(new Location(0, 0), new Tile('a'));
        player.placeTile(new Location(0, 1), new Tile('c'));
        player.placeTile(new Location(0, 2), new Tile('t'));
        player.placeTile(new Location(1, 2), new Tile('a'));
        player.placeTile(new Location(2, 2), new Tile('p'));
        player.placeTile(new Location(2, 3), new Tile('a'));
        player.placeTile(new Location(2, 4), new Tile('t'));
        assertTrue(player.gridValid());

        System.out.println(player.getGrid());
        Set<AIPlayer> nextPlayers = player.branchBackwards();
        
        for (AIPlayer p: nextPlayers) {
            System.out.println(p.getGrid());
        }
        assertEquals(nextPlayers.size(), 2);
        MultiSet<MultiSet<String>> expected = new MultiSet<MultiSet<String>>();
        expected.add(new MultiSet<String>(new String[] {"tap", "act"}));
        expected.add(new MultiSet<String>(new String[] {"tap", "pat"}));
        getAllWordsPlayed(nextPlayers);
        assertEquals(getAllWordsPlayed(nextPlayers), expected);
    }
}
