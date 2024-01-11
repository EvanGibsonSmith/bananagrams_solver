package src.main.game.players.PlayerAStar;

import java.util.function.BiFunction;
import java.util.function.Function;

import src.main.AI.AStar.AStarArrayList;
import src.main.AI.AStar.AStarFactory;
import src.main.AI.AStar.AbstractAStar;
import src.main.game.Grid;
import src.main.game.players.AIPlayers.BranchPlayers.AbstractBranchingPlayer;
import src.main.game.players.AIPlayers.BranchPlayers.BranchingPlayerFactory;

public class PlayerAStarArrayList { // TODO fix the way generics are being done a bit to make this work. Should extend PlayerAStarable
    final BiFunction<AbstractBranchingPlayer, AbstractBranchingPlayer, Double> cost = (p, q) -> (double) q.getHand().size() - p.getHand().size();   
    final Function<AbstractBranchingPlayer, Double> heuristic = (p) -> (double) p.getHand().size();
    final Function<AbstractBranchingPlayer, Boolean> isGoal = (p) -> p.getHand().size()==0;

    public PlayerAStarArrayList(AbstractBranchingPlayer branchingPlayerPrototype, AStarFactory abstractAStar) {
        /*BranchingPlayerFactory branchFactory = new BranchingPlayerFactory(branchingPlayerPrototype); // TODO put proper parameters in here
        AStarFactory branchFactory = new AStarFactory(abstractAStar, cost, heuristic, isGoal); // TODO put proper parameters in here
        super(new Grid(null), cost, heuristic, isGoal);*/
    }
}
