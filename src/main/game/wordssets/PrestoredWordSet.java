package src.main.game.wordssets;

import java.util.HashSet;
import java.util.HashMap;

import java.util.function.BiFunction;

// TODO document
public class PrestoredWordSet extends WordsSet {
    final int combinationLength;
    HashSet<String> wordsSet;
    HashMap<String, HashSet<String>> combinations = new HashMap<>();
    final char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    
    public PrestoredWordSet(HashSet<String> set, int combinationLength) {
        super(set);
        this.wordsSet = super.wordsSet;
        this.combinationLength = combinationLength;
        makeSubstrings(this.combinations);
        this.combinations = buildNewSet(true, (String word, String key) -> word.contains(key));
    }

    private HashMap<String, HashSet<String>> buildNewSet(boolean isSubstrings, BiFunction<String, String, Boolean> isIn) {
        // make a new empty set for all combinations
        HashMap<String, HashSet<String>> set = new HashMap<>();
        if (isSubstrings) {makeSubstrings(set);}
        else {makeSets(set);}

        for (String word: wordsSet) {
            for (String key: set.keySet()) {
                if (isIn.apply(word, key)) {
                    set.get(key).add(word);
                }
            }   
        }

        return set;
    }

    // used to make an empty set of each letter combo
    private void makeSets(HashMap<String, HashSet<String>> set) {
        // get the number of unique sets with the alphabets
        int numberOfSets = 1;
        for (int i = alphabet.length; i>alphabet.length-combinationLength; --i) {
            numberOfSets *= i;
        }
        
        for (int num=0; num<numberOfSets; ++num) { // turn number i into the corresponding letters
            int size = alphabet.length;
            int idx = num;
            String nextKey = "";
            while (size>alphabet.length-combinationLength) { 
                nextKey += alphabet[idx % size];
                idx = idx / size;
                --size;
            }
            set.put(nextKey, new HashSet<>());
        }
    }

    private void makeSubstrings(HashMap<String, HashSet<String>> set) {
        // get the number of unique sets with the alphabets
        int numberOfSets = (int) Math.pow(alphabet.length, combinationLength);
        
        for (int num=0; num<numberOfSets; ++num) { // turn number i into the corresponding letters
            String idx = Integer.toString(num, 26);
            idx = String.format("%1$" + combinationLength + "s", idx).replace(' ', '0'); // pad with zeros for base conversion

            String nextKey = "";
            for (char c: idx.toCharArray()) {
                nextKey += alphabet[Character.getNumericValue(c)];
            }
            set.put(nextKey, new HashSet<>());
        }
    }

    public HashMap<String, HashSet<String>> getCombinations() {
        return this.combinations;
    }
}
