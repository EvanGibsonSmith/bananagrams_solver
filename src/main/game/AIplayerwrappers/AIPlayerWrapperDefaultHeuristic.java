package src.main.game.AIplayerwrappers;

import java.util.function.BiFunction;
import java.util.function.Function;

import src.algorithms.astar.AbstractAStar;
import src.main.game.players.types.branchplayers.AbstractBranchingPlayer;

public class AIPlayerWrapperDefaultHeuristic extends AIPlayerWrapper {

    public AIPlayerWrapperDefaultHeuristic(Class<? extends AbstractAStar> astarClass, AbstractBranchingPlayer player) 
        throws Exception {
        super(astarClass, player, 
                AIPlayerAStarFunctions.handSizeCost(), 
                AIPlayerAStarFunctions.handSizeHeuristic(), 
                AIPlayerAStarFunctions.emptyHandGoal());
    }
}
