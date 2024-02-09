package test.game.AIplayerwrappers;

import org.junit.jupiter.api.Test;

import src.algorithms.astar.AStarArrayList;
import src.interfaces.TileBagable;
import src.main.game.AIplayerwrappers.AIPlayerWrapper;
import src.main.game.AIplayerwrappers.AIPlayerWrapperDefaultHeuristic;
import src.main.game.grids.DefaultGrid;
import src.main.game.players.bags.DefaultTileBag;
import src.main.game.players.brokers.AbstractBroker;
import src.main.game.players.brokers.HumanBroker;
import src.main.game.players.hand.Hand;
import src.main.game.players.types.branchplayers.AbstractBranchingPlayer;
import src.main.game.players.types.branchplayers.BranchingPlayerSerial;

public class AIPlayerTimeBenchmarkTests {
    AIPlayerWrapper player;

    @Test
    void runOneHand() throws Exception {
        final int HAND_SIZE = 40;
        // TODO NUM_PLAYERS has some kind of bug because it works when I remove it
        final boolean VERBOSE = true; // if the grids at each step will be printed out
        // setup player
        AbstractBroker broker = new HumanBroker(new Hand(), new DefaultTileBag()); // TODO create DefaultHumanBroker?
        AbstractBranchingPlayer playerPart = new BranchingPlayerSerial(null, new DefaultGrid(), broker);
        player = new AIPlayerWrapperDefaultHeuristic(AStarArrayList.class, playerPart); // game not needed for this test
        TileBagable bag = player.getPlayer().getBag();
        for (int i=0; i<HAND_SIZE; ++i) { // TODO make a setup hand or something in player?
            player.grabTile();
        }

        // play until bag is empty.
        while (!bag.isEmpty()) {
            long startTime = System.currentTimeMillis();
            player.playSolution();
            if (VERBOSE) {
                System.out.println("Time for this step: " + (System.currentTimeMillis() - startTime));
                System.out.println(player.getPlayer().getGrid());
            }
            // hand out a tile to tile player and remove tiles from bag for others
            player.grabTile();
            // TODO some bug in commented out block
            /*if (VERBOSE) {
                System.out.println("Hand: " + player.getPlayer().getHand());
            }*/
        }
    }
}
