package test;

import src.main.Location;

public class LocationTest {
    
    public static void main(String[] args) {
        // TODO make this actual junit and make some nice tests. Do equalvalence, hashcode, etc.
        Location l1 = new Location(1, 2);
        Location l2 = new Location(1, 2);

        System.out.println(l1.equals(l2));
         System.out.println(l1==l2);
    }
}
