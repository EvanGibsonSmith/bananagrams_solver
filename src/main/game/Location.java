package src.main.game;

import java.util.Objects;

public class Location {
    int row;
    int column;

    public Location(int row, int column) {
        this.row = row;
        this.column = column;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        // we now know obj is of type Location
        return (this.row==((Location) obj).row && this.column==((Location) obj).column);
    }

    @Override
    public int hashCode() { // needed for use in HashTable filled squares of grid
        return Objects.hash(row, column);
    }

    @Override
    public String toString() {
        return "(" + this.row + ", " + this.column + ")";
    }
    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }

    public Location below() {
        return new Location(this.row+1, this.column);
    }

    public Location above() {
        return new Location(this.row-1, this.column);
    }
    
    public Location left() {
        return new Location(this.row, this.column-1);
    }

    public Location right() {
        return new Location(this.row, this.column+1);
    }

}
