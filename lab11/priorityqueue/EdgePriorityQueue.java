package priorityqueue;

import graph.Edge;

import java.util.Comparator;
import java.util.HashMap;

/**
 * A Priority Queue for Graph vertices that can be used to implement Djikstra's Algorithm
 *
 * @author Rishabh Choudhury
 */

public class EdgePriorityQueue implements MinPQ {

    /**
     * heap array acts as underlying data structure
     */
    private Edge[] heap;

    /**
     * each key entry in hash map points to its respective position in the heap
     */
    private HashMap<Edge, Integer> edgeHeapIndex;

    /**
     * Comparator for comparing Item type
     */
    private Comparator<Edge> comparator;

    /**
     * Size of the heap
     */
    private int size;

    public EdgePriorityQueue(int heapSize, Comparator comparator) {
        heap = new Edge[heapSize];
        edgeHeapIndex = new HashMap<>();
        this.comparator = comparator;
        size = 0;
    }

    @Override
    public void add(Edge x) {
        if (size == heap.length - 1) {
            resize(heap.length * 2);
        }

        //add item to last position in heap
        ++size;
        heap[size] = x;
        edgeHeapIndex.put(x, size);
        //swim item to correct position in heap
        swim(size);
    }

    @Override
    public Edge getSmallest() {
        return heap[1];
    }

    @Override
    public Edge removeSmallest() {
        Edge val = heap[1];
        edgeHeapIndex.remove(val);

        heap[1] = heap[size--];
        sink(1);
        heap[size + 1] = null;

        if (size == (heap.length - 1) / 4) {
            resize(heap.length / 2);
        }

        return val;
    }

    @Override
    public int size() {
        return size;
    }

    /** Checks whether priority queue is empty
     *
     * @return {@code true} if empty. {@code false} otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /** Change the value of a particular vertex with the given value.
     * The priority queue is then re-heapify'd to accommodate the new
     * changes
     *
     * @param v the vertex
     * @param value the value of the vertex
     * */
    public void changePriority(Edge edge, double value) {
        if (!edgeHeapIndex.containsKey(edge)) {
            return;
        }

        int heapIndex = edgeHeapIndex.get(edge);
        heap[heapIndex].setEdgeWeight(value);
        swim(heapIndex);
        sink(heapIndex);
    }

    /** Checks if a given edge exists in the priority queue
     *
     * @param edge the edge to check
     * @return {@code true} if edge exists. {@code false} otherwise
     */
    public boolean contains(Edge edge) {
        return edgeHeapIndex.containsKey(edge);
    }

    // ==================================== HELPER METHODS ================================ //
    /** Creates a new heap with given capacity and
     * copies current heap values to the new heap.
     * Finally, assigns current heap to new heap
     *
     * @param capacity size of the new heap
     * */
    private void resize(int capacity) {
        Edge[] newHeap = new Edge[capacity];
        for (int i = 1; i <= size; ++i) {
            newHeap[i] = heap[i];
        }
        heap = newHeap;
    }


    /**
     * Re-heapify to maintain heap invariants after adding a new item to the heap
     *
     * @param child child index
     */
    private void swim(int child) {
        int parent = child / 2;
        while (child > 1 && isGreater(parent, child)) {
            swap(child, parent);
            child /= 2;
            parent = child / 2;
        }
    }

    /**
     * Re-heapify to maintain heap invariants after removal of root item from heap
     *
     * @param parent parent index
     */
    private void sink(int parent) {

        int child = 2 * parent;
        //left child greater than right child
        if (child < size && isGreater(child, child + 1)) {
            child++;
        }

        if (child <= size && isGreater(parent, child)) {
            swap(parent, child);
            sink(child);
        }
    }

    /**
     * Compares heap[i] and heap[j] to find the greater value
     *
     * @param i heap index
     * @param j heap index
     * @return {@code true} if heap[i] > heap[j]
     * <br>{@code false} if heap[i] <= heap[j]</br>
     */
    private boolean isGreater(int i, int j) {
        return comparator.compare(heap[i], heap[j]) > 0;
    }

    /**
     * Swaps heap[i] and heap[j] values
     *
     * @param i heap index
     * @param j heap index
     */
    private void swap(int i, int j) {

        //Update vertex heap index values
        edgeHeapIndex.put(heap[j], i);
        edgeHeapIndex.put(heap[i], j);

        Edge temp = heap[j];
        heap[j] = heap[i];
        heap[i] = temp;
    }


}
