package src.main.AI;

import java.util.HashSet;
import java.util.Set;

import src.main.game.Player;
import src.main.game.Tile;
import src.main.game.TileBag;
import src.main.game.Game;
import src.main.game.Grid;
import src.main.game.Location;

public class AIPlayer extends Player implements Branchable<Player> {
    
    public AIPlayer(Game game, Grid grid, TileBag bag) {
        super(game, grid, bag);
    }
    
    @Override
    public Set<Player> branch() {
        HashSet<Grid> output = new HashSet<>();
        for (Location loc: this.getGrid().placeableLocations()) {
            for (Tile t: this.getHand()) {
                // TODO add new copy of grid with the tile placed at location into output
            }
        }
        return null; // STUB
    }
}
