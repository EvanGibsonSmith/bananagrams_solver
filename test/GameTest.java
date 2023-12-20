package test;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;

import src.main.TileBag;
import src.main.Tile;
import src.main.Game;
import src.main.Player;

class GameTest {

    @Test
    void runSmallGame() {
        // TODO does not run as intended
        HashSet<String> validWords = new HashSet<String>();
        validWords.add("pan");
        validWords.add("par");
        validWords.add("part");
        validWords.add("pane");
        validWords.add("met");

        char[] letters = "abcde".toCharArray();
        Tile[] tiles = new Tile[letters.length];
        for (int i=0; i<letters.length; ++i) {
            tiles[i] = new Tile(letters[i]);
        }
        TileBag tileBag = new TileBag(tiles, 31415);
        assertEquals(tileBag.toString(), " a b c d e");
        Game game = new Game(1, tileBag, validWords);

        game.peel();
        game.peel();
        for (Player p: game.getPlayers()) {
            System.out.println(p.getBag());
            System.out.println(p.getHand());
        }
    }
}
