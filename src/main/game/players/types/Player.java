package src.main.game.players.types;

import src.main.game.Location;
import src.data_structures.MultiSet;
import src.interfaces.Copyable;
import src.interfaces.TileBagable;
import src.main.game.Game;
import src.main.game.Tile;
import src.main.game.grids.Grid;
import src.main.game.players.brokers.AbstractBroker;
import src.main.game.players.gridarrangers.GridArranger;
import src.main.game.players.hand.Hand;

public class Player implements Copyable<Player> {
    protected Game game;
    protected GridArranger gridArranger;
    protected AbstractBroker broker;

    protected Player() {} // does nothing, allowing extending classes to set/change gridArranger/broker

    public Player(Game game, GridArranger gridArranger, AbstractBroker broker) {
        this.game = game; 
        this.gridArranger = gridArranger;
        this.broker = broker;
    }

    @Override
    /**
     * Does a deep copy EXCEPT for the game object within player. Thus,
     * the new player will have a new gridArranger and grid, hand, and a broker object.
     * This copy will still be conneted to the same game.
     * @return Player copy of this
     */
    public Player copy() {
        GridArranger newGridArranger = gridArranger.copy();
        AbstractBroker newBroker = broker.copy();
        Hand newHand = this.getHand().copy(); // this way both broker and gridArranger have same hand
        newGridArranger.setHand(newHand);
        newBroker.setHand(newHand);
        
        return new Player(this.game, newGridArranger, newBroker);
    }

    public void setGame(Game g) {
        game = g;
    }

    public Hand getHand() {
        return broker.getHand();
    }

    public TileBagable getBag() { 
        return broker.getBag();
    }

    /***
     * If all of the tiles the player has have been made into a proper 
     * set of words on the Grid. In other words, they have a valid grid and nothing 
     * else to place.
     * @return boolean if the players has used all their tiles properly
     */
    public boolean canPeel() {
        return (getHand().handEmpty() && gridArranger.gridValid());
    }
    
    /***
     * Grabs a tile from the shared tile bag.
     * This tile is placed into the hand of the player and returned.
     * 
     * This method delegated to the broker.
     * @return The grabbed tile from the bag
     */
    public Tile grabTile() {return broker.grabTile();}

    /***
     * Grabs num tiles from the hand. 
     * All tiles grabbed are placed into the hand
     * 
     * This method is delegated to the broker.
     * @param num
     * @return
     */
    public boolean grabTile(int num) {return broker.grabTile(num);}

    /***
     * Gets a multiset of the tiles this player has.
     * This includes both the tiles on the grid and the tiles in hand.
     * @return MultiSet<Tile> of all tiles this player has
     */
    public MultiSet<Tile> getAllTiles() {
        MultiSet<Tile> gridTiles = this.getGrid().getTiles();
        MultiSet<Tile> copyTiles = gridTiles.copy();
        copyTiles.addAll(this.getHand().getMultiSet());
        return copyTiles;
    }

    protected Game getGame() {return game;}

    public Grid getGrid() {return gridArranger.getGrid();}

    /***
     * Places a given tile from the grid at the given location. This is done through
     * the grid arranger of the player. There are two safety checks in place
     * that are done through the placeTile method of the gridArranger. 
     * Safety Checks:
     *  1. The tile cannot be placed if the location on the grid is filled
     *  2. The tile cannot be placed if that tile is not present with the player's hand
     * The gridArranger method called will throw an IllegalArgumentException, which is not caught
     * by this method. 
     * @param loc Location object to place the tile
     * @param tile Tile to place. This uses equals, so the exact object from hand does not need to be used 
     */
    public void placeTile(Location loc, Tile tile) {gridArranger.placeTile(loc, tile);}

    /***
     * Removes a tile from the grid at the given location. 
     * This tile is placed back into the hand of the player (since in the game, a player would just be reconfiguring the grid).
     * This has only one safety check, if the location is empty.
     * Safety Checks:
     *  1. If location is empty
     * If this safety check fails there will be an uncaught IllegalArgumentException.
     * @param loc
     */
    public void removeTile(Location loc) {gridArranger.removeTile(loc);}

    /***
     * Clears the grid, removing all tiles. 
     * All tiles are placed back into the hand of the player, again delegating to the gridArranger object.
     */
    public void clearGrid() {gridArranger.clearGrid();}

    /***
     * Determines if the current grid is valid. This just is if it is a valid state to peel or win the game.
     * 
     * This is delegated to the gridArranger object
     * @return If the grid of this player is valid.
     */
    public boolean gridValid() {return gridArranger.gridValid();}

    /***
     * Allows a player to play an entire grid given (checking that the player has the proper tiles).
     * This plays the entire grid given, essentially makin 
     * @param g
     */
    public boolean playGrid(Grid g) { 
        if (!this.getAllTiles().equals(g.getTiles())) {
            return false; // if hand cannot be played on the grid
        }
        // now we know the player has the tiles to play this grid
        this.clearGrid(); // puts all tiles in player's hand to play on grid
        for (Location loc: g.getFilledSquares().keySet()) {
            Tile t = g.getTile(loc);

            // place tile on grid (removing it from hand)
            this.placeTile(loc, t);
        }
        return true; // in this case it was successful
    }

}
