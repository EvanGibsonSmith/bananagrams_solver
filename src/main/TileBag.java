package src.main;

import java.util.Random;

public class TileBag {
    Tile[] tiles;
    int maxSize;
    int nextIndex = 0;
    Random r = new Random();

    /**
     * Takes in the initial tiles in the bag and the max size,
     * Assumes the max size is the initial number of tiles in the 
     * bag. This makes sense for the application within the game
     * @param tiless
     */
    public TileBag(Tile[] tiles) {
        this.tiles = tiles;
        this.maxSize = tiles.length;
    }

    /**
     * Takes in the initial tiles in the bag and the max size,
     * in case the max size should be bigger than the number of tiles initially 
     * in the bag
     * @param tiles
     * @param maxSize
     */
    /*public TileBag(Tile[] tiles, int maxSize) { 
        this.tiles = tiles;
        this.maxSize = maxSize;
    }*/ // TODO this constructor doesn't work because the size of tiles is not maxSize

    /**
     * Initializes a bag with a max size given, but the bag
     * begins empty (potentially to add stuff too)
     * @param maxSize
     */
    public TileBag(int maxSize) { 
        this.tiles = new Tile[maxSize];
        this.maxSize = maxSize;
    }

    @Override
    public String toString() {
        String out = "";
        for (Tile t: this.tiles) {
            out += " " + t;
        }
        return out;
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
        ++this.nextIndex;
        if (isFull()) {throw new ArrayIndexOutOfBoundsException("Bag is full, cannot add another tile");}
    }
}
