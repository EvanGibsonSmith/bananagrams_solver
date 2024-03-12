package src.interfaces;

import src.main.game.Tile;

public interface TileBaggable extends Copyable<TileBaggable> {
    public int size();

    public boolean isFull();

    public boolean isEmpty();

    public Tile grabTile();

    public void addTile(Tile t);
}
