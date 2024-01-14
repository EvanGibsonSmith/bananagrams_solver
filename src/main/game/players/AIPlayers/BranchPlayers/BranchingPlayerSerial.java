package src.main.game.players.AIPlayers.BranchPlayers;
import java.util.Set;
import java.util.HashSet;

import src.main.game.NormalTileBag;
import src.main.game.players.Hand;
import src.main.game.Game;
import src.main.game.Grid;
import src.main.game.Location;

public class BranchingPlayerSerial extends AbstractPlayerBranchingMethods {

    // TODO is no game constructor bad? maybe a dummy game object would be better
    public BranchingPlayerSerial(Grid grid, NormalTileBag bag) {
        super(grid, bag);
    }

    // TODO another stupid constructor without game
    public BranchingPlayerSerial(Grid grid, NormalTileBag bag, Hand hand) {
        super(grid, bag, hand);
    }

    public BranchingPlayerSerial(Game game, Grid grid, NormalTileBag bag) {
        super(game, grid, bag);
    }

    public BranchingPlayerSerial(Game game, Grid grid, NormalTileBag bag, Hand hand) {
        super(game, grid, bag, hand);
    }

    // TODO doesn't work in the case of GAME being null so I'm changing it for this purpose, although dummy game might be nice
    public BranchingPlayerSerial copy() {
        return new BranchingPlayerSerial(new Grid(getGrid()), (NormalTileBag) getBag(), this.getHand().copy());
    }

    public BranchingPlayerSerial realCopy() { // TODO THE ACTUAL COPY THATS GOOD AND WORKS BUT OTHER ONE NEEDED FOR BRANCHING TO WORK RIHGT NOW
        return new BranchingPlayerSerial(getGame(), new Grid(getGrid()), (NormalTileBag) getBag(), this.getHand().copy());
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