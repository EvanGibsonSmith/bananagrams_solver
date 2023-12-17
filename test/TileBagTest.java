package test;

import src.main.TileBag;
import src.main.Tile;

import java.util.ArrayList;

public class TileBagTest {
    public static void main(String[] args) {
        String letters = "a"; // TODO will get bananagrams defualt letters at some point
        ArrayList<Tile> tiles = new ArrayList<>();
        for (char letter: letters.toCharArray()) {
            tiles.add(new Tile(letter));
        }
        TileBag bag = new TileBag(tiles.toArray(new Tile[tiles.size()]));
        
        System.out.println(bag.isFull()); // true
        System.out.println(bag.grabNextTile()); // always grabs A
        bag.addTile(new Tile('b')); // now bag only has b
        System.out.println(bag.grabNextTile()); // always will be B
        System.out.println(bag.isFull()); // false
        bag.addTile(new Tile('c')); // no error
        System.out.println(bag.isFull()); // true
        bag.addTile(new Tile('d')); // error, bag is full
    }
}
