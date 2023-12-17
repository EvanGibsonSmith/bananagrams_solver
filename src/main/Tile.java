package src.main;
public class Tile {
    final char letter;

    public Tile(Character letter) {
        this.letter = letter;
    }

    @Override
    public String toString() {
        return Character.toString(letter);
    }

    public char getLetter() {
        return letter;
    }

    public boolean isEmpty() { // TODO may need to delete this because it's extraneous as blank tiles will not be in filled squares
        return this.letter==' ';
    }
}
