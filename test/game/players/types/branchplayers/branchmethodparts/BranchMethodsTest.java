package test.game.players.types.branchplayers.branchmethodparts;

import java.util.Set;

import src.data_structures.MultiSet;
import src.main.game.Tile;
import src.main.game.players.types.branchplayers.AbstractBranchingPlayer;

public class BranchMethodsTest {
    
    static MultiSet<MultiSet<String>> getAllWordsPlayed(Set<? extends AbstractBranchingPlayer> players) {
        MultiSet<MultiSet<String>> out = new MultiSet<>();
        for (AbstractBranchingPlayer player: players) {
            out.add(player.getGrid().getWordsPlayed());
        }
        return out;
    }

    static MultiSet<MultiSet<Character>> getAllHandsCharacters(Set<? extends AbstractBranchingPlayer> players) {
        MultiSet<MultiSet<Character>> out = new MultiSet<>();
        for (AbstractBranchingPlayer player: players) {
            MultiSet<Character> charSet = new MultiSet<>();
            for (Tile t: player.getHand()) {
                charSet.add(t.getLetter());
            }
            out.add(charSet);
        }
        return out;
    }
}
