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
import src.main.AI.AIPlayer;
import src.data_structures.MultiSet;

class BranchDown {
    static Function <AIPlayer, Set<AIPlayer>> branchDown; // this will branch only down and add forward branches for these tests

    @BeforeAll
    static void defineBranchDown() {
        branchDown = (player) -> (player.branchForwardSingleDirection((byte) 1));
    }

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
        
        AIPlayer player = new AIPlayer(null, new Grid(wordsSet), tileBag); // game not needed for this test
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
        player.placeTile(new Location(1, 0), new Tile('c'));
        player.placeTile(new Location(2, 0), new Tile('t'));
        
        Set<AIPlayer> nextPlayers = branchDown.apply(player);
        
        assertEquals(nextPlayers.size(), 1);
        MultiSet<MultiSet<String>> expected = new MultiSet<MultiSet<String>>();
        expected.add(new MultiSet<String>(new String[] {"fact"}));
        getAllWordsPlayed(nextPlayers);
        assertEquals(getAllWordsPlayed(nextPlayers), expected);
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
        
        AIPlayer player = new AIPlayer(null, new Grid(wordsSet), tileBag); // game not needed for this test
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
        player.placeTile(new Location(1, 0), new Tile('c'));
        player.placeTile(new Location(2, 0), new Tile('t'));
        
        Set<AIPlayer> nextPlayers = branchDown.apply(player);
        
        assertEquals(nextPlayers.size(), 1);
        MultiSet<MultiSet<String>> expected = new MultiSet<MultiSet<String>>();
        expected.add(new MultiSet<String>(new String[] {"actor"}));
        assertEquals(getAllWordsPlayed(nextPlayers), expected);
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
        
        AIPlayer player = new AIPlayer(null, new Grid(wordsSet), tileBag); // game not needed for this test
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
        player.placeTile(new Location(1, 0), new Tile('c'));
        player.placeTile(new Location(2, 0), new Tile('t'));
        
        Set<AIPlayer> nextPlayers = branchDown.apply(player);

        assertEquals(nextPlayers.size(), 3);
        MultiSet<MultiSet<String>> expected = new MultiSet<MultiSet<String>>();
        expected.add(new MultiSet<String>(new String[] {"pact"}));
        expected.add(new MultiSet<String>(new String[] {"actor"}));
        expected.add(new MultiSet<String>(new String[] {"fact"}));
        assertEquals(getAllWordsPlayed(nextPlayers), expected);
    }

    @Test
    void acrossWordTestSingleDown() {
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
        
        AIPlayer player = new AIPlayer(null, new Grid(wordsSet), tileBag); // game not needed for this test
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
        player.placeTile(new Location(0, 1), new Tile('a'));
        player.placeTile(new Location(0, 2), new Tile('t'));
        
        Set<AIPlayer> nextPlayers = branchDown.apply(player);
        
        assertEquals(nextPlayers.size(), 1);
        MultiSet<MultiSet<String>> expected = new MultiSet<MultiSet<String>>();
        expected.add(new MultiSet<String>(new String[] {"cat", "rap"}));
        assertEquals(getAllWordsPlayed(nextPlayers), expected);    
    }

    @Test
    void acrossWordDoubleWord() {
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
        
        AIPlayer player = new AIPlayer(null, new Grid(wordsSet), tileBag); // game not needed for this test
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();

        System.out.println(player.getHand()); // player has cat letters
        // player places cat across
        player.placeTile(new Location(0, 1), new Tile('c'));
        player.placeTile(new Location(0, 2), new Tile('a'));
        player.placeTile(new Location(0, 3), new Tile('t'));
        
        Set<AIPlayer> nextPlayers = branchDown.apply(player);
        
        assertEquals(nextPlayers.size(), 1);
        MultiSet<MultiSet<String>> expected = new MultiSet<MultiSet<String>>();
        expected.add(new MultiSet<String>(new String[] {"cat", "cat"}));
        assertEquals(getAllWordsPlayed(nextPlayers), expected);
    }

    @Test
    void acrossWordTestTogether() {
        HashSet<String> wordsSet = new HashSet<String>();
        wordsSet.add("cat"); // first word to play, can ALSO be played down

        // can't be added
        wordsSet.add("mart");
        wordsSet.add("pane");
        wordsSet.add("app"); 

        // can be added
        wordsSet.add("par"); 
        wordsSet.add("rap");
        wordsSet.add("at"); // word that can be added from cat in TWO WAYS
        wordsSet.add("tat"); // word can be added from cat in TWO WAYS

        char[] letters = "tpcraat".toCharArray();
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

        System.out.println(player.getHand()); // player has cat letters
        // player places cat across
        player.placeTile(new Location(0, 1), new Tile('c'));
        player.placeTile(new Location(0, 2), new Tile('a'));
        player.placeTile(new Location(0, 3), new Tile('t'));
        
        Set<AIPlayer> nextPlayers = branchDown.apply(player);
        
        assertEquals(nextPlayers.size(), 7);
        MultiSet<MultiSet<String>> expected = new MultiSet<MultiSet<String>>();
        expected.add(new MultiSet<String>(new String[] {"cat", "cat"}));
        expected.add(new MultiSet<String>(new String[] {"cat", "at"}));
        expected.add(new MultiSet<String>(new String[] {"cat", "at"}));
        expected.add(new MultiSet<String>(new String[] {"cat", "tat"}));
        expected.add(new MultiSet<String>(new String[] {"cat", "tat"}));
        expected.add(new MultiSet<String>(new String[] {"cat", "par"}));
        expected.add(new MultiSet<String>(new String[] {"cat", "rap"}));
        assertEquals(getAllWordsPlayed(nextPlayers), expected);    

    }
}
