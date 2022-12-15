package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private BSTNode sentinel;
    private int size;

    /**
     * BSTNode class represents a node in the BST
     */
    private class BSTNode {
        K key;
        V value;
        BSTNode left;
        BSTNode right;

        /**
         * Construct a BSTNode with the given key and value pair
         *
         * @param key
         * @param value associated to the given key
         */
        BSTNode(K key, V value) {
            this.key = key;
            this.value = value;
            left = null;
            right = null;
        }
    }

    public BSTMap() {
        sentinel = new BSTNode(null, null);
        size = 0;
    }

    /* Removes all key mappings in the map */
    @Override
    public void clear() {
        sentinel.left = null;
        sentinel.right = null;
        size = 0;
    }

    /**
     * Returns whether key exists in the map
     *
     * @param key the key to be checked
     * @return true | false
     */
    @Override
    public boolean containsKey(K key) {
        BSTNode node = find(sentinel.left, sentinel.left, key);
        if (node != null && node.key.equals(key)) {
            return true;
        }
        return false;
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     *
     * @param key whose value is required
     * @return value associated with given key | null
     */
    @Override
    public V get(K key) {
        BSTNode node = find(sentinel.left, sentinel.left, key);
        if (node != null && node.key.equals(key)) {
            return node.value;
        }
        return null;
    }

    /**
     * Returns number of key value pairs in the map
     *
     * @return number of key value pairs
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Associates the specified value with the specified key in this map.
     *
     * @param key
     * @param value value associated to the given key
     */
    @Override
    public void put(K key, V value) {
        BSTNode newNode = new BSTNode(key, value);
        if (size == 0) {
            sentinel.left = newNode;
            sentinel.right = newNode;
        } else {
            BSTNode parentNode = find(sentinel.left, sentinel.left, key);
            if (key.compareTo(parentNode.key) < 0) {
                parentNode.left = newNode;
            } else {
                parentNode.right = newNode;
            }
        }
        ++size;
    }

    /**
     * Finds the position in the tree where new node should be inserted.
     * <p>
     * Returns the parent node to which the new child node should be attached.
     * If node with given key already exists in the tree then returns the existing node
     *
     * @param parentNode starting parent node
     * @param childNode  starting child node
     * @param key        key to check nodes against
     */
    private BSTNode find(BSTNode parentNode, BSTNode childNode, K key) {

        /* return the parent node */
        if (childNode == null) {
            return parentNode;
        }

        /* return the current node if it's key value equals given key */
        if (key.equals(childNode.key)) {
            return childNode;
        }

        /* if key is smaller than current node key, find parent in left tree
         *  else if key is greater than current node key, find parent in right tree */
        if (key.compareTo(childNode.key) < 0) {
            return find(childNode, childNode.left, key);
        } else {
            return find(childNode, childNode.right, key);
        }
    }

    @Override
    public Set keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator iterator() {
        throw new UnsupportedOperationException();
    }
}
