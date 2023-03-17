package priorityqueue;

import java.util.Comparator;
import java.util.HashMap;

/**
 * A Priority Queue for Graph vertices that can be used to implement Djikstra's Algorithm
 *
 * @author Rishabh Choudhury
 */

public class VertexPriorityQueue implements MinPQ {

    /**
     * heap array acts as underlying data structure
     */
    private Vertex[] heap;

    /**
     * each key entry in hash map points to its respective position in the heap
     */
    private HashMap<Vertex, Integer> vertexHeapIndex;

    /**
     * Comparator for comparing Item type
     */
    private Comparator<Vertex> comparator;

    /**
     * Size of the heap
     */
    private int size;

    public VertexPriorityQueue(int heapSize, Comparator comparator) {
        heap = new Vertex[heapSize];
        vertexHeapIndex = new HashMap<>();
        this.comparator = comparator;
        size = 0;
    }

    @Override
    public void add(Vertex x) {
        if (size == heap.length - 1) {
            resize(heap.length * 2);
        }

        //add item to last position in heap
        ++size;
        heap[size] = x;
        vertexHeapIndex.put(x, size);
        //swim item to correct position in heap
        swim(size);
    }

    @Override
    public Vertex getSmallest() {
        return heap[1];
    }

    @Override
    public Vertex removeSmallest() {
        Vertex val = heap[1];
        vertexHeapIndex.remove(val);

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

    public void changeNodeValue(Vertex v, int value) {
        if (!vertexHeapIndex.containsKey(v)) {
            return;
        }

        int heapIndex = vertexHeapIndex.get(v);
        heap[heapIndex].nodeValue = value;
        swim(heapIndex);
        sink(heapIndex);
    }

    // ==================================== HELPER METHODS ================================ //
    /** Creates a new heap with given capacity and
     * copies current heap values to the new heap.
     * Finally, assigns current heap to new heap
     *
     * @param capacity size of the new heap
     * */
    private void resize(int capacity) {
        Vertex[] newHeap = new Vertex[capacity];
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
        vertexHeapIndex.put(heap[j], i);
        vertexHeapIndex.put(heap[i], j);

        Vertex temp = heap[j];
        heap[j] = heap[i];
        heap[i] = temp;
    }


}
