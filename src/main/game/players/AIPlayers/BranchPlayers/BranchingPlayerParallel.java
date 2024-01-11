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

import src.main.game.TileBag;
import src.main.game.players.Hand;
import src.main.game.Game;
import src.main.game.Grid;

public class BranchingPlayerParallel extends AbstractPlayerBranchingMethods<BranchingPlayerParallel> {
    private ExecutorService executorService = Executors.newFixedThreadPool(2);

    public BranchingPlayerParallel(Game game, Grid grid, TileBag bag) {
        super(game, grid, bag);
    }

    public BranchingPlayerParallel(Game game, Grid grid, TileBag bag, Hand hand) {
        super(game, grid, bag, hand);
    }

    public BranchingPlayerParallel copy() {
        return new BranchingPlayerParallel(getGame(), new Grid(getGrid()), (TileBag) getBag(), this.getHand().copy());
    }

    public Set<BranchingPlayerParallel> branchForward() {
        List<Callable<Set<BranchingPlayerParallel>>> tasks = new ArrayList<>();
        tasks.add(() -> branchForwardSingleDirection((byte) 1));
        tasks.add(() -> branchForwardSingleDirection((byte) 0)); // just combine both directions

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

    @Override
    public Set<BranchingPlayerParallel> branch() {
        if (this.getGrid().isEmpty()) {
            return branchEmpty(); // edge case for when the grid is empty
        }
        Set<BranchingPlayerParallel> out = branchForward();
        out.addAll(branchBackward()); // just combine both directions
        return out;
    }
}
