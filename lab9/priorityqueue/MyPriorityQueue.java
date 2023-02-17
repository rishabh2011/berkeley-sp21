package priorityqueue;

import java.util.Comparator;

/**
 * A Priority Queue implementation
 *
 * @author Rishabh Choudhury
 */

public class MyPriorityQueue<Item> implements MinPQ<Item> {

    /**
     * heap array acts as underlying data structure
     */
    private Item[] heap;
    /**
     * Comparator for comparing Item type
     */
    private Comparator<Item> comparator;

    /**
     * Size of the heap
     */
    private int size;

    public MyPriorityQueue(int heapSize, Comparator comparator) {
        heap = (Item[]) new Object[heapSize];
        this.comparator = comparator;
        size = 0;
    }

    @Override
    public void add(Item x) {
        if (size == heap.length - 1) {
            resize(heap.length * 2);
        }

        //add item to last position in heap
        heap[++size] = x;
        //swim item to correct position in heap
        swim(size);
    }

    @Override
    public Item getSmallest() {
        return heap[1];
    }

    @Override
    public Item removeSmallest() {
        Item val = heap[1];
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

    // ==================================== HELPER METHODS ================================ //
    /** Creates a new heap with given capacity and
     * copies current heap values to the new heap.
     * Finally, assigns current heap to new heap
     *
     * @param capacity size of the new heap
     * */
    private void resize(int capacity) {
        Item[] newHeap = (Item[]) new Object[capacity];
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
        }
    }

    /**
     * Re-heapify to maintain heap invariants after removal of root item from heap
     *
     * @param parent parent index
     */
    private void sink(int parent) {

        int leftChild = 2 * parent;
        int rightChild = 2 * parent + 1;

        if (leftChild <= size && isGreater(parent, leftChild)) {
            swap(parent, leftChild);
            sink(leftChild);
        } else if (rightChild <= size && isGreater(parent, rightChild)) {
            swap(parent, rightChild);
            sink(rightChild);
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
        Item temp = heap[j];
        heap[j] = heap[i];
        heap[i] = temp;
    }


}
