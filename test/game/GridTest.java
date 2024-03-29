
package test.game;
import java.util.HashSet;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import src.data_structures.MultiSet;
import src.main.game.Location;
import src.main.game.Tile;
import src.main.game.grids.Grid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class GridTest {

    // TODO add more tests sometime
    @Test 
    void placeableLocations() {
        Grid g = new Grid((HashSet<String>) null); // no words need for this test
        g.placeUnsafe(new Location(0,0), new Tile("a".charAt(0)));
        
        System.out.println(g.placeableLocations());
        assertEquals(g.placeableLocations(), new HashSet<Location> (Arrays.asList(new Location(1,0),
                                                                                  new Location(-1,0),
                                                                                  new Location(0,1),
                                                                                  new Location(0,-1))));

        
        g.placeUnsafe(new Location(0,1), new Tile("b".charAt(0))); // second location (this location should be blocked from output)
        assertEquals(g.placeableLocations(), new HashSet<Location> (Arrays.asList(new Location(1,0),
                                                                                  new Location(-1,0),
                                                                                  //new Location(0,1), now filled with tile
                                                                                  new Location(0, -1),
                                                                                  new Location(1,1),
                                                                                  new Location(0, 2),
                                                                                  //new Location(0,0), filled with first tile
                                                                                  new Location(-1, 1))));   
        
        g.placeUnsafe(new Location(3, 3), new Tile("c".charAt(0))); // island location
        assertEquals(g.placeableLocations().size(), 10); // island adds 4 new placable locations, so 10 in total

        System.out.println(g);
    }

    @Test 
    void removeLocations() {
        Grid g = new Grid((HashSet<String>) null); // words not needed for this test

        assertEquals(g.size(), 0);

        g.placeUnsafe(new Location(0, 0), new Tile("a".charAt(0)));
        g.placeUnsafe(new Location(1, 0), new Tile("b".charAt(0)));
        g.placeUnsafe(new Location(2, 0), new Tile("c".charAt(0)));
        g.placeUnsafe(new Location(3, 2), new Tile("d".charAt(0)));

        assertEquals(g.size(), 4);

        Tile removed = g.remove(new Location(2, 0));
        assertEquals(removed, new Tile("c".charAt(0)));
        assertEquals(g.size(), 3);

        removed = g.remove(new Location(-1, -1)); // no tile present
        assertNull(removed);
        assertEquals(g.size(), 3);

        System.out.println(g);

    }

    @Test
    void buildSmallGrid () {
        HashSet<String> wordsSet = new HashSet<String>();
        wordsSet.add("pan");
        wordsSet.add("par");
        wordsSet.add("part");
        wordsSet.add("pane");
        wordsSet.add("met");
        Grid g = new Grid(wordsSet); // highly specific words for testing added

        g.placeUnsafe(new Location(0, 0), new Tile('p'));
        g.placeUnsafe(new Location(0, 1), new Tile('a'));
        g.placeUnsafe(new Location(0, 2), new Tile('n'));
        
        assertEquals(g.getWordsPlayed(), new MultiSet<String>(new String[] {"pan"}));

        assertNotEquals(g.getWordsPlayed(), new MultiSet<String>(new String[] {"pan", "a"})); // single letter is not a word that needs to be checked

        g.placeUnsafe(new Location(1, 0), new Tile('a'));
        g.placeUnsafe(new Location(2, 0), new Tile('r'));
        
        assertEquals(g.getWordsPlayed(), new MultiSet<String>(new String[] {"pan", "par"}));
        assertTrue(g.validWords()); // all words are within the small word set created
        assertTrue(g.tilesConnected());

        System.out.println(g);

        g.placeUnsafe(new Location(3, 0), new Tile('t'));

        assertEquals(g.getWordsPlayed(), new MultiSet<String>(new String[] {"part", "pan"}));

        g.placeUnsafe(new Location(0, 3), new Tile('e'));

        assertEquals(g.getWordsPlayed(), new MultiSet<String>(new String[] {"part", "pane"})); // note old words are gone now
        
        assertTrue(g.validWords()); // still a valid configuration
        assertTrue(g.tilesConnected());
        System.out.println(g);

        g.placeUnsafe(new Location(1, 3), new Tile('t'));

        assertEquals(g.getWordsPlayed(), new MultiSet<String>(new String[] {"part", "pane", "et"}));
        assertFalse(g.validWords()); // et is not a valid word
        assertTrue(g.tilesConnected()); // still portion is still fine though
        g.placeUnsafe(new Location(-1, 3), new Tile('m')); // now all words are fine (with met)

        assertEquals(g.getWordsPlayed(), new MultiSet<String>(new String[] {"part", "pane", "met"}));
        assertTrue(g.validWords()); // et is not a valid word
        assertTrue(g.tilesConnected()); // still portion is still fine though
        System.out.println(g);
    } 

    @Test 
    void removeTile() {
        Grid g = new Grid((HashSet<String>) null); // words not important for this test

        g.placeUnsafe(new Location(0, 0), new Tile('a'));
        g.placeUnsafe(new Location(0, 1), new Tile('b'));
        g.placeUnsafe(new Location(0, 2), new Tile('c'));

        assertEquals(g.getTile(new Location(0, 1)), new Tile('b'));
        g.remove(new Location(0, 1));
        assertEquals(g.getTile(new Location(0, 1)), null); // cannot remove it again, now null
        g.remove(new Location(0, 2));
        assertEquals(g.getTile(new Location(0, 2)), null);
        assertEquals(g.getTile(new Location(0, 0)), new Tile('a'));
    }
    
    @Test 
    void twoLetterWordValid() {
        HashSet<String> wordsSet = new HashSet<String>();
        wordsSet.add("it");
        wordsSet.add("qi");

        Grid g = new Grid(wordsSet); // words not important for this test
        g.placeUnsafe(new Location(0, 0), new Tile('q'));
        g.placeUnsafe(new Location(1, 0), new Tile('i'));
        g.placeUnsafe(new Location(1, 1), new Tile('t'));

        System.out.println(g);
        assertTrue(g.validWords());
    }
    
    @Test
    void copyTest() {
        Grid g1 = new Grid(new HashSet<>());
        g1.placeUnsafe(new Location(-1, -1), new Tile('a'));
        Grid g2 = new Grid(g1); // should be copy
        assertEquals(g1, g2);
        g1.placeUnsafe(new Location(0, 0), new Tile('b'));
        System.out.println(g1);
        System.out.println(g2);
        assertNotEquals(g1, g2);
    }
}
