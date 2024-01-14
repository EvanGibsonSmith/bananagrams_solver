package src.main.game.players.AIPlayers.BranchPlayers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;

import src.main.game.NormalTileBag;
import src.main.game.TileBagable;
import src.main.game.players.Hand;
import src.main.game.Game;
import src.main.game.Grid;

public class BranchingPlayerParallel extends AbstractPlayerBranchingMethods {
    private ExecutorService executorService = Executors.newFixedThreadPool(2);

    public BranchingPlayerParallel(Game game, Grid grid, TileBagable bag) {
        super(game, grid, bag);
    }

    public BranchingPlayerParallel(Game game, Grid grid, TileBagable bag, Hand hand) {
        super(game, grid, bag, hand);
    }

    public BranchingPlayerParallel copy() {
        return new BranchingPlayerParallel(getGame(), new Grid(getGrid()), getBag(), this.getHand().copy());
    }

    public Set<BranchingPlayerParallel> branchForward() {
        List<Callable<Set<BranchingPlayerParallel>>> tasks = new ArrayList<>();
        tasks.add(() ->  branchForwardSingleDirection((byte) 1));
        tasks.add(() ->  branchForwardSingleDirection((byte) 0)); // just combine both directions

        // just adds the two sets from the threads
        Set<BranchingPlayerParallel> out = new HashSet<>();
        try {
            List<Future<Set<BranchingPlayerParallel>>> futures = executorService.invokeAll(tasks);
            for (Future<Set<BranchingPlayerParallel>> p: futures) {
                try {
                    out.addAll(p.get());
                }
                catch (InterruptedException| ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        return out;
    }
}
