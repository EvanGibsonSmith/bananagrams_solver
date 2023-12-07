package src;
import java.util.ArrayList;
import java.util.HashSet;

public class Grid {
    ArrayList<ArrayList<Tile>> grid = new ArrayList<>();
    HashSet<String> words;
    HashSet<Tile> filledSquares = new HashSet<>();

    public Grid(HashSet<String> words) {
        this.words = words;
    }

    /** Places a tile on the grid, does not check if it is valid (could be "floating", invalid words, etc) */
    private void placeUnsafe() {

    }

    /**
     * Checks if the grid is in a valid state. This includes no floating letters, 
     * @return if the Grid is in a valid configuration
     */
    private Boolean valid() { // this function may be able to be improved by short circuiting with easy conditions to check first
        return true; // STUB
    }
    
    /**
     * Places a tile on the board, checking if the move is a valid choice. 
     * This check includes all words that are created with the placement.
     * @return True if place was successful, false if not
     */
    public Boolean place() {
        return true;
    }

    /**
     * Gives the locations where are of the tiles could potentially be placed, not taking into account if a word can be made.
     * THIS DOES NOT TAKE INTO ACCOUNT IF WORD CAN BE MADE, just if it is connected
     * @return array of locations
     */
    public Location[] placeableTiles() {
        HashSet<Tile> tilePlaceLocations = new HashSet<Tile>();
        for (Tile sq: filledSquares) {
            Tile[] adj = {grid.get(sq.getRow()+1).get(sq.getColumn()),
                              grid.get(sq.getRow()-1).get(sq.getColumn()),
                              grid.get(sq.getRow()).get(sq.getColumn()+1),
                              grid.get(sq.getRow()).get(sq.getColumn()-1)};

            for (Tile tile: adj) {
                if (tile.getLetter()==' ') { // check if the tile is blank
                    tilePlaceLocations.add(tile);
                }
            }
        }
        return null; // STUB
    }
}
