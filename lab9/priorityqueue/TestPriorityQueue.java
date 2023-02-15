package priorityqueue;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Comparator;

public class TestPriorityQueue {
    @Test
    public void testPriorityQueue() {
        Comparator<Integer> intComparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        };
        MyPriorityQueue<Integer> pq = new MyPriorityQueue<>(2, intComparator);
        assertTrue(pq.size() == 0);
        pq.add(10);
        pq.add(5);
        pq.add(6);
        pq.add(6);
        assertTrue(pq.size() == 4);
        assertTrue(pq.getSmallest() == 5);
        assertTrue(pq.removeSmallest() == 5);
        assertTrue(pq.getSmallest() == 6);
        assertTrue(pq.removeSmallest() == 6);
        assertTrue(pq.removeSmallest() == 6);
        assertTrue(pq.size() == 1);
    }
}
