package src.main.AI.AStar;

import java.lang.reflect.Constructor;

import java.util.function.BiFunction;
import java.util.function.Function;

import src.main.Buildable;
import src.main.AI.Branchable;

// TODO create a general factory abstract class? These two factories are too similar....
public class AStarFactory<T extends Branchable<T>> implements Buildable<AbstractAStar<T>> {// TODO complete. Make static?
    private Constructor<? extends AbstractAStar<T>> constructor;
    BiFunction<T, T, Double> cost;
    Function<T, Double> heuristic;
    Function<T, Boolean> isGoal; 

    // TODO build this with prototype design to copy a given object extending from AbstractBranchingPlayer

    public AStarFactory(Class<? extends AbstractAStar<T>> clazz,
                        BiFunction<T, T, Double> cost, 
                        Function<T, Double> heuristic, 
                        Function<T, Boolean> isGoal) throws Exception {
        // get constructor for the unknown extending class and inject player dependencies
        constructor = clazz.getDeclaredConstructor(new Class<?>[] {BiFunction.class, Function.class, Function.class}); 

        // save injected dependencies for builds TODO should this be a one time build factory or should we just use it that way? Seems like possible overkill
        this.cost = cost;
        this.heuristic = heuristic;
        this.isGoal = isGoal;
    }

    // TODO fix this up so it actually builds well and takes in build parameters
    public AbstractAStar<T> build() throws Exception {
        return constructor.newInstance(cost, heuristic, isGoal); // TODO fix generics so this can be nicer.
    }
}
