package src.main.game.players.bags;


import java.util.LinkedList;
import java.util.Queue;

import src.interfaces.TileBaggable;
import src.main.game.Tile;

// TODO document
public class CheatTileBag implements TileBaggable{
    int capacity = -1;
    Queue<Tile> queue;
    // TODO complete to work with cheatBroker

    public CheatTileBag() {
        queue = new LinkedList<>();
    }

    public CheatTileBag(int capacity) {
        this();
        this.capacity = capacity;
    }

    public CheatTileBag(Queue<Tile> queue) {
        this.queue = queue;
    }

    public CheatTileBag(char[] chars) {
        Queue<Tile> newQueue = new LinkedList<>();
        for (char c: chars) {
            newQueue.add(new Tile(c));
        }
        this.queue = newQueue;
    }

    public CheatTileBag(Queue<Tile> queue, int capacity) {
        this(queue);
        this.capacity = capacity;
    }

    public Tile grabTile() {return queue.poll();}

    public boolean grabTile(int num) {
        if (num > size()) {return false;}
        for (int i=0; i<num; ++i) {
            grabTile();
        }
        return true;
    }
    
    public boolean isEmpty() {return queue.isEmpty();}

    public boolean isFull() {return (queue.size()==capacity && capacity!=-1);} // if capacity not set queue.size() is never negative 1

    public int size() {return queue.size();}

    public void addTile(Tile t) {
        if (!isFull()) {
            queue.add(t);
        }
        // TODO make it throw something
    }

    public void clear() {queue.clear();}

    public void setBag(Queue<Tile> queue) {
        if (!isFull()) {
            this.queue = queue;
        }
        // TODO make it throw something
    }

    public CheatTileBag copy() {return new CheatTileBag(new LinkedList<Tile>(queue));}
}
