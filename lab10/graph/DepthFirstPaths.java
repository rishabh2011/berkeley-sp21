package graph;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Stack;

/**
 * Enables Depth First Search functionality in a graph
 */
public class DepthFirstPaths implements GraphSearch {

    /**
     * Marks all visited vertices as true/false
     */
    boolean[] marked;
    /**
     * Each index represents a vertex and its value represents it's connected vertex
     */
    int[] edgeTo;

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
    public DepthFirstPaths(Graph g, int s) {
        marked = new boolean[g.V()];
        Arrays.fill(marked, false);
        edgeTo = new int[g.V()];
        for (int i = 0; i < marked.length; ++i) {
            edgeTo[i] = i;
        }
        source = s;
        dfs(g, s);
    }

    /**
     * Performs Depth first search on given graph and source vertex
     *
     * @param g Graph
     * @param v source vertex to start dfs from
     */
    private void dfs(Graph g, int v) {
        marked[v] = true;
        for (Integer adjV : g.adj(v)) {
            if (!marked[adjV]) {
                edgeTo[adjV] = v;
                dfs(g, adjV);
            }
        }
    }

    @Override
    public boolean hasPathTo(int v) {
        if (v < 0 || v >= marked.length) {
            return false;
        }
        return marked[v];
    }

    @Override
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
