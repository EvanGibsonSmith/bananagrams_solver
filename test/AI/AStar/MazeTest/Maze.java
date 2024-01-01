package test.AI.AStar.MazeTest;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import src.main.AI.Branchable;

public class Maze implements Branchable<Maze> {
    public int rows;
    public int columns;
    public boolean[][] grid;
    public int playerRow=0;
    public int playerColumn=0;
    public int density;
    public int moves = 0;
    public Random rand = new Random();

    public Maze(int rows, int columns, int density) {
        this.rows = rows;
        this.columns = columns;
        this.grid = new boolean[rows][columns];
        grid[0][0] = false;
        this.density = density;

        // fill squares
        for (int row=0; row<rows; ++row) {
            for (int column=0; column<columns; ++column) {
                int next = rand.nextInt(100);
                if (next<density) {
                    this.grid[row][column] = true;
                }
            }
        }
        this.grid[0][0] = false; // starting point for the maze
    }

    public Maze(int rows, int columns, int density, int playerRow, int playerColumn, boolean[][] grid, int moves) {
        this(rows, columns, density); 
        this.playerRow = playerRow;
        this.playerColumn = playerColumn;
        this.grid = grid;
        this.moves = moves;
    }

    public Maze copy() {
        return new Maze(this.rows, this.columns, this.density, this.playerRow, this.playerColumn, this.grid, this.moves);
    }

    public boolean playerLocationValid() {
        return !grid[playerRow][playerColumn];
    }

    @Override 
    public boolean equals(Object obj) {
        if (obj.getClass()!=this.getClass()) {return false;}
        Maze mobj = ((Maze) obj);
        return (this.grid == mobj.grid && this.playerRow==mobj.playerRow && this.playerColumn==mobj.playerColumn);
    }

    @Override 
    public int hashCode() {
        return this.grid.hashCode() + this.playerRow + this.playerColumn;
    }

    @Override
    public Set<Maze> branch() {
        Set<Maze> out = new HashSet<>();
        Maze newMaze; 

        newMaze = this.copy();
        if (playerRow!=0) {
            --newMaze.playerRow;
            ++newMaze.moves;
            if (newMaze.playerLocationValid()) {out.add(newMaze);}
        }

        if (playerRow!=this.rows-1) {
            newMaze = this.copy();
            ++newMaze.playerRow;
            ++newMaze.moves;
            if (newMaze.playerLocationValid()) {out.add(newMaze);}
        }

        if (playerColumn!=0) {
            newMaze = this.copy();
            --newMaze.playerColumn;
            ++newMaze.moves;
            if (newMaze.playerLocationValid()) {out.add(newMaze);}
        }

        if (playerColumn!=this.columns-1) {
            newMaze = this.copy();
            ++newMaze.playerColumn;
            ++newMaze.moves;
            if (newMaze.playerLocationValid()) {out.add(newMaze);}
        }

        return out;
    }

    @Override
    public String toString() {
        String out = "";
        for (int column=0; column<this.columns; ++column) {
            for (int row=0; row<this.rows; ++row) {
                if (row==playerRow && column==playerColumn) {
                    out += " P ";
                }
                else if (this.grid[row][column]==false) {
                    out += " O ";
                }
                else {
                    out += " X ";
                }
            }
            out += "\n";
        }
        return out;
    }


    public static void main(String[] args) {
        Maze m = new Maze(3, 3, 50);
        System.out.println(m);

        Set<Maze> mBranch = m.branch();
        System.out.println(mBranch.size());
        for (Maze bm: mBranch) {
            System.out.println(bm);
        }

        Maze mc1 = new Maze(3, 3, 50);
        Maze mc2 = mc1.copy();
        System.out.println(mc1.equals(mc2));
    }



}
