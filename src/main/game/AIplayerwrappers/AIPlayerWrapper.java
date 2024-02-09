package src.main.game.AIplayerwrappers;

import java.util.function.BiFunction;
import java.util.function.Function;

import src.algorithms.astar.AbstractAStar;
import src.factories.AStarFactory;
import src.main.game.players.types.branchplayers.AbstractBranchingPlayer;

abstract public class AIPlayerWrapper {
    // TODO are these default values good practice?
    protected BiFunction<AbstractBranchingPlayer, AbstractBranchingPlayer, Double> cost;   
    protected Function<AbstractBranchingPlayer, Double> heuristic;
    protected Function<AbstractBranchingPlayer, Boolean> isGoal;

    protected AStarFactory<AbstractBranchingPlayer> aStarFactory;
    protected AbstractAStar<AbstractBranchingPlayer> aStar; // specific class for implementation of these is given as parameter
    protected AbstractBranchingPlayer player; // the player created (type determines how branch works)

    public AIPlayerWrapper(Class<? extends AbstractAStar> astarClass, AbstractBranchingPlayer player,
                           BiFunction<AbstractBranchingPlayer, AbstractBranchingPlayer, Double> cost, 
                           Function<AbstractBranchingPlayer, Double> heuristic, 
                           Function<AbstractBranchingPlayer, Boolean> isGoal) 
        throws Exception {
        // TODO does this need to be stored in the class? Probably doesn't hurt
        this.cost = cost;
        this.heuristic = heuristic;
        this.isGoal = isGoal;

        this.player = player;
        // set up a star with these costs and heuristic
        this.aStarFactory = new AStarFactory<AbstractBranchingPlayer>((Class<? extends AbstractAStar<AbstractBranchingPlayer>>) astarClass, player, cost, heuristic, isGoal);
    }

    // TODO add other appropriate methods here for extending classes like CheatPlayer. Maybe create a player interface instead of just the abstract object to enforce this?
    public void grabTile() {
        player.grabTile();
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
        try {
            aStar = aStarFactory.build(); // build new a* from this player
        }
        catch (Exception e) {
            e.printStackTrace(); // TODO make these try and catches nicer
        }
        aStar.compute();
        return aStar.getGoal();
    }

    // TODO is this reasonable or a bad workaround when public?
    private void setStart() {
        aStarFactory.setStart(player);
        // make astar attached to this new player TODO instead of creating new object
    }

    public void playSolution() {
        player = solveGrid();
        // TODO is building another A* good? It's not that inefficient since this isn't involved 
        // in the branching actions and it makes sense for A* to not be able to change
        setStart();
        // not building new a star so info from this one cam be seen
    }
}
