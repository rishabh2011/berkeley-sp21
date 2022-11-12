package DebugExercise;

import static org.junit.Assert.*;
import org.junit.Test;

public class ArrayTest {

    @Test
    public void testArrayMax(){
        int[] a = new int[]{1};
        int[] b = new int[]{1};

        int[] expected = new int[]{1};
        int[] actual = DebugExercise2.arrayMax(a, b);
        assertArrayEquals(expected, actual);
    }

}
