package priorityqueue;

import java.util.Comparator;

public class Vertex {
    public int node;
    public int nodeValue;

    public Vertex(int node, int nodeValue) {
        this.node = node;
        this.nodeValue = nodeValue;
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof Vertex)) {
            return false;
        }

        Vertex v = (Vertex) obj;
        return node == v.node;
    }

    @Override
    public int hashCode() {
        return node;
    }
}
