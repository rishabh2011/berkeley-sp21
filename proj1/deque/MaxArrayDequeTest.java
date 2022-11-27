package deque;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Comparator;

public class MaxArrayDequeTest {
    @Test
    public void comparatorStringTest() {
//        Comparator<String> sc = CustomComparators.getStringComparator();
//        MaxArrayDeque<String> mad = new MaxArrayDeque<>(sc);
        MaxArrayDeque<String> mad = new MaxArrayDeque<>(null);

        mad.addFirst("Hyderabad");
        mad.addLast("Kolkata");
        mad.addLast("Chennai");
        mad.addLast("Bangalore");
        mad.addLast("Karnataka");

//        assertEquals("Kolkata", mad.max());
        assertEquals("Kolkata", mad.max(CustomComparators.getStringComparator()));
    }

    @Test
    public void comparatorIntegerTest() {
//        Comparator<Integer> ic = CustomComparators.getIntegerComparator();
//        MaxArrayDeque<Integer> mad = new MaxArrayDeque<>(ic);
        MaxArrayDeque<Integer> mad = new MaxArrayDeque<>(null);

        mad.addFirst(5);
        mad.addLast(12);
        mad.addLast(3);
        mad.addLast(8);
        mad.addLast(12);

//        assertEquals((Integer)12, mad.max());
        assertEquals((Integer) 12, mad.max(CustomComparators.getIntegerComparator()));
    }
}
