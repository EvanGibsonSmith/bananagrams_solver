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
import src.main.game.players.types.branchplayers.AbstractBranchingPlayer;
import src.main.game.players.types.branchplayers.BranchingPlayerSerial;
import src.data_structures.MultiSet;

public class BranchTest {
    
    @Test 
    void smallForwardAndBackward() {
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
        
        AbstractBranchingPlayer player = new BranchingPlayerSerial(null, new Grid(wordsSet), tileBag); // game not needed for this test
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();

        System.out.println(player.getHand());
        player.placeTile(new Location(0, 0), new Tile('a'));
        player.placeTile(new Location(0, 1), new Tile('c'));
        player.placeTile(new Location(0, 2), new Tile('t'));
        player.placeTile(new Location(1, 2), new Tile('a'));
        player.placeTile(new Location(2, 2), new Tile('p'));
        assertTrue(player.gridValid());

        System.out.println(player.getGrid());
        Set<? extends AbstractBranchingPlayer> nextPlayers = player.branch(); // can add tap or pat to p, and remove act or tap
        
        for (AbstractBranchingPlayer p: nextPlayers) {
            System.out.println(p.getGrid());
        }
        assertEquals(nextPlayers.size(), 4);
        MultiSet<MultiSet<String>> expected = new MultiSet<MultiSet<String>>();
        expected.add(new MultiSet<String>(new String[] {"act", "tap", "tap"}));
        expected.add(new MultiSet<String>(new String[] {"tap"}));
        expected.add(new MultiSet<String>(new String[] {"act"}));
        expected.add(new MultiSet<String>(new String[] {"act", "tap", "pat"}));
        assertEquals(BranchMethodsTest.getAllWordsPlayed(nextPlayers), expected);

        
        MultiSet<MultiSet<Character>> expectedHands = new MultiSet<>();
        expectedHands.add(new MultiSet<Character>(new Character[] {})); // played pat
        expectedHands.add(new MultiSet<Character>(new Character[] {})); // played tap
        expectedHands.add(new MultiSet<Character>(new Character[] {'a', 'p', 'a', 't'})); // removed tap 
        expectedHands.add(new MultiSet<Character>(new Character[] {'a', 'c', 'a', 't'})); // removed act 
        assertEquals(BranchMethodsTest.getAllHandsCharacters(nextPlayers), expectedHands);
    }
}
