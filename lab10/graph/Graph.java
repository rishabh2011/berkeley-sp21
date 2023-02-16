package graph;

/**
 * Graph functions to be implemented by a concrete Graph class
 */
public interface Graph {
    /**
     * Returns the number of vertices in the graph
     */
    int V();

    /**
     * Returns the number of edges in the graph
     */
    int E();

    /**
     * Connects vertex v and vertex w with an edge
     */
    void addEdge(int v, int w);

    /**
     * Returns all connected neighbours of vertex v
     */
    Iterable<Integer> adj(int v);
}
