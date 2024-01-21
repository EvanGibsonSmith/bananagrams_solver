package src.main.run;

import java.util.Scanner;

import src.algorithms.astar.AStarArrayList;
import src.main.game.Tile;
import src.main.game.AIplayerwrappers.CheatPlayerWrapper;
import src.main.game.grids.DefaultGrid;
import src.main.game.players.brokers.CheatBroker;
import src.main.game.players.hand.Hand;
import src.main.game.players.types.branchplayers.AbstractBranchingPlayer;
import src.main.game.players.types.branchplayers.BranchingPlayerSerial;

public class RunCheatPlayer {
    
    public static void main(String[] args) throws Exception{
        // TODO use scanner to make the cheat player easy to use here with user input
        Scanner scnr = new Scanner(System.in);

        CheatBroker initialCheatBroker = new CheatBroker(new Hand());
        AbstractBranchingPlayer branchablePlayer = new BranchingPlayerSerial(null, new DefaultGrid(), initialCheatBroker);
        CheatPlayerWrapper cheatPlayer = new CheatPlayerWrapper(AStarArrayList.class, branchablePlayer);

        System.out.println("Please put in the initial bananagrams characters you drew:");
        Hand initialHand = new Hand(scnr.nextLine().toCharArray());
        initialCheatBroker.setHand(initialHand);

        while (true) {
            System.out.println("Press enter to solve");
            scnr.nextLine();
            System.out.println("Solving...");
            cheatPlayer.playSolution();
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