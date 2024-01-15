package src.factories;

import java.lang.reflect.Constructor;

import java.util.function.BiFunction;
import java.util.function.Function;

import src.algorithms.astar.AbstractAStar;
import src.interfaces.Branchable;
import src.interfaces.Buildable;
import src.main.game.players.types.branchplayers.AbstractBranchingPlayer;
import src.main.game.players.types.branchplayers.BranchingPlayerParallel;

// TODO create a general factory abstract class? These two factories are too similar....
public class AStarFactory<T extends Branchable<T>> implements Buildable<AbstractAStar<T>> {
    private Constructor<? extends AbstractAStar<T>> constructor;
    T start;
    BiFunction<T, T, Double> cost;
    Function<T, Double> heuristic;
    Function<T, Boolean> isGoal; 

    // TODO build this with prototype design to copy a given object extending from AbstractBranchingPlayer
    // TODO create an immutable version and a version that can change player seperately? If so maybe that should be static
    public AStarFactory(Class<? extends AbstractAStar<T>> clazz,
                        T start,
                        BiFunction<T, T, Double> cost, 
                        Function<T, Double> heuristic, 
                        Function<T, Boolean> isGoal) throws  Exception {
        // get constructor for the unknown extending class and inject player dependencies
        // note we know Object will be type T
        // TODO fix this bug causing test to fail
        // TODO fix AbstractBranchingPlayer hardcoded ing
        // TODOd delete this it's for debugging
        constructor = clazz.getDeclaredConstructor(new Class[] {Object.class, BiFunction.class, Function.class, Function.class});

        // save injected dependencies for builds TODO should this be a one time build factory or should we just use it that way? Seems like possible overkill
        this.start = start;
        this.cost = cost;
        this.heuristic = heuristic;
        this.isGoal = isGoal;
    }

    // TODO fix this up so it actually builds well and takes in build parameters
    public AbstractAStar<T> build() throws Exception {
        return constructor.newInstance(start, cost, heuristic, isGoal); // TODO fix generics so this can be nicer.
    }

    public void setStart(T newStart) {start = newStart;}
}
