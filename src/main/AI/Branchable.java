package src.main.AI;

import java.util.Set;

public interface Branchable<K extends Branchable<? extends K>> {
    
    public Set<? extends K> branch(); // TODO maybe the ? extends is a weird implementation
    
}
