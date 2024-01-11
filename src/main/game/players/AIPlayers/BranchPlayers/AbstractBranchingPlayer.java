package src.main.game.players.AIPlayers.BranchPlayers;

import java.util.Set;

import src.main.game.Tile;
import src.main.game.TileBag;
import src.main.game.players.Hand;
import src.main.game.players.Player;
import src.main.AI.Branchable;
import src.main.game.Game;
import src.main.game.Grid;
import src.main.game.Location;

// TODO should be protected since the AIPlayer is all that uses it?
public abstract class AbstractBranchingPlayer<T extends AbstractBranchingPlayer<T>> extends Player implements Branchable<T> {

    public AbstractBranchingPlayer(Game game, Grid grid, TileBag bag) {
        super(game, grid, bag);
    }

    public AbstractBranchingPlayer(Game game, Grid grid, TileBag bag, Hand hand) {
        super(game, grid, bag, hand);
    }

    public abstract Set<T> branch();
    
    // allows a player to play an entire grid given in (assuming that the players has those tiles) 
    public void playGrid(Grid g) { 
        // clear hand 
        super.clearGrid();

        for (Location loc: g.getFilledSquares().keySet()) {
            Tile t = g.getTile(loc);

            // place tile on grid and remove it
            super.getGrid().placeUnsafe(loc, t);
            super.getHand().remove(t);
        }
    }

    @Override
    public AbstractBranchingPlayer<T> copy() {return copy();} // casts to AbstractBranchingPlayer

}
