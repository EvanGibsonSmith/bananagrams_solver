package test.game.players.types.branchplayers.branchmethodparts;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;

import src.main.game.Tile;
import src.main.game.grids.Grid;
import src.main.game.players.bags.NormalTileBag;
import src.main.game.players.types.branchplayers.AbstractBranchingPlayer;
import src.main.game.players.types.branchplayers.BranchingPlayerSerial;
import src.data_structures.MultiSet;

public class BranchEmptyTest {
    
    @Test 
    void singleBranchTilesBeforeTest() {
        HashSet<String> wordsSet = new HashSet<String>();
        // below can be played
        wordsSet.add("act");
        wordsSet.add("cat");
        wordsSet.add("tap");
        wordsSet.add("pat");

        // cannot be played
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

        System.out.println(player.getGrid());
        Set<? extends AbstractBranchingPlayer> nextPlayers = player.branch(); // the overall branch function should just call branchEmpty right now
        
        for (AbstractBranchingPlayer p: nextPlayers) {
            System.out.println(p.getGrid());
        }

        assertEquals(nextPlayers.size(), 8);
        MultiSet<MultiSet<String>> expected = new MultiSet<MultiSet<String>>();
        expected.add(new MultiSet<String>(new String[] {"act"}));
        expected.add(new MultiSet<String>(new String[] {"cat"}));
        expected.add(new MultiSet<String>(new String[] {"tap"}));
        expected.add(new MultiSet<String>(new String[] {"pat"}));
        // same words but down
        expected.add(new MultiSet<String>(new String[] {"act"}));
        expected.add(new MultiSet<String>(new String[] {"cat"}));
        expected.add(new MultiSet<String>(new String[] {"tap"}));
        expected.add(new MultiSet<String>(new String[] {"pat"}));
        BranchMethodsTest.getAllWordsPlayed(nextPlayers);
        assertEquals(BranchMethodsTest.getAllWordsPlayed(nextPlayers), expected);

        MultiSet<MultiSet<Character>> expectedHands = new MultiSet<>();
        expectedHands.add(new MultiSet<Character>(new Character[] {'a', 'p', 'a', 't'})); // played act
        expectedHands.add(new MultiSet<Character>(new Character[] {'a', 'p', 'a', 't'})); // played cat
        expectedHands.add(new MultiSet<Character>(new Character[] {'a', 'c', 'a', 't'})); // played tap
        expectedHands.add(new MultiSet<Character>(new Character[] {'a', 'c', 'a', 't'})); // played pat
        // same hands but words were played down
        expectedHands.add(new MultiSet<Character>(new Character[] {'a', 'p', 'a', 't'})); // played act
        expectedHands.add(new MultiSet<Character>(new Character[] {'a', 'p', 'a', 't'})); // played cat
        expectedHands.add(new MultiSet<Character>(new Character[] {'a', 'c', 'a', 't'})); // played tap
        expectedHands.add(new MultiSet<Character>(new Character[] {'a', 'c', 'a', 't'})); // played pat
        assertEquals(BranchMethodsTest.getAllHandsCharacters(nextPlayers), expectedHands);
    }

    @Test 
    void singleTwoLetterWord() {
        HashSet<String> wordsSet = new HashSet<String>();
        wordsSet.add("as");
        wordsSet.add("cat");

        char[] letters = "as".toCharArray();
        Tile[] tiles = new Tile[letters.length];
        for (int i=0; i<letters.length; ++i) {
            tiles[i] = new Tile(letters[i]);
        }
        NormalTileBag tileBag = new NormalTileBag(tiles, 1);
        
        AbstractBranchingPlayer player = new BranchingPlayerSerial(null, new Grid(wordsSet), tileBag); // game not needed for this test
        player.grabTile();
        player.grabTile();

        System.out.println(player.getGrid());
        Set<? extends AbstractBranchingPlayer> nextPlayers = player.branch(); // the overall branch function should just call branchEmpty right now
        
        for (AbstractBranchingPlayer p: nextPlayers) {
            System.out.println(p.getGrid());
        }

        assertEquals(nextPlayers.size(), 2);
        MultiSet<MultiSet<String>> expected = new MultiSet<MultiSet<String>>();
        expected.add(new MultiSet<String>(new String[] {"as"}));
        expected.add(new MultiSet<String>(new String[] {"as"}));

        BranchMethodsTest.getAllWordsPlayed(nextPlayers);
        assertEquals(BranchMethodsTest.getAllWordsPlayed(nextPlayers), expected);
    }
}
