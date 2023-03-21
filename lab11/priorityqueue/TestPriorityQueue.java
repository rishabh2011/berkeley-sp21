package priorityqueue;

import graph.Edge;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Comparator;

public class TestPriorityQueue {
    @Test
    public void testPriorityQueue() {
        Comparator<Edge> edgeComparator = Comparator.comparingDouble(Edge::getEdgeWeight);
        EdgePriorityQueue pq = new EdgePriorityQueue(2, edgeComparator);
        assertTrue(pq.size() == 0);


        Edge v1 = new Edge(3, 6);
        Edge v2 = new Edge(4, 2);
        Edge v3 = new Edge(1, 1);
        Edge v4 = new Edge(2, 5);
        Edge v5 = new Edge(5, 7);

        pq.add(v1);
        pq.add(v2);
        pq.add(v3);
        pq.add(v4);
        pq.add(v5);

        assertTrue(pq.size() == 5);
        assertTrue(pq.getSmallest().equals(v3));
        assertTrue(pq.removeSmallest().equals(v3));
        assertTrue(pq.getSmallest().equals(v2));
        //pq.changePriority(v2., 8);
        assertTrue(pq.getSmallest().equals(v4));
    }
}
