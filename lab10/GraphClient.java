import edu.princeton.cs.algs4.In;
import graph.BreadthFirstPaths;
import graph.GraphSearch;
import graph.MyGraph;
import graph.DepthFirstPaths;

public class GraphClient {

    public static void main(String[] args) {
        In in = new In();
        System.out.println("Enter number of vertex");
        MyGraph graph = new MyGraph(in);
        printGraph(graph);
        GraphSearch dfp = new DepthFirstPaths(graph, 0);
        printPathDFP(dfp, 7);
        printPathDFP(dfp, 3);
        GraphSearch bfp = new BreadthFirstPaths(graph, 0);
        printPathDFP(bfp, 4);
        printPathDFP(bfp, 8);
    }

    /**
     * Prints the underlying graph
     *
     * @param graph graph to print
     */
    public static void printGraph(MyGraph graph) {
        for (int i = 0; i < graph.V(); ++i) {
            System.out.print(i + " - ");
            for (Integer j : graph.adj(i)) {
                System.out.print(j + " ");
            }
            System.out.println();
        }
    }

    /**
     * Prints if a path exists from given vertex to source vertex
     * using depth first paths
     *
     * @param gs depth first paths object
     * @param v   vertex to check
     */
    public static void printPathDFP(GraphSearch gs, int v) {
        if (gs.hasPathTo(v)) {
            System.out.print("Path to vertex " + v + " : ");
            for (Integer vertex : gs.pathTo(v)) {
                System.out.print(vertex + " ");
            }
            System.out.println();
        }
    }

}
