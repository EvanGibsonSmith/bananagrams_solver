package test;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;

import src.main.TileBag;
import src.main.Tile;
import src.main.Game;
import src.main.Player;
import src.main.Location;

class GameTest {

    // TODO more precise smaller tests in here?
    @Test
    void testThis() {

    }

    @Test
    void testPeel() {
        char[] letters = "abcde".toCharArray();
        Tile[] tiles = new Tile[letters.length];
        for (int i=0; i<letters.length; ++i) {
            tiles[i] = new Tile(letters[i]);
        }
        TileBag tileBag = new TileBag(tiles, 3314159);
        assertEquals(tileBag.toString(), " a b c d e");
        Game game = new Game(3, tileBag, null); // words are not important for this test

        game.peel();
        ArrayList<HashMap<Tile, Integer>> playerTile = new ArrayList<>(); // each player will have one tile
        ArrayList<TileBag> playerBags = new ArrayList<>(); 
        for (Player p: game.getPlayers()) {
            playerBags.add(p.getBag());
            playerTile.add(p.getHand());
        }

        assertEquals(new HashSet<TileBag>(playerBags).size(), 1); // checks that all the bags are the same (as set size is therefore 1)
        assertEquals(new HashSet<HashMap<Tile, Integer>>(playerTile).size(), 3); // every tile should be unique for each player

        // all of the players should be attached to the same bag within the game
    }

    @Test
    void smallTwoGame() { // note this test may change based on implementation of the peel/bag
        // TODO should some of the parts of this test go in the player testing as well?
        HashSet<String> validWords = new HashSet<>();
        validWords.add("pet");
        validWords.add("mint");


        char[] letters = "aecdefgnimkltntp".toCharArray();
        Tile[] tiles = new Tile[letters.length];
        for (int i=0; i<letters.length; ++i) {
            tiles[i] = new Tile(letters[i]);
        }
        TileBag tileBag = new TileBag(tiles, 3314159);
        Game game = new Game(2, tileBag, validWords); // words are not important for this test
        Player[] players = game.getPlayers();

        // peel to start the players with their tiles
        game.peel();
        game.peel();
        game.peel();

        for (Player p : game.getPlayers()) {
            System.out.println(p.getHand()); // note we have pet and bit as words for each player
        }   

        Player p1 = players[0]; // get first player, the one with "pet"
        Player p2 = players[1]; // get second player, the one with "bit"
        p1.placeTile(new Location(0, 0), new Tile('p')); // note that new tile is equal to other tile, so this works rather than finding it within hand
        assertEquals(p1.getHand().size(), 2);
        p1.placeTile(new Location(0, 1), new Tile('e'));
        assertEquals(p1.getHand().size(), 1);
        p1.placeTile(new Location(0,2), new Tile('t'));
        assertEquals(p1.getHand().size(), 0);
        System.out.println(p1.getGrid());

        // player 1 can now peel with pet but player 2 cannot
        assertTrue(p1.canPeel());
        assertFalse(p2.canPeel());
        game.peel(); // now both players have an extra letter and cannot peel
        assertEquals(p1.getHand().size(), 1);
        assertEquals(p2.getHand().size(), 4);
        assertFalse(p1.canPeel());
        assertFalse(p2.canPeel());
        System.out.println(p1.getHand());
        System.out.println(p2.getHand());
        
        p2.placeTile(new Location(0, 0), new Tile('i'));
        assertFalse(p2.getHand().keySet().contains(new Tile('i')));
        assertEquals(p2.getHand().size(), 3);
        p2.placeTile(new Location(-1, 0), new Tile('m'));
        assertEquals(p2.getHand().size(), 2);
        p2.placeTile(new Location(2, 0), new Tile('n'));
        assertEquals(p2.getHand().size(), 1);
        p2.placeTile(new Location(1, 0), new Tile('t'));
        // mint is misspelled downward as mitn. This means p2 while all of the letters are placed should not be able to peel
        System.out.println(p2.getGrid());
        assertEquals(p2.getHand().size(), 0);
        assertFalse(p2.canPeel());
        // p1 still can't peel
        assertFalse(p1.canPeel());

        // p2 fixes their mistake
        p2.removeTile(new Location(2, 0)); // remove n
        assertEquals(p2.getHand().size(), 1); // should have n now only
        assertTrue(p2.getHand().keySet().contains(new Tile('n')));

        p2.removeTile(new Location(1, 0)); // remove t
        assertEquals(p2.getHand().size(), 2); // now has n and t
        assertTrue(p2.getHand().keySet().contains(new Tile('t')));

        p2.placeTile(new Location(1, 0), new Tile('n'));
        p2.placeTile(new Location(2, 0), new Tile('t'));

        // now mint is spelled correctly and p2 should be able to peel. p1 cannot since they have not placed their next letter
        assertFalse(p1.canPeel());
        assertTrue(p2.canPeel());
        game.peel();

        // p1 should have 2 in the hand that have not been placed after pet is on the board
        assertEquals(p1.getHand().size(), 2);
        System.out.println(p1.getHand());
        p1.placeTile(new Location(-3, -3), new Tile('a'));
        p1.placeTile(new Location(3, 3), new Tile('d'));
        
        // even though all of p1's tiles are placed they cannot peel because islands and words are invalid
        assertFalse(p1.canPeel());
        assertFalse(p2.canPeel()); // has new tile in hand
    }

    @Test
    void gameCompleteGridComplete() {
        HashSet<String> validWords = new HashSet<>();
        validWords.add("leg");
        validWords.add("bag");

        char[] letters = "bbalegg".toCharArray();
        Tile[] tiles = new Tile[letters.length];
        for (int i=0; i<letters.length; ++i) {
            tiles[i] = new Tile(letters[i]);
        }
        TileBag tileBag = new TileBag(tiles, 314159);
        Game game = new Game(2, tileBag, validWords); // words are not important for this test

        // start game, give each player three tiles
        game.peel();
        game.peel();
        game.peel();

        for (Player p: game.getPlayers()) {
            System.out.println(p.getHand());
        }

        Player p1 = game.getPlayers()[0]; // has tiles a b g to play bag
        Player p2 = game.getPlayers()[1]; // has tiles e g l to play leg
        
        assertFalse(game.gameComplete());
        p1.placeTile(new Location(1, 1), new Tile('b'));
        p1.placeTile(new Location(2, 1), new Tile('a'));
        p1.placeTile(new Location(3, 1), new Tile('g'));
        System.out.println(p1.getGrid());
        assertTrue(game.gameComplete()); // one tile in bag, but not enough to give to both players so game is complete

        p2.placeTile(new Location(0, 1), new Tile('l'));
        p2.placeTile(new Location(0, 2), new Tile('e'));
        p2.placeTile(new Location(0, 3), new Tile('g'));
        System.out.println(p2.getGrid());
        assertTrue(game.gameComplete()); // game is still complete, but both players are valid now
    }

    @Test
    void gameCompleteEnoughTiles() {
        HashSet<String> validWords = new HashSet<>();
        validWords.add("can");
        validWords.add("cat");

        char[] letters = "accceant".toCharArray();
        Tile[] tiles = new Tile[letters.length];
        for (int i=0; i<letters.length; ++i) {
            tiles[i] = new Tile(letters[i]);
        }
        TileBag tileBag = new TileBag(tiles, 271828);
        Game game = new Game(2, tileBag, validWords); // words are not important for this test

        // start game, give each player three tiles
        game.peel();
        game.peel();
        game.peel();

        for (Player p: game.getPlayers()) {
            System.out.println(p.getHand());
        }

        Player p1 = game.getPlayers()[0];
        Player p2 = game.getPlayers()[1];

        // p1 can play "can" with this configuration
        assertTrue(p1.gridValid());
        p1.placeTile(new Location(0, 0), new Tile('c'));
        p1.placeTile(new Location(0, 1), new Tile('a'));
        p1.placeTile(new Location(0, 2), new Tile('n'));
        assertTrue(p1.gridValid());

        assertFalse(game.gameComplete()); // game is not complete because another peel can be performed

        assertEquals(p1.getHand().size(), 0);
        assertEquals(p2.getHand().size(), 3);
        assertEquals(game.getBag().size(), 2);
        System.out.println(game.getBag());
        game.peel();
        System.out.println(game.getBag());

        assertEquals(p1.getHand().size(), 1); // TODO EVERY getHand().size() is measuring DISTINCT letters right now
        assertEquals(p2.getHand().size(), 3); // 3 DISTINCT letters (FIXME)

        assertEquals(game.getBag().size(), 0);

        assertFalse(game.gameComplete()); // now game is not complete because none of the players can peel

        // both players have valid grids 
    }
}
