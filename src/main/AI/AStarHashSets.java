package src.main.AI;

import src.data_structures.IndexMinPQ;

import java.util.Collections;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class AStarHashSets<T extends Branchable<T>> {
    HashMap<Integer, Integer> from = new HashMap<>(); // where each grid is "from" in result, used to trace path
    HashMap<Integer, T> objects = new HashMap<>(); // creates correspondance between indexes in IndexMinPQ and grid objects
    HashMap<Integer, Double> costTo = new HashMap<>(); // distance for each grid to start location (index 0)
    HashSet<Integer> visited = new HashSet<>();
    IndexMinPQ<Double> pq = new IndexMinPQ<>(100000); // TODO fix the size issue by altering or extending IndexMinPQ class
    Integer endIndex;

    // FIXME this never terminates even when all options should be exausted. Priority queue never empties?! This is because we don't check for any equality before adding to visited. Because the hashmap is only indicies, we can't check the OBJECT as needed
    public AStarHashSets(T start, BiFunction<T, T, Double> cost, Function<T, Double> heuristic, Function<T, Boolean> isGoal) {
        objects.put(0, start); // set index 0 to start
        costTo.put(0, 0.0); // set index 0 to 0.0
        pq.insert(objects.size()-1, heuristic.apply(start)); // estimated total distance is just heuristic
        from.put(0, -1); // only the root is from -1

        int currIndex = 0; // start at first object
        while (!isGoal.apply(objects.get(currIndex))) {
            T currObj = objects.get(currIndex);
            if (!visited.contains(currIndex)) {
                visited.add(currIndex);
                for (T branchObj: currObj.branch()) { 
                    // TODO all this stuff shouldn't be in branch it should be up BEFORE branch.
                    int branchIndex = objects.size();
                    objects.put(branchIndex, branchObj); // new object at next index
                    costTo.put(branchIndex, costTo.get(currIndex) + cost.apply(currObj, branchObj));
                    from.put(branchIndex, currIndex);
                    pq.insert(branchIndex, costTo.get(currIndex) + heuristic.apply(currObj));
                }
            }
            else {
                // TODO implement, but this isn't critical right now
                //relax();
                //pq.decreaseKey();
            }
            currIndex = pq.delMin();
        }
        endIndex = currIndex;
    }

    public HashMap<Integer, Integer> getFrom() {
        return this.from;
    }

    public int visitedSize() {
        return visited.size();
    }
    
    public ArrayList<T> getPath() {
        ArrayList<T> path = new ArrayList<>();
        for (int idx=endIndex; idx!=-1; idx=from.get(idx)) {
            path.add(objects.get(idx));
        }

        Collections.reverse(path);
        return path;
    }


}
