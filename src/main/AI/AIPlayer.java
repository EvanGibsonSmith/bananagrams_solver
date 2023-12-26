package src.main.AI;

import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Consumer;

import src.main.game.Player;
import src.main.game.Tile;
import src.main.game.TileBag;
import src.main.game.Game;
import src.main.game.Grid;
import src.main.game.Location;
import src.data_structures.MultiSet;

public class AIPlayer extends Player implements Branchable<AIPlayer> {
    
    public AIPlayer(Game game, Grid grid, TileBag bag) {
        super(game, grid, bag);
    }

    /*private Set<Grid> branchLocationDown(HashSet<String> words) {

    }

    private Set<Grid> branchLocationRight() {

    }*/

    // TODO should be handled by word set when that class is made. Maybe an AI extension version could exist as well
    //private HashSet<String> hasLetters() {
    //
    //}

    private void possibleWordsDown(Location loc) {
        String downFragment = this.getGrid().getWordDown(loc); // TODO maybe these functions could go in the AI Grid with the AI specific functionality?
        MultiSet<Tile> hand = this.getHand();
        HashSet<String> wordsSet = this.getGrid().getWordsSet();
    }

    /* TODO need serious testing (also make some object that handles this stuff, like a wordSet object and AIWordSet extension?)
        An issue in general is we might be able to use the information from the substring, but technically words can have multiple
        instances of the same substring which need to be covered.
    */
    /**
     * Validates the a word given, with a grid fragment, the letters that need to be in that order
     * in the word because they are on the board, and the tiles in the hand are compatible. It does 
     * NOT validate that there is actual space on the board for the word to be placed, just the constraints of 
     * the letters. 
     * 
     * Examples:
     *      word = "apple", gridFragment = "pp", tileHand = {a, l, e}.
     *          The gridFragment is in the word, and we have the tiles to place the word, becasue the grid 
     *          fragment covers the "pp", so this does not return null.
     *      
     *      word = "football", gridFragment = "oot", tileHand = {f, o, x, a}
     *          The gridFragment is within the word but we do not have the tiles to play football
     * 
     *      word = "messy", gridFragment = "met", tileHand = {e, s, s, y}
     *          We cannot place the word because the gridFragment is not within the word
     * 
     * @param word The word to check if it can be placd
     * @param gridFragment The tiles already on the board the word would need to connect to
     * @param tileHand The tiles in the hand of the player currently
     * @return boolean of this word is valid by letters
     */
    private boolean validateWordLetters(String word, String gridFragment, MultiSet<Tile> tileHand) {
        // check word fragment is within word because otherwise word couldn't be placed here
        // TODO check which order checking these two conditions is more efficient
        if (!word.contains(gridFragment)) { // if gridFragment not in word (most words will short circuit here)
            return false;
        }
        // now that we know grid fragment is in word can remove it from the characters needed in word with MultiSet
        MultiSet<Character> wordCharSet = new MultiSet<Character>();
        for (char c: word.toCharArray()) {wordCharSet.add(c);}
        MultiSet<Character> gridFragmentCharSet = new MultiSet<Character>();
        for (char c: gridFragment.toCharArray()) {gridFragmentCharSet.add(c);}
        MultiSet<Character> charHand = new MultiSet<Character>();
        for (Tile t: tileHand) {charHand.add(t.getLetter());}


        wordCharSet.removeAll(gridFragmentCharSet);
        if (!charHand.subset(wordCharSet)) { // if we don't have the letters for the rest of the word, it's invalid
            return false;
        }

        return true;
    }

    // TODO kind of odd place for this function. Maybe test this?
    private ArrayList<Integer> indexesOf(String word, String sub) {
        ArrayList<Integer> out = new ArrayList<>();
        int start = 0;
        while (start!=-1) {
            int newIndex = word.indexOf(sub, 0);
            out.add(newIndex);
            start = newIndex;
        }
        return out;
    }

    // TODO maybe this could be broken up, might require a small helper package class
    private ArrayList<Grid> placeWord(String word, String gridFragment, Location loc, int direction) {
        ArrayList<Grid> outGrids = new ArrayList<>();
        ArrayList<Integer> subIndexes = indexesOf(word, gridFragment);
        Location wordStart = null; // begins at the start of the word
        for (int idx: subIndexes) { // this will mostly be one index, with exception of something like eye on e in two ways
            if (direction==0) {wordStart = new Location(loc.getRow(), loc.getColumn()-idx);}
            else if (direction==1) {wordStart = new Location(loc.getRow()-idx, loc.getColumn());}
            else {throw new IllegalArgumentException("Direction must be 0 for left to right or 1 for up to down");}

            Location cursor = new Location(wordStart); // begins at the start of the word, moves along word for collisions
            Grid grid = this.getGrid();
            for (int i=0; i<word.length(); ++i) {
                // TODO need to find a way to "skip over" part of word already completed. I think it might work now?

                // if location is not within already covered part then can't place this word
                if (!(i>=idx && i<idx+gridFragment.length()) && (grid.locationFilled(loc))) {
                    return null;
                }         

                // move cursor along word
                if (direction==0) {cursor = cursor.right();}
                else if (direction==1) {cursor = cursor.below();}
            }
            
            // if null was not returned, we can place word
            Grid newGrid = new Grid(grid);
            MultiSet<Tile> hand = this.getHand();
            for (int letterIndex=0; letterIndex<word.length(); ++letterIndex) {

                if (grid.locationFilled(loc)) { // skips over already placed portion of word
                    Tile nextTile = new Tile(word.charAt(letterIndex));
                    hand.remove(nextTile); // remove from hand, place into grid
                    newGrid.placeUnsafe(loc, nextTile);
                }

                // move cursor along word
                if (direction==0) {cursor = cursor.right();}
                else if (direction==1) {cursor = cursor.below();}
            }
            outGrids.add(newGrid);
        }
        return outGrids;
    }

    // TODO This might be kind of inefficient  
    public Set<AIPlayer> branch_forward() {
        ArrayList<Grid> branchedGrids = new ArrayList<>();

        Grid grid = this.getGrid();
        for (Location loc: grid.filledLocations()) {
            for (String word: grid.getWordsSet()) {
                String gridFragment = grid.getWordDown(loc);
                if (validateWordLetters(word, gridFragment, this.getHand())) { // only if the word has valid letters do we start to check position
                    ArrayList<Grid> nextGrid = placeWord(word, gridFragment, loc, 0);
                    if (nextGrid!=null) {
                        branchedGrids.addAll(nextGrid);
                    }
                }
            }
        }
            
        return null; // STUB
    }

    public Set<AIPlayer> branch_backward() {
        return null; // STUB ("backward" meaning removing words and tiles)
    }

    @Override
    public Set<AIPlayer> branch() {
        return null; // TODO STUB, will combine forward and backward branchinh
    }
}
