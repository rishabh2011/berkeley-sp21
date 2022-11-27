package deque;

import java.util.Iterator;

/**
 * ArrayDeque provides a Deque implementation using an array data structure
 */
public class ArrayDeque<T> implements Iterable<T>, Deque<T> {

    static final int DEF_START_SIZE = 8;  //starting array size
    final int MIN_RESIZE = 16;   //minimum array size before resizing is considered
    final double MIN_USAGE_FACTOR = 0.25; //minimum array usage factor to consider for resizing
    private T[] items;
    private int size;
    private int nextFirst;
    private int nextLast;

    public ArrayDeque() {
        this(DEF_START_SIZE);
    }

    public ArrayDeque(int capacity) {
        items = (T[]) new Object[capacity];
        size = 0;
        nextFirst = 0;
        nextLast = 1;
    }

    /**
     * Adds the given item to the front of the deque
     *
     * @param item : item to be added to the deque
     */
    @Override
    public void addFirst(T item) {
        items[nextFirst] = item;
        ++size;
        setNextFirst();

        //Existing array is full
        if (items[nextFirst] != null) {
            resize(size * 2);
        }
    }

    /**
     * Sets the next available first item index in the array
     */
    private void setNextFirst() {
        if (nextFirst == 0) {
            nextFirst = items.length - 1;
        } else {
            --nextFirst;
        }
    }

    /**
     * Adds the given item to the end of the deque
     *
     * @param item item to be added to the deque
     */
    @Override
    public void addLast(T item) {
        items[nextLast] = item;
        ++size;
        setNextLast();

        //Existing array is full
        if (items[nextLast] != null) {
            resize(size * 2);
        }
    }

    /**
     * Sets the next available last item index in the array
     */
    private void setNextLast() {
        if (nextLast == items.length - 1) {
            nextLast = 0;
        } else {
            ++nextLast;
        }
    }

    /**
     * Removes the first item in the deque and returns the removed item
     *
     * @return item that was removed | null
     */
    @Override
    public T removeFirst() {
        T item;
        if (size == 0) {
            return null;
        } else if (nextFirst == items.length - 1) { //loop to the start of the array and remove the first item
            item = items[0];
            items[0] = null;
            nextFirst = 0;
        } else { //remove the first item
            item = items[nextFirst + 1];
            items[nextFirst + 1] = null;
            ++nextFirst;
        }
        --size;

        if (items.length >= MIN_RESIZE && ((double) size / (double) items.length) < 0.25) {
            resize(items.length / 2);
        }

        return item;
    }

    /**
     * Removes the last item in the deque and returns the removed item
     *
     * @return item that was removed | null
     */
    @Override
    public T removeLast() {
        T item;
        if (size == 0) {
            return null;
        } else if (nextLast == 0) { //loop to the back of the array and remove the last item
            item = items[items.length - 1];
            items[items.length - 1] = null;
            nextLast = items.length - 1;
        } else { //remove the last item
            item = items[nextLast - 1];
            items[nextLast - 1] = null;
            --nextLast;
        }
        --size;

        if (items.length >= MIN_RESIZE && ((double) size / (double) items.length) < MIN_USAGE_FACTOR) {
            resize(items.length / 2);
        }

        return item;
    }

    /**
     * Creates a new array of given capacity and copies existing array values to the new array
     *
     * @param capacity the size of the new array
     */
    private void resize(int capacity) {
        T[] newArr = (T[]) new Object[capacity];
        int currIndex = nextFirst + 1;
        for (int i = 0; i < size; i++) {
            //loop to the front of the array if the end of the array has been reached
            if (currIndex == items.length) {
                currIndex = 0;
            }
            newArr[i] = items[currIndex];
            ++currIndex;
        }
        items = newArr;
        nextFirst = items.length - 1;
        nextLast = size;
    }

    /**
     * Returns the size of the Deque
     *
     * @return size of the deque
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Prints the deque
     */
    @Override
    public void printDeque() {
        for (T item : items) {
            System.out.print(item + " ");
        }
    }

    /**
     * Iterates through the deque and returns the item found at the specified index
     *
     * @return item found at the given index | null
     */
    @Override
    public T get(int index) {
        //Return null for invalid index (Also applies for empty array)
        if (index >= items.length || index < 0) {
            return null;
        }
        //Given index crosses the end of array
        //Therefore loop to the beginning keeping track of the number of elements skipped
        if (nextFirst + 1 + index >= items.length) {
            int numItemsBeforeLooping = items.length - 1 - nextFirst;
            return items[index - numItemsBeforeLooping];
        } else {
            return items[nextFirst + 1 + index];
        }
    }

    /**
     * Returns whether the parameter o is equal to the Deque.
     *
     * @param o An object
     * @return true | false
     */
    public boolean equals(Object o) {
        if (!(o instanceof ArrayDeque)) { //o is not an array deque
            return false;
        } else if (size != ((ArrayDeque<T>) o).size()) { //deque o is not of same size
            return false;
        } else {
            for (int i = 0; i < size; i++) {
                T item = ((ArrayDeque<T>) o).get(i);
                if (get(i) != item) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns an iterator for ArrayDeque
     *
     * @return iterator for ArrayDeque
     */
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    /**
     * Iterator class for ArrayDeque
     */
    private class ArrayDequeIterator implements Iterator<T> {

        int iterPos;

        public ArrayDequeIterator() {
            iterPos = nextFirst + 1;
        }

        public boolean hasNext() {
            return iterPos != nextLast;
        }

        public T next() {
            //loop to the front of the array if the end of the array has been reached
            if (iterPos == items.length) {
                iterPos = 0;
            }

            T item = items[iterPos];
            ++iterPos;
            return item;
        }
    }
}
