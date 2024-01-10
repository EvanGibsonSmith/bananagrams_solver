package src.main.game.players.PlayerAStar;

import java.util.function.BiFunction;
import java.util.function.Function;

import src.main.AI.AStar.AStarArrayList;
import src.main.game.Grid;
import src.main.game.players.AIPlayers.BranchPlayers.AbstractBranchingPlayer;

public class PlayerAStarArrayList { // TODO fix the way generics are being done a bit to make this work. Should extend PlayerAStarable
    // TODO complete these wrapper classes
    /*final AStarArrayList astar; // TODO fix parameters
    final Function<AbstractBranchablePlayer, Double> heuristic = (p) -> (double) p.getHand().size();
    final BiFunction<AbstractBranchablePlayer, AbstractBranchablePlayer, Double> cost = (p, q) -> (double) q.getHand().size() - p.getHand().size();
    final Function<AbstractBranchablePlayer, Boolean> isGoal = (p) -> p.getHand().size()==0;

    public PlayerAStarArrayList() {
        super(new Grid(null), cost, heuristic, isGoal);
    }*/
}
