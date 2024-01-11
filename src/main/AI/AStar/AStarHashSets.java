package src.main.AI.AStar;

import src.data_structures.DynamicIndexMinPQ;
import src.data_structures.IndexMinPQ;
import src.main.AI.Branchable;

import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

// TODO should we store the computed solution so it doesn't have to be recomputed?? Might be nice, but necessary?
public class AStarHashSets<T extends Branchable<T>> extends AbstractAStar<T> {
    HashMap<Integer, Integer> from = new HashMap<>(); // where each grid is "from" in result, used to trace path
    HashMap<Integer, T> objects = new HashMap<>(); // creates correspondance between indexes in IndexMinPQ and grid objects
    HashMap<T, Integer> indexes = new HashMap<>();
    HashSet<T> visited = new HashSet<>();
    Integer endIndex = null;
    HashMap<Integer, Double> costTo = new HashMap<>(); // distance for each grid to start location (index 0)
    IndexMinPQ<Double> pq; // will be fixed size in constructor if size given
    
    public AStarHashSets(T start, BiFunction<T, T, Double> cost, Function<T, Double> heuristic, Function<T, Boolean> isGoal) {
        super(start, cost, heuristic, isGoal);
        this.pq = new DynamicIndexMinPQ<>();
    }

    public AStarHashSets(T start, BiFunction<T, T, Double> cost, Function<T, Double> heuristic, Function<T, Boolean> isGoal, int size) {
        super(start, cost, heuristic, isGoal);
        this.pq = new IndexMinPQ<>(size); // define the set size here
    }

    protected void relax(int currIdx, T currObj, T branchObj, BiFunction<T, T, Double> cost, Function<T, Double> heuristic) {
        if (!visited.contains(branchObj)) {
            Integer branchIdx = objects.size();
            objects.put(branchIdx, branchObj);
            indexes.put(branchObj, branchIdx);
            costTo.put(branchIdx, costTo.get(currIdx) + cost.apply(currObj, branchObj));
            from.put(branchIdx, currIdx);
            pq.insert(branchIdx, costTo.get(branchIdx) + heuristic.apply(branchObj));
        } 
        else { // if visited, currIdx is potential new parent to branchObj
            int branchIdx = indexes.get(branchObj);
            double newCost = costTo.get(currIdx) + cost.apply(currObj, branchObj);
            double oldCost = costTo.get(branchIdx);
            if (newCost<oldCost) {
                costTo.put(branchIdx, newCost);
                from.put(branchIdx, currIdx);
            }
        }
    }

    // TODO DRY between these two? Create a small helper class for them both to use this
    // this method runs in all of the constructors for this method and performs the A*. 
    // it is not in the constructor itself since the type and size of pq needs to be defined before this runs
    // and a call to another constructor must be the first line of another constuctor.
    public void compute() {
        objects.put(0, start); // set index 0 to start
        indexes.put(start, 0); // set index 0 to start
        costTo.put(0, 0.0); // set index 0 to 0.0
        pq.insert(objects.size()-1, heuristic.apply(start)); // estimated total distance is just heuristic
        from.put(0, -1); // only the root is from -1

        int currIdx = 0; // start at first object
        while (!pq.isEmpty()) {
            T currObj = objects.get(currIdx);
            if (isGoal.apply(currObj)) {endIndex=currIdx; return;} // if goal reached we are done
            for (T branchObj: currObj.branch()) {
                relax(currIdx, currObj, branchObj, cost, heuristic);
            }
            visited.add(currObj); // even if already visited, we can add this index
            currIdx = pq.delMin();
        }
        endIndex = currIdx;
    }

    public HashMap<Integer, Integer> getFrom() {return this.from;}

    public int visitedSize() {
        return this.visited.size();
    }

    public ArrayList<T> getPath() {
        if (endIndex==null) {return null;}
        ArrayList<T> path = new ArrayList<>();
        for (int idx=endIndex; idx!=-1; idx=from.get(idx)) {
            path.add(objects.get(idx));
    }

        Collections.reverse(path);
        return path;
    }

    public void connected(int idx) {
        for (int i: this.from.keySet()) {
            if (from.get(i)==idx) {
                System.out.println();
                System.out.println(i);
                System.out.println(objects.get(i));
            }
        }
    }

    public T getGoal() {
        return this.objects.get(this.endIndex);
    }
}
