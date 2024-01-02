package src.main.AI;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.List;
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
    private ExecutorService executorService = Executors.newFixedThreadPool(2); // TODO look into number of threads more

    public AIPlayer(Game game, Grid grid, TileBag bag) {
        super(game, grid, bag);
    }

    public AIPlayer(Game game, Grid grid, TileBag bag, MultiSet<Tile> hand) {
        super(game, grid, bag, hand);
    }

    public AIPlayer copy() { // TODO make it possible to copy multiple types of players? Should deep copy game too?
        // TODO make copying nicer and make copyable interface.
        return new AIPlayer(game, this.getGrid().copy(), getBag(), this.getHand().copy()); // TODO does AI Player even need a bag? dump is a last resort in this configuration
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

    public Set<AIPlayer> branchForwardSingleDirection(byte direction) {
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

        Set<AIPlayer> branchedPlayers = new HashSet<>();
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
                    AIPlayer nextPlayer = placeWord(candidateWord, startWordLocation, direction);
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

    public Set<AIPlayer> branchForward() {
        List<Callable<Set<AIPlayer>>> tasks = new ArrayList<>();
        tasks.add(() -> branchForwardSingleDirection((byte) 1));
        tasks.add(() -> branchForwardSingleDirection((byte) 0)); // just combine both directions

        // just adds the two sets from the threads
        Set<AIPlayer> out = new HashSet<>();
        try {
            List<Future<Set<AIPlayer>>> futures = executorService.invokeAll(tasks);
            for (Future<Set<AIPlayer>> p: futures) {
                try {
                    out.addAll(p.get());
                }
                catch (InterruptedException| ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        return out;
    }

    private AIPlayer removeWord(Location loc, byte direction) {
        AIPlayer nextPlayer = this.copy();
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

    public Set<AIPlayer> branchBackwardSingleDirection(byte direction) {
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
        
        Set<AIPlayer> branchedPlayers = new HashSet<>();
        for (Location loc: startLocations) { // get starts of words to remove
            AIPlayer nextPlayer = removeWord(loc, direction);
            if (nextPlayer!=null && !foundGrids.contains(nextPlayer.getGrid())) {
                branchedPlayers.add(nextPlayer);
                foundGrids.add(nextPlayer.getGrid());
            }
        }

        return branchedPlayers;
    }

    public Set<AIPlayer> branchBackward() {
        Set<AIPlayer> out = branchBackwardSingleDirection((byte) 1);
        out.addAll(branchBackwardSingleDirection((byte) 0)); // just combine both directions
        return out;
    }
  
    // similar to place word, but with no extra checks of preexisting letters and no stipulation of connection.
    // always placed at the origin
    private AIPlayer placeFirstWord(String word, byte direction) {
        AIPlayer nextPlayer = this.copy();
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

    public Set<AIPlayer> branchEmpty() {
        Set<AIPlayer> branchedPlayers = new HashSet<>();
        for (String candidateWord: this.getGrid().getWordsSet()) {
            // below works, but may be slightly inefficient because placeWord has additional checks not needed 
            AIPlayer nextRightPlayer = placeFirstWord(candidateWord, (byte) 0);
            if (nextRightPlayer!=null) {
                branchedPlayers.add(nextRightPlayer);
            }

            AIPlayer nextDownPlayer = placeFirstWord(candidateWord, (byte) 1);
            if (nextDownPlayer!=null) {
                branchedPlayers.add(nextDownPlayer);
            }
        }
        return branchedPlayers;
    }

    @Override
    public Set<AIPlayer> branch() {
        if (this.getGrid().isEmpty()) {
            return branchEmpty(); // edge case for when the grid is empty
        }
        Set<AIPlayer> out = branchForward();
        out.addAll(branchBackward()); // just combine both directions
        return out;
    }
}
