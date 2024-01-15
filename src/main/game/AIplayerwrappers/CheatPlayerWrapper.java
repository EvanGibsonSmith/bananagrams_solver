package src.main.game.AIplayerwrappers;

import src.algorithms.astar.AbstractAStar;
import src.main.game.players.brokers.CheatBroker;
import src.main.game.players.types.branchplayers.AbstractBranchingPlayer;

// CheatPlayer essentially just switches out the CheatBroker object for broker used when interacting with a real life game
public class CheatPlayerWrapper extends AIPlayerWrapper {

    // TODO AbstractAStar has no generic so class type can be passed more easily. This ok?
    // TODO need to make an actual CheatPlayer that has the weird broker?
    public CheatPlayerWrapper(Class<? extends AbstractAStar> aStarClass, AbstractBranchingPlayer player) 
            throws Exception {
        super((Class<? extends AbstractAStar<AbstractBranchingPlayer>>) aStarClass, player);
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
