package src.main.game.players.types;

import src.data_structures.MultiSet;
import src.main.game.Game;
import src.main.game.Tile;
import src.main.game.grids.Grid;
import src.main.game.players.bags.NormalTileBag;
import src.main.game.players.brokers.HumanBroker;
import src.main.game.players.gridarrangers.GridArranger;
import src.main.game.players.hand.Hand;
import src.main.game.wordssets.WordsSet;

public class HumanPlayer extends Player {
    // Note that the broker type is now restricted to HumanBroker through the use of these constructors
    // but not in the player class itself
    protected HumanPlayer(GridArranger gridArranger, HumanBroker broker) {
        super.gridArranger = gridArranger;
        super.broker = broker;
    }

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
        return new HumanPlayer(gridArranger.copy(), (HumanBroker) broker.copy());
    }

    public void setGame(Game g) {
        game = g;
    }

    public NormalTileBag getBag() { 
        return (NormalTileBag) broker.getBag();
    }
}
