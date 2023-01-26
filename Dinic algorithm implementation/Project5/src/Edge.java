public class Edge {
    public int vertex1;
    public int vertex2;
    public int capacity;
    public int flow;
    public Edge reverseEdge;
    public boolean r;
    Edge(int vertex1, int vertex2, int capacity, int flow) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.capacity = capacity;
        this.flow = flow;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "vertex1=" + vertex1 +
                ", vertex2=" + vertex2 +
                ", capacity=" + capacity +
                ", flow=" + flow +
                '}';
    }
}
