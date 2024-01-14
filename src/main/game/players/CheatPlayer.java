package src.main.game.players;

import src.main.AI.AStar.AbstractAStar;
import src.main.game.players.AIPlayers.AIPlayer;
import src.main.game.players.AIPlayers.BranchPlayers.AbstractBranchingPlayer;

// CheatPlayer essentially just switches out the CheatBroker object for broker used when interacting with a real life game
public class CheatPlayer extends AIPlayer {
    
    // TOOD maybe CheatPlayer can use the branching factory to create a player for AIPlayer in this constructor
    /*public CheatPlayer(Class<? extends AbstractBranchingPlayer> branchingPlayerClass, 
                       Class<? extends AbstractAStar<AbstractBranchingPlayer>> astarClass, // TODO fix generics. This helped passing in arguments though
                       HashSet<String> words) throws Exception {
        super(branchingPlayerClass, astarClass, null, null, null);
        // no game needed since CheatPlayer is for real life games, no bag since we override broker
        //super(branchingPlayerClass, astarClass, null, words, null);  
        // TODO bad to "Reach" into player to swtich out Broker when player should be high level anyway
        //getPlayer().broker = new CheatBroker(new Hand()); // reach in and change broker type 
    }*/

    public CheatPlayer(Class<? extends AbstractAStar<AbstractBranchingPlayer>> aStarClass, AbstractBranchingPlayer player) 
            throws Exception {
        super(aStarClass, player);
    }

    /*
     * TODO document
     * The broker can be accessed in this player because
     * the tiles should represent the tiles the tiles the player
     * got in a real life game, so they can control tile flow.
     * This broker is used to control the player and A*
     */
    public CheatBroker getBroker() {
        return (CheatBroker) getPlayer().broker;
    }

    // TODO maybe make the methods accessed here do through a more develeped AIPlayer instead of just grabbing the object itself
    public AbstractAStar<AbstractBranchingPlayer> getAStar() {
        return super.aStar;
    }
}
