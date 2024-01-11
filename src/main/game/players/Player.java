package src.main.game.players;

import src.data_structures.MultiSet;
import src.main.game.Location;
import src.main.game.Game;
import src.main.game.Grid;
import src.main.game.Tile;
import src.main.game.TileBag;
import src.main.game.WordsSet;
import src.main.game.Copyable;

public class Player implements Copyable<Player> {
    GridArranger gridArranger;
    HumanBroker broker; // TODO needs to be some extension a general broker?
    Game game;

    protected Player() {} // does nothing, allowing extending classes to set/change protected variables
    
    // used by extending classes to use other gridArrangers and brokers TODO should this be an interface then?
    protected Player(GridArranger gridArranger, HumanBroker broker) {
        this.gridArranger = gridArranger;
        this.broker = broker;
    }
    // TODO many constructors? Hand maybe should be hidden from outside world and initialized with MultiSet<Tile>
    public Player(Game game, WordsSet wordsSet, TileBag bag, Hand hand) {
        gridArranger = new GridArranger(new Grid(wordsSet), hand);
        broker = new HumanBroker(hand, bag);
    }

    public Player(Game game, WordsSet wordsSet, TileBag bag, MultiSet<Tile> handSet) {
        Hand hand = new Hand(handSet);
        gridArranger = new GridArranger(new Grid(wordsSet), hand);
        broker = new HumanBroker(hand, bag);
    }

    public Player(Game game, Grid grid, TileBag bag, Hand hand) {
        gridArranger = new GridArranger(grid, hand);
        broker = new HumanBroker(hand, bag);
    }

    public Player(Game game, Grid grid, TileBag bag, MultiSet<Tile> handSet) {
        Hand hand = new Hand(handSet);
        gridArranger = new GridArranger(grid, hand);
        broker = new HumanBroker(hand, bag);
    }
    
    public Player(Game game, Grid grid, TileBag bag) {
        Hand hand = new Hand();
        gridArranger = new GridArranger(grid, hand);
        broker = new HumanBroker(hand, bag);
    }

    @Override
    public Player copy() {
        return new Player(gridArranger.copy(), broker.copy());
    }

    public void setGame(Game g) {
        game = g;
    }

    public Hand getHand() {
        return broker.getHand();
    }

    @Override
    public String toString() {
        return "";
    }   

    /**
     * If all of the tiles the player has have been made into a proper 
     * set of words on the Grid. In other words, they have a valid grid and nothing 
     * else to place.
     * @return boolean if the players has used all their tiles properly
     */
    public boolean canPeel() {
        return (getHand().handEmpty() && gridArranger.gridValid());
    }
    
    public Tile grabTile() {return broker.grabTile();}

    public boolean grabTile(int num) {return broker.grabTile(num);}

    public TileBag getBag() {return broker.getBag();}

    protected Game getGame() {return game;}

    public Grid getGrid() {return gridArranger.getGrid();}

    public void placeTile(Location loc, Tile tile) {gridArranger.placeTile(loc, tile);}

    public void removeTile(Location loc) {gridArranger.removeTile(loc);}

    public void clearGrid() {gridArranger.clearGrid();}

    public boolean gridValid() {return gridArranger.gridValid();}

}
