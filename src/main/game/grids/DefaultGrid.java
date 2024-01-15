package src.main.game.grids;

import src.main.game.wordssets.DefaultWordsSet;

// TODO document just a nice default instead of loading scrabble words this handles it
public class DefaultGrid extends Grid {    
    public DefaultGrid() {
        super(new DefaultWordsSet());
    }
}
