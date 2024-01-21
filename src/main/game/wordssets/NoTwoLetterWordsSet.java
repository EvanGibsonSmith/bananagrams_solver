package src.main.game.wordssets;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

public class NoTwoLetterWordsSet extends WordsSet {
    
    public NoTwoLetterWordsSet() {
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
