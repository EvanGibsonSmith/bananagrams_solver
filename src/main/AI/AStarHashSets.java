package src.main.AI;

import src.data_structures.IndexMinPQ;

import java.util.Collections;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class AStarHashSets<T extends Branchable<T>> {
    HashMap<Integer, Integer> from = new HashMap<>(); // where each grid is "from" in result, used to trace path
    HashMap<Integer, T> objects = new HashMap<>(); // creates correspondance between indexes in IndexMinPQ and grid objects
    HashMap<T, Integer> indexes = new HashMap<>(); // TODO extra space, wasn't needed without relax
    HashMap<Integer, Double> costTo = new HashMap<>(); // distance for each grid to start location (index 0)
    HashSet<T> visited = new HashSet<>();
    IndexMinPQ<Double> pq = new IndexMinPQ<>(100000); // TODO fix the size issue by altering or extending IndexMinPQ class
    Integer endIndex = null;

    public AStarHashSets(T start, BiFunction<T, T, Double> cost, Function<T, Double> heuristic, Function<T, Boolean> isGoal) {
        objects.put(0, start); // set index 0 to start
        indexes.put(start, 0); // set index 0 to start
        costTo.put(0, 0.0); // set index 0 to 0.0
        pq.insert(objects.size()-1, heuristic.apply(start)); // estimated total distance is just heuristic TODO this needed?
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
    }

    private void relax(int currIdx, T currObj, T branchObj, BiFunction<T, T, Double> cost, Function<T, Double> heuristic) { // technically redundant to have both, but it makes it easier
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

    public HashMap<Integer, Integer> getFrom() {
        return this.from;
    }

    public int visitedSize() {
        return visited.size();
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
