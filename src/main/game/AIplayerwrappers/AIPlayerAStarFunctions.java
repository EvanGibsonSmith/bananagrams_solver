package src.main.game.AIplayerwrappers;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.HashMap;

import src.main.game.players.types.branchplayers.AbstractBranchingPlayer;
import src.main.game.Tile;

// TODO document
public class AIPlayerAStarFunctions {
    static final HashMap<Character, Integer> scrabbleLetters = getScrabbleMap();

    private static HashMap<Character, Integer> getScrabbleMap() {
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        int[] letterValues = new int[] {1,3,3,2,1,4,2,4,1,8,5,1,3,1,1,3,10,1,1,1,1,4,4,8,4,10};
        HashMap<Character, Integer> scrabbleLetterValues = new HashMap<>();
        for (int idx=0; idx<alphabet.length; ++idx) {
            scrabbleLetterValues.put(alphabet[idx], letterValues[idx]);
        }
        return scrabbleLetterValues;
    }

    public static Function<AbstractBranchingPlayer, Boolean> emptyHandGoal() {
        return (p) -> p.getHand().size()==0;
    }

    // NOTE: Not really useful, but good for checking goal is working properly
    public static Function<AbstractBranchingPlayer, Boolean> placeQGoal() {
        return (p) -> !p.getHand().contains(new Tile('q'));
    }


    public static BiFunction<AbstractBranchingPlayer, AbstractBranchingPlayer, Double> handSizeCost() {
        return (p, q) -> (double) q.getHand().size() - p.getHand().size();
    }

    public static Function<AbstractBranchingPlayer, Double> handSizeHeuristic() {
        return (p) -> (double) p.getHand().size();
    }

    private static int getHandScrabbleTotal(AbstractBranchingPlayer p) {
        int total = 0;
        for (Tile t: p.getHand()) {
            total += scrabbleLetters.get(t.getLetter());
        }
        return total;
    }

    public static BiFunction<AbstractBranchingPlayer, AbstractBranchingPlayer, Double> handScrabbleCost() {
        return (p, q) -> (double) getHandScrabbleTotal(q) - getHandScrabbleTotal(p);
    }

    public static Function<AbstractBranchingPlayer, Double> handScrabbleHeuristic() {
        return (p) -> (double) getHandScrabbleTotal(p);
    }
    
    private static int sumWordLengths(AbstractBranchingPlayer p, int optimalLength) {
        int total = 0;
        for (String word: p.getGrid().getWordsPlayed()) {
            total += Math.abs(word.length() - optimalLength); // if length is correct, adds nothing to cost
        }
        return total;
    }

    // NOTE: Below heuristics are too slow and would likely need to be tracked by a special grid object as words are placed to speed up
    public static BiFunction<AbstractBranchingPlayer, AbstractBranchingPlayer, Double> bestWordLengthCost(int n) {
        return (p, q) -> (double) sumWordLengths(q, n) - sumWordLengths(p, n);
    }

    public static Function<AbstractBranchingPlayer, Double> bestWordLengthHeuristic(int n) {
        return (p) -> (double) sumWordLengths(p, n);
    }

    public static BiFunction<AbstractBranchingPlayer, AbstractBranchingPlayer, Double> wordLengthFiveCost() {
        return bestWordLengthCost(5);
    }

    public static Function<AbstractBranchingPlayer, Double> wordLengthFiveHeuristic() {
        return bestWordLengthHeuristic(5);
    }
}
