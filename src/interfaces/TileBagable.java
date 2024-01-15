package src.interfaces;

import src.main.game.Tile;

public interface TileBagable extends Copyable<TileBagable> {
    public int size();

    public boolean isFull();

    public boolean isEmpty();

    public Tile grabTile();

    public void addTile(Tile t);
}
