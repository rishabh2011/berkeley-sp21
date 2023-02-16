package graph;

import java.util.*;

/**
 * Enables Breadth First Search functionality in a graph
 */
public class BreadthFirstPaths implements GraphSearch {

    /**
     * Marks all visited vertices as true/false
     */
    boolean[] marked;
    /**
     * Each index represents a vertex and its value represents it's connected vertex
     */
    int[] edgeTo;

    /**
     * Each index represents a vertex and
     * its value represents the distance of the vertex from the source vertex
     */
    int[] distanceTo;

    /**
     * Source vertex
     */
    int source;

    /**
     * Performs Breadth first search on given graph and source vertex
     *
     * @param g Graph
     * @param s source vertex to start dfs from
     */
    public BreadthFirstPaths(Graph g, int s) {
        marked = new boolean[g.V()];
        Arrays.fill(marked, false);
        edgeTo = new int[g.V()];
        for (int i = 0; i < marked.length; ++i) {
            edgeTo[i] = i;
        }
        distanceTo = new int[g.V()];
        Arrays.fill(distanceTo, 0);
        source = s;
        bfs(g);
    }

    /**
     * Performs Breadth first search on given graph and source vertex
     *
     * @param g Graph
     */
    private void bfs(Graph g) {

        //Initialize the fringe and add the source vertex to the fringe
        ArrayDeque<Integer> fringe = new ArrayDeque<>();
        fringe.addLast(source);
        marked[source] = true;

        //Repeat until fringe is empty
        while (!fringe.isEmpty()) {
            //Get the first vertex in queue
            int vertex = fringe.removeFirst();
            //For all adjacent vertices of current vertex
            for (Integer adjV : g.adj(vertex)) {
                //not yet visited
                if (!marked[adjV]) {
                    //Mark vertex as visited
                    marked[adjV] = true;
                    //Add vertex to fringe
                    fringe.addLast(adjV);

                    edgeTo[adjV] = vertex;
                    distanceTo[adjV] = distanceTo[vertex] + 1; //Calculate distance from source
                }
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
