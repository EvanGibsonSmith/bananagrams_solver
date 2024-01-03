package test.AI.AIPlayer;

import java.util.Set;

import src.data_structures.MultiSet;
import src.main.AI.AIPlayer;
import src.main.game.Tile;

public class BranchTestMethods {
    
    static MultiSet<MultiSet<String>> getAllWordsPlayed(Set<? extends AIPlayer> players) {
        MultiSet<MultiSet<String>> out = new MultiSet<>();
        for (AIPlayer player: players) {
            out.add(player.getGrid().getWordsPlayed());
        }
        return out;
    }

    static MultiSet<MultiSet<Character>> getAllHandsCharacters(Set<? extends AIPlayer> players) {
        MultiSet<MultiSet<Character>> out = new MultiSet<>();
        for (AIPlayer player: players) {
            MultiSet<Character> charSet = new MultiSet<>();
            for (Tile t: player.getHand()) {
                charSet.add(t.getLetter());
            }
            out.add(charSet);
        }
        return out;
    }
}
