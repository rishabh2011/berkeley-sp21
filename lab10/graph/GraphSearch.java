package graph;

/**
 * Graph search functions to be implemented by any graph searching classes
 */
public interface GraphSearch {
    /**
     * Checks if a path exists from source vertex to given vertex
     *
     * @param v vertex
     * @return {@code true} : path exists from source vertex to given vertex
     * <br>{@code false} : given vertex is invalid or no path exists from source vertex to given vertex</br>
     */
    boolean hasPathTo(int v);

    /**
     * Returns path from given vertex to source vertex if it exists
     *
     * @param v vertex
     */
    Iterable<Integer> pathTo(int v);
}
