package graph;

import priorityqueue.EdgePriorityQueue;

import graph.Edge;

import java.util.Collections;
import java.util.Comparator;
import java.util.Stack;


/**
 * Provides an implementation of Djikstra's Algorithm
 */
public class ShortestPaths {

    /** The fringe is an edge priority queue required
     * to perform best first search
     */
    EdgePriorityQueue fringe;

    /**
     * Each index represents a vertex and its value represents it's connected vertex
     */
    public int[] edgeTo;

    /**
     * Each index represents a vertex and its value represents its distance from the source vertex
     */
    public double[] distanceTo;

    /**
     * Marks all visited vertices as true/false
     */
    boolean[] marked;

    /**
     * Source vertex
     */
    int source;

    /**
     * Initialize class variables required for
     * performing best first search
     *
     * @param g Graph
     * @param s source vertex to start best first search from
     */
    public ShortestPaths(Graph g, int s) {

        source = s;

        //Create vertex comparator to be used by the fringe
        // and initialize fringe with said comparator
        Comparator<Edge> edgeComparator = Comparator.comparingDouble(Edge::getEdgeWeight);
        fringe = new EdgePriorityQueue(g.V(), edgeComparator);

        //Fill marked, distanceTo and edgeTo arrays with default values
        distanceTo = new double[g.V()];
        edgeTo = new int[g.V()];
        marked = new boolean[g.V()];
        for (int i = 0; i < g.V(); ++i) {
            distanceTo[i] = Integer.MAX_VALUE;
            edgeTo[i] = i;
            marked[i] = false;
        }
        distanceTo[source] = 0;

        fillFringe(g, s);
        bestFirstSearch(g);
    }

    /**
     * Performs best first search also known as
     * Djikstra's algorithm on the given graph
     *
     * @param g Graph
     */
    private void bestFirstSearch(Graph g) {
        while(!fringe.isEmpty()) {
            Edge edge = fringe.removeSmallest();
            marked[edge.vertex] = true;
            relax(g, edge);
        }
    }

    /** Relax each edge going out from the current edge vertex
     *
     * @param g directed graph
     * @param edge current edge
     * */
    private void relax(Graph g, Edge edge) {
        for (Edge adjEdge : g.adj(edge.vertex)) {
            //Adjacent vertex not visited
            if (fringe.contains(adjEdge)) {

                //relax edge
                double distanceFromV = distanceTo[edge.vertex] + adjEdge.edgeWeight;
                if (distanceTo[adjEdge.vertex] > distanceFromV) {
                    //update distance to
                    distanceTo[adjEdge.vertex] = distanceFromV;
                    //update edge to
                    edgeTo[adjEdge.vertex] = edge.vertex;
                    //update fringe priority
                    fringe.changePriority(adjEdge, distanceFromV);
                }
            }
        }
    }

    /** Fills fringe with default values i.e. priority
     * infinity for all vertices except source vertex
     * which has priority 0
     *
     * @param g directed graph
     * @param source source vertex
     */
    private void fillFringe(Graph g, int source) {
        for (int i = 0; i < g.V(); ++i) {
            Edge edge = new Edge(i, Integer.MAX_VALUE);
            fringe.add(edge);
        }

        Edge edge = new Edge(source, 0);
        fringe.changePriority(edge, 0);
    }

    /** Check if path exists from source vertex to given vertex
     *
     * @param v vertex for which path is checked
     * @return {@code true} if path exists from source. {@code false} otherwise
     */
    public boolean hasPathTo(int v) {
        if (v < 0 || v >= marked.length) {
            return false;
        }
        return marked[v];
    }

    /** Returns a path from source vertex to given vertex
     * if it exists
     *
     * @param v vertex for which path is required
     * @return path from given vertex to source vertex
     */
    public Iterable<Integer> pathTo(int v) {
        //Check invalid vertex or path doesn't exist
        if (v < 0 || v >= marked.length) {
            return null;
        } else if (!hasPathTo(v)) {
            return null;
        }

        Stack<Integer> path = new Stack<>();
        int vertex = v;
        while (vertex != source) {
            path.push(vertex);
            vertex = edgeTo[vertex];
        }
        path.push(source);
        Collections.reverse(path);
        return path;
    }
}
