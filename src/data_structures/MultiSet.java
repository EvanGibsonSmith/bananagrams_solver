package src.data_structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

// TODO make this a proper set by making it implement Set (and probably extend AbstractSet as well) instead of just iterable
public class MultiSet<E> implements Iterable<E> {
    HashMap<E, Integer> map = new HashMap<>();
    int size = 0;

    public MultiSet() {}

    public MultiSet(E[] arr) {
        for (E elem: arr) {
            this.add(elem);
        }
    }

    public MultiSet(HashMap<E, Integer> map) {
        this.map = map;
        this.size = map.size();
    }

    public MultiSet(MultiSet<E> other) {
        this.map = new HashMap<>(other.map);
        this.size = other.size; // primitive so nothing needed
    }

    public MultiSet<E> copy() {
        return new MultiSet<E>(this);
    }

    public String toString() {
        if (this.isEmpty()) {return "";}

        String out = "{";
        for (E element: map.keySet()) {
            int duplicates = map.get(element);
            for (int i=0; i<duplicates; ++i) {
                out += element.toString() + ", ";
            }
        }
        return out.substring(0, out.length()-2) + "}";
    }

    // TODO test this I believe it works though. Also best location for it?
    public static MultiSet<Character> toMultiSet(String str) {
        MultiSet<Character> set = new MultiSet<>();
        for (char c: str.toCharArray()) {
            set.add(c);
        }
        return set;
    }

    @Override
    public Iterator<E> iterator() {
        ArrayList<E> iterList = new ArrayList<>();
        for (E key: map.keySet()) {
            int numElements = map.get(key);
            for (int i=0; i<numElements; ++i) { // add this element that number of times
                iterList.add(key);
            }
        }
        return iterList.iterator();
    }

    @Override
    public int hashCode() {
        return this.map.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MultiSet)) {return false;}
        MultiSet multiObj = (MultiSet) obj;
        if (this.size()!=(multiObj.size())) {return false;}

        for (E elem: this.map.keySet()) {
            if (multiObj.numberOf(elem)!=this.numberOf(elem)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Add method has O(1) time complexity
     * 
     * Adds object to the multiset if not present, otherwise increments
     * underlying HashMap. 
     * @param obj
     */
    public void add(E obj) {
        map.put(obj, map.getOrDefault(obj, 0)+1); // adds one if tile present, otherwise sets to 1
        ++size;
    }

    // TODO a method to add a set could be nice, like addAll

    /**
     * Remove method has O(1) time complexity
     * 
     * Removes obj given from the MultiSet if present within the
     * set, otherwise throws error because element is not present.
     * @param obj
     * @throws IllegalArgumentException
     */
    public void remove(E obj) {
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
        return size;
    }

    /**
     * sizeDistinct method has O(1) time complexity 
     * 
     * Gets the number of distinct elements in the set based on the underlying 
     * HashMap.
     * @return number of distinct elements
     */
    public int sizeDistinct() {
        return map.size();
    }

    /**
     * isEmpty has O(1) time complexity
     * 
     * if the set has any elements, true if so, otherwise false
     * @return boolean is set empty
     */
    public boolean isEmpty() {return map.isEmpty();}

    /**
     * contains has O(1) time complexity 
     * 
     * if the set contains the given value
     */
    public boolean contains(E obj) {return map.containsKey(obj);}

    /**
     * numbersOf has an O(1) time complexity
     * 
     * Gets the number of a given element within the set. If not present, this will
     * return zero, otherwise it will return the number of this element in the set.
     * @param elem element to check for within MultiSet
     * @return number of this element
     */
    public int numberOf(E elem) {
        return map.getOrDefault(elem, 0);
    }

    /**
     * getUniqueElements has O(1) time complexity 
     * 
     * Gets the unique elements from the MultiSet. Returns a set,
     * specifically a hashset of all unique elements.
     * 
     * @param other
     * @return set of all unique elements from this set
     */
    public Set<E> getUniqueElements() {return map.keySet();}

    /**
     * subset has O(N) time complexity where N is the number of keys of the larger set. 
     * Note the keys are not the size of the set, but the number of unique elements
     * 
     * Returns is this is a subset of the other multiset, i.e every element in this set including
     * duplicates is in the other set.
     * 
     * @param other the other set to see if this is subset of
     * @result if the set is subset
     */
    public boolean subset(MultiSet<E> other) {
        for (E elem: map.keySet()) {
            if (!other.contains(elem)) {return false;} // if other set doesn't even have object not a subset
            if (this.numberOf(elem) > other.numberOf(elem)) { // if this set has more of any object, not a subset
                return false;
            }
        }
        return true;
    }

    /**
     * addTo has O(N) time complexity where N is the number of keys of the larger set. 
     * Note the keys are not the size of the set, but the number of unique elements
     * 
     * Like union but adds this set to the other one, altering this object.
     * 
     * Combines the two sets, summing the number of elements they both share, adding 
     * in the number of elements when they are not shared. Alters this MultiSet.
     * 
     * @param other the other MultiSet to add to
     */
    public void addTo (MultiSet<E> other) {
        for (E elem: other.getUniqueElements()) {
            if (!this.contains(elem)) { // if we don't have the element add it to map
                map.put(elem, other.numberOf(elem)); // TODO use an addAll method later after completing it?
            }
            else {
                map.put(elem, this.numberOf(elem) + other.numberOf(elem));
            }
        }
    }

    /**
     * contains has O(N) time complexity where N is the number of keys of the larger set. 
     * Note the keys are not the size of the set, but the number of unique elements
     * 
     * Combines the two sets, summing the number of elements they both share, adding 
     * in the number of elements when they are not shared. Returns a new MultiSet,
     * not altering either of the inputs.
     * 
     * @param other the other MultiSet to add to
     * @return a new MultiSet that is union of this and other
     */
    public MultiSet<E> union (MultiSet<E> other) {
        MultiSet<E> copy = new MultiSet<E>(map);
        copy.addTo(other);
        return copy;
    }

    /**
     * removeAll has O(TODO) time complexity where N is the ???
     * Note the keys are not the size of the set, but the number of unique elements
     * 
     * Removes all elements in the other set from this set.
     * 
     * Combines the two sets, removing the number of elements they both share. 
     * If the other set has an element that this set does not have, it does not effect 
     * the result, and is simply not considered. This alters MultiSet
     *      
     * @param other the other MultiSet from which to remove
     */
    public void removeAll (MultiSet<E> other) {
        for (E elem: other.getUniqueElements()) {
            if (this.contains(elem)) { // only need to consider if we have this element
                // if this set has more of element it will still be present after operation
                if (this.numberOf(elem)>other.numberOf(elem)) { 
                    this.map.put(elem, this.numberOf(elem)-other.numberOf(elem));
                }
                else { // otherwise, all of this element will be gone
                    this.map.remove(elem);
                }
            }
        }
    }
}
