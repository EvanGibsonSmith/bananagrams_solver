package src.algorithms.astar;

import java.util.function.BiFunction;
import java.util.function.Function;

import src.interfaces.Branchable;

// Like AStarArrayList but tracks the number of states 

public class AStarArrayListStats<T extends Branchable<T>> extends AStarArrayList<T> {

    public AStarArrayListStats(Object start, BiFunction<T, T, Double> cost, Function<T, Double> heuristic, Function<T, Boolean> isGoal) {
        super((T) start, cost, heuristic, isGoal);
    }

    public int getDepth() {
        return getPath().size();
    }

    public int getNumVisitedStates() {
        return visited.size();
    }

    public double getApproximateEffectiveBranchingFactor() {
        if (!solved()) {return -1.0;}
        // approximate formula for effective branching factor 
        // Artificial Intelligence A Modern Approach Third Edition, Stuart J. Russell and Peter Norvig, Section 3.6
        return Math.pow(getNumVisitedStates()/((double) getDepth()), 1/((double) getDepth())); 
    }
}
