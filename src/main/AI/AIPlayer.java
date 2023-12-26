package src.main.AI;

import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;

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

    // TODO need serious testing
    private boolean validateWord(String word, String gridFragment, MultiSet<Character> charHand) {
        // check word fragment is within word because otherwise word couldn't be placed here
        if (!word.contains(gridFragment)) {
            return false;
        }
        // now that we know grid fragment is in word can remove it from the characters needed in word with MultiSet
        MultiSet<Character> wordCharSet = new MultiSet<Character>();
        for (char c: word.toCharArray()) {wordCharSet.add(c);}
        MultiSet<Character> gridFragmentCharSet = new MultiSet<Character>();
        for (char c: gridFragment.toCharArray()) {gridFragmentCharSet.add(c);}
        wordCharSet.removeAll(gridFragmentCharSet);
        if (!charHand.subset(wordCharSet)) { // if we don't have the letters for the rest of the word, it's invalid
            return false;
        }

        return true;
    }

    // TODO should be in the word set class when that is made
    /*private HashSet<String> validWords(MultiSet<Tile> hand, HashSet<String> words) {
        MultiSet<Character> characterHand = new MultiSet<>();
        for (Tile t: hand) {characterHand.add(t.getLetter());}

        HashSet<String> validWords = new HashSet<>();
        for (String word: words) {
            validateWord(word, , hand);
        }
    }*/

    // TODO this should be handled by the Word Set when that class is made later
    /*private HashSet<String> thatContainSubstring(String substring, HashSet<String> words) {
        HashSet<String> validWords = new HashSet<>();
        for (String word: words) {
            if (word.contains(substring)) { // TODO think about edge case where instance could occur twice, na in banana. This is somewhat important later
                validWords.add(word);
            }
        }
        return validWords;
    }*/

    public Set<AIPlayer> branch_forward() {
        HashSet<Grid> output = new HashSet<>();
        HashMap<Location, String> wordDownMap = this.getGrid().getWordsDown(); // TODO maybe these functions could go in the AI Grid with the AI specific functionality?
        HashMap<Location, String>  wordRightMap = this.getGrid().getWordsRight();

        /*for (Location loc: wordDownMap.keySet()) {
            HashSet<String> possibleWords = possibleWordsDown(loc);

        }

        if (wordRight==null) {
            
        }*/ // TODO COMPLETE THIS
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
