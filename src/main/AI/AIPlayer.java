package src.main.AI;

import java.util.Set;
import java.util.ArrayList;

import src.main.game.Player;
import src.main.game.Tile;
import src.main.game.TileBag;
import src.main.game.Game;
import src.main.game.Grid;
import src.main.game.Location;
import src.data_structures.MultiSet;

public class AIPlayer extends Player implements Branchable<AIPlayer> {
    
    public AIPlayer(Game game, Grid grid, TileBag bag) {
        super(game, grid, bag);
    }

    // TODO kind of odd place for this function. Maybe test this?
    private ArrayList<Integer> indexesOf(String word, String sub) {
        ArrayList<Integer> out = new ArrayList<>();
        int start = 0;
        while (start!=-1) {
            int newIndex = word.indexOf(sub, 0);
            out.add(newIndex);
            start = newIndex;
        }
        return out;
    }

    // TODO maybe this could be broken up, might require a small helper package class. Also document
    private Grid placeWord(String word, Location wordStartLoc, int direction) {
        Location cursor = new Location(wordStartLoc);

        Player nextPlayer = new AIPlayer(this.game, getGrid(), getBag());
        MultiSet<Tile> nextHand = nextPlayer.getHand();
        Grid nextGrid = new Grid(this.getGrid());

        String currentGridFragment = ""; // represents the letters in the word from grid we are looking over
        for (int i=0; i<word.length(); ++i) {
            if (nextGrid.locationFilled(cursor)) { // if this location is filled add to the word fragment to check is substring
                currentGridFragment += nextGrid.getTile(cursor);
            }
            else { // if this location is blank the currentGridFragment is done so we check it
                // check fragment, can't play word if not. If fragment blank branch is always false
                if (!word.substring(i-currentGridFragment.length(), i+1).equals(currentGridFragment)) {
                    return null; // no grid can be made
                }
                currentGridFragment = ""; // if the grid fragment words, reset it since we now have blank tiles

                // check this letter in the word is within the players hand since it isn't already on board
                if (!nextHand.contains(new Tile(word.charAt(i)))) {
                    return null; // if hand doesn't have needed letter can't play the word
                }      
                // since this letter can be placed from hand put it in grid, and take from hand
                Tile nextPlaced = new Tile(word.charAt(i));
                nextHand.remove(nextPlaced); 
                nextGrid.placeUnsafe(cursor, nextPlaced);
            }

            // move cursor along proposed word placement
            if (direction==0) {cursor = cursor.right();}
            else if (direction==1) {cursor = cursor.below();}
        }
        
        return nextGrid;
    }

    // TODO This might be kind of inefficient  
    public Set<AIPlayer> branch_forward() {
        ArrayList<Grid> branchedGrids = new ArrayList<>();

        // TODO just goes DOWN right now
        Grid grid = this.getGrid();
        for (Location loc: grid.downStartLocations()) {
            String gridFragment = grid.getDownFragment(loc);

            for (String candidateWord: grid.getWordsSet()) {
                ArrayList<Integer> idxs = indexesOf(candidateWord, gridFragment);
                // below will usually be empty or only one index with execption of something like eye where e can be on either side
                for (Integer idx: idxs) { 
                    // given loc on grid we know the idx letter of the word goes there, so can get start of word
                    Location startWordLocation = new Location(loc.getRow()-idx, loc.getColumn());
                    Grid nextGrid = placeWord(candidateWord, startWordLocation, 1);
                    if (nextGrid!=null) {
                        branchedGrids.add(nextGrid);
                    }
                }
            }

        }
        return null; // STUB
    }

    public Set<AIPlayer> branch_backward() {
        return null; // STUB ("backward" meaning removing words and tiles)
    }

    @Override
    public Set<AIPlayer> branch() {
        return null; // TODO STUB, will combine forward and backward branchinh
    }
}
