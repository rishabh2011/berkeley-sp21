package deque;

import java.util.Iterator;

/**
 * LinkedListDeque provides a Deque implementation using Circular linked list data structure
 */
public class LinkedListDeque<T> implements Iterable<T>, Deque<T> {
    private LLNode sentinel;
    private int size;

    private class LLNode {
        LLNode prev;
        T item;
        LLNode next;

        /**
         * Creates a new LLNode
         *
         * @param i : item to be inserted into the node
         * @param p : previous list node
         * @param n : next list node
         */
        LLNode(T i, LLNode p, LLNode n) {
            prev = p;
            item = i;
            next = n;
        }
    }

    public LinkedListDeque() {
        sentinel = new LLNode(null, null, null);
        size = 0;
    }

    /**
     * Adds the given item to the front of the deque
     *
     * @param item : item to be added to the deque
     */
    @Override
    public void addFirst(T item) {

        LLNode prevFirstNode = sentinel.next;
        LLNode newFirstNode = new LLNode(item, sentinel, prevFirstNode);
        sentinel.next = newFirstNode;
        ++size;

        if (prevFirstNode != null) {
            prevFirstNode.prev = newFirstNode;
        }

        //One item in list
        if (newFirstNode.next == null) {
            makeCircular(newFirstNode);
        }
    }

    /**
     * Makes the list circular when the first node is added
     * <p>Both Sentinel node pointers point to the given node and vice versa
     * completing a full circle</p>
     *
     * @param node the first node added to the list
     */
    private void makeCircular(LLNode node) {
        sentinel.next = node;
        sentinel.prev = node;
        node.next = sentinel;
        node.prev = sentinel;
    }

    /**
     * Adds the given item to the end of the deque
     *
     * @param item item to be added to the deque
     */
    @Override
    public void addLast(T item) {
        LLNode prevLastNode = sentinel.prev;
        LLNode newLastNode = new LLNode(item, prevLastNode, sentinel);
        sentinel.prev = newLastNode;
        ++size;

        if (prevLastNode != null) {
            prevLastNode.next = newLastNode;
        }

        //One item in list
        if (newLastNode.prev == null) {
            makeCircular(newLastNode);
        }
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
        if (sentinel.next == null) {
            System.out.println("No items in the deque");
        } else {
            LLNode temp = sentinel.next;
            while (temp != sentinel) {
                System.out.print(temp.item + " ");
                temp = temp.next;
            }
        }
    }

    /**
     * Removes the first item in the deque and returns the removed item
     *
     * @return item that was removed | null
     */
    @Override
    public T removeFirst() {
        LLNode firstNode = sentinel.next;
        if (firstNode != null) {
            --size;
            //the list will become empty after removal of firstNode
            if (isEmpty()) {
                sentinel.next = null;
                sentinel.prev = null;
            } else {
                //point sentinel.next to 2nd node after first
                sentinel.next = firstNode.next;
                //point 2nd node.prev to sentinel
                firstNode.next.prev = sentinel;
            }
            return firstNode.item;
        }
        return null;
    }

    /**
     * Removes the last item in the deque and returns the removed item
     *
     * @return item that was removed | null
     */
    @Override
    public T removeLast() {
        LLNode lastNode = sentinel.prev;
        if (lastNode != null) {
            --size;
            //the list will become empty after removal of lastNode
            if (isEmpty()) {
                sentinel.next = null;
                sentinel.prev = null;
            } else {
                //point sentinel.prev to 2nd last node
                sentinel.prev = lastNode.prev;
                //point 2nd last node.next to sentinel
                lastNode.prev.next = sentinel;
            }
            return lastNode.item;
        }
        return null;
    }

    /**
     * Iterates through the deque and returns the item found at the specified index
     *
     * @return item found at the given index | null
     */
    @Override
    public T get(int index) {
        //Return null for invalid index (Also applies for empty list)
        if (index >= size || index < 0) {
            return null;
        } else if (index > (size / 2)) {  //index falls in the latter half of the list
            return getBack(size - 1 - index);
        } else {
            return getFront(index);
        }
    }

    /**
     * Iterates from the back of the list to the specified index and returns the item
     *
     * @return item found at the given index | null
     */
    private T getBack(int index) {
        LLNode temp = sentinel.prev;
        for (int i = 0; i < index; i++) {
            temp = temp.prev;
        }
        return temp.item;
    }

    /**
     * Iterates from the front of the list to the specified index and returns the item
     *
     * @return item found at the given index | null
     */
    private T getFront(int index) {
        LLNode temp = sentinel.next;
        for (int i = 0; i < index; i++) {
            temp = temp.next;
        }
        return temp.item;
    }

    /**
     * Recursively iterates through the deque and returns the item found at the specified index
     *
     * @return item found at the given index | null
     */
    public T getRecursive(int index) {
        //Return null for invalid index (Also applies for empty list)
        if (index >= size || index < 0) {
            return null;
        } else if (index > (size / 2)) { //index falls in the latter half of the list
            return getBackRecursive(sentinel.prev, size - 1 - index);
        } else {
            return getFrontRecursive(sentinel.next, index);
        }
    }

    /**
     * Recursively Iterates from the back of the list to the specified index and returns the item
     *
     * @return item found at the given index | null
     */
    private T getBackRecursive(LLNode node, int index) {
        if (index == 0) {
            return node.item;
        }
        return getBackRecursive(node.prev, --index);
    }

    /**
     * Recursively Iterates from the front of the list to the specified index and returns the item
     *
     * @return item found at the given index | null
     */
    private T getFrontRecursive(LLNode node, int index) {
        if (index == 0) {
            return node.item;
        }
        return getFrontRecursive(node.next, --index);
    }

    /**
     * Returns whether the parameter o is equal to the Deque.
     *
     * @param o An object
     * @return true | false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Deque) {
            if (size != ((Deque<T>) o).size()) { //deque o is not of same size
                return false;
            } else {
                LLNode temp = sentinel.next;
                for (int i = 0; i < size; i++) {
                    T odItem = ((Deque<T>) o).get(i);
                    if (temp.item != odItem) {
                        return false;
                    }
                    temp = temp.next;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Returns an iterator for LinkedListDeque
     *
     * @return iterator for LinkedDeque
     */
    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }

    /**
     * Iterator class for LinkedListDeque
     */
    private class LinkedListDequeIterator implements Iterator<T> {

        LLNode currentNode;

        LinkedListDequeIterator() {
            currentNode = sentinel.next;
        }

        public boolean hasNext() {

            return currentNode != sentinel && currentNode != null;
        }

        public T next() {
            T item = currentNode.item;
            currentNode = currentNode.next;
            return item;
        }
    }

}
