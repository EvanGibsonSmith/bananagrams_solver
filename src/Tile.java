package src;
public class Tile {
    final char letter;

    public Tile(Character letter) {
        this.letter = letter;
    }

    public char getLetter() {
        return letter;
    }

    public boolean isEmpty() {
        return this.letter==' ';
    }
}
