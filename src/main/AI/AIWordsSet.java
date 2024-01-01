package src.main.AI;

import src.main.game.WordsSet;

import java.util.HashSet;

public class AIWordsSet extends WordsSet {
    HashSet<String> wordsSet;
    
    public AIWordsSet(HashSet<String> set) {
        super(set);
    }

    public boolean contains(String word) {
        return wordsSet.contains(word);
    }
    
    // TODO Complete the methods to improve branching
}
