package src;
public class Tile {
    final int tileRow;
    final int tileColumn;
    final char letter;

    public Tile(Character letter) {
        this.letter = letter;
    }

    public char getLetter() {
        return letter;
    }

    public int getRow() {
        return tileRow;
    }

    public int getColumn() {
        return tileColumn;
    }
}
