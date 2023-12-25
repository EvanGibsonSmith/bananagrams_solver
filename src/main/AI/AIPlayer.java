package src.main.AI;

import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;

import src.main.game.Player;
import src.main.game.Tile;
import src.main.game.TileBag;
import src.main.game.Game;
import src.main.game.Grid;
import src.main.game.Location;

public class AIPlayer extends Player implements Branchable<AIPlayer> {
    
    public AIPlayer(Game game, Grid grid, TileBag bag) {
        super(game, grid, bag);
    }
    
    @Override
    public Set<AIPlayer> branch() {
        HashSet<Grid> output = new HashSet<>();
        //ArrayList<String> wordDown = this.getGrid().getWordsDown(); // TODO maybe these functions could go in the AI Grid with the AI specific functionality?
        //ArrayList<String> wordRight = this.getGrid().getWordsRight();
        /*if (wordDown==null) {

        }

        if (wordRight==null) {
            
        }*/
        return null; // STUB
    }
}
