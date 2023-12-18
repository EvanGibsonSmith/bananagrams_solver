package src.main;

import java.util.HashSet;
import java.util.Queue;
import java.util.LinkedList;
import java.util.HashMap;

public class Grid {
    private Location topLeft = null;
    private Location bottomRight = null;
    private HashSet<String> wordsSet; 
    public HashMap<Location, Tile> filledSquares = new HashMap<>(); // TODO make private later

    public Grid(HashSet<String> wordsSet) {
        this.wordsSet = wordsSet;
    }

    /**
     * Gets the top left location bounding the letters
     * @return Location topLeft
     */
    public Location getTopLeft() {
        return topLeft;
    }

    /**
     * Gets the bottonRight location bounding the letters
     * @return Location bottomRight
     */
    public Location getBottomRight() {
        return bottomRight;
    }

    /**
     * Updates the topLeft and bottomRight if the location given is outside of those ranges. 
     * This function should be called when a new location is added and needs to check the bounding box
     */
    private void updateBoundingBox(Location loc) {
        // edge case: if they are null (nothing placed). 
        // Technically both should get set at the same time but I have them seperate
        if (this.topLeft==null) {this.topLeft=loc;} if (this.bottomRight==null) {this.bottomRight=loc;}

        // update topLeft
        if (loc.getRow() < this.topLeft.getRow() && loc.getColumn() < this.topLeft.getColumn()) {
            this.topLeft = loc;
        }
        else if (loc.getRow() < this.topLeft.getRow()) {
            this.topLeft = new Location(loc.getRow(), this.topLeft.getColumn()); // new row, but column remains the same
        }
        else if (loc.getColumn() < this.topLeft.getColumn()) {
            this.topLeft = new Location(this.topLeft.getRow(), loc.getColumn()); // new column, but row remains the same
        }

        // update bottomRight
        if (loc.getRow() > this.bottomRight.getRow() && loc.getColumn() > this.bottomRight.getColumn()) {
            this.bottomRight = loc;
        }
        else if (loc.getRow() > this.bottomRight.getRow()) {
            this.bottomRight = new Location(loc.getRow(), this.bottomRight.getColumn()); // new row, but column remains the same
        }
        else if (loc.getColumn() > this.bottomRight.getColumn()) {
            this.bottomRight = new Location(this.bottomRight.getRow(), loc.getColumn()); // new column, but row remains the same
        }
    }

    /**
     * @param loc
     * @return if the location full
     */
    public boolean locationFilled(Location loc) {
        return filledSquares.containsKey(loc);
    }
    
    /** Places a tile on the grid, does not check if it is valid (could be "floating", invalid words, etc)
     * Does not check if the square already has a tile on it. If tile is replaced, that tile placed on may "dissapear"
     * @param loc TODO document
     * @param tile TODO document
    */
    // TODO make private?
    public void placeUnsafe(Location loc, Tile tile) {
        filledSquares.put(loc, tile);
        updateBoundingBox(loc);
    }

    /**
     * Removes the tile from the given locations. Returns null if that location didn't have anything in it
     * @param loc the location to remove
     * @return the removed tile
     */
    public Tile remove(Location loc) {
        if (this.filledSquares.get(loc)==null) {return null;}
        Tile removedTile = this.filledSquares.get(loc);
        this.filledSquares.remove(loc); 
        return removedTile;
    }

    /**
     * Takes the tiles and gives a bounding box of the locations with tiles actually within them.
     * TODO TEST THIS AND DOCUMENT
     * @return
     */
    /**public Location[] boundingBox() {
        int maxX = 0; int minX = 0;
        int maxY = 0; int minY = 0;
        for (Location loc: filledSquares.keySet()) {
            if (loc.getRow() > maxX) {maxX = loc.getRow();}
            else if (loc.getRow() < minX) {minX = loc.getRow();}

            if (loc.getRow() > maxY) {maxY = loc.getRow();}
            else if (loc.getRow() > maxY) {maxY = loc.getRow();}
        }

        return {new Location(maxX, minY); new Location(maxX, minY);};
    }**/

    @Override
    public String toString() {
        String out = "";
        if (topLeft==null || bottomRight==null) {return "";}

        int rowMin = topLeft.row; int rowMax = bottomRight.row;
        int colMin = topLeft.column; int colMax = bottomRight.column;
        for (int row=rowMin; row<=rowMax; ++row) {
            for (int col=colMin; col<=colMax; ++col) {
                Location thisLoc = new Location(row, col);
                if (filledSquares.containsKey(thisLoc)) {
                    out += " " + filledSquares.get(thisLoc).toString().toUpperCase();
                }
                else {out += " _";}
            }
            out += "\n";
        }
        return out;
    }
    

    // TODO the two functions below, DRY?
    
    /**
     * Given a specific tile, this gets the word in which this tile is the starting letter that go to the right.
     * @param loc the location at the start of the word
     * @return null if this tile is not the start of a word, the word if it is
     */
    private String getWordRight(Location loc) { // TODO TEST ME
        if (!(filledSquares.get(loc)!=null && filledSquares.get(loc.left())==null)) { // if NOT conditions making location START of a word
            return null; // if left of this tile not empty or this tile is empty no word can be formed
        }
        String word = ""; // build word from letter now
        Location cursor = loc;
        while (filledSquares.get(cursor)!=null) { // if cursor not empty add to word
            word += filledSquares.get(cursor).getLetter();
            cursor = cursor.right();
        }
        
        // if the length of the word is 1, is doesn't need to be checked with the dictionary because it's already connected somewhere else
        if (word.length()==1) {return null;} 
        return word;
    }

    /**
     * Given a specific tile, this gets the word in which this tile is the starting letter that go to the right.
     * @param loc the location at the start of the word
     * @return null if this tile is not the start of a word, the word if it is
     */
    private String getWordDown(Location loc) {
        if (!(filledSquares.get(loc)!=null && filledSquares.get(loc.above())==null)) { // if NOT conditions making location START of a word
            return null; // if left of this tile not empty or this tile is empty no word can be formed
        }
        String word = ""; // build word from letter now
        Location cursor = loc;
        while (filledSquares.get(cursor)!=null) { // if cursor not empty add to word
            word += filledSquares.get(cursor).getLetter();
            cursor = cursor.below();
        }

        // if the length of the word is 1, is doesn't need to be checked with the dictionary because it's already connected somewhere else
        if (word.length()==1) {return null;} 
        return word;
    }

    /**
     * Gets every word played on the board, and returns a list of them.
     * In a valid board these should all be acceptable words.
     * @return
     */
    public HashSet<String> getWordsPlayed() {  // TODO TEST ME  MAKE PRIVATE LATER?
        HashSet<String> words = new HashSet<>();
        for (Location loc: filledSquares.keySet()) {
            String downWord = getWordDown(loc);
            if (downWord!=null) {
                words.add(downWord);
            }

            String rightWord = getWordRight(loc);
            if (rightWord!=null) {
                words.add(rightWord);
            }
        }
        return words;
    }

    /**
     * Checks all of the words on the grid and determines if they are all within the set of valid words.
     * Does not check for disconnected island. Just iterates through the letter and each of their words
     * @return True if all of the words are valid, False is not 
     */
    public Boolean validWords() { // TODO make private again/ for testing. Or make __validWords instead?
        HashSet<String> words = getWordsPlayed();
        for (String word: words) {
            if (!this.wordsSet.contains(word)) { // if one of the words is not valid then board is not valid.
                return false;
            }
        }
        return true;
    }
 
    /**
     * TODO fill this documentation including big O
     * @return
     */
    public Boolean validIslands() { // TODO make this private again, this is for testing.
        if (filledSquares.keySet().iterator().hasNext()==false) {return true;} // base case. If nothing placed, everything is connected        
        
        Location rootLocation = filledSquares.keySet().iterator().next(); // guarenteed to exist because base case covers if not
        HashSet<Location> visited = new HashSet<>();
        Queue<Location> nextLocations = new LinkedList<Location>(); // TODO I think I have the time complexites correct here
        nextLocations.add(rootLocation);
        while (!nextLocations.isEmpty()) {
            Location loc = nextLocations.poll(); // TODO potential improvment? Instead of checking if this square is filled in while loop check as you add? More complicated for sure but maybe slightly faster
            // only "expand" island if this square is actually a letter/haven't visited already
            if (filledSquares.keySet().contains(loc) && !visited.contains(loc)) { 
                visited.add(loc);

                nextLocations.add(loc.above());
                nextLocations.add(loc.below());
                nextLocations.add(loc.left());
                nextLocations.add(loc.right());
            }
        }

        // now that all connected to root node have been found, the sets should have the same elements (and thus size, which is O(1))
        return (visited.size()==filledSquares.size());
    }

    /**
     * Checks if the grid is in a valid state. This includes no floating letters, 
     * @return if the Grid is in a valid configuration
     */
    public Boolean valid() { // this function may be able to be improved by short circuiting with easy conditions to check first
        return (validWords() && validIslands());
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
    public HashSet<Location> placeableLocations() {  // TODO TEST ME
        HashSet<Location> tilePlaceLocations = new HashSet<>();
        for (Location sq: filledSquares.keySet()) {
            Location[] adj = {new Location(sq.getRow()+1, sq.getColumn()),
                              new Location(sq.getRow()-1, sq.getColumn()),
                              new Location(sq.getRow(),   sq.getColumn()+1),
                              new Location(sq.getRow(),   sq.getColumn()-1)};

            for (Location tile: adj) {
                if (!filledSquares.containsKey(tile)) { // check if the tile is blank
                    tilePlaceLocations.add(tile);
                }
            }
        }
        return tilePlaceLocations; 
    }
}
