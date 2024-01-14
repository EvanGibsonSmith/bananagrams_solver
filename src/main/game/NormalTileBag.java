package src.main.game;

import java.util.Random;

public class NormalTileBag implements TileBagable {
    Tile[] tiles;
    int maxSize;
    int nextIndex;
    Random random;

    /**
     * Takes in the initial tiles in the bag and the max size,
     * Assumes the max size is the initial number of tiles in the 
     * bag. This makes sense for the application within the game
     * @param tiles
     */
    public NormalTileBag(Tile[] tiles) {
        this.tiles = tiles;
        this.maxSize = tiles.length;
        // since bag begins full nextIndex is the length
        this.nextIndex = tiles.length;
        this.random = new Random();
    }

    public NormalTileBag(Tile[] tiles, int seed) {
        this(tiles);
        this.random = new Random(seed);
    }

    /**
     * Initializes a bag with a max size given, but the bag
     * begins empty (potentially to add stuff too)
     * @param maxSize
     */
    public NormalTileBag(int maxSize) { 
        this.tiles = new Tile[maxSize];
        this.maxSize = maxSize;
    }

    /**
     * Initializes a bag with a max size given, but the bag
     * begins empty (potentially to add stuff too)
     * @param maxSize
     */
    public NormalTileBag(int maxSize, int seed) { 
        this(maxSize);
        this.random = new Random(seed);
    }

    @Override
    public String toString() {
        String out = "";
        for (Tile t: this.tiles) {
            out += " " + t;
        }
        return out.substring(1);
    }

    public Tile[] getTiles() {
        return this.tiles;
    }   

    public int size() {
        return nextIndex;
    }

    public boolean isFull() {
        return (maxSize==size());
    }

    public boolean isEmpty() {
        return (size()==0);
    }

    public Tile grabTile() {
        int tileIndex = random.nextInt(nextIndex);
        Tile grabbedTile = tiles[tileIndex];
        // to fill gap made place last tile in this spot
        tiles[tileIndex] = tiles[nextIndex-1]; // take final spot and place it here to leave array compact
        tiles[nextIndex-1] = null;
        --nextIndex; // end is cleared so the next index decrements
        return grabbedTile;
    }

    public void addTile(Tile t) {
        tiles[nextIndex] = t;
        if (isFull()) {throw new ArrayIndexOutOfBoundsException("Bag is full, cannot add another tile");}
        ++this.nextIndex;
    }
}
