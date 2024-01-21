package src.main.game.players.types.branchplayers;

import java.util.Set;

import src.main.game.Tile;
import src.main.game.grids.Grid;
import src.main.game.players.brokers.AbstractBroker;
import src.main.game.players.gridarrangers.GridArranger;
import src.main.game.players.types.Player;
import src.interfaces.Branchable;
import src.main.game.Game;
import src.main.game.Location;

// TODO should be protected since the AIPlayer is all that uses it?
public abstract class AbstractBranchingPlayer extends Player implements Branchable<AbstractBranchingPlayer> {

    // TODO is it good that this handles the "implementation part of a broker?"
    // TODO this should probably not take a grid and instead take a grid arranger
    public AbstractBranchingPlayer(Game game, Grid grid, AbstractBroker broker) {
        super(game, new GridArranger(grid, broker.getHand()), broker); // TODO fix this weird hand this later? Is it needed?
    }

    public AbstractBranchingPlayer(Game game, GridArranger gridArranger, AbstractBroker broker) {
        super(game, gridArranger, broker); // TODO fix this weird hand this later? Is it needed?
    }

    /* TODO delete comment of constructors when AbstractBranchingPlayer
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
    */  

    public AbstractBroker getBroker() {
        return broker;
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
}