package src.main.game.players.AIPlayers;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.HashSet;

import src.main.AI.AStar.AStarFactory;
import src.main.AI.AStar.AbstractAStar;
import src.main.game.players.AIPlayers.BranchPlayers.AbstractBranchingPlayer;
import src.main.game.players.AIPlayers.BranchPlayers.BranchingPlayerFactory;
import src.main.game.Game;
import src.main.game.Grid;
import src.main.game.TileBag;

// TODO could this extend player instead of composition? Is that a good choice?
// TODO rename from AIPlayer since this doesn't extend player?
public class AIPlayer {
    private final BiFunction<AbstractBranchingPlayer, AbstractBranchingPlayer, Double> cost = (p, q) -> (double) q.getHand().size() - p.getHand().size();   
    private final Function<AbstractBranchingPlayer, Double> heuristic = (p) -> (double) p.getHand().size();
    private final Function<AbstractBranchingPlayer, Boolean> isGoal = (p) -> p.getHand().size()==0;

    private AStarFactory aStarFactory;
    private AbstractAStar<AbstractBranchingPlayer> aStar; // specific class for implementation of these is given as parameter
    private AbstractBranchingPlayer player; // the player created (type determines how branch works)

    private void setup(Class<? extends AbstractBranchingPlayer> branchingPlayerClass, 
                    Class<? extends AbstractAStar<AbstractBranchingPlayer>> astarClass,
                    Game game, Grid grid, TileBag bag) throws Exception {
        BranchingPlayerFactory branchingPlayerFactory = new BranchingPlayerFactory(branchingPlayerClass, game, grid, bag);
        player = branchingPlayerFactory.build();

        AStarFactory aStarFactory = new AStarFactory(astarClass, player, cost, heuristic, isGoal);
        aStar = aStarFactory.build();
    }

    public AIPlayer(Class<? extends AbstractBranchingPlayer> branchingPlayerClass, 
                    Class<? extends AbstractAStar<AbstractBranchingPlayer>> astarClass,
                    Game game, HashSet<String> words, TileBag bag) throws Exception {
        Grid grid = new Grid(words);
        setup(branchingPlayerClass, astarClass, game, grid, bag);
    }

    public AIPlayer(Class<? extends AbstractBranchingPlayer> branchingPlayerClass, 
                    Class<? extends AbstractAStar<AbstractBranchingPlayer>> astarClass,
                    Game game, Grid grid, TileBag bag) throws Exception {
        setup(branchingPlayerClass, astarClass, game, grid, bag);
    }

    /*
     * TODO DOCUMENT
     * Player methods can be accessed through this
     */
    public AbstractBranchingPlayer getPlayer() {return player;} // TODO type is known, might just have to be cast though

    /*
     * TODO DOCUMENT
     * Solves the grid the player has, returns resulting player but does not update this player.
     * Computers A*, so quite slow TODO mention A* storage on compute when done
     */
    public AbstractBranchingPlayer solveGrid() {
        aStar.compute();
        return aStar.getGoal();
    }

    public void playSolution() throws Exception {
        player = solveGrid();
        // TODO is building another A* good? It's not that inefficient since this isn't involved 
        // in the branching actions and it makes sense for A* to not be able to change
        aStarFactory.setStart(player); // make astar attached to this new player TODO instead of creating new object
        aStar = aStarFactory.build(); // build new a* from this player
    }
}
