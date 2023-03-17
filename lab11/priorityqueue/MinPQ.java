package priorityqueue;

/**
 * (Min) Priority Queue: Allowing tracking and removal of
 * the smallest item in a priority queue.
 */
public interface MinPQ {
    /**
     * Adds the item to the priority queue.
     */
    void add(Vertex x);

    /**
     * Returns the smallest item in the priority queue.
     *
     */
    Vertex getSmallest();

    /**
     * Removes the smallest item from the priority queue.
     */
    Vertex removeSmallest();

    /**
     * Returns the size of the priority queue.
     */
    int size();
}
