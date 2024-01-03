package src.main.AI;

import java.util.Set;

import src.main.game.Player;
import src.main.game.Tile;
import src.main.game.TileBag;
import src.main.game.Game;
import src.main.game.Grid;
import src.data_structures.MultiSet;

public abstract class AIPlayer<T extends AIPlayer<T>> extends Player implements Branchable<T> {

    public AIPlayer(Game game, Grid grid, TileBag bag) {
        super(game, grid, bag);
    }

    public AIPlayer(Game game, Grid grid, TileBag bag, MultiSet<Tile> hand) {
        super(game, grid, bag, hand);
    }

    public abstract T copy();

    public abstract Set<T> branch();

}
