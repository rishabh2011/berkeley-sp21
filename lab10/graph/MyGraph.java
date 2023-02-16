package graph;

import edu.princeton.cs.algs4.In;

import java.util.LinkedList;

/**
 * Represents a graph data structure
 *
 * @author Rishabh Choudhury
 */
public class MyGraph implements Graph {

    /**
     * Adjacency lists
     * which is an array of Linked list data structure
     * Each index represents a vertex.
     * Each linked list associated with a vertex represents all neighbours of that vertex
     */
    private LinkedList<Integer>[] adj;

    /**
     * Number of vertices present in the graph
     */
    private int V;

    /**
     * Number of edges present in the graph
     */
    private int E;

    /**
     * Creates a new graph data structure with given vertices size
     *
     * @param V number of vertices of the graph
     */
    public MyGraph(int V) {
        this.V = V;
        adj = (LinkedList<Integer>[]) new LinkedList[V]; //Create vertices array
        for (int i = 0; i < V; ++i) {
            //For each vertex index, create a linked list
            adj[i] = new LinkedList<>();
        }
    }

    /**
     * Takes user input, creates a graph and adds edges accordingly
     *
     * @param in user input
     *           <br>1. First input is total number of vertices </br>
     *           <br>2. Second input is total number of edges </br>
     *           <br>3. Each consecutive input after are vertices v and w.
     *           An edge is automatically created to connect the two vertices </br>
     */
    public MyGraph(In in) {
        this(in.readInt()); //Create a graph with given number of vertices
        System.out.println("Enter number of edges");
        E = in.readInt();
        for (int i = 0; i < E;) {
            //Add an edge
            System.out.print("Enter vertex v: ");
            int v = in.readInt(); //read vertex v
            if (v < 0 || v >= V) {
                System.out.println("Invalid vertex v. Please enter a valid vertex between 0 and " + (V - 1));
                continue;
            }
            System.out.print("Enter vertex w: ");
            int w = in.readInt(); //read vertex w
            if (w < 0 || w >= V) {
                System.out.println("Invalid vertex w. Please enter a valid vertex between 0 and " + (V - 1));
                continue;
            }
            addEdge(v, w); //connect vertices v and w with an edge
            ++i;
            System.out.println();
        }
    }

    @Override
    public int V() {
        return V;
    }

    @Override
    public int E() {
        return E;
    }

    @Override
    public void addEdge(int v, int w) {
        //connect v to w
        adj[v].addFirst(w);

        //connect w to v
        adj[w].addFirst(v);
    }

    @Override
    public Iterable<Integer> adj(int v) {
        return adj[v];
    }
}
