package graph;

import graph.Edge;

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
     *
     * @param v vertex v
     * @param w vertex w
     * @param value weight of the edge
     */
    void addEdge(int v, int w, double value);

    /**
     * Returns all connected neighbours of vertex v
     */
    Iterable<Edge> adj(int v);
}
