package src.main.game.players.AIPlayers.BranchPlayers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

import src.main.game.Game;
import src.main.game.Grid;
import src.main.game.Location;
import src.main.game.Tile;
import src.main.game.TileBag;
import src.main.game.players.Hand;

// visibility is only for other AIPlayer within this folder to use
abstract class AbstractPlayerBranchingMethods<ExtendingBranchPlayer extends AbstractPlayerBranchingMethods<ExtendingBranchPlayer>> extends AbstractBranchingPlayer<ExtendingBranchPlayer> {

    public AbstractPlayerBranchingMethods(Game game, Grid grid, TileBag bag) {
        super(game, grid, bag);
    }

    public AbstractPlayerBranchingMethods(Game game, Grid grid, TileBag bag, Hand hand) {
        super(game, grid, bag, hand);
    }

    public abstract ExtendingBranchPlayer copy();

    protected ArrayList<Integer> indexesOf(String word, String sub) {
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

    protected ExtendingBranchPlayer placeWord(String word, Location wordStartLoc, byte direction) {
        Location cursor = new Location(wordStartLoc);

        ExtendingBranchPlayer nextPlayer = this.copy();
        Hand nextHand = nextPlayer.getHand();
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

    public Set<ExtendingBranchPlayer> branchForwardSingleDirection(byte direction) {
        BiFunction<Grid, Location, String> getGridFragmentFunction;
        HashSet<Location> startLocations;
        BiFunction<Location, Integer, Location> wordStartLocationFunction;
        Grid grid = this.getGrid();
        if (direction==0) {
            getGridFragmentFunction = (g, loc) -> g.getRightFragment(loc);
            startLocations = grid.rightStartLocations();
            wordStartLocationFunction = (loc, idx) -> new Location(loc.getRow(), loc.getColumn()-idx);
        }
        else if (direction==1) {
            getGridFragmentFunction = (g, loc) -> g.getDownFragment(loc);
            startLocations = grid.downStartLocations();
            wordStartLocationFunction = (loc, idx) -> new Location(loc.getRow()-idx, loc.getColumn());
        }
        else {throw new IllegalArgumentException("Direction must be 0 for right or 1 for down");}

        Set<ExtendingBranchPlayer> branchedPlayers = new HashSet<>();
        HashSet<Grid> foundGrids = new HashSet<>(); // some duplicates may be found but set rids that
        startLocations.stream().parallel().forEach(loc -> {
            String gridFragment = getGridFragmentFunction.apply(grid, loc);
            grid.getWordsSet().stream().parallel().forEach(candidateWord -> {
                if (candidateWord.equals(gridFragment)) {return;} // EDGE CASE: don't consider word already there, nothing will change
                ArrayList<Integer> idxs = indexesOf(candidateWord, gridFragment);
                // below will usually be empty or only one index with execption of something like eye where e can be on either side
                for (int idx: idxs) { 
                    // given loc on grid we know the idx letter of the word goes there, so can get start of word
                    Location startWordLocation = wordStartLocationFunction.apply(loc, idx); // this function gets location of start of word based on direction
                    ExtendingBranchPlayer nextPlayer = placeWord(candidateWord, startWordLocation, direction);
                    // do not include if grid has been seen, no grid is present, OR the new work incidentally made the grid invalid
                    if (nextPlayer!=null && !foundGrids.contains(nextPlayer.getGrid()) && nextPlayer.getGrid().valid()) {
                        foundGrids.add(nextPlayer.getGrid());
                        branchedPlayers.add(nextPlayer);
                    }
                }
            });
        });

        return branchedPlayers;
    }

    protected abstract Set<ExtendingBranchPlayer> branchForward();

    protected ExtendingBranchPlayer removeWord(Location loc, byte direction) {
        ExtendingBranchPlayer nextPlayer = this.copy();
        Grid grid = nextPlayer.getGrid();
        Function<Location, Location> move;
        Function<Location, Boolean> connectedToOtherWord;
        if (direction==0) {
            move = (cursor) -> cursor.right();
            connectedToOtherWord = (cursor) -> (grid.locationFilled(cursor.above()) || grid.locationFilled(cursor.below()));
        }
        else if (direction==1) {
            move = (cursor) -> cursor.below();
            connectedToOtherWord = (cursor) -> (grid.locationFilled(cursor.left()) || grid.locationFilled(cursor.right()));
        }
        else {throw new IllegalArgumentException("Direction must be 0 for right or 1 for down");}

        Location cursor = loc;
        while (grid.locationFilled(cursor)) {
            if (!connectedToOtherWord.apply(cursor)) {
                Tile t = grid.remove(cursor);
                nextPlayer.getHand().add(t);
            }
            cursor = move.apply(cursor);
        }
        if (!nextPlayer.gridValid()) {return null;}
        return nextPlayer;
    }

    public Set<ExtendingBranchPlayer> branchBackwardSingleDirection(byte direction) {
        HashSet<Location> startLocations;
        Grid grid = this.getGrid();
        HashSet<Grid> foundGrids = new HashSet<>();
        foundGrids.add(grid);
        if (direction==0) {
            startLocations = grid.rightStartLocations();
        }
        else if (direction==1) {
            startLocations = grid.downStartLocations();
        }
        else {throw new IllegalArgumentException("Direction must be 0 for right or 1 for down");}
        
        Set<ExtendingBranchPlayer> branchedPlayers = new HashSet<>();
        for (Location loc: startLocations) { // get starts of words to remove
            ExtendingBranchPlayer nextPlayer = removeWord(loc, direction);
            if (nextPlayer!=null && !foundGrids.contains(nextPlayer.getGrid())) {
                branchedPlayers.add(nextPlayer);
                foundGrids.add(nextPlayer.getGrid());
            }
        }

        return branchedPlayers;
    }

    public Set<ExtendingBranchPlayer> branchBackward() {
        Set<ExtendingBranchPlayer> out = branchBackwardSingleDirection((byte) 1);
        out.addAll(branchBackwardSingleDirection((byte) 0)); // just combine both directions
        return out;
    }
  
    // similar to place word, but with no extra checks of preexisting letters and no stipulation of connection.
    // always placed at the origin
    protected ExtendingBranchPlayer placeFirstWord(String word, byte direction) {
        ExtendingBranchPlayer nextPlayer = this.copy();
        Location cursor = new Location(0, 0);
        Grid grid = nextPlayer.getGrid();
        Hand hand = nextPlayer.getHand();
        Function<Location, Location> move;
        if (direction==0) {
            move = (loc) -> loc.right();
        }
        else if (direction==1) {
            move = (loc) -> loc.below();
        }
        else {throw new IllegalArgumentException("Direction must be 0 for right or 1 for down");}

        for (Character c: word.toCharArray()) {
            Tile t = new Tile(c);
            if (!hand.contains(t)) {
                return null; // if hand doesn't contain letter return nothing since valid grid can't be made
            } 
            grid.placeUnsafe(cursor, t);
            hand.remove(t);
            cursor = move.apply(cursor); // do not have to check if the grid is valid because this is the only word
        }

        return nextPlayer;
    }

    public Set<ExtendingBranchPlayer> branchEmpty() {
        Set<ExtendingBranchPlayer> branchedPlayers = new HashSet<>();
        for (String candidateWord: this.getGrid().getWordsSet()) {
            // below works, but may be slightly inefficient because placeWord has additional checks not needed 
            ExtendingBranchPlayer nextRightPlayer = placeFirstWord(candidateWord, (byte) 0);
            if (nextRightPlayer!=null) {
                branchedPlayers.add(nextRightPlayer);
            }

            ExtendingBranchPlayer nextDownPlayer = placeFirstWord(candidateWord, (byte) 1);
            if (nextDownPlayer!=null) {
                branchedPlayers.add(nextDownPlayer);
            }
        }
        return branchedPlayers;
    }

    @Override
    public Set<ExtendingBranchPlayer> branch() {
        if (this.getGrid().isEmpty()) {
            return branchEmpty(); // edge case for when the grid is empty
        }
        Set<ExtendingBranchPlayer> out = branchForward();
        out.addAll(branchBackward()); // just combine both directions
        return out;
    }   
}
