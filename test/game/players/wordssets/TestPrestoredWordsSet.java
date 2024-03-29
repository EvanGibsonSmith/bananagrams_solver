package test.game.players.wordssets;

import org.junit.jupiter.api.Test;

import src.main.game.wordssets.PrestoredWordSet;

import java.util.HashSet;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;

public class TestPrestoredWordsSet {

    @Test
    void testCombinations() {  
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

        System.out.println(wordsSet.size());

        PrestoredWordSet set = new PrestoredWordSet(wordsSet, 1);
        assertEquals(set.getCombinations().size(), 26);

        set = new PrestoredWordSet(wordsSet, 2);
        assertEquals(set.getCombinations().size(), 26*26);
    }
}
