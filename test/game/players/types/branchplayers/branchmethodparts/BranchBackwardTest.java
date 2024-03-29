package test.game.players.types.branchplayers.branchmethodparts;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.util.HashSet;
import java.util.Set;

import src.main.game.Location;
import src.main.game.Tile;
import src.main.game.grids.Grid;
import src.main.game.players.bags.NormalTileBag;
import src.main.game.players.types.branchplayers.BranchingPlayerSerial;
import src.data_structures.MultiSet;

public class BranchBackwardTest {
    
    @Test 
    void singleBranchTilesBeforeTest() {
        HashSet<String> wordsSet = new HashSet<String>();
        wordsSet.add("act"); // first word to play
        wordsSet.add("tap");
        wordsSet.add("pat");

        wordsSet.add("par");
        wordsSet.add("part");
        wordsSet.add("pane");

        wordsSet.add("fact"); // ONLY word that can be added from act

        char[] letters = "actapat".toCharArray();
        Tile[] tiles = new Tile[letters.length];
        for (int i=0; i<letters.length; ++i) {
            tiles[i] = new Tile(letters[i]);
        }
        NormalTileBag tileBag = new NormalTileBag(tiles, 1);
        
        BranchingPlayerSerial player = new BranchingPlayerSerial(null, new Grid(wordsSet), tileBag); // game not needed for this test
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
        Set<BranchingPlayerSerial> nextPlayers = player.branchBackward();
        
        for (BranchingPlayerSerial p: nextPlayers) {
            System.out.println(p.getGrid());
        }
        assertEquals(nextPlayers.size(), 2);
        MultiSet<MultiSet<String>> expected = new MultiSet<MultiSet<String>>();
        expected.add(new MultiSet<String>(new String[] {"tap", "act"}));
        expected.add(new MultiSet<String>(new String[] {"tap", "pat"}));
        BranchMethodsTest.getAllWordsPlayed(nextPlayers);
        assertEquals(BranchMethodsTest.getAllWordsPlayed(nextPlayers), expected);

        MultiSet<MultiSet<Character>> expectedHands = new MultiSet<>();
        expectedHands.add(new MultiSet<Character>(new Character[] {'a', 't'})); // removed pat 
        expectedHands.add(new MultiSet<Character>(new Character[] {'a', 'c'})); // removed act 
        assertEquals(BranchMethodsTest.getAllHandsCharacters(nextPlayers), expectedHands);
    }
}
