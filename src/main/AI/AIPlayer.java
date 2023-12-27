package src.main.AI;

import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.Function;
import java.util.function.BiFunction;

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

    public AIPlayer(Game game, Grid grid, TileBag bag, MultiSet<Tile> hand) {
        super(game, grid, bag, hand);
    }

    public AIPlayer copy() { // TODO make it possible to copy multiple types of players? Should deep copy game too?
        // TODO make copying nicer and make copyable interface.
        return new AIPlayer(game, new Grid(getGrid()), getBag(), this.getHand().copy()); // TODO does AI Player even need a bag? dump is a last resort in this configuration
    }

    // TODO kind of odd place for this function. Maybe test this?
    private ArrayList<Integer> indexesOf(String word, String sub) {
        ArrayList<Integer> out = new ArrayList<>();
        int start = 0;
        while (start!=-1) {
            int newIndex = word.indexOf(sub, start);
            if (newIndex==-1) {break;}; // if new index is "real index" add
            out.add(newIndex);
            start = newIndex+sub.length();
        }
        return out;
    }

    // TODO maybe this could be broken up, might require a small helper package class. Also document
    // TODO instead of duplicating the nextPlayer within this classe should the class act on an 
    // existing player and we copy the player outside of this class?
    private AIPlayer placeWord(String word, Location wordStartLoc, byte direction) {
        Location cursor = new Location(wordStartLoc);

        AIPlayer nextPlayer = this.copy();
        MultiSet<Tile> nextHand = nextPlayer.getHand();
        Grid nextGrid = nextPlayer.getGrid();

        String currentGridFragment = ""; // represents the letters in the word from grid we are looking over
        for (int i=0; i<word.length(); ++i) {
            if (nextGrid.locationFilled(cursor)) { // if this location is filled add to the word fragment to check is substring
                currentGridFragment += nextGrid.getTile(cursor);
            }
            else { // if this location is blank the currentGridFragment is done so we check it
                // check fragment, can't play word if not. If fragment blank branch is always false
                if (!word.substring(i-currentGridFragment.length(), i).equals(currentGridFragment)) {
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
        
        return nextPlayer;
    }

    public Set<AIPlayer> branch_forward_single_direction(byte direction) {
        BiFunction<Grid, Location, String> getGridFragmentFunction;
        Function<Grid, HashSet<Location>> startLocationsFunction;
        BiFunction<Location, Integer, Location> wordStartLocationFunction;
        if (direction==0) {
            getGridFragmentFunction = (g, loc) -> g.getRightFragment(loc);
            startLocationsFunction = (g) -> g.rightStartLocations();
            wordStartLocationFunction = (loc, idx) -> new Location(loc.getRow(), loc.getColumn()-idx);
        }
        else if (direction==1) {
            getGridFragmentFunction = (g, loc) -> g.getDownFragment(loc);
            startLocationsFunction = (g) -> g.downStartLocations();
            wordStartLocationFunction = (loc, idx) -> new Location(loc.getRow()-idx, loc.getColumn());
        }
        else {throw new IllegalArgumentException("Direction must be 0 for right or 1 for down");}

        Set<AIPlayer> branchedPlayers = new HashSet<>();
        HashSet<Grid> foundGrids = new HashSet<>(); // some duplicates may be found but set rids that

        Grid grid = this.getGrid();
        for (Location loc: startLocationsFunction.apply(grid)) {
            String gridFragment = getGridFragmentFunction.apply(grid, loc);

            for (String candidateWord: grid.getWordsSet()) {
                if (candidateWord.equals(gridFragment)) {continue;} // EDGE CASE: don't consider word already there, nothing will change
                ArrayList<Integer> idxs = indexesOf(candidateWord, gridFragment);
                // below will usually be empty or only one index with execption of something like eye where e can be on either side
                for (int idx: idxs) { 
                    // given loc on grid we know the idx letter of the word goes there, so can get start of word
                    Location startWordLocation = wordStartLocationFunction.apply(loc, idx); // this function gets location of start of word based on direction
                    AIPlayer nextPlayer = placeWord(candidateWord, startWordLocation, direction);
                    // do not include if grid has been seen, no grid is present, OR the new work incidentally made the grid invalid
                    if (nextPlayer!=null && !foundGrids.contains(nextPlayer.getGrid()) && nextPlayer.getGrid().valid()) {
                        foundGrids.add(nextPlayer.getGrid());
                        branchedPlayers.add(nextPlayer);
                    }
                }
            }
        }

        return branchedPlayers;
    }

    public Set<AIPlayer> branch_forward() {
        Set<AIPlayer> out = branch_forward_single_direction((byte) 1);
        out.addAll(branch_forward_single_direction((byte) 0)); // just combine both directions
        return out;
    }

    public Set<AIPlayer> branch_backward() {
        return null; // STUB ("backward" meaning removing words and tiles)
    }

    @Override
    public Set<AIPlayer> branch() {
        return null; // TODO STUB, will combine forward and backward branchinh
    }
}
