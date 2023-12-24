package test;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import src.main.game.Tile;
import src.main.game.TileBag;

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

        bag.grabTile();
        bag.grabTile();

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

        bag.grabTile();
        bag.grabTile();

        assertFalse(bag.isEmpty());

        bag.grabTile();
        bag.grabTile();
        bag.grabTile();
        bag.grabTile();

        assertTrue(bag.isEmpty());

        bag.addTile(new Tile('c'));

        assertFalse(bag.isEmpty());

        bag.grabTile();

        assertTrue(bag.isEmpty());

    }

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
        nextTile = bag.grabTile();
        assertEquals(nextTile.getLetter(), 'a'); // always grabs A (only item in bag, so while random this is guarenteed)
        assertEquals(nextTile, new Tile('a')); // testing if tile object equals works (more of tile test) 

        bag.addTile(new Tile('b')); // now bag only has b

        nextTile = bag.grabTile(); // always will be b, despite randomness

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

    @Test 
    void grabbedTileInBag() {
        String letters = "abcde";
        ArrayList<Tile> tiles = new ArrayList<>();
        for (char letter: letters.toCharArray()) {
            tiles.add(new Tile(letter));
        }
        TileBag bag = new TileBag(tiles.toArray(new Tile[tiles.size()]), 31415);

        while (!bag.isEmpty()) {
            Tile tile = bag.grabTile();
            
            // tile should not be in bag
            boolean tileInBag = false;
            for (Tile bagTile: bag.getTiles()) {
                if (bagTile==tile) {tileInBag=true;}
            }   
            System.out.println(bag);
            System.out.println(tile);
            assertFalse(tileInBag);
        }
    }

    // TODO do more testing on the ins and outs of the randomness with seeds and with the swapping/nulling behavior.

}
