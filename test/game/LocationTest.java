package test.game;

import org.junit.jupiter.api.Test;

import src.main.game.Location;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class LocationTest {
    static Location L00 = new Location(0, 0);
    static Location L11 = new Location(1, 1);
    static Location LN1N1 = new Location(-1, -1);
    static Location L3N2 = new Location(3, -2);

    @Test
    void equality() {
        // TODO make this actual junit and make some nice tests. Do equalvalence, hashcode, etc.
        Location l1 = new Location(1, 2);
        Location l2 = new Location(1, 2);

        assertEquals(l1, l2);
        assertFalse(l1==l2);
    }

    @Test 
    void above() {
        assertEquals(L00.above(), new Location(-1, 0));
        assertEquals(L11.above(), new Location(0, 1));
        assertEquals(LN1N1.above(), new Location(-2, -1));
        assertEquals(L3N2.above(), new Location(2, -2));
    }

    @Test 
    void below() {
        assertEquals(L00.below(), new Location(1, 0));
        assertEquals(L11.below(), new Location(2, 1));
        assertEquals(LN1N1.below(), new Location(0, -1));
        assertEquals(L3N2.below(), new Location(4, -2));
    }

    @Test
    void left() {
        assertEquals(L00.below(), new Location(1, 0));
        assertEquals(L11.below(), new Location(2, 1));
        assertEquals(LN1N1.below(), new Location(0, -1));
        assertEquals(L3N2.below(), new Location(4, -2));
    }

    @Test
    void right() {
        assertEquals(L00.right(), new Location(0, 1));
        assertEquals(L11.right(), new Location(1, 2));
        assertEquals(LN1N1.right(), new Location(-1, 0));
        assertEquals(L3N2.right(), new Location(3, -1));
    }

    @Test
    void getRow() {
        assertEquals(L00.getRow(), 0);
        assertEquals(L11.getRow(), 1);
        assertEquals(LN1N1.getRow(), -1);
        assertEquals(L3N2.getRow(), 3);
    }

    @Test
    void getColumn() {
        assertEquals(L00.getColumn(), 0);
        assertEquals(L11.getColumn(), 1);
        assertEquals(LN1N1.getColumn(), -1);
        assertEquals(L3N2.getColumn(), -2);
    }


}
