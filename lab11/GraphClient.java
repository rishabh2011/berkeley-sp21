import edu.princeton.cs.algs4.In;
import graph.ShortestPaths;
import graph.EdgeWeightedDigraph;
import graph.Edge;

public class GraphClient {

    public static void main(String[] args) {

        System.out.println("Enter number of vertex");
        EdgeWeightedDigraph graph = new EdgeWeightedDigraph(new In(args[0]));

        int s = Integer.parseInt(args[1]);
        ShortestPaths sp = new ShortestPaths(graph, s);

        for (int t = 0; t < graph.V(); t++)
        {
            StdOut.print(s + " to " + t);
            StdOut.printf(" (%4.2f): ", sp.distanceTo[t]);
            if (sp.hasPathTo(t))
                for (int v : sp.pathTo(t))
                    StdOut.print(v + "   ");
            StdOut.println();
        }

    }

}
