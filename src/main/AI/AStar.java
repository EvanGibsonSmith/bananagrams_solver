package src.main.AI;

import src.data_structures.IndexMinPQ;

import java.util.function.Function;
import java.util.ArrayList;

public class AStar<T extends Branchable<T>, K extends Comparable<K>> {
    ArrayList<T> from = new ArrayList<T>();

    public AStar(T start, Function<T, K> heuristic) {
        // TODO COMPLETE STUB (this constructor will perform the search)
        start.branch();
    }
}
