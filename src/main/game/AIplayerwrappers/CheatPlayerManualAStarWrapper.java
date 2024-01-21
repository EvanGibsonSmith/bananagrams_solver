package src.main.game.AIplayerwrappers;

import java.util.function.Function;
import java.util.function.BiFunction;

import src.algorithms.astar.AbstractAStar;
import src.main.game.players.brokers.CheatBroker;
import src.main.game.players.types.branchplayers.AbstractBranchingPlayer;

// TODO have this be a super class of CheatPlayer so that CheatPlayer can just be a specific heuristic of this?
// CheatPlayer essentially just switches out the CheatBroker object for broker used when interacting with a real life game
public class CheatPlayerManualAStarWrapper extends CheatPlayerWrapper {

    // TODO AbstractAStar has no generic so class type can be passed more easily. This ok?
    public CheatPlayerManualAStarWrapper(Class<? extends AbstractAStar> aStarClass, 
                                       AbstractBranchingPlayer player,
                                       Function<AbstractBranchingPlayer, Double> heuristic,
                                       BiFunction<AbstractBranchingPlayer, AbstractBranchingPlayer, Double> cost) 
                    throws Exception {
        super((Class<? extends AbstractAStar<AbstractBranchingPlayer>>) aStarClass, player);
        super.heuristic = heuristic;
        super.cost = cost;
    }

    /*
     * TODO document
     * The broker can be accessed in this player because
     * the tiles should represent the tiles the tiles the player
     * got in a real life game, so they can control tile flow.
     * This broker is used to control the player and A*
     */
    public CheatBroker getBroker() {
        return (CheatBroker) getPlayer().getBroker();
    }

    // TODO maybe make the methods accessed here do through a more develeped AIPlayer instead of just grabbing the object itself
    public AbstractAStar<AbstractBranchingPlayer> getAStar() {
        return super.aStar;
    }
}
