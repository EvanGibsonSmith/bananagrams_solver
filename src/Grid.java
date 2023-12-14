package src;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashMap;

public class Grid {
    private ArrayList<ArrayList<Tile>> grid = new ArrayList<>(); // TODO needed at all?
    private HashSet<String> wordsSet; 
    private HashMap<Location, Tile> filledSquares = new HashMap<>();

    public Grid(HashSet<String> wordsSet) {
        this.wordsSet = wordsSet;
    }

    /** Places a tile on the grid, does not check if it is valid (could be "floating", invalid words, etc)
     * Does not check if the square already has a tile on it. If tile is replaced, that tile placed on may "dissapear"
    */
    private void placeUnsafe(Location loc, Tile tile) {
        filledSquares.put(loc, tile);
    }
    

    // TODO the two functions below, DRY?
    
    /**
     * Given a specific tile, this gets the word in which this tile is the starting letter that go to the right.
     * @param loc the location at the start of the word
     * @return null if this tile is not the start of a word, the word if it is
     */
    private String getWordRight(Location loc) { // TODO TEST ME
        if (!filledSquares.get(loc.left()).isEmpty() || filledSquares.get(loc).isEmpty()) {
            return null; // if left of this tile not empty or this tile is empty no word can be formed
        }
        String word = ""; // build word from letter now
        Location cursor = loc;
        while (!filledSquares.get(cursor).isEmpty()) { // if cursor not empty add to word
            word += filledSquares.get(cursor).getLetter();
            cursor = cursor.right();
        }
        return word;
    }

    /**
     * Given a specific tile, this gets the word in which this tile is the starting letter that go to the right.
     * @param loc the location at the start of the word
     * @return null if this tile is not the start of a word, the word if it is
     */
    private String getWordDown(Location loc) {
        if (!filledSquares.get(loc.below()).isEmpty() || filledSquares.get(loc).isEmpty()) {
            return null; // if left of this tile not empty or this tile is empty no word can be formed
        }
         String word = ""; // build word from letter now
        Location cursor = loc;
        while (!filledSquares.get(cursor).isEmpty()) { // if cursor not empty add to word
            word += filledSquares.get(cursor).getLetter();
            cursor = cursor.right();
        }
        return word;
    }

    /**
     * Gets every word played on the board, and returns a list of them.
     * In a valid board these should all be acceptable words.
     * @return
     */
    private ArrayList<String> getWordsPlayed() {  // TODO TEST ME
        ArrayList<String> words = new ArrayList<>();
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
    private Boolean validWords() {
        ArrayList<String> words = getWordsPlayed();
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
    private Boolean validIslands() {
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
    public ArrayList<Location> placeableLocations() {  // TODO TEST ME
        ArrayList<Location> tilePlaceLocations = new ArrayList<>();
        for (Location sq: filledSquares.keySet()) {
            Location[] adj = {new Location(sq.getRow()+1, sq.getColumn()),
                              new Location(sq.getRow()-1, sq.getColumn()),
                              new Location(sq.getRow(),   sq.getColumn()+1),
                              new Location(sq.getRow(),   sq.getColumn()-1)}

            for (Location tile: adj) {
                if (filledSquares.containsKey(adj)) { // check if the tile is blank
                    tilePlaceLocations.add(tile);
                }
            }
        }
        return tilePlaceLocations; // STUB
    }
}
