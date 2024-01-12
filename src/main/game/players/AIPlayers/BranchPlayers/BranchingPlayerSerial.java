package src.main.game.players.AIPlayers.BranchPlayers;
import java.util.Set;
import java.util.HashSet;

import src.main.game.TileBag;
import src.main.game.players.Hand;
import src.main.game.Game;
import src.main.game.Grid;
import src.main.game.Location;

public class BranchingPlayerSerial extends AbstractPlayerBranchingMethods {
    
    public BranchingPlayerSerial(Game game, Grid grid, TileBag bag) {
        super(game, grid, bag);
    }

    public BranchingPlayerSerial(Game game, Grid grid, TileBag bag, Hand hand) {
        super(game, grid, bag, hand);
    }

    public BranchingPlayerSerial copy() {
        return new BranchingPlayerSerial(getGame(), new Grid(getGrid()), (TileBag) getBag(), this.getHand().copy());
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