package src.main;

public class Game {
    TileBag tiles;
    Grid[] grids;

    public Game(int numPlayers, TileBag tiles) {
        this.tiles = tiles;
        this.grids = new Grid[numPlayers];
    }

    public Boolean gameComplete() {
        for (Grid g: this.grids) {

        }
    }
    
}
