package src.main.game.AIplayerwrappers;

import java.util.function.BiFunction;
import java.util.function.Function;

import src.algorithms.astar.AbstractAStar;
import src.factories.AStarFactory;
import src.main.game.players.types.branchplayers.AbstractBranchingPlayer;

// TODO could this extend player instead of composition? Is that a good choice?
// TODO rename from AIPlayer since this doesn't extend player?
// TODO create an interface for some of the player methods this is required to implement
// TODO make abstract since it doesn't handle the broker in any way?
public class AIPlayerWrapper {
    // TODO make the cost and heuristic final by assigning them within a private blank constructor 
    protected BiFunction<AbstractBranchingPlayer, AbstractBranchingPlayer, Double> cost = (p, q) -> (double) q.getHand().size() - p.getHand().size();   
    protected Function<AbstractBranchingPlayer, Double> heuristic = (p) -> (double) p.getHand().size(); // TODO made this not final for potential setting in extending classes. Better way?
    protected Function<AbstractBranchingPlayer, Boolean> isGoal = (p) -> p.getHand().size()==0;

    protected AStarFactory<AbstractBranchingPlayer> aStarFactory;
    protected AbstractAStar<AbstractBranchingPlayer> aStar; // specific class for implementation of these is given as parameter
    protected AbstractBranchingPlayer player; // the player created (type determines how branch works)

    // TODO the aStarClass should extend AbstractAStar<AbstractBranchingPlayer> but passing generics 
    // in classes doesn't really work. If possible try and find a workaround.
    public AIPlayerWrapper(Class<? extends AbstractAStar> astarClass, AbstractBranchingPlayer player) 
                        throws Exception {
        this.player = player;
        // because of this generic issue for passing classes cast to proper type here.
        aStarFactory = new AStarFactory<AbstractBranchingPlayer>((Class<? extends AbstractAStar<AbstractBranchingPlayer>>) astarClass, player, cost, heuristic, isGoal);
        aStar = aStarFactory.build();
    }

    // REWORK/ Don't deal with factory and class types when just passing a player is easier for htat
    /*private void setup(Class<? extends AbstractBranchingPlayer> branchingPlayerClass, 
                       Class<? extends AbstractAStar<AbstractBranchingPlayer>> astarClass,
                       Game game, Grid grid, NormalTileBag bag) throws Exception {
        BranchingPlayerFactory branchingPlayerFactory = new BranchingPlayerFactory(branchingPlayerClass, game, grid, bag);
        player = branchingPlayerFactory.build();
        aStarFactory = new AStarFactory<AbstractBranchingPlayer>(astarClass, player, cost, heuristic, isGoal);
        aStar = aStarFactory.build();
    }*/
    
    // TODO delete below constructor's if it works out better the other way
    /*public AIPlayer(Class<? extends AbstractBranchingPlayer> branchingPlayerClass, 
                    Class<? extends AbstractAStar> astarClass,
                    Game game, HashSet<String> words, NormalTileBag bag) throws Exception {
        Grid grid = new Grid(words);
        setup(branchingPlayerClass, (Class<? extends AbstractAStar<AbstractBranchingPlayer>>) astarClass, game, grid, bag);
    }

    public AIPlayer(Class<? extends AbstractBranchingPlayer> branchingPlayerClass, 
                    Class<? extends AbstractAStar<AbstractBranchingPlayer>> astarClass,
                    Game game, Grid grid, NormalTileBag bag) throws Exception {
        setup(branchingPlayerClass, astarClass, game, grid, bag);
    }*/

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
