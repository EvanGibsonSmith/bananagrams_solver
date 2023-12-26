package src.main.game;

import java.util.HashSet;
import java.util.Queue;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Set;

public class Grid {
    protected Location topLeft = null;
    protected Location bottomRight = null;
    protected HashSet<String> wordsSet; // TODO may want to make this it's own class with trie structure included and contains
    protected HashMap<Location, Tile> filledSquares = new HashMap<>();

    public Grid(HashSet<String> wordsSet) {
        this.wordsSet = wordsSet;
    }

    // Copy constructor
    public Grid(Grid g) {
        this.filledSquares = g.filledSquares;
        this.topLeft = g.topLeft;
        this.bottomRight = g.bottomRight;
        this.wordsSet = g.wordsSet;
    }

    public HashSet<String> getWordsSet() {
        return this.wordsSet;
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

    /**
     * The filled locations on the grid
     * @return locations
     */
    public Set<Location> filledLocations() {
        return filledSquares.keySet();
    }


    /**
     * Gets the tile object at the specified location. Will 
     * return null if the tile is empty
     * @param loc
     * @return
     */
    public Tile getTile(Location loc) {
        return filledSquares.get(loc);
    }

    /**
     * Gives the number of filled squares on the grid, not based on bounding box
     * @return integer representing size of grid based on tiles placed
     */
    public int size() {
        return filledSquares.size();
    }
    
    /** Places a tile on the grid, does not check if it is valid (could be "floating", invalid words, etc)
     * Does not check if the square already has a tile on it. If tile is replaced, that tile placed on may "dissapear"
     * @param loc location to place the tile on the grid
     * @param tile the tile to place
    */
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
    
    /**
     * Given a specific tile, this gets the word in which this tile is the starting letter that go to the right.
     * @param loc the location at the start of the word
     * @return null if this tile is not the start of a word, the word if it is
     */
    public String getWordRight(Location loc) {
        if (!(filledSquares.get(loc)!=null && filledSquares.get(loc.left())==null)) { // if NOT conditions making location START of a word
            return null; // if left of this tile not empty or this tile is empty no word can be formed
        }
        String word = ""; // build word from letter now
        Location cursor = loc;
        while (filledSquares.get(cursor)!=null) { // if cursor not empty add to word
            word += filledSquares.get(cursor).getLetter();
            cursor = cursor.right();
        }
        
        //if (word.length()==1) {return null;} if length is one, technically not word needed to be checked, but useful to include
        return word;
    }

    /**
     * Given a specific tile, this gets the word in which this tile is the starting letter that go to the right.
     * @param loc the location at the start of the word
     * @return null if this tile is not the start of a word, the word if it is
     */
    public String getWordDown(Location loc) {
        if (!(filledSquares.get(loc)!=null && filledSquares.get(loc.above())==null)) { // if NOT conditions making location START of a word
            return null; // if left of this tile not empty or this tile is empty no word can be formed
        }
        String word = ""; // build word from letter now
        Location cursor = loc;
        while (filledSquares.get(cursor)!=null) { // if cursor not empty add to word
            word += filledSquares.get(cursor).getLetter();
            cursor = cursor.below();
        }

        //if (word.length()==1) {return null;} if length is one, technically not word needed to be checked, but useful to include
        return word;
    }

    // TODO document
    public HashMap<Location, String> getWordsDown() {
        HashMap<Location, String> map = new HashMap<>();
        for (Location loc: this.filledSquares.keySet()) {
            map.put(loc, getWordDown(loc));
        }
        return map;
    }

    // TODO document
    public HashMap<Location, String> getWordsRight() {
        HashMap<Location, String> map = new HashMap<>();
        for (Location loc: this.filledSquares.keySet()) {
            map.put(loc, getWordRight(loc));
        }
        return map;
    }

    /**
     * Gets every word played on the board, and returns a list of them.
     * In a valid board these should all be acceptable words.
     * @return
     */
    public HashSet<String> getWordsPlayed() {
        HashSet<String> words = new HashSet<>();
        for (Location loc: filledSquares.keySet()) {
            String downWord = getWordDown(loc);
            if (downWord!=null && downWord.length()!=1) {
                words.add(downWord);
            }

            String rightWord = getWordRight(loc);
            if (rightWord!=null && rightWord.length()!=1) {
                words.add(rightWord);
            }
        }
        return words;
    }

    /**
     * Checks all of the words on the grid and determines if they are all within the set of valid words.
     * Does not check for disconnected island. Just iterates through the letter and each of their words
     * @return true if all of the words are valid, false is not 
     */
    // FIXME there is a extreme edge case in which only ONE TILE is placed that this will ALWAYS consider valid. Don't even know if I need to care
    public Boolean validWords() {
        HashSet<String> words = getWordsPlayed();
        for (String word: words) {
            if (!this.wordsSet.contains(word)) { // if one of the words is not valid then board is not valid.
                return false;
            }
        }
        return true;
    }
 
    /**
     * Using BFS through the tiles this determines if all of tiles are connected as they must be 
     * in a valid configuration for the game. Checks if we can visit the same number of tiles 
     * as the total number of tiles in the game from a single starting tile. In this case
     * the single island condition for the grid is satified.
     * @return true if all tiles are connected, otherwise false 
     */
    public boolean tilesConnected() { 
        if (filledSquares.keySet().iterator().hasNext()==false) {return true;} // base case. If nothing placed, everything is connected        
        
        Location rootLocation = filledSquares.keySet().iterator().next(); // guarenteed to exist because base case covers if not
        HashSet<Location> visited = new HashSet<>();
        Queue<Location> nextLocations = new LinkedList<Location>();
        nextLocations.add(rootLocation);
        while (!nextLocations.isEmpty()) {
            Location loc = nextLocations.poll();
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
        return (visited.size()==this.size());
    }

    /**
     * Checks if the grid is in a valid state. This includes no floating letters, 
     * @return if the Grid is in a valid configuration
     */
    public Boolean valid() { // this function may be able to be improved by short circuiting with easy conditions to check first
        return (validWords() && tilesConnected());
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
