package src.main.game.wordssets;

import java.util.HashSet;
import java.util.Iterator;
import java.util.stream.Stream;

import src.interfaces.Copyable;

// TODO make this class not public so it can be tied tightly to the Grid object, can be set up with HashSet
public class WordsSet implements Iterable<String>, Copyable<WordsSet> {
    protected HashSet<String> wordsSet;

    public WordsSet() {
        this.wordsSet = new HashSet<>();
    }

    public WordsSet(HashSet<String> set) {
        this.wordsSet = set;
    }
    
    // deep copy of the wordsSet
    public WordsSet copy() {
        return new WordsSet(new HashSet<String>(this.wordsSet));
    }
    
    @Override
    public Iterator<String> iterator() {
        return this.wordsSet.iterator();
    }

    public Stream<String> stream() {
        return this.wordsSet.stream();
    }

    public boolean contains(String word) {
        return wordsSet.contains(word);
    }
}
