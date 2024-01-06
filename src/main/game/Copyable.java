package src.main.game;

public interface Copyable<E extends Copyable<E>> { 
    public E copy();
}
