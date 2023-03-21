package graph;

public class Edge {
    int vertex;
    double edgeWeight;

    public Edge(int vertex, double edgeWeight) {
        this.vertex = vertex;
        this.edgeWeight = edgeWeight;
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof Edge)) {
            return false;
        }

        Edge edge = (Edge) obj;
        return vertex == edge.vertex;
    }

    @Override
    public int hashCode() {
        return vertex;
    }

    public void setEdgeWeight(double value) {
        edgeWeight = value;
    }

    public double getEdgeWeight() {
        return edgeWeight;
    }
}