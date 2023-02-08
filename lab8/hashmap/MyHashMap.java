package hashmap;

import java.util.*;

/**
 * A hash table-backed Map implementation. Provides amortized constant time
 * access to elements via get(), remove(), and put() in the best case.
 * <p>
 * Assumes null keys will never be inserted, and does not resize down upon remove().
 *
 * @author Rishabh Choudhury
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    /**
     * Hash table
     */
    private Collection<Node>[] buckets;
    /**
     * the number of buckets in the hash map
     */
    private int bucketsSize = 16;
    /**
     * the max load factor (N/M) of the bucket
     */
    private double maxLoadFactor = 0.75;
    /**
     * the min load factor (N/M) of the bucket
     */
    private double minLoadFactor = 0.25;
    /**
     * The number of elements in the hash table
     */
    private int size = 0;

    /**
     * Constructors
     */
    public MyHashMap() {
        buckets = createTable(bucketsSize);
    }

    public MyHashMap(int initialSize) {
        bucketsSize = initialSize;
        buckets = createTable(initialSize);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad     maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        this(initialSize);
        maxLoadFactor = maxLoad;
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     * <p>
     * The only requirements of a hash table bucket are that we can:
     * 1. Insert items (`add` method)
     * 2. Remove items (`remove` method)
     * 3. Iterate through items (`iterator` method)
     * <p>
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     * <p>
     * Override this method to use different data structures as
     * the underlying bucket type
     * <p>
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     * <p>
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] hashTable = new Collection[tableSize];
        //Initialize all buckets with a data structure
        for (int i = 0; i < hashTable.length; ++i) {
            hashTable[i] = createBucket();
        }
        return hashTable;
    }

    /**
     * Resize hash table to given size
     *
     * @param tableSize the size of the resized table
     */
    private void resizeTable(int tableSize) {
        Collection<Node>[] resizedHashTable = createTable(tableSize);
        //Iterate through all buckets
        for (int i = 0; i < buckets.length; ++i) {
            //Iterate through all keys in this bucket and
            //Calculate their indices in new table and assign accordingly
            for (Node node : buckets[i]) {
                int hashTableIndex = hashTableIndex(node.key);
                resizedHashTable[hashTableIndex].add(node);
            }
        }

        //Set hash table to the newly resized hash table
        buckets = resizedHashTable;
    }

    public void clear() {
        for (Collection<Node> bucket : buckets) {
            bucket.clear();
        }
        size = 0;
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    public V get(K key) {
        //Calculate hash table index
        int hashTableIndex = hashTableIndex(key);

        //Check key exists in hash table
        for (Node node : buckets[hashTableIndex]) {
            //Existing entry found
            if (node.key.hashCode() == key.hashCode() && node.key.equals(key)) {
                return node.value;
            }
        }
        return null;
    }

    public int size() {
        return size;
    }

    public void put(K key, V value) {
        //Calculate hash table index
        int hashTableIndex = hashTableIndex(key);

        //Insert into hash table / update hash table value
        for (Node node : buckets[hashTableIndex]) {
            //Existing entry found
            if (node.key.hashCode() == key.hashCode() && node.key.equals(key)) {
                node.value = value;
                return;
            }
        }

        //Create new node and add to hash table
        Node newNode = createNode(key, value);
        buckets[hashTableIndex].add(newNode);

        ++size;

        //Resize hash table if load factor exceeds max load factor
        if (size / bucketsSize >= maxLoadFactor) {
            bucketsSize *= 2;
            resizeTable(bucketsSize);
        }
    }

    public Set<K> keySet() {
        Set<K> keySet = new HashSet<>();
        //Iterate through all buckets
        for (int i = 0; i < buckets.length; ++i) {
            //Iterate through all keys in this bucket and
            //add to keySet
            for (Node node : buckets[i]) {
                keySet.add(node.key);
            }
        }

        return keySet;
    }

    public V remove(K key) {
        return remove(key, null, false);
    }

    public V remove(K key, V value) {
        return remove(key, value, true);
    }

    /**
     * Helper function for removing an entry from the hash table
     *
     * @param key        key corresponding to the entry that should be removed
     * @param value      value that should match the value of the entry referenced by given key
     * @param checkValue {@code true} | {@code false} indicating whether value should be considered
     * @return the value of the removed key if exists | {@code null}
     */
    private V remove(K key, V value, boolean checkValue) {
        //Calculate hash table index
        int hashTableIndex = hashTableIndex(key);

        //Insert into hash table / update hash table value
        for (Node node : buckets[hashTableIndex]) {
            //Existing entry found
            if (node.key.hashCode() == key.hashCode() && node.key.equals(key)) {

                //value should be checked and doesn't match
                if (checkValue && node.value != value) {
                    return null;
                }
                --size;
                buckets[hashTableIndex].remove(node);

                //Resize if required
                if (size / bucketsSize <= minLoadFactor) {
                    bucketsSize /= 2;
                    resizeTable(bucketsSize);
                }

                return node.value;
            }
        }
        return null;
    }

    public Iterator<K> iterator() {
        return new HashMapIterator();
    }

    /**
     * Iterator class that iterates over all keys in the hash map
     */
    private class HashMapIterator implements Iterator<K> {

        /* Hold all hash map keys in an array */
        K[] keys;
        /* Keys array current index */
        int currentKeysIndex = 0;

        HashMapIterator() {
            keys = (K[]) keySet().toArray();
        }

        public boolean hasNext() {
            return currentKeysIndex != keys.length;
        }

        public K next() {
            if (hasNext()) {
                return keys[currentKeysIndex++];
            }
            return null;
        }
    }

    /**
     * Returns the hash table index of the given key based on its hash code
     *
     * @return hash table index of the given key
     */
    private int hashTableIndex(K key) {
        //Set the most significant bit to 0 to ensure positive numbers
        return (key.hashCode() & 0x7fffffff) % bucketsSize;
    }

}
