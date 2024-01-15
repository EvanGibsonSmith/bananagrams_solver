package src.factories;

import java.lang.reflect.Constructor;

import src.interfaces.Buildable;
import src.interfaces.TileBagable;
import src.main.game.Game;
import src.main.game.Grid;
import src.main.game.players.types.branchplayers.AbstractBranchingPlayer;

// TODO create a general factory abstract class?
public class BranchingPlayerFactory implements Buildable<AbstractBranchingPlayer> {// TODO complete. Make static?
    private Constructor<? extends AbstractBranchingPlayer> constructor; 
    private Game game;
    private Grid grid; 
    private TileBagable bag;

    // TODO build this with prototype design to copy a given object extending from AbstractBranchingPlayer

    public BranchingPlayerFactory(Class<? extends AbstractBranchingPlayer> clazz, Game game, Grid grid, TileBagable bag) throws Exception {
        // get constructor for the unknown extending class and inject player dependencies
        constructor = clazz.getDeclaredConstructor(new Class<?>[] {Game.class, Grid.class, TileBagable.class}); 

        // save injected dependencies for builds TODO should this be a one time build factory or should we just use it that way? Seems like possible overkill
        this.game = game;
        this.grid = grid;
        this.bag = bag;
    }

    // TODO fix this up so it actually builds well and takes in build parameters
    public AbstractBranchingPlayer build() throws Exception {
        return constructor.newInstance(game, grid, bag); // TODO fix generics so this can be nicer.
    }
}
