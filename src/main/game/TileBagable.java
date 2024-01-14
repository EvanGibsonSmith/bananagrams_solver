package src.main.game;

public interface TileBagable {
    public int size();

    public boolean isFull();

    public boolean isEmpty();

    public Tile grabTile();

    public void addTile(Tile t);
}
