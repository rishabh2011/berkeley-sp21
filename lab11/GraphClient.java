import edu.princeton.cs.algs4.In;
import graph.ShortestPaths;
import graph.WeightedDigraph;
import graph.Graph.Edge;

public class GraphClient {

    public static void main(String[] args) {
        In in = new In();
        System.out.println("Enter number of vertex");
        WeightedDigraph graph = new WeightedDigraph(in);
        printGraph(graph);
    }

    /**
     * Prints the underlying graph
     *
     * @param graph graph to print
     */
    public static void printGraph(WeightedDigraph graph) {
        for (int i = 0; i < graph.V(); ++i) {
            System.out.print(i + " - ");
            for (Edge j : graph.adj(i)) {
                System.out.print(j + " ");
            }
            System.out.println();
        }
    }

    /**
     * Prints if a path exists from given vertex to source vertex
     * using Best first search (Djikstra's Algorithm)
     *
     * @param gs depth first paths object
     * @param v   vertex to check
     */
    public static void printPathBestFirst(ShortestPaths gs, int v) {
        if (gs.hasPathTo(v)) {
            System.out.print("Path to vertex " + v + " : ");
            for (Integer vertex : gs.pathTo(v)) {
                System.out.print(vertex + " ");
            }
            System.out.println();
        }
    }

}
