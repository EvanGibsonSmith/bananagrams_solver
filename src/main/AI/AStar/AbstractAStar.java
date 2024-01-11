package src.main.AI.AStar;

import src.main.AI.Branchable;

import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.ArrayList;

public abstract class AbstractAStar<T extends Branchable> { // TODO FIX GENERICS
    T start;
    BiFunction<T, T, Double> cost; 
    Function<T, Double> heuristic; 
    Function<T, Boolean> isGoal;

    public AbstractAStar(T start, BiFunction<T, T, Double> cost, Function<T, Double> heuristic, Function<T, Boolean> isGoal) {
        this.start = start;
        this.cost = cost;
        this.heuristic = heuristic;
        this.isGoal = isGoal;
    }

    abstract public void compute();

    abstract public int visitedSize();
    
    abstract public ArrayList<T> getPath();

    abstract public T getGoal();

}
