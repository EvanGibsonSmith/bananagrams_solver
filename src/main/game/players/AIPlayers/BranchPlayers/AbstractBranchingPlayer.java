package src.main.game.players.AIPlayers.BranchPlayers;

import java.util.Set;

import src.main.game.Tile;
import src.main.game.TileBagable;
import src.main.game.NormalTileBag;
import src.main.game.players.HumanBroker;
import src.main.game.players.Hand;
import src.main.game.players.Player;
import src.main.AI.Branchable;
import src.main.game.Game;
import src.main.game.Grid;
import src.main.game.Location;

// TODO should be protected since the AIPlayer is all that uses it?
public abstract class AbstractBranchingPlayer extends Player implements Branchable<AbstractBranchingPlayer> {

    // TODO this seems bad. Maybe a dummy game to not need these constructors for when game is null
    public AbstractBranchingPlayer(Grid grid, TileBagable bag) {
        super(null, grid, bag);

        // TODO kind of dumb that this broker implementation gets overridden by AIPlayer or fine?
        super.broker = new HumanBroker(new Hand(), (NormalTileBag) bag); // TODO ridiculous cast
    }

    // TODO another bad no game constructor
    public AbstractBranchingPlayer(Grid grid, TileBagable bag, Hand hand) {
        super(null, grid, bag);

        // TODO kind of dumb that this broker implementation gets overridden by AIPlayer or fine?
        super.broker = new HumanBroker(hand, (NormalTileBag) bag); // TODO ridiculous cast
    }

    public AbstractBranchingPlayer(Game game, Grid grid, TileBagable bag) {
        super(game, grid, bag);

        // TODO kind of dumb that this broker implementation gets overridden by AIPlayer or fine?
        super.broker = new HumanBroker(new Hand(), (NormalTileBag) bag); // TODO ridiculous cast
    }

    public AbstractBranchingPlayer(Game game, Grid grid, TileBagable bag, Hand hand) {
        super(game, grid, bag, hand);
        
        // TODO kind of dumb that this broker implementation gets overridden by AIPlayer or fine?
        super.broker = new HumanBroker(new Hand(), game);
    }

    public abstract Set<? extends AbstractBranchingPlayer> branch();
    
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
    public AbstractBranchingPlayer copy() {return copy();} // casts to AbstractBranchingPlayer

}
