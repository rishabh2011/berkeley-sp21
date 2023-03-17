package graph;

import priorityqueue.Vertex;
import priorityqueue.VertexPriorityQueue;

import graph.Graph.Edge;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Stack;


/**
 * Provides implementation of Djikstra's Algorithm
 */
public class ShortestPaths {

    VertexPriorityQueue fringe;

    /**
     * Each index represents a vertex and its value represents it's connected vertex
     */
    int[] edgeTo;

    /**
     * Each index represents a vertex and its value represents its distance from the source vertex
     */
    int[] distanceTo;

    /**
     * Source vertex
     */
    int source;

    /**
     * Performs Depth first search on given graph and source vertex
     *
     * @param g Graph
     * @param s source vertex to start dfs from
     */
    public ShortestPaths(Graph g, int s) {

        //Create vertex comparator to be used by the fringe
        // and initialize fringe with said comparator
        Comparator<Vertex> vertexComparator = Comparator.comparingInt(o -> o.nodeValue);
        fringe = new VertexPriorityQueue(g.V(), vertexComparator);

        //Fill distanceTo array with default values
        distanceTo = new int[g.V()];
        Arrays.fill(distanceTo, Integer.MAX_VALUE);
        distanceTo[0] = 0;

        //Fill edgeTo array with default values
        edgeTo = new int[g.V()];
        for (int i = 0; i < edgeTo.length; ++i) {
            edgeTo[i] = i;
        }

        source = s;
        bestFirstSearch(g, s);
    }

    /**
     * Performs Depth first search on given graph and source vertex
     *
     * @param g Graph
     * @param v source vertex to start dfs from
     */
    private void bestFirstSearch(Graph g, int v) {
        for (Graph.Edge adjV : g.adj(v)) {

        }
    }

    private void fillFringe(Graph g) {
        for (int i = 0; i < g.V(); ++i) {
            fringe.add(g.)
        }
    }


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
