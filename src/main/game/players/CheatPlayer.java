package src.main.game.players;

import src.main.AI.AStar.AbstractAStar;
import src.main.game.players.AIPlayers.BranchPlayers.AbstractBranchingPlayer;
import src.main.game.players.PlayerAStar.PlayerAStarable;

import java.util.HashSet;

// CheatPlayer essentially just switches out the CheatBroker object for broker used when interacting with a real life game
public class CheatPlayer extends AIPlayer {
    
    public CheatPlayer(HashSet<String> wordsSet, Hand hand, PlayerAStarable astar) {
        super.gridArranger = new GridArranger(wordsSet, hand);;
        super.broker = new CheatBroker(hand);
        this.AIClass = AStarClass.newInstance();
    }

    public solveGrid() {

    }


    
}
