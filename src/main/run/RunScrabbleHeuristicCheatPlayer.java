package src.main.run;

import java.util.HashMap;
import java.util.Scanner;
import java.util.function.BiFunction;
import java.util.function.Function;

import src.algorithms.astar.AStarArrayList;
import src.main.game.Tile;
import src.main.game.AIplayerwrappers.CheatPlayerManualAStarWrapper;
import src.main.game.grids.DefaultGrid;
import src.main.game.players.brokers.CheatBroker;
import src.main.game.players.hand.Hand;
import src.main.game.players.types.branchplayers.AbstractBranchingPlayer;
import src.main.game.players.types.branchplayers.BranchingPlayerSerial;

// TODO dry with the pther run cheat player class?
public class RunScrabbleHeuristicCheatPlayer {
    
    private static HashMap<Character, Integer> getScrabbleMap() {
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        int[] letterValues = new int[] {1,3,3,2,1,4,2,4,1,8,5,1,3,1,1,3,10,1,1,1,1,4,4,8,4,10};
        HashMap<Character, Integer> scrabbleLetterValues = new HashMap<>();
        for (int idx=0; idx<alphabet.length; ++idx) {
            scrabbleLetterValues.put(alphabet[idx], letterValues[idx]);
        }
        return scrabbleLetterValues;
    }

    private static Double scrabbleHeuristic(AbstractBranchingPlayer p) {
        HashMap<Character, Integer> map = getScrabbleMap();
        Hand hand = p.getHand();
        double total = 0;
        for (Tile t: hand) {
            total += map.get(t.getLetter());
        }
        return total;
    }

    private static Double scrabbleCost(AbstractBranchingPlayer p, AbstractBranchingPlayer q) {
        return scrabbleHeuristic(q) - scrabbleHeuristic(p);
    }

    public static void main(String[] args) throws Exception{
        // TODO use scanner to make the cheat player easy to use here with user input
        Scanner scnr = new Scanner(System.in);

        // set heuristic and cost
        Function<AbstractBranchingPlayer, Double> heuristic = (p) -> scrabbleHeuristic(p);
        BiFunction<AbstractBranchingPlayer, AbstractBranchingPlayer, Double> cost = (p, q) -> scrabbleCost(p, q);

        CheatBroker initialCheatBroker = new CheatBroker(new Hand());
        AbstractBranchingPlayer branchablePlayer = new BranchingPlayerSerial(null, new DefaultGrid(), initialCheatBroker);
        CheatPlayerManualAStarWrapper cheatPlayer = new CheatPlayerManualAStarWrapper(AStarArrayList.class, branchablePlayer, heuristic, cost);

        System.out.println("Please put in the initial bananagrams characters you drew:");
        Hand initialHand = new Hand(scnr.nextLine().toCharArray());
        initialCheatBroker.setHand(initialHand);

        while (true) {
            System.out.println("Press enter to solve");
            scnr.nextLine();
            System.out.println("Solving...");
            long startTimeMilliseconds = System.currentTimeMillis();
            cheatPlayer.playSolution();
            System.out.println("Time for this step: " + (System.currentTimeMillis() - startTimeMilliseconds));
            // display the result now that player has updated to solution
            System.out.println("Hand: " + cheatPlayer.getBroker().getHand());
            System.out.println("Grid: \n" + cheatPlayer.getPlayer().getGrid());

            System.out.println("Enter any new tiles added to hand");
            char[] newChars = scnr.nextLine().toCharArray();
            for (char c: newChars) {cheatPlayer.getBroker().forceAddTile(new Tile(c));}
            // new tiles are updated so we must update the astar
        }
    }
}
