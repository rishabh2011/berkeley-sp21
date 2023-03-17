package priorityqueue;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Comparator;

public class TestPriorityQueue {
    @Test
    public void testPriorityQueue() {
        Comparator<Vertex> vertexComparator = Comparator.comparingInt(o -> o.nodeValue);
        VertexPriorityQueue pq = new VertexPriorityQueue(2, vertexComparator);
        assertTrue(pq.size() == 0);


        Vertex v1 = new Vertex(3, 6);
        Vertex v2 = new Vertex(4, 2);
        Vertex v3 = new Vertex(1, 1);
        Vertex v4 = new Vertex(2, 5);
        Vertex v5 = new Vertex(5, 7);

        pq.add(v1);
        pq.add(v2);
        pq.add(v3);
        pq.add(v4);
        pq.add(v5);

        assertTrue(pq.size() == 5);
        assertTrue(pq.getSmallest().equals(v3));
        assertTrue(pq.removeSmallest().equals(v3));
        assertTrue(pq.getSmallest().equals(v2));
        pq.changeNodeValue(v2, 8);
        assertTrue(pq.getSmallest().equals(v4));
    }
}
