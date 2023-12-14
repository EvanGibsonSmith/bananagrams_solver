package src;

public class Location {
    int row;
    int column;

    public Location(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return this.row;
    }

    public void setRow(int r) {
        this.row = r;
    }

    public int getColumn() {
        return this.column;
    }

    public void setColumn(int c) {
        this.column = c;
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

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        // we now know obj is of type Location
        return (this.row==((Location) obj).row && this.column==((Location) obj).column);
    }

}
