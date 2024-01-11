package test.AI.AIPlayer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;

import src.main.game.Grid;
import src.main.game.Location;
import src.main.game.Tile;
import src.main.game.TileBag;
import src.main.game.players.AIPlayers.BranchPlayers.BranchingPlayerParallel;
import src.data_structures.MultiSet;


class BranchForward {

    @Test 
    void accidentalInvalidation() {
        HashSet<String> wordsSet = new HashSet<String>();
        wordsSet.add("at"); // first words played
        wordsSet.add("as");

        // can't play
        wordsSet.add("par");
        wordsSet.add("part");
        wordsSet.add("pane");
        wordsSet.add("fair");
        wordsSet.add("ale");

        wordsSet.add("sat"); // could be played, but causes invalid board state from other word (ta) formed

        char[] letters = "abastt".toCharArray();
        Tile[] tiles = new Tile[letters.length];
        for (int i=0; i<letters.length; ++i) {
            tiles[i] = new Tile(letters[i]);
        }
        TileBag tileBag = new TileBag(tiles, 42);
        
        BranchingPlayerParallel player = new BranchingPlayerParallel(null, new Grid(wordsSet), tileBag); // game not needed for this test
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();

        System.out.println(player.getHand()); // player has act letters
        // player places act down
        player.placeTile(new Location(0, 0), new Tile('a'));
        player.placeTile(new Location(1, 0), new Tile('s'));

        Set<BranchingPlayerParallel> nextPlayers = player.branchForward();

        // can play sat right now
        assertEquals(nextPlayers.size(), 3);
        MultiSet<MultiSet<String>> expected = new MultiSet<MultiSet<String>>();
        expected.add(new MultiSet<String>(new String[] {"as", "sat"}));
        expected.add(new MultiSet<String>(new String[] {"as", "at"}));
        expected.add(new MultiSet<String>(new String[] {"as", "as"}));
        BranchTestMethods.getAllWordsPlayed(nextPlayers);
        assertEquals(BranchTestMethods.getAllWordsPlayed(nextPlayers), expected);


        player.placeTile(new Location(0, -1), new Tile('t'));
        nextPlayers = player.branchForward();

        assertEquals(nextPlayers.size(), 0);   
    }

}

