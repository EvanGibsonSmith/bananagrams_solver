package src.main.game.players;

import java.util.HashSet;

import src.main.AI.AStar.AbstractAStar;
import src.main.game.Game;
import src.main.game.NormalTileBag;
import src.main.game.WordsSet;
import src.main.game.players.AIPlayers.BranchPlayers.AbstractBranchingPlayer;
import src.main.game.players.AIPlayers.BranchPlayers.BranchingPlayerParallel;
import src.main.AI.AStar.AStarArrayList;

public class DefaultCheatPlayer extends CheatPlayer {
    WordsSet words = new DefaultWordsSet(); // TODO make this class
    
    // small wrapper class for default bananagrams settings, null game, blank game, and best A*/branch algos
    public DefaultCheatPlayer() throws Exception {
        super(BranchingPlayerParallel.class, 
                AStarArrayList.class, words); 
        // TODO bad to "Reach" into player to swtich out Broker when player should be high level anyway
        getPlayer().broker = new CheatBroker(getPlayer().broker.getHand()); // reach in and change broker type 
    }
    
}
