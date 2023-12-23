package src.main;

import java.util.HashSet;
import src.data_structures.MultiSet;

public class Player {
    Grid grid;
    Game game;
    TileBag bag;
    MultiSet<Tile> hand = new MultiSet<>(); // represents hand player has and how much of each tile

    public Player(Game game, Grid grid, TileBag bag) {
        this.grid = grid;
        this.bag = bag;
    }

    public Player(Game game, HashSet<String> wordsSet, TileBag bag) {
        this.grid = new Grid(wordsSet);
        this.bag = bag;
    }

    // TODO make get function return immutable versions
    // TODO do we want this function facing outward or exiwting at all
    public Grid getGrid() {
        return this.grid;
    }

    // TODO do we want this function facing outward or exiwting at all
    public TileBag getBag() {
        return this.bag;
    }

    // TODO shoudl this exist? Make immutable if so, and rename to hand within class
    public MultiSet<Tile> getHand() {
        return this.hand;
    }

    @Override
    public String toString() {
        return ""; // TODO STUB
    }   

    /**
     * If player has no tiles left in their hand. This means all the tiles are on the Grid
     * @return boolean of tiles left
     */
    public boolean handEmpty() {
        return this.hand.isEmpty();
    }
    
    /**
     * Grabs a tile from the bag and adds it to the players hand.
     * In the context of a game, this is essentially a one player peel.
     * There are no checks to see if the player can actually do this, (i.e) 
     * another player is done or this player is done. That is the responsibility of the 
     * game
     * @return Tile grabbed, null if no tile could be grabbed (bag empty)
     */
    public Tile grabTile() {
        if (bag.isEmpty()) {return null;} // edge case

        Tile grabbedTile = bag.grabTile();
        // if this tile not in hand add it otherwise increment counter
        // TODO below should be encapsulated in a multiset class?
        this.hand.add(grabbedTile); 
        return grabbedTile;
    }

    /**
     * Player drops one tile into the bag. This is not done during the game
     * without drawing three tiles. 
     * @param dropTile the tile that is dropped from player
     */
    // TODO how important would it be to make this return a boolean if removal was successful?
    public void dropTile(Tile dropTile) { // TODO maybe make private? Should be ok because game will only ever use dump? Don't know borderline case
        hand.remove(dropTile);
    }

    /**
     * @param t the tile to check if it is within the hand
     * @return if the tile is within the hand
     */
    public boolean inHand(Tile t) {
        return this.hand.contains(t);
    }

    // TODO make unsafe version to save time for AI?
    /**
     * Places the tile from the hand into the given location on the grid.
     * If the tile doesn't exists in the hand or location is already full, an error 
     * is thrown.
     * @param loc
     * @param tile
     * @throws IllegalArgumentException If location is already full or tile not in hand
     */
    public void placeTile(Location loc, Tile tile) throws IllegalArgumentException {
        if (this.grid.locationFilled(loc)) {
            throw new IllegalArgumentException("Location is already filled with a tile"); //
        }
        if (!inHand(tile)) {
            throw new IllegalArgumentException("Tile was not within hand");
        }
        // TODO make placeTile also trigger the game to check for a peel?
        this.grid.placeUnsafe(loc, tile); // now that checks have been done we can place this
        this.hand.remove(tile); // remove tile from hand now on grid
    }

    // TODO make unsafe version to save time for AI?
    /**
     * Removes tile from the specified location and places that tile into the hand.
     * If the location does not have a tile it will throw an error
     * @param loc location to remove a tile
     * @throws IllegalArgumentException If location is empty with no tile
     */
    public void removeTile(Location loc) throws IllegalArgumentException {
        if (!this.grid.locationFilled(loc)) {
            throw new IllegalArgumentException("Cannot remove tile from an empty location"); //
        }
        // TODO make placeTile also trigger the game to check for a peel?
        Tile tile = this.grid.remove(loc); // now that checks have been done we can place this

        // TODO below should be in a multiset class?
        this.hand.add(tile); // adds tile from the grid to hand
    }

    /**
     * The action within the game of dumping one tile to get three in return.
     * This is usually done to remove a diffcult letter, like Q, X, or Z. In a 
     * dump other players in the game do not need to participate like peel
     * @param dropTile the tile chosen to be dumped off
     * @return boolean if the dump was successful (were there three tiles to grab)
     */
    public boolean dump(Tile dropTile) {
        if (!this.hand.contains(dropTile)) {return false;}
        dropTile(dropTile); 

        grabTile(); // grab three tiles (could be easily changed later for alternate implementations)
        grabTile();
        grabTile();
        return true;
    }

    /**
     * If the grid is valid
     * @return boolean true or false if the grid 
     */
    public boolean gridValid() {
        return this.grid.valid();
    }

    /**
     * If all of the tiles the player has have been made into a proper 
     * set of words on the Grid. In other words, they have a valid grid and nothing 
     * else to place.
     * @return boolean if the players has used all their tiles properly
     */
    public boolean canPeel() {
        return (this.handEmpty() && this.gridValid());
    }

}
