package test.AI.AIPlayer;

import java.util.Set;

import src.data_structures.MultiSet;
import src.main.game.Tile;
import src.main.game.players.PlayerAI.AbstractAIPlayer;

public class BranchTestMethods {
    
    static MultiSet<MultiSet<String>> getAllWordsPlayed(Set<? extends AbstractAIPlayer> players) {
        MultiSet<MultiSet<String>> out = new MultiSet<>();
        for (AbstractAIPlayer player: players) {
            out.add(player.getGrid().getWordsPlayed());
        }
        return out;
    }

    static MultiSet<MultiSet<Character>> getAllHandsCharacters(Set<? extends AbstractAIPlayer> players) {
        MultiSet<MultiSet<Character>> out = new MultiSet<>();
        for (AbstractAIPlayer player: players) {
            MultiSet<Character> charSet = new MultiSet<>();
            for (Tile t: player.getHand()) {
                charSet.add(t.getLetter());
            }
            out.add(charSet);
        }
        return out;
    }
}
