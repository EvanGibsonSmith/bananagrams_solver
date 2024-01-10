package test.AI.AIPlayer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.function.Function;
import java.util.Set;

import src.main.game.Grid;
import src.main.game.Location;
import src.main.game.Tile;
import src.main.game.TileBag;
import src.main.game.players.PlayerAI.AIPlayerParallelBranch;
import src.data_structures.MultiSet;

class BranchRight {
    static Function <AIPlayerParallelBranch, Set<AIPlayerParallelBranch>> branchRight; // this will branch only down and add forward branches for these tests

    @BeforeAll
    static void defineBranchRight() {
        branchRight = (player) -> (player.branchForwardSingleDirection((byte) 0));
    }

    MultiSet<MultiSet<String>> getAllWordsPlayed(Set<AIPlayerParallelBranch> players) {
        MultiSet<MultiSet<String>> out = new MultiSet<MultiSet<String>>();
        for (AIPlayerParallelBranch player: players) {
            out.add(player.getGrid().getWordsPlayed());
        }
        return out;
    }

    @Test 
    void singleBranchTilesBeforeTest() {
        HashSet<String> wordsSet = new HashSet<String>();
        wordsSet.add("act"); // first word to play

        wordsSet.add("par");
        wordsSet.add("part");
        wordsSet.add("pane");

        wordsSet.add("fact"); // ONLY word that can be added from act

        char[] letters = "opcraft".toCharArray();
        Tile[] tiles = new Tile[letters.length];
        for (int i=0; i<letters.length; ++i) {
            tiles[i] = new Tile(letters[i]);
        }
        TileBag tileBag = new TileBag(tiles, 1);
        
        AIPlayerParallelBranch player = new AIPlayerParallelBranch(null, new Grid(wordsSet), tileBag); // game not needed for this test
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
        
        Set<AIPlayerParallelBranch> nextPlayers = branchRight.apply(player);
        
        assertEquals(nextPlayers.size(), 1);
        MultiSet<MultiSet<String>> expected = new MultiSet<MultiSet<String>>();
        expected.add(new MultiSet<String>(new String[] {"fact"}));
        BranchTestMethods.getAllWordsPlayed(nextPlayers);
        assertEquals(BranchTestMethods.getAllWordsPlayed(nextPlayers), expected);
    }

    @Test 
    void singleBranchTilesAfterTest() {
        HashSet<String> wordsSet = new HashSet<String>();
        wordsSet.add("act"); // first word to play

        wordsSet.add("par");
        wordsSet.add("part");
        wordsSet.add("pane");

        wordsSet.add("actor"); // ONLY word that can be added from act

        char[] letters = "opcraft".toCharArray();
        Tile[] tiles = new Tile[letters.length];
        for (int i=0; i<letters.length; ++i) {
            tiles[i] = new Tile(letters[i]);
        }
        TileBag tileBag = new TileBag(tiles, 1);
        
        AIPlayerParallelBranch player = new AIPlayerParallelBranch(null, new Grid(wordsSet), tileBag); // game not needed for this test
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();

        System.out.println(player.getHand()); // player has act letters
        // player places act down
        player.placeTile(new Location(0, 0), new Tile('a'));
        player.placeTile(new Location(0, 1), new Tile('c'));
        player.placeTile(new Location(0, 2), new Tile('t'));
        
        Set<AIPlayerParallelBranch> nextPlayers = branchRight.apply(player);
        
        assertEquals(nextPlayers.size(), 1);
        MultiSet<MultiSet<String>> expected = new MultiSet<MultiSet<String>>();
        expected.add(new MultiSet<String>(new String[] {"actor"}));
        assertEquals(BranchTestMethods.getAllWordsPlayed(nextPlayers), expected);
    }

    @Test
    void branchForwardTest() {  
        HashSet<String> wordsSet = new HashSet<String>();
        wordsSet.add("act"); // first word to play

        wordsSet.add("par");
        wordsSet.add("part");
        wordsSet.add("pane");

        wordsSet.add("pact"); // word that can be added from act
        wordsSet.add("fact"); // word that can be added from act
        wordsSet.add("actor"); // word that can be added from act

        char[] letters = "opcraft".toCharArray();
        Tile[] tiles = new Tile[letters.length];
        for (int i=0; i<letters.length; ++i) {
            tiles[i] = new Tile(letters[i]);
        }
        TileBag tileBag = new TileBag(tiles, 1);
        
        AIPlayerParallelBranch player = new AIPlayerParallelBranch(null, new Grid(wordsSet), tileBag); // game not needed for this test
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();

        System.out.println(player.getHand()); // player has act letters
        // player places act down
        player.placeTile(new Location(0, 0), new Tile('a'));
        player.placeTile(new Location(0, 1), new Tile('c'));
        player.placeTile(new Location(0, 2), new Tile('t'));
        
        Set<AIPlayerParallelBranch> nextPlayers = branchRight.apply(player);

        assertEquals(nextPlayers.size(), 3);
        MultiSet<MultiSet<String>> expected = new MultiSet<MultiSet<String>>();
        expected.add(new MultiSet<String>(new String[] {"pact"}));
        expected.add(new MultiSet<String>(new String[] {"actor"}));
        expected.add(new MultiSet<String>(new String[] {"fact"}));
        assertEquals(BranchTestMethods.getAllWordsPlayed(nextPlayers), expected);
    }

    @Test
    void downWordTestSingleDown() {
        HashSet<String> wordsSet = new HashSet<String>();
        wordsSet.add("cat"); // first word to play

        wordsSet.add("spar");
        wordsSet.add("smart");
        wordsSet.add("sane");

        wordsSet.add("app"); // word that CANNOT be added from cat
        wordsSet.add("rap"); // word that can be added from cat

        char[] letters = "opcraft".toCharArray();
        Tile[] tiles = new Tile[letters.length];
        for (int i=0; i<letters.length; ++i) {
            tiles[i] = new Tile(letters[i]);
        }
        TileBag tileBag = new TileBag(tiles, 1);
        
        AIPlayerParallelBranch player = new AIPlayerParallelBranch(null, new Grid(wordsSet), tileBag); // game not needed for this test
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();

        System.out.println(player.getHand()); // player has cat letters
        // player places cat across
        player.placeTile(new Location(0, 0), new Tile('c'));
        player.placeTile(new Location(1, 0), new Tile('a'));
        player.placeTile(new Location(2, 0), new Tile('t'));
        
        Set<AIPlayerParallelBranch> nextPlayers = branchRight.apply(player);
        
        assertEquals(nextPlayers.size(), 1);
        MultiSet<MultiSet<String>> expected = new MultiSet<MultiSet<String>>();
        expected.add(new MultiSet<String>(new String[] {"cat", "rap"}));
        assertEquals(BranchTestMethods.getAllWordsPlayed(nextPlayers), expected);    
        
    }

    @Test
    void downWordDoubleWord() {
        HashSet<String> wordsSet = new HashSet<String>();
        wordsSet.add("cat"); // first word to play, can ALSO be played down

        // can't be added
        wordsSet.add("mart");
        wordsSet.add("pane");
        wordsSet.add("app"); 
        wordsSet.add("crass");
        wordsSet.add("lass");

        char[] letters = "tpcraat".toCharArray();
        Tile[] tiles = new Tile[letters.length];
        for (int i=0; i<letters.length; ++i) {
            tiles[i] = new Tile(letters[i]);
        }
        TileBag tileBag = new TileBag(tiles, 1);
        
        AIPlayerParallelBranch player = new AIPlayerParallelBranch(null, new Grid(wordsSet), tileBag); // game not needed for this test
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();

        System.out.println(player.getHand()); // player has cat letters
        // player places cat across
        player.placeTile(new Location(1, 0), new Tile('c'));
        player.placeTile(new Location(2, 0), new Tile('a'));
        player.placeTile(new Location(3, 0), new Tile('t'));
        
        Set<AIPlayerParallelBranch> nextPlayers = branchRight.apply(player);
        
        assertEquals(nextPlayers.size(), 1);
        MultiSet<MultiSet<String>> expected = new MultiSet<MultiSet<String>>();
        expected.add(new MultiSet<String>(new String[] {"cat", "cat"}));
        assertEquals(BranchTestMethods.getAllWordsPlayed(nextPlayers), expected);
    }

    @Test
    void downWordTestTogether() {
        HashSet<String> wordsSet = new HashSet<String>();
        wordsSet.add("cat"); // first word to play, can ALSO be played down

        // can't be added
        wordsSet.add("mart");
        wordsSet.add("pane");
        wordsSet.add("app"); 

        wordsSet.add("par"); 
        wordsSet.add("rap");
        wordsSet.add("at"); // word that can be added from cat in TWO WAYS
        wordsSet.add("tip"); // word that can be added from cat
        wordsSet.add("tat"); // word that can be added from cat

        char[] letters = "tpcraat".toCharArray();
        Tile[] tiles = new Tile[letters.length];
        for (int i=0; i<letters.length; ++i) {
            tiles[i] = new Tile(letters[i]);
        }
        TileBag tileBag = new TileBag(tiles, 1);
        
        AIPlayerParallelBranch player = new AIPlayerParallelBranch(null, new Grid(wordsSet), tileBag); // game not needed for this test
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();

        System.out.println(player.getHand()); // player has cat letters
        // player places cat across
        player.placeTile(new Location(1, 0), new Tile('c'));
        player.placeTile(new Location(2, 0), new Tile('a'));
        player.placeTile(new Location(3, 0), new Tile('t'));
        
        Set<AIPlayerParallelBranch> nextPlayers = branchRight.apply(player);
        
        assertEquals(nextPlayers.size(), 7);
        MultiSet<MultiSet<String>> expected = new MultiSet<MultiSet<String>>();
        expected.add(new MultiSet<String>(new String[] {"cat", "cat"}));
        expected.add(new MultiSet<String>(new String[] {"cat", "at"}));
        expected.add(new MultiSet<String>(new String[] {"cat", "at"}));
        expected.add(new MultiSet<String>(new String[] {"cat", "tat"}));
        expected.add(new MultiSet<String>(new String[] {"cat", "tat"}));
        expected.add(new MultiSet<String>(new String[] {"cat", "par"}));
        expected.add(new MultiSet<String>(new String[] {"cat", "rap"}));
        assertEquals(BranchTestMethods.getAllWordsPlayed(nextPlayers), expected);    
    }
}
