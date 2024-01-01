package src.main.AI;

import src.data_structures.IndexMinPQ;

import java.util.Collections;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class AStarArrayList<T extends Branchable<T>> {
    ArrayList<Integer> from = new ArrayList<>(); // where each grid is "from" in result, used to trace path
    ArrayList<T> objects = new ArrayList<>(); // creates correspondance between indexes in IndexMinPQ and grid objects
    HashMap<T, Integer> indexes = new HashMap<>(); // TODO extra space, wasn't needed without relax
    ArrayList<Double> costTo = new ArrayList<>(); // distance for each grid to start location (index 0)
    HashSet<T> visited = new HashSet<>();
    IndexMinPQ<Double> pq = new IndexMinPQ<>(100000); // TODO fix the size issue by altering or extending IndexMinPQ class
    Integer endIndex;

    // FIXME this never terminates even when all options should be exausted. Priority queue never empties?! This is because we don't check for any equality before adding to visited. Because the hashmap is only indicies, we can't check the OBJECT as needed
    public AStarArrayList(T start, BiFunction<T, T, Double> cost, Function<T, Double> heuristic, Function<T, Boolean> isGoal) {
        objects.add(start); // set index 0 to start
        indexes.put(start, 0);
        costTo.add(0.0); // set index 0 to 0.0
        pq.insert(0, heuristic.apply(start)); // estimated total distance is just heuristic TODO this needed?
        from.add(-1); // only the root is from -1

        int currIdx = 0; // start at first object
        while (!pq.isEmpty()) {
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

    private void relax(int currIdx, T currObj, T branchObj, BiFunction<T, T, Double> cost, Function<T, Double> heuristic) { // technically redundant to have both, but it makes it easier
        if (!visited.contains(branchObj)) {
            Integer branchIdx = objects.size();
            objects.add(branchObj);
            indexes.put(branchObj, branchIdx);
            costTo.add(costTo.get(currIdx) + cost.apply(currObj, branchObj));
            from.add(currIdx);
            pq.insert(branchIdx, costTo.get(branchIdx) + heuristic.apply(branchObj));
        } 
        else { // if visited, currIdx is potential new parent to branchObj
            int branchIdx = indexes.get(branchObj);
            double newCost = costTo.get(currIdx) + cost.apply(currObj, branchObj);
            double oldCost = costTo.get(branchIdx);
            if (newCost<oldCost) {
                costTo.add(branchIdx, newCost);
                from.add(branchIdx, currIdx);
            }
        }
    }

    public ArrayList<Integer> getFrom() {
        return this.from;
    }

    public int visitedSize() {
        return visited.size();
    }

    public void connected(int idx) {
        for (int i: this.from) {
            if (from.get(i)==idx) {
                System.out.println();
                System.out.println(i);
                System.out.println(objects.get(i));
            }
        }
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


}
