package src.main.AI;

import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;

import src.main.game.Grid;
import src.main.game.Location;
import src.main.game.WordsSet;
import src.main.game.Tile;

public class AIGrid extends Grid {

    public AIGrid(HashSet<String> wordsSet) {
        super(new AIWordsSet(wordsSet, 1));
    }

    public AIGrid(Grid g) {
        super(g);
        this.wordsSet = new AIWordsSet(g.getWordsSet(), 1); // TODO make combination length more flexible
    }

    @Override 
    public AIGrid copy() {
        return new AIGrid(this);
    }

    @Override
    public AIWordsSet getWordsSet() {
        return (AIWordsSet) super.getWordsSet();
    }
}
