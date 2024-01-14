package src.main.game.players.AIPlayers.BranchPlayers;
import java.util.Set;

import src.main.game.players.AbstractBroker;
import src.main.game.Game;
import src.main.game.Grid;

// TODO eventually create an extension of this that has more "Normal" player constructors for the tests that works without explicitly passing in the broker?
public class BranchingPlayerSerial extends AbstractPlayerBranchingMethods<BranchingPlayerSerial> {

    public BranchingPlayerSerial(Game game, Grid grid, AbstractBroker broker) {
        super(game, grid, broker, false);
    }

    public Set<BranchingPlayerSerial> branchForward() {
        Set<BranchingPlayerSerial> out = branchForwardSingleDirection((byte) 1);
        out.addAll(branchForwardSingleDirection((byte) 0)); // just combine both directions
        return out;
    }
    public Set<BranchingPlayerSerial> branchBackward() {
        Set<BranchingPlayerSerial> out = branchBackwardSingleDirection((byte) 1);
        out.addAll(branchBackwardSingleDirection((byte) 0)); // just combine both directions
        return out;
    }

    @Override
    public Set<BranchingPlayerSerial> branch() {
        if (this.getGrid().isEmpty()) {
            return branchEmpty(); // edge case for when the grid is empty
        }
        Set<BranchingPlayerSerial> out = branchForward();
        out.addAll(branchBackward()); // just combine both directions
        return out;
    }
}