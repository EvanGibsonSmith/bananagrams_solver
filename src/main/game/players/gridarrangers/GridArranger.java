package src.main.game.players.gridarrangers;

import src.interfaces.Copyable;
import src.main.game.Location;
import src.main.game.Tile;
import src.main.game.grids.Grid;
import src.main.game.players.hand.Hand;

import java.util.HashSet;
import java.util.Set;

// TODO probably will change visibility when packages are reorganized
public class GridArranger implements Copyable<GridArranger>{
    Grid grid;
    Hand hand; // represents hand player has and how much of each tile

    public GridArranger(Grid grid, Hand hand) {
        this.grid = grid;
        this.hand = hand;
    }

    public GridArranger(Grid grid) {
        this.grid = grid;
        this.hand = new Hand();
    }

    public GridArranger(HashSet<String> wordsSet, Hand hand) {
        this.grid = new Grid(wordsSet);
        this.hand = hand;
    }

    @Override
    // TODO document. DOES NOT COPY HAND?! GOOD CHOICE?
    public GridArranger copy() {
        return new GridArranger(grid.copy(), hand); 
    }

    public Grid getGrid() {
        return this.grid;
    }

    public Hand getHand() {return hand;}
    
    public void setHand(Hand hand) {this.hand = hand;}

    /**
     * If the grid is valid, allowing the player to peel or win
     * @return boolean true or false if the grid 
     */
    public boolean gridValid() {
        return this.grid.valid();
    }

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
        if (!hand.inHand(tile)) {
            throw new IllegalArgumentException("Tile was not within hand");
        }
        this.grid.placeUnsafe(loc, tile); // now that checks have been done we can place this
        this.hand.remove(tile); // remove tile from hand now on grid
    }

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
        Tile tile = this.grid.remove(loc); // now that checks have been done we can place this
        this.hand.add(tile); // adds tile from the grid to hand
    }

    /**
     * Removes all of the tiles from the grid, essentially resetting the grid.
     * Technically, the grid object remains the same, and isn't simply replaced.
     * The size of the grid is also not changed, so while the grid is now empty 
     * it remains the size needed for the words that were placed in it.
     */
    public void clearGrid() {
        Set<Location> filled = new HashSet<>(this.getGrid().filledLocations());
        for (Location loc: filled) {
            Tile tile = this.grid.remove(loc); // now that checks have been done we can place this
            this.hand.add(tile); // adds tile from the grid to hand
        }
    }
}
