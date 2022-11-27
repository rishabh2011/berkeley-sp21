package deque;

public interface Deque<T> {
    /**
     * Adds the given item to the front of the deque
     *
     * @param item : item to be added to the deque
     */
    public void addFirst(T item);

    /**
     * Adds the given item to the end of the deque
     *
     * @param item item to be added to the deque
     */
    public void addLast(T item);

    /**
     * Returns true if Deque is empty. Else returns false
     *
     * @return true | false
     */
    default boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns the size of the Deque
     *
     * @return size of the deque
     */
    public int size();

    /**
     * Displays all items in the Deque
     */
    public void printDeque();

    /**
     * Removes the first item in the deque and returns the removed item
     *
     * @return item that was removed | null
     */
    public T removeFirst();

    /**
     * Removes the last item in the deque and returns the removed item
     *
     * @return item that was removed | null
     */
    public T removeLast();

    /**
     * Iterates through the deque and returns the item found at the specified index
     *
     * @return item found at the given index | null
     */
    public T get(int index);
}
