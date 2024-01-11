package src.main.game.players.AIPlayers.BranchPlayers;
import java.util.Set;
import java.util.HashSet;

import src.main.game.TileBag;
import src.main.game.players.Hand;
import src.main.game.Game;
import src.main.game.Grid;
import src.main.game.Location;

public class BranchingPlayerSerial extends AbstractPlayerBranchingMethods<BranchingPlayerSerial> {
    
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

    public Set<BranchingPlayerSerial> branchBackwardSingleDirection(byte direction) {
        HashSet<Location> startLocations;
        Grid grid = this.getGrid();
        HashSet<Grid> foundGrids = new HashSet<>();
        foundGrids.add(grid);
        if (direction==0) {
            startLocations = grid.rightStartLocations();
        }
        else if (direction==1) {
            startLocations = grid.downStartLocations();
        }
        else {throw new IllegalArgumentException("Direction must be 0 for right or 1 for down");}
        
        Set<BranchingPlayerSerial> branchedPlayers = new HashSet<>();
        for (Location loc: startLocations) { // get starts of words to remove
            BranchingPlayerSerial nextPlayer = removeWord(loc, direction);
            if (nextPlayer!=null && !foundGrids.contains(nextPlayer.getGrid())) {
                branchedPlayers.add(nextPlayer);
                foundGrids.add(nextPlayer.getGrid());
            }
        }
        return branchedPlayers;
    }

    public Set<BranchingPlayerSerial> branchBackward() {
        Set<BranchingPlayerSerial> out = branchBackwardSingleDirection((byte) 1);
        out.addAll(branchBackwardSingleDirection((byte) 0)); // just combine both directions
        return out;
    }

    public Set<BranchingPlayerSerial> branchEmpty() {
        Set<BranchingPlayerSerial> branchedPlayers = new HashSet<>();
        for (String candidateWord: this.getGrid().getWordsSet()) {
            // below works, but may be slightly inefficient because placeWord has additional checks not needed 
            BranchingPlayerSerial nextRightPlayer = placeFirstWord(candidateWord, (byte) 0);
            if (nextRightPlayer!=null) {
                branchedPlayers.add(nextRightPlayer);
            }
            BranchingPlayerSerial nextDownPlayer = placeFirstWord(candidateWord, (byte) 1);
            if (nextDownPlayer!=null) {
                branchedPlayers.add(nextDownPlayer);
            }
        }
        return branchedPlayers;
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