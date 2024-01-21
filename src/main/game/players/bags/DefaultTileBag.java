package src.main.game.players.bags;

import src.main.game.Tile;

// TODO complete this. default tile bag in the game of banangrams based on the rules. Simple wrapper
public class DefaultTileBag extends NormalTileBag {
    private static char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static int[] letterValues = new int[] {13, 3, 3, 6, 18, 3, 4, 3, 12, 2, 2, 5, 3, 8, 11, 3, 2, 9, 6, 9, 6, 3, 3, 2, 3, 2};
    
    private static Tile[] buildDefaultTiles() {   
        // get size of total bag (144, but not hard coded size here)
        int bagSize=0;
        for (int value: letterValues) {bagSize+=value;}

        Tile[] tiles = new Tile[bagSize];
        int cursor = 0;
        for (int letterIdx=0; letterIdx<alphabet.length; ++letterIdx) {
            char letter = alphabet[letterIdx];
            int letterValue = letterValues[letterIdx];
            for (int c=0; c<letterValue; ++c) {
                tiles[cursor] = new Tile(letter);
                ++cursor;
            }
        }

        return tiles;
    }
    
    public DefaultTileBag() {
        super(buildDefaultTiles());
    }
}
