package test.AI;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;

import src.main.game.Grid;
import src.main.game.Location;
import src.main.game.Tile;
import src.main.game.TileBag;
import src.data_structures.MultiSet;
import src.main.AI.AIPlayer;

class AIPlayerTest {

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
        
        System.out.println(player.getGrid());

        HashSet<AIPlayer> nextPlayers = (HashSet<AIPlayer>) player.branch_forward();
        
        assertEquals(nextPlayers.size(), 1);
        for (AIPlayer p: nextPlayers) { // only one player
            //assertEquals(p.getGrid().gridWords(), "pact"); // TODO create gridWords method
        }
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
        
        System.out.println(player.getGrid());

        HashSet<AIPlayer> nextPlayers = (HashSet<AIPlayer>) player.branch_forward(); 
        
        assertEquals(nextPlayers.size(), 1);
        for (AIPlayer p: nextPlayers) { // only one player
            //assertEquals(p.getGrid().gridWords(), "pact"); // TODO create gridWords method
        }
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
        
        System.out.println(player.getGrid());

        HashSet<AIPlayer> nextPlayers = (HashSet<AIPlayer>) player.branch_forward(); // TODO in future make branch_forward down for these tests

        for (AIPlayer p: nextPlayers) {
            System.out.println(p.getGrid()); 
        }

        assertEquals(nextPlayers.size(), 3);

        // TODO add formal test of how set of word sets matches what is expected
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
        
        System.out.println(player.getGrid());

        HashSet<AIPlayer> nextPlayers = (HashSet<AIPlayer>) player.branch_forward(); // TODO in future make branch_forward down for these tests
        
        for (AIPlayer p: nextPlayers) {
            System.out.println(p.getGrid());
            //assertEquals(p.getGrid().getWords(), new MultiSet<String>(new String[] {"cat", "rap"})); // TODO add getWords to getGrid and array constructor to MultiSet
        }
        assertEquals(nextPlayers.size(), 1);
        
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
        
        System.out.println(player.getGrid());

        HashSet<AIPlayer> nextPlayers = (HashSet<AIPlayer>) player.branch_forward(); // TODO in future make branch_forward down for these tests
        
        assertEquals(nextPlayers.size(), 1);

        for (AIPlayer p: nextPlayers) { // only one player
            System.out.println(p.getGrid());
            //assertEquals(p.getGrid().getWords(), new MultiSet<String>(new String[] {"cat", "cat"})); // TODO add getWords to getGrid and array constructor to MultiSet
        }
    }

    @Test
    void acrossWordTestTogether() {
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
        
        System.out.println(player.getGrid());

        HashSet<AIPlayer> nextPlayers = (HashSet<AIPlayer>) player.branch_forward(); // TODO in future make branch_forward down for these tests
        
        assertEquals(nextPlayers.size(), 9);

        for (AIPlayer p: nextPlayers) {
            System.out.println(p.getGrid());
        }
        // TODO add formal test on how the set of sets of words should be equal for both instead of printing
    }
}
