package src.data_structures;

public class DynamicIndexMinPQ<Key extends Comparable<Key>> extends IndexMinPQ<Key> {

    public DynamicIndexMinPQ() {
        super(10); // 10 chosen since same size ArrayList begins with
    }

    public DynamicIndexMinPQ(int capacity) {
        super(capacity);
    }

    private int[] growArray(int[] arr) {
        int[] newArr = new int[arr.length*2]; // double size to keep ammortized cost down
        // set all to -1, then add preexisting values
        for (int i = 0; i < newArr.length; ++i) {
            newArr[i] = -1;
        }
        for (int e=0; e < arr.length; ++e) {
            newArr[e] = arr[e];
        }
        this.maxN*=2; // maxN, which now represents max capacity before next growth, must be doubled too
        return newArr;
    }

    private Key[] growArray(Key[] arr) {
        Key[] newArr = (Key[]) new Comparable[arr.length*2]; // double size to keep ammortized cost down
        // set all to -1, then add preexisting values
        for (int e=0; e < arr.length; ++e) {
            newArr[e] = arr[e];
        }
        this.maxN*=2; // maxN, which now represents max capacity before next growth, must be doubled too
        return newArr;
    }

    // throw an IllegalArgumentException if i is an invalid index
    @Override
    protected void validateIndex(int i) {
        if (i < 0) throw new IllegalArgumentException("index is negative: " + i);
    }

    
    /**
     * Is {@code i} an index on this priority queue?
     *
     * @param  i an index
     * @return {@code true} if {@code i} is an index on this priority queue;
     *         {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
     */
    @Override
    public boolean contains(int i) {
        validateIndex(i);
        return qp[i] != -1;
    }

    /**
     * Associates key with index {@code i}.
     *
     * @param  i an index
     * @param  key the key to associate with index {@code i}
     * @throws IllegalArgumentException if there already is an item associated
     *         with index {@code i}
     */
    @Override
    public void insert(int i, Key key) {
        validateIndex(i);
        n++;
        // grow if needed
        if (i>=qp.length) {
            qp = growArray(qp);
            pq = growArray(pq);
            keys = growArray(keys);
        }
        
        if (contains(i)) throw new IllegalArgumentException("index is already in the priority queue");
        qp[i] = n;
        pq[n] = i;
        keys[i] = key;
        swim(n);
    }
}
