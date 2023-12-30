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

    // TODO there appears to be a problem with either BOTH of my heuristics or A star itself.
    @Test
    void small() {
        Maze m = new Maze(4, 2, 0);
        BiFunction<Maze, Maze, Double> cost = (m1, m2) -> (double) (m2.moves - m1.moves);
        Function<Maze, Double> heuristic = (m1) -> (double) (Math.abs(m1.playerRow-m1.rows+1) + Math.abs(m1.playerColumn-m1.columns+1));
        Function<Maze, Boolean> isGoal = (ma) -> ((ma.playerRow==ma.rows-1) && (ma.playerColumn==ma.columns-1));
        AStarHashSets<Maze> astar = new AStarHashSets<Maze>(m, cost, heuristic, isGoal);
        
        ArrayList<Maze> path = astar.getPath();
        for (Maze next: path) {
            //System.out.println(next);
        }
        System.out.println(astar.visitedSize());
        for (Maze pathMaze: astar.getPath()) {
            System.out.println(pathMaze); // TODO objects seems to be collecting duplicates
        }
    }
}
