package src.main.AI;

import java.util.Set;

import src.main.game.Tile;
import src.main.game.TileBag;
import src.main.game.player.Player;
import src.main.game.Game;
import src.main.game.Grid;
import src.main.game.player.Hand;
import src.main.game.Location;

public abstract class AIPlayer<T extends AIPlayer<T>> extends Player implements Branchable<T> {

    public AIPlayer(Game game, Grid grid, TileBag bag) {
        super(game, grid, bag);
    }

    public AIPlayer(Game game, Grid grid, TileBag bag, Hand hand) {
        super(game, grid, bag, hand);
    }

    public abstract T copy();

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

}
