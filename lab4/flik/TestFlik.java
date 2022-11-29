package flik;

import static org.junit.Assert.*;
import org.junit.Test;

public class TestFlik {
    @Test
    public void testFlik(){
        assertTrue(Flik.isSameNumber(5, 5));
        assertFalse(Flik.isSameNumber(0, 2));
        assertTrue(Flik.isSameNumber(-1, -1));

        assertTrue(Flik.isSameNumber(new Integer(5), new Integer(5)));
    }
}
