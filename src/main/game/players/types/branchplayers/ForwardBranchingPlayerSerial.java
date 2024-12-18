package src.main.game.players.types.branchplayers;

import java.util.Set;

import src.main.game.Game;
import src.main.game.grids.Grid;
import src.main.game.players.bags.NormalTileBag;
import src.main.game.players.brokers.AbstractBroker;
import src.main.game.players.brokers.HumanBroker;
import src.main.game.players.gridarrangers.GridArranger;
import src.main.game.players.hand.Hand;

public class ForwardBranchingPlayerSerial extends BranchingPlayerSerial {
        
    public ForwardBranchingPlayerSerial(Game game, Grid grid, NormalTileBag tileBag) {
        super(game, grid, new HumanBroker(new Hand(), tileBag));
    }

    public ForwardBranchingPlayerSerial(Game game, Grid grid, AbstractBroker broker) {
        super(game, grid, broker);
    }

    public ForwardBranchingPlayerSerial(Game game, GridArranger gridArranger, AbstractBroker broker) {
        super(game, gridArranger, broker);
    }

    @Override
    public Set<BranchingPlayerSerial> branch() {
        if (this.getGrid().isEmpty()) {
            return branchEmpty(); // edge case for when the grid is empty
        }
        Set<BranchingPlayerSerial> out = branchForward(); // ONLY branching forward
        return out;
    }
}
