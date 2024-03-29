package test.game.players.types.branchplayers;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;
import java.util.Scanner;

import src.main.game.Location;
import src.main.game.Tile;
import src.main.game.grids.Grid;
import src.main.game.players.bags.NormalTileBag;
import src.main.game.players.types.branchplayers.BranchingPlayerParallel;


public class BranchStressTest {
    
    @Test
    void StressTest1() {
        HashSet<String> wordsSet = new HashSet<>();
        try (Scanner scnr = new Scanner (new File("src/resources/scrabbleWords.txt"))) {
            scnr.useDelimiter("\n");
            while (scnr.hasNext()) {
                String next = scnr.next();
                wordsSet.add(next.substring(0, next.length()-1));
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Below is to test extremely small set of words (time of around 14 on my laptop) full scrabble words took 1514540.
        // With parallel streams on my laptop for branchForward took 310315, with another threading (way too many threads, 100, 356692).
        // With just 2 threads, we have 317062. Naturally, removing one direction from forward branch halved the time to 170045.
        // On a larger computers
        // Complete 
        /*
        wordsSet.clear();
        wordsSet.add("act");
        wordsSet.add("cat");
        wordsSet.add("pat");
        wordsSet.add("tap");
        */

        char[] letters = "actapat".toCharArray();
        Tile[] tiles = new Tile[letters.length];
        for (int i=0; i<letters.length; ++i) {
            tiles[i] = new Tile(letters[i]);
        }
        NormalTileBag tileBag = new NormalTileBag(tiles, 1);
        
        BranchingPlayerParallel player = new BranchingPlayerParallel(null, new Grid(wordsSet), tileBag); // game not needed for this test
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();

        System.out.println(player.getHand());
        player.placeTile(new Location(0, 0), new Tile('a'));
        player.placeTile(new Location(0, 1), new Tile('c'));
        player.placeTile(new Location(0, 2), new Tile('t'));
        player.placeTile(new Location(1, 2), new Tile('a'));
        player.placeTile(new Location(2, 2), new Tile('p'));
        assertTrue(player.gridValid());

        System.out.println(player.getGrid());
        long startTime = System.currentTimeMillis();
        Set<BranchingPlayerParallel> nextPlayers = player.branch(); // can add tap or pat to p, and remove act or tap
        long endTime = System.currentTimeMillis();
        System.out.println("Time: " + (endTime - startTime));
        for (BranchingPlayerParallel p: nextPlayers) {
            System.out.println(p.getGrid());
        }
    }

    @Test
    void StressTestEmpty() {
        HashSet<String> wordsSet = new HashSet<>();
        try (Scanner scnr = new Scanner (new File("src/resources/scrabbleWords.txt"))) {
            scnr.useDelimiter("\n");
            while (scnr.hasNext()) {
                String next = scnr.next();
                wordsSet.add(next.substring(0, next.length()-1));
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Below is to test extremely small set of words (time of around 14 on my laptop) full scrabble words took 1514540.
        // With parallel streams on my laptop for branchForward took 310315, with another threading (way too many threads, 100, 356692).
        // With just 2 threads, we have 317062. Naturally, removing one direction from forward branch halved the time to 170045.
        // On a larger computers
        // Complete 
        /*
        wordsSet.clear();
        wordsSet.add("act");
        wordsSet.add("cat");
        wordsSet.add("pat");
        wordsSet.add("tap");
        */

        char[] letters = "baazar".toCharArray();
        Tile[] tiles = new Tile[letters.length];
        for (int i=0; i<letters.length; ++i) {
            tiles[i] = new Tile(letters[i]);
        }
        NormalTileBag tileBag = new NormalTileBag(tiles, 1);
        
        BranchingPlayerParallel player = new BranchingPlayerParallel(null, new Grid(wordsSet), tileBag); // game not needed for this test
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();
        player.grabTile();

        assertTrue(player.gridValid());

        System.out.println(player.getGrid());
        long startTime = System.currentTimeMillis();
        Set<BranchingPlayerParallel> nextPlayers = player.branch(); // can add tap or pat to p, and remove act or tap
        long endTime = System.currentTimeMillis();
        System.out.println("Time: " + (endTime - startTime));
        for (BranchingPlayerParallel p: nextPlayers) {
            System.out.println(p.getGrid());
        }
    }
}
