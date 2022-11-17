package deque;

/**
 * LinkedListDeque provides a Deque implementation using Circular linked list data structure
 */
public class LinkedListDeque<T> {
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
        public LLNode(T i, LLNode p, LLNode n) {
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
     * Returns true if Deque is empty. Else returns false
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the size of the Deque
     */
    public int size() {
        return size;
    }

    /**
     * Prints the deque
     */
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
     */
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
     */
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
     */
    public T get(int index) {
        //Return null for invalid index (Also applies for empty list)
        if (index >= size || index < 0) return null;
            //index falls in the latter half of the list
        else if (index > (size / 2)) {
            return getBack(size - 1 - index);
        } else {
            return getFront(index);
        }
    }

    /**
     * Iterates from the back of the list to the specified index and returns the item
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
     */
    public T getRecursive(int index) {
        //Return null for invalid index (Also applies for empty list)
        if (index >= size || index < 0) return null;
            //index falls in the latter half of the list
        else if (index > (size / 2)) {
            return getBackRecursive(sentinel.prev, size - 1 - index);
        } else {
            return getFrontRecursive(sentinel.next, index);
        }
    }

    /**
     * Recursively Iterates from the back of the list to the specified index and returns the item
     */
    private T getBackRecursive(LLNode node, int index) {
        if (index == 0) {
            return node.item;
        }
        return getBackRecursive(node.prev, --index);
    }

    /**
     * Recursively Iterates from the front of the list to the specified index and returns the item
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
     */
    public boolean equals(Object o) {
        if (!(o instanceof LinkedListDeque)) return false;  //o is not a deque
        else if (size != ((LinkedListDeque<T>) o).size()) return false; //deque o is not of same size
        else {
            LLNode temp = sentinel.next;
            for (int i = 0; i < size; i++) {
                T item = ((LinkedListDeque<T>) o).get(i);
                if (temp.item != item) {
                    return false;
                }
                temp = temp.next;
            }
        }
        return true;
    }

}
