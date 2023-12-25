package src.main.AI;

import java.util.Set;

public interface Branchable<K extends Branchable<K>> {
    
    public Set<K> branch();
    
}
