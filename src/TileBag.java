package src;

import java.util.Random;

public class TileBag {
    Tile[] tiles;
    int maxSize;
    int nextIndex = 0;
    Random r;

    // TODO this data structure kind of needs to be redone
    public TileBag(Tile[] tiles) {
        this.tiles = tiles;
        this.maxSize = tiles.length;
    }

    public Tile grabNextTile() {
        int tileIndex = r.nextInt(tiles.length);
        Tile grabbedTile = tiles[tileIndex];
        // to fill gap made place last tile in this spot
        tiles[tileIndex] = tiles[nextIndex];
        tiles[tiles.length-1] = null;
        return grabbedTile;
    }

    public Boolean isFull() {
        return (maxSize==nextIndex);
    }

    public void addTile(Tile t) {
        tiles[nextIndex] = t;
        ++this.maxSize;
        if (isFull()) {throw new ArrayIndexOutOfBoundsException("Bag is full, cannot add another tile");}
    }
}
