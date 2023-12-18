package src.main;

import java.util.HashSet;

public class Player {
    Grid grid;
    TileBag bag;
    HashSet<Tile> tiles = new HashSet<>(); // represents tiles player has

    public Player(Grid grid, TileBag bag) {
        this.grid = grid;
        this.bag = bag;
    }

    public Player(HashSet<String> wordsSet, TileBag bag) {
        this.grid = new Grid(wordsSet);
        this.bag = bag;
    }

    /**
     * If player has no tiles left in their "hand". This means all the tiles are on the Grid
     * @return boolean of tiles left
     */
    public boolean handEmpty() {
        return this.tiles.isEmpty();
    }
    
    /**
     * Grabs a tile from the bag and adds it to the players hand.
     * In the context of a game, this is essentially a one player peel
     * @return Tile grabbed, null if no tile could be grabbed (bag empty)
     */
    public Tile grabTile() {
        if (bag.isEmpty()) {return null;} // edge case

        Tile grabbedTile = bag.grabTile();
        this.tiles.add(grabbedTile);
        return grabbedTile;
    }

    /**
     * Player drops one tile into the bag. This is not done during the game
     * without drawing three tiles. 
     * @param dropTile the tile that is dropped from player
     * @return true if a tile was dropped, false if none was found that matched parameter
     */
    public boolean dropTile(Tile dropTile) { // TODO maybe make private? Should be ok because game will only ever use dump? Don't know borderline case
        return tiles.remove(dropTile);
        /*if (tiles.contains(dropTile)) { // TODO I believe tiles.remove already works this way so this extra stuff isn't needed?
            tiles.remove(dropTile);
            return true;
        }
        return false;*/
    }

    /**
     * The action within the game of dumping one tile to get three in return.
     * This is usually done to remove a diffcult letter, like Q, X, or Z. In a 
     * dump other players in the game do not need to participate like peel
     * @param dropTile the tile chosen to be dumped off
     * @return boolean if the dump was successful (were there three tiles to grab)
     */
    public boolean dump(Tile dropTile) {
        boolean dropped = dropTile(dropTile); // the cost for dropping tile within game 
        if (!dropped) {return false;} // if it wasn't successfully dropped then don't move forward

        grabTile();
        grabTile();
        grabTile();
        return true;
    }

    /**
     * If all of the tiles the player has have been made into a proper 
     * set of words on the Grid. In other words, they have a valid grid and nothing 
     * else to place.
     * @return boolean if the players has used all their tiles properly
     */
    public boolean usedTiles() {
        return (this.handEmpty() && this.grid.valid())
    }

}
