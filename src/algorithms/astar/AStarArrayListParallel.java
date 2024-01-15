package src.algorithms.astar;

import java.util.function.BiFunction;
import java.util.function.Function;

import src.data_structures.DynamicIndexMinPQ;
import src.data_structures.IndexMinPQ;
import src.interfaces.Branchable;

// TODO doesn't WORK not parallelized
public class AStarArrayListParallel<T extends Branchable<T>> extends AStarArrayList<T> {
    // TODO maybe implement this to make a nice parallel A*. An extension that tracks how much has been explored could be used for meta heuristics
    public AStarArrayListParallel(Object start, BiFunction<T, T, Double> cost, Function<T, Double> heuristic, Function<T, Boolean> isGoal) {
        super((T) start, cost, heuristic, isGoal);
        this.pq = new DynamicIndexMinPQ<>();
    }

    public AStarArrayListParallel(T start, BiFunction<T, T, Double> cost, Function<T, Double> heuristic, Function<T, Boolean> isGoal, int size) {
        super(start, cost, heuristic, isGoal);
        this.pq = new IndexMinPQ<>(size); // define the set size here
    }

    public void compute() {
        objects.add(start); // set index 0 to start
        indexes.put(start, 0);
        costTo.add(0.0); // set index 0 to 0.0
        pq.insert(0, heuristic.apply(start)); // estimated total distance is just heuristic 
        from.add(-1); // only the root is from -1

        int currIdx = 0; // start at first object
        while (!pq.isEmpty()) { // TODO make this parallelized LATER
            currIdx = pq.delMin();
            T currObj = objects.get(currIdx);
            if (isGoal.apply(currObj)) {endIndex=currIdx; return;} // if goal reached we are done
            for (T branchObj: currObj.branch()) {
                relax(currIdx, currObj, branchObj, cost, heuristic);
            }
            visited.add(currObj);
        }
        endIndex = currIdx;
    }
}
