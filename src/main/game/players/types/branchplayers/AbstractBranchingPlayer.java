package src.main.game.players.types.branchplayers;

import java.util.Set;

import src.main.game.grids.Grid;
import src.main.game.players.brokers.AbstractBroker;
import src.main.game.players.gridarrangers.GridArranger;
import src.main.game.players.types.Player;
import src.interfaces.Branchable;
import src.main.game.Game;

public abstract class AbstractBranchingPlayer extends Player implements Branchable<AbstractBranchingPlayer> {

    public AbstractBranchingPlayer(Game game, Grid grid, AbstractBroker broker) {
        super(game, new GridArranger(grid, broker.getHand()), broker);
    }

    public AbstractBranchingPlayer(Game game, GridArranger gridArranger, AbstractBroker broker) {
        super(game, gridArranger, broker);
    }

    public AbstractBroker getBroker() {
        return broker;
    }

    public abstract Set<? extends AbstractBranchingPlayer> branch();
    
}
