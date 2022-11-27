package deque;

import java.util.Comparator;

public class CustomComparators {
    /**
     * Returns a String comparator for the given deque
     *
     * @return String Comparator
     */
    public static StringComparator getStringComparator() {
        return new StringComparator();
    }

    /**
     * Returns an Integer comparator for the given deque
     *
     * @return Integer Comparator
     */
    public static IntegerComparator getIntegerComparator() {
        return new IntegerComparator();
    }

    private static class StringComparator implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    }

    private static class IntegerComparator implements Comparator<Integer> {

        @Override
        public int compare(Integer o1, Integer o2) {
            return o1 - o2;
        }
    }
}
