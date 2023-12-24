package src.main.game;

public class Tile {
    final char letter;

    public Tile(Character letter) {
        this.letter = letter;
    }

    @Override
    public String toString() {
        return Character.toString(letter);
    }

    @Override 
    public boolean equals(Object obj) {
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        return (((Tile) obj).getLetter() == this.getLetter());
    }

    
    @Override 
    public int hashCode() {
        return ((Character) this.letter).hashCode();
    }

    public char getLetter() {
        return letter;
    }
}
