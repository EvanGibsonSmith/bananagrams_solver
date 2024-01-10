package src.main.game.players.PlayerAStar;

import java.util.function.BiFunction;
import java.util.function.Function;

import src.main.AI.AStar.AbstractAStar;
import src.main.AI.Branchable;

// TODO put contract nicely (anything extending this should have a constructor with zero arguemtns)
public abstract class PlayerAStarable<T extends Branchable<T>> extends AbstractAStar<T> {
    public PlayerAStarable(T start, BiFunction<T, T, Double> cost, Function<T, Double> heuristic, Function<T, Boolean> isGoal) {
        super(start, cost, heuristic, isGoal);
    }
}
