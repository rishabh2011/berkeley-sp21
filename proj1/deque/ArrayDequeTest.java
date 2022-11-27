package deque;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayDequeTest {

    @Test
    /* Checks the addFirst, size, isempty and get method */
    public void addFirstIsEmptySizeGetTest() {

        ArrayDeque<Integer> ad = new ArrayDeque<>();
        assertEquals(null, ad.get(0));
        assertTrue(ad.isEmpty());

        // [0][7][9][8][3][5][4][2]
        // resize
        // [7][9][8][3][5][4][2][0][][][][][][][][6]

        ad.addFirst(0);
        ad.addFirst(2);
        ad.addFirst(4);
        ad.addFirst(5);
        ad.addFirst(3);
        ad.addFirst(8);
        ad.addFirst(9);
        ad.addFirst(7);
        ad.addFirst(6);

        assertEquals((Integer) 6, ad.get(0));
        assertEquals((Integer) 9, ad.get(2));
        assertEquals(null, ad.get(-12));
        assertEquals(null, ad.get(120));

        assertFalse(ad.isEmpty());
        assertEquals(9, ad.size());

        ad.printDeque();
    }

    @Test
    /* Checks the addFirst, size, isempty and get method */
    public void addLastIsEmptySizeGetTest() {

        ArrayDeque<Integer> ad = new ArrayDeque<>();
        assertEquals(null, ad.get(0));
        assertTrue(ad.isEmpty());

        // [0][2][4][5][3][8][9][7]
        // resize
        // [0][2][4][5][3][8][9][7][6][][][][][][][]

        ad.addLast(0);
        ad.addLast(2);
        ad.addLast(4);
        ad.addLast(5);
        ad.addLast(3);
        ad.addLast(8);
        ad.addLast(9);
        ad.addLast(7);
        ad.addLast(6);

        assertEquals((Integer) 6, ad.get(8));
        assertEquals((Integer) 9, ad.get(6));
        assertEquals(null, ad.get(-12));
        assertEquals(null, ad.get(120));

        assertFalse(ad.isEmpty());
        assertEquals(9, ad.size());

        ad.printDeque();
    }

    @Test
    /* Checks the addFirst, size, isempty and get method */
    public void addIsEmptySizeGetTest() {

        ArrayDeque<Integer> ad = new ArrayDeque<>();
        assertEquals(null, ad.get(0));
        assertTrue(ad.isEmpty());

        // [0][2][5][8][9][7][3][4]
        // resize
        // [7][3][4][0][2][5][8][9][][][][][][][][6]

        ad.addFirst(0);
        ad.addLast(2);
        ad.addFirst(4);
        ad.addLast(5);
        ad.addFirst(3);
        ad.addLast(8);
        ad.addLast(9);
        ad.addFirst(7);
        ad.addFirst(6);

        assertEquals((Integer) 4, ad.get(3));
        assertEquals((Integer) 2, ad.get(5));
        assertEquals(null, ad.get(-12));
        assertEquals(null, ad.get(120));

        assertFalse(ad.isEmpty());
        assertEquals(9, ad.size());

        ad.printDeque();
    }

    @Test
    /* Checks the remove first, size, isempty and get method */
    public void addRemoveFirstIsEmptySizeGetTest() {

        ArrayDeque<Integer> ad = new ArrayDeque<>();
        assertEquals(null, ad.get(0));
        assertTrue(ad.isEmpty());
        assertEquals(null, ad.removeFirst());

        // [0][9][8][1][3][5][4][2]
        // resize
        // [9][8][1][3][5][4][2][0][][][][][][][6][7]
        // [][][][][][4][2][0][][][][][][][][]
        // resize
        //[4][2][0][][][][3][5]

        ad.addFirst(0);
        ad.addFirst(2);
        ad.addFirst(4);
        ad.addFirst(5);
        ad.addFirst(3);
        ad.addFirst(1);
        ad.addFirst(8);
        ad.addFirst(9);
        ad.addFirst(7);
        ad.addFirst(6);
        ad.removeFirst();
        ad.removeFirst();
        ad.removeFirst();
        ad.removeFirst();
        ad.removeFirst();
        ad.removeFirst();
        ad.removeFirst();
        ad.addFirst(5);
        ad.addFirst(3);

        assertEquals((Integer) 5, ad.get(1));
        assertEquals((Integer) 0, ad.get(4));
        assertEquals(null, ad.get(-12));
        assertEquals(null, ad.get(120));

        assertFalse(ad.isEmpty());
        assertEquals(5, ad.size());

        ad.printDeque();
    }

    @Test
    /* Checks the remove last, size, isempty and get method */
    public void addRemoveLastIsEmptySizeGetTest() {

        ArrayDeque<Integer> ad = new ArrayDeque<>();
        assertEquals(null, ad.get(0));
        assertTrue(ad.isEmpty());
        assertEquals(null, ad.removeLast());

        // [0][9][8][1][3][5][4][2]
        // [][0][8][1][3][5][][]

        ad.addFirst(0);
        ad.addFirst(2);
        ad.addFirst(4);
        ad.addFirst(5);
        ad.addFirst(3);
        ad.addFirst(1);
        ad.addFirst(8);
        ad.removeLast();
        ad.addFirst(0);
        ad.removeLast();
        ad.removeLast();

        assertEquals((Integer) 5, ad.get(4));
        assertEquals((Integer) 8, ad.get(1));
        assertEquals(null, ad.get(-12));
        assertEquals(null, ad.get(120));

        assertFalse(ad.isEmpty());
        assertEquals(5, ad.size());

        ad.printDeque();
    }

    @Test
    /* Check if you can create LinkedListDeques with different parameterized types*/
    public void multipleParamTest() {

        ArrayDeque<String> ad1 = new ArrayDeque<String>();
        ArrayDeque<Double> ad2 = new ArrayDeque<Double>();
        ArrayDeque<Boolean> ad3 = new ArrayDeque<Boolean>();

        ad1.addFirst("string");
        ad2.addFirst(3.14159);
        ad3.addFirst(true);

        String s = ad1.removeFirst();
        double d = ad2.removeFirst();
        boolean b = ad3.removeFirst();

    }

    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void comparisonTest() {

        ArrayDeque<Integer> ad = new ArrayDeque<Integer>();
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
        for (int i = 0; i < 1000000; i++) {
            ad.addLast(i);
            lld1.addLast(i);
        }

        for (double i = 0; i < 500000; i++) {
            assertEquals(lld1.removeLast(), ad.removeLast());
        }

        for (double i = 999999; i > 500000; i--) {
            assertEquals(lld1.removeLast(), ad.removeLast());
        }

        assertTrue(ad.equals(lld1));
        assertTrue(lld1.equals(ad));
    }

    @Test
    /* Check if passed object is equal to the deque */
    public void equalsTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();
        ad1.addFirst(10);
        ad1.addLast(20);
        ad1.addFirst(30);

        assertFalse(ad1.equals(40));

        ArrayDeque<Integer> ad2 = new ArrayDeque<Integer>();
        ad2.addFirst(10);
        ad2.addLast(20);
        ad2.addFirst(30);
        ad2.addFirst(40);

        assertFalse(ad1.equals(ad2));

        ad2.removeFirst();

        boolean isEqual = ad1.equals(ad2);
        assertTrue(isEqual);
    }

    @Test
    public void randomizedTest() {
        int n = 500;
        ArrayDeque<Integer> ad = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            int operationNumber = StdRandom.uniform(0, 6);
            //Add First
            if (operationNumber == 0) {
                int randVal = StdRandom.uniform(0, 100);
                ad.addFirst(randVal);
                System.out.println("Add First : " + randVal);
            }
            //Remove First
            else if (operationNumber == 1) {
                Integer value = ad.removeFirst();
                System.out.println("Remove First : " + value);
            }
            //Remove Last
            else if (operationNumber == 2) {
                Integer value = ad.removeLast();
                System.out.println("Remove Last : " + value);
            }
            //Add Last
            else if (operationNumber == 3) {
                int randVal = StdRandom.uniform(0, 100);
                ad.addLast(randVal);
                System.out.println("Add Last : " + randVal);
            }
            //Size
            else if (operationNumber == 4) {
                System.out.println("Size : " + ad.size());
            }
            //Get
            else if (operationNumber == 5) {
                int randIndex = StdRandom.uniform(0, 10);
                Integer value = ad.get(randIndex);
                System.out.println("Get Value at index : " + randIndex + " = " + value);
            }
        }

        System.out.println("List Empty? : " + ad.isEmpty());
        System.out.println("Size : " + ad.size());
        ad.printDeque();
    }
}
