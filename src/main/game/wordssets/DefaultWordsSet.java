package src.main.game.wordssets;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

// TODO document. Just has the default scrabble words put right into it. Put into tests to simplify them. Also needs to be tested
public class DefaultWordsSet extends WordsSet {
    
    public DefaultWordsSet() {
        super(); 
        HashSet<String> wordsSet = new HashSet<>();
        try (Scanner scnr = new Scanner (new File("src/resources/scrabbleWords.txt"))) {
            scnr.useDelimiter("\n");
            while (scnr.hasNext()) {
                String next = scnr.next();
                if (next.length()-1>2) {
                    wordsSet.add(next.substring(0, next.length()-1));
                }
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        super.wordsSet = wordsSet;
    }
}
