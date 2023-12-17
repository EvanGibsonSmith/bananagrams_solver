
package test;
import java.util.HashSet;

import src.main.Grid;
import src.main.Location;
import src.main.Tile;
public class GridTest {
    
    public static void main(String[] args) {
        // TODO use actual junit and make the tests useful and nice
        HashSet<String> wordsSet = new HashSet<String>();
        wordsSet.add("pan");
        wordsSet.add("par");
        wordsSet.add("part");
        wordsSet.add("pane");
        wordsSet.add("met");
        Grid g = new Grid(wordsSet); // just putting the specific words used into manual set

        System.out.println("Starting");
        g.placeUnsafe(new Location(0, 0), new Tile("p".charAt(0)));
        g.placeUnsafe(new Location(0, 1), new Tile("a".charAt(0)));
        g.placeUnsafe(new Location(0, 2), new Tile("n".charAt(0)));

        g.placeUnsafe(new Location(1, 0), new Tile("a".charAt(0)));
        g.placeUnsafe(new Location(2, 0), new Tile("r".charAt(0)));
        
        System.out.println(g.getWordsPlayed());
        System.out.println(g.validWords());
        System.out.println(g.validIslands());
        System.out.println(g);

        g.placeUnsafe(new Location(3, 0), new Tile("t".charAt(0)));
        g.placeUnsafe(new Location(0, 3), new Tile("e".charAt(0)));

        System.out.println(g.getWordsPlayed());
        System.out.println(g.validWords());
        System.out.println(g.validIslands());
        System.out.println(g);
        System.out.println("DONE");

        g.placeUnsafe(new Location(-1, 3), new Tile("m".charAt(0)));
        g.placeUnsafe(new Location(1, 3), new Tile("t".charAt(0)));


        System.out.println(g.getWordsPlayed());
        System.out.println(g.validWords());
        System.out.println(g.validIslands());
        System.out.println(g);
        System.out.println("DONE");

        // TODO add a bounding box test. I found an small error, I think they are all fixed but there are quite a few components so it would be nice to test.
    } 
}
