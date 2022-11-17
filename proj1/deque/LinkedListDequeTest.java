package deque;

import org.junit.Test;

import static org.junit.Assert.*;

import edu.princeton.cs.algs4.StdRandom;


/**
 * Performs some basic linked list tests.
 */
public class LinkedListDequeTest {

    @Test
    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {


        LinkedListDeque<String> lld1 = new LinkedListDeque<String>();

        assertTrue("A newly initialized LLDeque should be empty", lld1.isEmpty());
        lld1.addFirst("front");

        // The && operator is the same as "and" in Python.
        // It's a binary operator that returns true if both arguments true, and false otherwise.
        assertEquals(1, lld1.size());
        assertFalse("lld1 should now contain 1 item", lld1.isEmpty());

        lld1.addLast("middle");
        assertEquals(2, lld1.size());

        lld1.addLast("back");
        assertEquals(3, lld1.size());

        System.out.println("Printing out deque: ");
        lld1.printDeque();

    }

    @Test
    public void addFirstTest() {
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addFirst(30);
        lld1.addFirst(20);
        lld1.addFirst(10);

        lld1.printDeque();
    }

    @Test
    public void addLastTest() {
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addLast(40);
        lld1.addLast(50);
        lld1.addLast(60);

        lld1.printDeque();
    }

    @Test
    public void removeFirstTest() {
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
        lld1.removeFirst();
        assertTrue(lld1.size() == 0);

        lld1.addFirst(10);
        lld1.addLast(20);
        lld1.addFirst(30);
        lld1.removeFirst();
        lld1.addFirst(40);
        lld1.addFirst(50);

        assertTrue(lld1.size() == 4);
        lld1.printDeque();
    }

    @Test
    public void removeLastTest() {
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
        lld1.removeLast();
        assertTrue(lld1.size() == 0);

        lld1.addFirst(10);
        lld1.addLast(20);
        lld1.addFirst(30);
        lld1.removeLast();
        lld1.addFirst(40);
        lld1.addFirst(50);

        assertTrue(lld1.size() == 4);
        lld1.printDeque();
    }

    @Test
    /** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
    public void addRemoveTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
        // should be empty
        assertTrue("lld1 should be empty upon initialization", lld1.isEmpty());

        lld1.addFirst(10);
        // should not be empty
        assertFalse("lld1 should contain 1 item", lld1.isEmpty());

        lld1.removeFirst();
        // should be empty
        assertTrue("lld1 should be empty after removal", lld1.isEmpty());

    }

    @Test
    /* Tests removing from an empty deque */
    public void removeEmptyTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addFirst(3);

        lld1.removeLast();
        lld1.removeFirst();
        lld1.removeLast();
        lld1.removeFirst();

        int size = lld1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);

    }

    @Test
    /* Check if you can create LinkedListDeques with different parameterized types*/
    public void multipleParamTest() {

        LinkedListDeque<String> lld1 = new LinkedListDeque<String>();
        LinkedListDeque<Double> lld2 = new LinkedListDeque<Double>();
        LinkedListDeque<Boolean> lld3 = new LinkedListDeque<Boolean>();

        lld1.addFirst("string");
        lld2.addFirst(3.14159);
        lld3.addFirst(true);

        String s = lld1.removeFirst();
        double d = lld2.removeFirst();
        boolean b = lld3.removeFirst();

    }

    @Test
    /* check if null is return when removing from an empty LinkedListDeque. */
    public void emptyNullReturnTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();

        boolean passed1 = false;
        boolean passed2 = false;
        assertEquals("Should return null when removeFirst is called on an empty Deque,", null, lld1.removeFirst());
        assertEquals("Should return null when removeLast is called on an empty Deque,", null, lld1.removeLast());
    }

    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void bigLLDequeTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
        for (int i = 0; i < 1000000; i++) {
            lld1.addLast(i);
        }

        for (double i = 0; i < 500000; i++) {
            assertEquals("Should have the same value", i, (double) lld1.removeFirst(), 0.0);
        }

        for (double i = 999999; i > 500000; i--) {
            assertEquals("Should have the same value", i, (double) lld1.removeLast(), 0.0);
        }
    }

    @Test
    public void getInvalidIndexTest() {
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
        assertEquals(null, lld1.get(0)); //Empty list

        lld1.addFirst(10);
        lld1.addLast(20);
        lld1.addFirst(30);

        assertEquals((Integer) 30, lld1.get(0));
        assertEquals(null, lld1.get(-3)); //Invalid index
        assertEquals(null, lld1.get(10)); //Invalid index
    }

    @Test
    /* Check get returns item at given index */
    public void getTest() {
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
        for (int i = 0; i < 1000000; i++) {
            lld1.addLast(i);
        }

        assertEquals((Integer) 650000, lld1.get(650000));
        assertEquals((Integer) 250000, lld1.get(250000));
    }

    @Test
    public void getRecursiveInvalidIndexTest() {
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
        assertEquals(null, lld1.getRecursive(0)); //Empty list

        lld1.addFirst(10);
        lld1.addLast(20);
        lld1.addFirst(30);

        assertEquals((Integer) 20, lld1.getRecursive(2));
        assertEquals(null, lld1.getRecursive(-3)); //Invalid index
        assertEquals(null, lld1.getRecursive(10)); //Invalid index
    }

    @Test
    /* Check get returns item at given index */
    public void getRecursiveTest() {
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
        for (int i = 0; i < 1000; i++) {
            lld1.addLast(i);
        }

        assertEquals((Integer) 650, lld1.getRecursive(650));
        assertEquals((Integer) 250, lld1.getRecursive(250));
    }

    @Test
    /* Check if passed object is equal to the deque */
    public void equalsTest() {
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
        lld1.addFirst(10);
        lld1.addLast(20);
        lld1.addFirst(30);

        assertFalse(lld1.equals(40));

        LinkedListDeque<Integer> lld2 = new LinkedListDeque<Integer>();
        lld2.addFirst(10);
        lld2.addLast(20);
        lld2.addFirst(30);
        lld2.addFirst(40);

        assertFalse(lld1.equals(lld2));

        lld2.removeFirst();

        boolean isEqual = lld1.equals(lld2);
        assertTrue(isEqual);
    }

    @Test
    public void randomizedTest() {
        int n = 500;
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
        for (int i = 0; i < n; i++) {
            int operationNumber = StdRandom.uniform(0, 7);
            //Add First
            if (operationNumber == 0) {
                int randVal = StdRandom.uniform(0, 100);
                lld1.addFirst(randVal);
                System.out.println("Add First : " + randVal);
            }
            //Remove First
            else if (operationNumber == 1) {
                Integer value = lld1.removeFirst();
                System.out.println("Remove First : " + value);
            }
            //Remove Last
            else if (operationNumber == 2) {
                Integer value = lld1.removeLast();
                System.out.println("Remove Last : " + value);
            }
            //Add Last
            else if (operationNumber == 3) {
                int randVal = StdRandom.uniform(0, 100);
                lld1.addLast(randVal);
                System.out.println("Add Last : " + randVal);
            }
            //Size
            else if (operationNumber == 4) {
                System.out.println("Size : " + lld1.size());
            }
            //Get Iterative
            else if (operationNumber == 5) {
                int randIndex = StdRandom.uniform(0, 10);
                Integer value = lld1.get(randIndex);
                System.out.println("Get Iterative Value at index : " + randIndex + " = " + value);
            }
            //Get Recursive
            else if (operationNumber == 6) {
                int randIndex = StdRandom.uniform(0, 10);
                Integer value = lld1.getRecursive(randIndex);
                System.out.println("Get Recursive Value at index : " + randIndex + " = " + value);
            }
        }

        System.out.println("List Empty? : " + lld1.isEmpty());
        System.out.println("Size : " + lld1.size());
        lld1.printDeque();
    }
}
