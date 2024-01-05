package test.AI.AStar.MazeTest;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import src.main.AI.AStarHashSets;

public class AStarMaze {

    @Test
    void base() {
        Maze m = new Maze(1, 1, 0);
        BiFunction<Maze, Maze, Double> cost = (m1, m2) -> (double) (m2.moves - m1.moves);
        Function<Maze, Double> heuristic = (m1) -> (double) (Math.abs(m1.playerRow-m1.rows+1) + Math.abs(m1.playerColumn-m1.columns+1));
        Function<Maze, Boolean> isGoal = (ma) -> ((ma.playerRow==ma.rows-1) && (ma.playerColumn==ma.columns-1));
        AStarHashSets<Maze> astar = new AStarHashSets<Maze>(m, cost, heuristic, isGoal);
        
        ArrayList<Maze> path = astar.getPath();
        for (Maze next: path) {
            System.out.println(next);
        }
    }

    @Test
    void small() {
        Maze m = new Maze(5, 5, 30);
        //m.grid[0][4]=true;
        //m.grid[1][0]=true;
        //m.grid[2][4]=true;
        //m.grid[3][4]=true; m.grid[3][3]=true;
        //m.grid[4][4]=true;
        BiFunction<Maze, Maze, Double> cost = (m1, m2) -> (double) (m2.moves - m1.moves);
        Function<Maze, Double> heuristic = (m1) -> (double) (Math.abs(m1.playerRow-m1.rows+1) + Math.abs(m1.playerColumn));
        Function<Maze, Boolean> isGoal = (ma) -> ((ma.playerRow==ma.rows-1) && (ma.playerColumn==ma.columns-1));
        AStarHashSets<Maze> astar = new AStarHashSets<Maze>(m, cost, heuristic, isGoal);
        System.out.println(m);

        ArrayList<Maze> path = astar.getPath();
        if (path==null) {System.out.println("No Path");}
        else {
            for (Maze next: path) {
                System.out.println(next);
            }
        }
    }
}
