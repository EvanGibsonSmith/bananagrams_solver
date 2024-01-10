package src.main.game.players;

import src.main.AI.AStar.AbstractAStar;
import src.main.game.players.AIPlayers.BranchPlayers.AbstractBranchingPlayer;
import src.main.game.players.PlayerAStar.PlayerAStarable;

import java.util.HashSet;

public class CheatPlayer {
    // TODO note that the A star implementation going into this class should go into the AI Player class!!!
    AbstractAStar AIClass; // TODO AbstractAStar can't be parametrized with player becuase player isn't branchable. Get AIPlayer working
    // TODO this AI functionality should be moved into AIPlayer which can handle this and we can extend AIPLayer instead
    
    public CheatPlayer(HashSet<String> wordsSet, Hand hand, PlayerAStarable astar) {
        super.gridArranger = new GridArranger(wordsSet, hand);;
        super.broker = new AIBroker(hand);
        this.AIClass = AStarClass.newInstance();
    }

    public solveGrid() {

    }


    
}
