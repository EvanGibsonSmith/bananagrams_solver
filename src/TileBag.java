package src;

import java.util.Random;

public class TileBag {
    Tile[] tiles;
    int maxSize;
    Random r;

    // TODO this data structure kind of needs to be redone
    public TileBag(Tile[] tiles) {
        this.tiles = tiles;
        this.maxSize = tiles.length;
    }

    public Tile grabNextTile() {
        int tileIndex = r.nextInt(tiles.length);
        Tile grabbedTile = tiles[tileIndex];
        return grabbedTile;
    }

    public void addTile(Tile t) {
        // STUB TODO
    }
}
