package src.main.AI;

import java.util.HashSet;
import java.util.Set;

import src.main.game.Grid;
import src.main.game.Location;

public class WordSearchGrid extends Grid {
    public WordSearchGrid(HashSet<String> wordsSet) {
        super(wordsSet);
    }
    // TODO useless now I think. The idea of an AI Grid may be more useful later?
}
