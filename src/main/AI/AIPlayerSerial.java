package src.main.AI;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.Function;
import java.util.function.BiFunction;
import src.main.game.Tile;
import src.main.game.TileBag;
import src.main.game.Game;
import src.main.game.Grid;
import src.main.game.Location;
import src.data_structures.MultiSet;

public class AIPlayerSerial extends AIPlayer<AIPlayerSerial> {
    
    public AIPlayerSerial(Game game, Grid grid, TileBag bag) {
        super(game, grid, bag);
    }

    public AIPlayerSerial(Game game, Grid grid, TileBag bag, MultiSet<Tile> hand) {
        super(game, grid, bag, hand);
    }

    public AIPlayerSerial copy() {
        // TODO make copying nicer and make copyable interface.
        return new AIPlayerSerial(game, new Grid(getGrid()), getBag(), this.getHand().copy());
    }

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

    private AIPlayerSerial placeWord(String word, Location wordStartLoc, byte direction) {
        Location cursor = new Location(wordStartLoc);
        AIPlayerSerial nextPlayer = this.copy();
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

    public Set<AIPlayerSerial> branchForwardSingleDirection(byte direction) {
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
        Set<AIPlayerSerial> branchedPlayers = new HashSet<>();
        HashSet<Grid> foundGrids = new HashSet<>(); // some duplicates may be found but set rids that
        for (Location loc: startLocations) {
            String gridFragment = getGridFragmentFunction.apply(grid, loc);
            for (String candidateWord: grid.getWordsSet()) {
                if (candidateWord.equals(gridFragment)) {continue;} // EDGE CASE: don't consider word already there, nothing will change
                ArrayList<Integer> idxs = indexesOf(candidateWord, gridFragment);
                // below will usually be empty or only one index with execption of something like eye where e can be on either side
                for (int idx: idxs) { 
                    // given loc on grid we know the idx letter of the word goes there, so can get start of word
                    Location startWordLocation = wordStartLocationFunction.apply(loc, idx); // this function gets location of start of word based on direction
                    AIPlayerSerial nextPlayer = placeWord(candidateWord, startWordLocation, direction);
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

    public Set<AIPlayerSerial> branchForward() {
        Set<AIPlayerSerial> out = branchForwardSingleDirection((byte) 1);
        out.addAll(branchForwardSingleDirection((byte) 0)); // just combine both directions
        return out;
    }

    private AIPlayerSerial removeWord(Location loc, byte direction) {
        AIPlayerSerial nextPlayer = this.copy();
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

    public Set<AIPlayerSerial> branchBackwardSingleDirection(byte direction) {
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
        
        Set<AIPlayerSerial> branchedPlayers = new HashSet<>();
        for (Location loc: startLocations) { // get starts of words to remove
            AIPlayerSerial nextPlayer = removeWord(loc, direction);
            if (nextPlayer!=null && !foundGrids.contains(nextPlayer.getGrid())) {
                branchedPlayers.add(nextPlayer);
                foundGrids.add(nextPlayer.getGrid());
            }
        }
        return branchedPlayers;
    }

    public Set<AIPlayerSerial> branchBackward() {
        Set<AIPlayerSerial> out = branchBackwardSingleDirection((byte) 1);
        out.addAll(branchBackwardSingleDirection((byte) 0)); // just combine both directions
        return out;
    }
  
    // similar to place word, but with no extra checks of preexisting letters and no stipulation of connection.
    // always placed at the origin
    private AIPlayerSerial placeFirstWord(String word, byte direction) {
        AIPlayerSerial nextPlayer = this.copy();
        Location cursor = new Location(0, 0);
        Grid grid = nextPlayer.getGrid();
        MultiSet<Tile> hand = nextPlayer.getHand();
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

    public Set<AIPlayerSerial> branchEmpty() {
        Set<AIPlayerSerial> branchedPlayers = new HashSet<>();
        for (String candidateWord: this.getGrid().getWordsSet()) {
            // below works, but may be slightly inefficient because placeWord has additional checks not needed 
            AIPlayerSerial nextRightPlayer = placeFirstWord(candidateWord, (byte) 0);
            if (nextRightPlayer!=null) {
                branchedPlayers.add(nextRightPlayer);
            }
            AIPlayerSerial nextDownPlayer = placeFirstWord(candidateWord, (byte) 1);
            if (nextDownPlayer!=null) {
                branchedPlayers.add(nextDownPlayer);
            }
        }
        return branchedPlayers;
    }
    @Override
    public Set<AIPlayerSerial> branch() {
        if (this.getGrid().isEmpty()) {
            return branchEmpty(); // edge case for when the grid is empty
        }
        Set<AIPlayerSerial> out = branchForward();
        out.addAll(branchBackward()); // just combine both directions
        return out;
    }
}