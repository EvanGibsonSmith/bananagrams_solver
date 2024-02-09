package src.main.game.AIplayerwrappers;

import java.util.function.BiFunction;
import java.util.function.Function;

import src.main.game.players.types.branchplayers.AbstractBranchingPlayer;

// TODO document
public class AIPlayerAStarFunctions {
    
    public static Function<AbstractBranchingPlayer, Boolean> emptyHandGoal() {
        return (p) -> p.getHand().size()==0;
    }


    public static BiFunction<AbstractBranchingPlayer, AbstractBranchingPlayer, Double> handSizeCost() {
        return (p, q) -> (double) q.getHand().size() - p.getHand().size();
    }

    public static Function<AbstractBranchingPlayer, Double> handSizeHeuristic() {
        return (p) -> (double) p.getHand().size();
    }


    public static BiFunction<AbstractBranchingPlayer, AbstractBranchingPlayer, Double> handScrabbleCost() {
        return null; // TODO STUB
    }

    public static Function<AbstractBranchingPlayer, Double> handScrabbleHeuristic() {
        return null; // TODO STUB
    }
}
