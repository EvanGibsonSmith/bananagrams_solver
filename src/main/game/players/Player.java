package src.main.game.players;

import src.data_structures.MultiSet;
import src.main.game.Location;
import src.main.game.Game;
import src.main.game.Grid;
import src.main.game.Tile;
import src.main.game.TileBagable;
import src.main.game.NormalTileBag;
import src.main.game.WordsSet;
import src.main.game.Copyable;

// TODO make this a "non broker player" or something? Maybe seperate broker implementor or allow extending classes to do it themselves?
public class Player implements Copyable<Player> {
    protected GridArranger gridArranger; // TODO maybe broker could be better and not have to be passed to extending classes
    protected AbstractBroker broker; // TODO needs to be some extension a general broker?
    Game game;

    // TODO player doesn't handle broker at all except in protected constructor? Maybe rename or rework.
    protected Player() {} // does nothing, allowing extending classes to set/change protected variables
    
    // used by extending classes to use other gridArrangers and brokers TODO should this be an interface then?
    protected Player(GridArranger gridArranger, AbstractBroker broker) {
        this.gridArranger = gridArranger;
        this.broker = broker;
    }

    // TODO so many of these constructors DON'T USE GAME. Fix this
    // TODO many constructors? Hand maybe should be hidden from outside world and initialized with MultiSet<Tile>
    public Player(Game game, WordsSet wordsSet, TileBagable bag, Hand hand) {
        gridArranger = new GridArranger(new Grid(wordsSet), hand);
        // TODO make below abstract broker with broker factory?
        //broker = new HumanBroker(hand, bag); // can be any implementation of abstract broker, but is HumanBroker by default
    }

    public Player(Game game, WordsSet wordsSet, TileBagable bag, MultiSet<Tile> handSet) {
        Hand hand = new Hand(handSet);
        gridArranger = new GridArranger(new Grid(wordsSet), hand);
    }

    public Player(Game game, Grid grid, TileBagable bag, Hand hand) {
        gridArranger = new GridArranger(grid, hand);
    }

    public Player(Game game, Grid grid, TileBagable bag, MultiSet<Tile> handSet) {
        Hand hand = new Hand(handSet);
        gridArranger = new GridArranger(grid, hand);
    }
    
    public Player(Game game, Grid grid, TileBagable bag) {
        Hand hand = new Hand();
        gridArranger = new GridArranger(grid, hand);
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

    // TODO type needs to be more specific again. Also not sure if I want this in player.
    // It is currently being used in GameTest
    public TileBagable getBag() { 
        return broker.getBag();
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

    protected Game getGame() {return game;}

    public Grid getGrid() {return gridArranger.getGrid();}

    public void placeTile(Location loc, Tile tile) {gridArranger.placeTile(loc, tile);}

    public void removeTile(Location loc) {gridArranger.removeTile(loc);}

    public void clearGrid() {gridArranger.clearGrid();}

    public boolean gridValid() {return gridArranger.gridValid();}

}
