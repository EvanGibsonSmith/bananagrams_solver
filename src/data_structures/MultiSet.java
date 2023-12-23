package src.data_structures;

import java.util.HashMap;

public class MultiSet<K> {
    HashMap<K, Integer> map = new HashMap<>();
    int size = 0;

    @Override
    public String toString() {
        String out = "{";
        for (K element: map.keySet()) {
            int duplicates = map.get(element);
            for (int i=0; i<duplicates; ++i) {
                out += element + ", "; // FIXME should it be element.toString()? Check
            }
        }
        return out.substring(0, out.length()-2) + "}";
    }

    /**
     * Add method has O(1) time complexity
     * 
     * Adds object to the multiset if not present, otherwise increments
     * underlying HashMap. 
     * @param obj
     */
    public void add(K obj) {
        map.put(obj, map.getOrDefault(obj, 0)+1); // adds one if tile present, otherwise sets to 1
        ++size;
    }

    /**
     * Remove method has O(1) time complexity
     * 
     * Removes obj given from the MultiSet if present within the
     * set, otherwise throws error because element is not present.
     * @param obj
     * @throws IllegalArgumentException
     */
    public void remove(K obj) {
        if (!map.containsKey(obj)) {
            throw new IllegalArgumentException("Cannot remove object that is not present from a MultiSet");
        }

        if (map.get(obj)==1) { // if only one of this object left, remove it from HashMap entirely
            map.remove(obj);
        }
        else {
            // can decrement because only one. map is guarenteed to have obj from first if statement
            map.put(obj, map.get(obj)-1);
        }
        --size;
    }

    /**
     * size method has O(1) time complexity
     * 
     * Gets the size of the set, which in this case is the total number of elements 
     * in the set, including duplicates. Stored within data structure for quick time 
     * complexity.
     * @return number of elements
     */
    public int size() {
        return this.size;
    }

    /**
     * sizeDistinct method has O(1) time complexity 
     * 
     * Gets the number of distinct elements in the set based on the underlying 
     * HashMap.
     * @return number of distinct elements
     */
    public int sizeDistinct() {
        return this.map.size();
    }

    /**
     * isEmpty has O(1) time complexity
     * 
     * if the set has any elements, true if so, otherwise false
     * @return boolean is set empty
     */
    public boolean isEmpty() {return this.map.isEmpty();}

    /**
     * contains has O(1) time complexity 
     * 
     * if the set contains the given value
     */
    public boolean contains(K obj) {return this.map.containsKey(obj);}
}
