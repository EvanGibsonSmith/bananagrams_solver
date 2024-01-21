package src.main.game.players.types;

import src.data_structures.MultiSet;
import src.main.game.Location;
import src.main.game.Game;
import src.main.game.Tile;
import src.main.game.grids.Grid;
import src.main.game.players.bags.NormalTileBag;
import src.main.game.players.brokers.HumanBroker;
import src.main.game.players.gridarrangers.GridArranger;
import src.main.game.players.hand.Hand;
import src.main.game.wordssets.WordsSet;

public class HumanPlayer extends Player {
    protected GridArranger gridArranger; // TODO maybe broker could be better and not have to be passed to extending classes
    protected HumanBroker broker; // TODO needs to be some extension a general broker?
    Game game;
    
    // used by extending classes to use other gridArrangers and brokers TODO should this be an interface then?
    protected HumanPlayer(GridArranger gridArranger, HumanBroker broker) {
        this.gridArranger = gridArranger;
        this.broker = broker;
    }
    // TODO many constructors? Hand maybe should be hidden from outside world and initialized with MultiSet<Tile>
    public HumanPlayer(Game game, WordsSet wordsSet, NormalTileBag bag, Hand hand) {
        gridArranger = new GridArranger(new Grid(wordsSet), hand);
        broker = new HumanBroker(hand, bag); // can be any implementation of abstract broker, but is HumanBroker by default
    }

    public HumanPlayer(Game game, WordsSet wordsSet, NormalTileBag bag, MultiSet<Tile> handSet) {
        Hand hand = new Hand(handSet);
        gridArranger = new GridArranger(new Grid(wordsSet), hand);
        broker = new HumanBroker(hand, bag);
    }

    public HumanPlayer(Game game, Grid grid, NormalTileBag bag, Hand hand) {
        gridArranger = new GridArranger(grid, hand);
        broker = new HumanBroker(hand, bag);
    }

    public HumanPlayer(Game game, Grid grid, NormalTileBag bag, MultiSet<Tile> handSet) {
        Hand hand = new Hand(handSet);
        gridArranger = new GridArranger(grid, hand);
        broker = new HumanBroker(hand, bag);
    }
    
    public HumanPlayer(Game game, Grid grid, NormalTileBag bag) {
        Hand hand = new Hand();
        gridArranger = new GridArranger(grid, hand);
        broker = new HumanBroker(hand, bag);
    }

    @Override
    public HumanPlayer copy() {
        return new HumanPlayer(gridArranger.copy(), broker.copy());
    }

    public void setGame(Game g) {
        game = g;
    }

    public Hand getHand() {
        return broker.getHand();
    }

    // TODO type needs to be more specific again. Also not sure if I want this in player.
    // It is currently being used in GameTest
    public NormalTileBag getBag() { 
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