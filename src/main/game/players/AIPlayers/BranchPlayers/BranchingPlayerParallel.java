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

import src.main.game.players.AbstractBroker;
import src.main.game.Game;
import src.main.game.Grid;

// TODO eventually create an extension of this that has more "Normal" player constructors for the tests that works without explicitly passing in the broker?
public class BranchingPlayerParallel extends AbstractPlayerBranchingMethods<BranchingPlayerParallel> {
    private ExecutorService executorService = Executors.newFixedThreadPool(2);

    public BranchingPlayerParallel(Game game, Grid grid, AbstractBroker broker) {
        super(game, grid, broker, true);
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
