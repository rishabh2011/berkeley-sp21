package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    Comparator<T> comparator;

    public MaxArrayDeque(Comparator<T> c) {
        comparator = c;
    }

    /**
     * Returns the max element in the deque using own comparator
     *
     * @return max element
     */
    public T max() {
        return getMax(comparator);
    }

    /**
     * Returns the max element in the deque using given comparator
     *
     * @return max element
     */
    public T max(Comparator<T> c) {
        return getMax(c);
    }

    /**
     * Returns the max element in the deque
     *
     * @param c a comparator object
     * @return max element in the deque
     */
    private T getMax(Comparator<T> c) {
        if (isEmpty()) {
            return null;
        }

        T max = get(0);
        for (int i = 1; i < size(); ++i) {
            T nextItem = get(i);
            if (c.compare(nextItem, max) > 0) {
                max = nextItem;
            }
        }
        return max;
    }
}
