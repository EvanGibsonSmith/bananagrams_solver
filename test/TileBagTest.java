package test;

import src.main.TileBag;
import src.main.Tile;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class TileBagTest {

    @Test
    void fullBag() {
        String letters = "aaaatfywuiofasfgh";
        ArrayList<Tile> tiles = new ArrayList<>();
        for (char letter: letters.toCharArray()) {
            tiles.add(new Tile(letter));
        }
        TileBag bag = new TileBag(tiles.toArray(new Tile[tiles.size()]));

        assertTrue(bag.isFull());

        bag.grabNextTile();
        bag.grabNextTile();

        assertFalse(bag.isFull());

        bag.addTile(new Tile('z'));

        assertFalse(bag.isFull());

        bag.addTile(new Tile('a'));

        assertTrue(bag.isFull());
    }

    @Test
    void emptyBag() {
        String letters = "abbcdd";
        ArrayList<Tile> tiles = new ArrayList<>();
        for (char letter: letters.toCharArray()) {
            tiles.add(new Tile(letter));
        }
        TileBag bag = new TileBag(tiles.toArray(new Tile[tiles.size()]));

        assertFalse(bag.isEmpty());

        bag.grabNextTile();
        bag.grabNextTile();

        assertFalse(bag.isEmpty());

        bag.grabNextTile();
        bag.grabNextTile();
        bag.grabNextTile();
        bag.grabNextTile();

        assertTrue(bag.isEmpty());

        bag.addTile(new Tile('c'));

        assertFalse(bag.isEmpty());

        bag.grabNextTile();

        assertTrue(bag.isEmpty());

    }-

    @Test
    void smallBagTest() {
        String letters = "a";
        ArrayList<Tile> tiles = new ArrayList<>();
        for (char letter: letters.toCharArray()) {
            tiles.add(new Tile(letter));
        }
        TileBag bag = new TileBag(tiles.toArray(new Tile[tiles.size()]));
        assertTrue(bag.isFull()); // bag has only one element made so it begins full

        Tile nextTile;
        nextTile = bag.grabNextTile();
        assertEquals(nextTile.getLetter(), 'a'); // always grabs A (only item in bag, so while random this is guarenteed)
        assertEquals(nextTile, new Tile('a')); // testing if tile object equals works (more of tile test) 

        bag.addTile(new Tile('b')); // now bag only has b

        nextTile = bag.grabNextTile(); // always will be b, despite randomness

        assertEquals(nextTile.getLetter(), 'b'); // always grabs A (only item in bag, so while random this is guarenteed)
        assertEquals(nextTile, new Tile('b')); // testing if tile object equals works (more of tile test) 

        assertFalse(bag.isFull()); // false
        bag.addTile(new Tile('c')); // no error
        assertTrue(bag.isFull()); // true
    
        try {
            bag.addTile(new Tile('d'));
        } catch (Exception e) {
            assertEquals(e.getClass(), ArrayIndexOutOfBoundsException.class);
        }
    }
}
